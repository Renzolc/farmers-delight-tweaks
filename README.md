# Farmer's Delight Tweaks (`fd_storage_compat`)

NeoForge 1.21.1 add-on for [Farmer's Delight](https://github.com/vectorwing/FarmersDelight).

## Features

- Storage crates and sacks for missing vanilla foods, seeds, and saplings
- Modded seed/sapling sacks + produce crates for Cultural / Veggies / Expanded / Fruits / Crabber's Delight, Quark blossom+ancient saplings, and Supplementaries flax seeds (recipes gated with `neoforge:mod_loaded`)
- Cutting-board uncrafts for FD rope/canvas/net/fences and knives
- Pack-compat cutting recipes for Supplementaries fiber/decor, Quark rope, More Delight knives, and Handcrafted sheets
- Cutting-board / Decrafter uncrafts for Create kinetics/machines/components when Create is loaded
- Cutting-board / Decrafter uncrafts for Sophisticated Backpacks + Sophisticated Storage (backpacks, storage tiers, upgrades) when those mods are loaded
- Decrafter Upgrade for Sophisticated Backpacks (optional). Putting an item in it returns full craft ingredients into the backpack. The cutting board and Decrafter block stay partial.

## Integrations

See [INTEGRATIONS.md](INTEGRATIONS.md) for Renzo-pack FD addon tag/recipe bridges (squid/calamari, cheese, bread slices, cut veggies, SAR ingredients).

## Build

```bash
./gradlew build
```

Jar: `build/libs/farmersdelight_tweaks-*.jar`

## License

MIT

## Attribution

Crate bottom / bag layout textures are derived from Farmer's Delight assets (vectorwing) for personal/compat use. Vanilla item icons used for compositing are Mojang assets.

## Decrafter

Craft on a crafting table with:

```
C C C
K A P
C C C
```

- `C` = cutting board
- `K` = any knife (`#c:tools/knife`)
- `A` = any axe (`#minecraft:axes`)
- `P` = any pickaxe (`#minecraft:pickaxes`)

Hopper-fed **auto cutting board** (tools built into the machine — no tool slot in the GUI): insert from top/sides, extract from bottom. Right-click for a GUI.

Processing priority:
1. **Planks → wooden slabs** (1 plank → 2 matching slabs)
2. **Wooden slabs → sticks** (1 slab → 1 stick; run wood through twice for the old stick rate)
3. **Beds → 3 matching wool + 3 oak planks**
4. **Mob heads → matching spawn eggs** (1 head → 1 egg; vanilla heads plus modded `_head`/`_skull` names when the egg exists)
5. **All Farmer's Delight cutting-board recipes** (matched by input item only)
6. Reverse crafting as fallback for items with no cutting recipe



### Create

When `create` is installed, cutting-board recipes (also used by the Decrafter) salvage common kinetics, machines, logistics parts, casings, and tools. High-ratio cheap crafts (e.g. shafts ×8) are skipped on the cutting board — use the Decrafter with a full craft batch to reverse those via vanilla crafting fallback. Decorative palette blocks and most dye variants are skipped.

### Sophisticated Backpacks / Storage

When `sophisticatedbackpacks` / `sophisticatedstorage` are installed, cutting-board recipes (also used by the Decrafter) salvage backpacks, storage containers, and upgrades back toward previous tiers / main materials. Recipes match by item id only — **empty** backpacks and storage before uncrafting or contents are lost. Optional Chipped/Sawmill upgrades and creative infinity upgrades are skipped.

Crafting recipes auto-unlock in the recipe book when you obtain the required ingredients (or a crate/sack for unpack recipes).


### Decrafter Upgrade

When Sophisticated Backpacks is installed, one upgrade is added: **Decrafter Upgrade** (`fd_storage_compat:decrafter_upgrade`).

Craft it shapeless with 4 leather and 1 Decrafter. Only one fits in a backpack.

Open the backpack, open the upgrade, and put an item in the input slot. If this mod has an uncraft for it, the full craft ingredients are inserted into the backpack. If they do not fit, the input stays. There is no take-by-hand grid. Damaged tools are left alone. Backpacks and storage still match by item id, so empty them first.

The cutting board and the Decrafter block keep their partial salvage. Full counts exist only through this upgrade, including a full return of the upgrade itself (4 leather + 1 Decrafter). The cutting board has no recipe for it, and the Decrafter block will not reverse-craft it, so you need a Decrafter Upgrade already installed and a spare upgrade in its input.
