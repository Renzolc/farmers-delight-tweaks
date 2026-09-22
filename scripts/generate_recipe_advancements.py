#!/usr/bin/env python3
"""Generate recipe-unlock advancements for crafting shaped/shapeless recipes."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RECIPE_DIR = ROOT / "src/main/resources/data/fd_storage_compat/recipe"
ADV_DIR = ROOT / "src/main/resources/data/fd_storage_compat/advancement/recipes"
MOD_ID = "fd_storage_compat"
CRAFTING_TYPES = {"minecraft:crafting_shaped", "minecraft:crafting_shapeless"}

CATEGORY_MAP = {
    "building": "building_blocks",
    "misc": "misc",
    "redstone": "redstone",
    "equipment": "combat",
}


def recipe_id_from_path(path: Path) -> str:
    rel = path.relative_to(RECIPE_DIR).with_suffix("").as_posix()
    return f"{MOD_ID}:{rel}"


def criterion_name(item_or_tag: str) -> str:
    # item_or_tag like "minecraft:apple" or "#fd_storage_compat:crates"
    raw = item_or_tag.lstrip("#").replace(":", "_").replace("/", "_")
    return f"has_{raw}"


def inventory_predicate(item: str | None = None, tag: str | None = None) -> dict:
    if tag:
        value = tag if tag.startswith("#") else f"#{tag}"
    else:
        value = item
    return {
        "trigger": "minecraft:inventory_changed",
        "conditions": {
            "items": [
                {
                    "items": value
                }
            ]
        },
    }


def collect_unlock_keys(data: dict, recipe_path: Path) -> list[tuple[str, dict]]:
    """Return list of (criterion_name, criterion_body) for inventory unlocks."""
    rid = recipe_path.stem
    rtype = data["type"]

    # Special-case decrafter: unlock with crates tag
    if rid == "decrafter":
        return [("has_crate", inventory_predicate(tag="fd_storage_compat:crates"))]

    keys: list[tuple[str, dict]] = []
    seen: set[str] = set()

    def add_item(item: str):
        if item in seen:
            return
        seen.add(item)
        keys.append((criterion_name(item), inventory_predicate(item=item)))

    def add_tag(tag: str):
        token = f"#{tag}"
        if token in seen:
            return
        seen.add(token)
        keys.append((criterion_name(token), inventory_predicate(tag=tag)))

    if rtype == "minecraft:crafting_shaped":
        key_map = data.get("key", {})
        # Prefer unique ingredients from key map
        for _sym, ing in key_map.items():
            if "item" in ing:
                add_item(ing["item"])
            elif "tag" in ing:
                add_tag(ing["tag"])
    elif rtype == "minecraft:crafting_shapeless":
        for ing in data.get("ingredients", []):
            if "item" in ing:
                add_item(ing["item"])
            elif "tag" in ing:
                add_tag(ing["tag"])

    # For simple pack/unpack with a single ingredient, that's ideal.
    # If somehow empty, fall back to result item (won't unlock usefully but safe).
    if not keys:
        result = data.get("result", {}).get("id")
        if result:
            add_item(result)
    return keys


def build_advancement(data: dict, recipe_path: Path) -> dict:
    recipe_id = recipe_id_from_path(recipe_path)
    inv_keys = collect_unlock_keys(data, recipe_path)

    criteria = {}
    for name, body in inv_keys:
        criteria[name] = body
    criteria["has_the_recipe"] = {
        "trigger": "minecraft:recipe_unlocked",
        "conditions": {"recipe": recipe_id},
    }

    req_names = ["has_the_recipe"] + [n for n, _ in inv_keys]
    adv = {
        "parent": "minecraft:recipes/root",
        "criteria": criteria,
        "requirements": [req_names],
        "rewards": {"recipes": [recipe_id]},
    }

    # Preserve load conditions so advancements match gated recipes
    if "neoforge:conditions" in data:
        adv = {"neoforge:conditions": data["neoforge:conditions"], **adv}

    return adv


def ensure_show_notification(path: Path, data: dict) -> bool:
    if data.get("type") not in CRAFTING_TYPES:
        return False
    if data.get("show_notification") is True:
        return False
    data["show_notification"] = True
    # Keep key order roughly: conditions, type, category, show_notification, rest
    ordered = {}
    for k in ("neoforge:conditions", "type", "category", "show_notification"):
        if k in data:
            ordered[k] = data[k]
    for k, v in data.items():
        if k not in ordered:
            ordered[k] = v
    path.write_text(json.dumps(ordered, indent=2) + "\n")
    return True


def main() -> None:
    ADV_DIR.mkdir(parents=True, exist_ok=True)
    # Clear previously generated advancements under recipes/
    if ADV_DIR.exists():
        for old in ADV_DIR.rglob("*.json"):
            old.unlink()

    count = 0
    notif = 0
    for path in sorted(RECIPE_DIR.rglob("*.json")):
        if "cutting" in path.parts:
            continue
        data = json.loads(path.read_text())
        rtype = data.get("type")
        if rtype not in CRAFTING_TYPES:
            continue
        if rtype.startswith("farmersdelight:cutting") or "cutting" in str(rtype):
            continue

        if ensure_show_notification(path, data):
            notif += 1
            data = json.loads(path.read_text())

        category = CATEGORY_MAP.get(data.get("category", "misc"), "misc")
        rel = path.relative_to(RECIPE_DIR).with_suffix("").as_posix()
        out = ADV_DIR / category / f"{rel}.json"
        out.parent.mkdir(parents=True, exist_ok=True)
        adv = build_advancement(data, path)
        out.write_text(json.dumps(adv, indent=2) + "\n")
        count += 1

    print(f"Generated {count} advancements; set show_notification on {notif} recipes")


if __name__ == "__main__":
    main()
