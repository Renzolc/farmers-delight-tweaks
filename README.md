# Farmer's Delight Tweaks (`fd_storage_compat`)

NeoForge 1.21.1 add-on for [Farmer's Delight](https://github.com/vectorwing/FarmersDelight).

## Features

- Storage crates and sacks for missing vanilla foods, seeds, and saplings
- Modded seed/sapling sacks + produce crates for Cultural / Veggies / Expanded / Fruits / Crabber's Delight, Quark blossom+ancient saplings, and Supplementaries flax seeds (recipes gated with `neoforge:mod_loaded`)
- Cutting-board uncrafts for FD rope/canvas/net/fences and knives
- Pack-compat cutting recipes for Supplementaries fiber/decor, Quark rope, More Delight knives, and Handcrafted sheets
- Cutting-board / Decrafter uncrafts for Create kinetics/machines/components when Create is loaded
- Cutting-board / Decrafter uncrafts for Sophisticated Backpacks + Sophisticated Storage (backpacks, storage tiers, upgrades) when those mods are loaded
- Simple and Advanced Uncrafter backpack upgrades (optional, when Sophisticated Backpacks is loaded) that return full craft ingredients for those same uncrafts. The cutting board stays partial.

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


### Uncrafter upgrades

When Sophisticated Backpacks is installed, two upgrade items are added:

- **Simple Uncrafter** — open the backpack, open the upgrade, put the item in the input slot. Full ingredients appear in a 3×3 and you take them yourself (into your cursor or inventory). Needs `1 flint knife + 2 iron + upgrade base + Decrafter`.
- **Advanced Uncrafter** — same input and the same full-return recipes, but recovered items are inserted into the backpack automatically. If the backpack cannot fit them, the input stays put. Crafted from the Simple upgrade plus `2 hoppers + 2 redstone`.

Only one of each upgrade fits in a backpack. Both can be installed together. They only uncraft items this mod already has a cutting recipe for, plus the two upgrade items themselves. Damaged tools are left alone. Backpacks and storage still match by item id, so empty them first.

The cutting board and Decrafter are unchanged and still pay the partial salvage. There is no cutting-board recipe for the Uncrafter upgrades, and the Decrafter will not reverse-craft them. To uncraft an Uncrafter you need a second one installed and the spare sitting in the input slot (the installed copy is not consumed).
