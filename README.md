# Farmer's Delight Tweaks (`fd_storage_compat`)

NeoForge 1.21.1 add-on for [Farmer's Delight](https://github.com/vectorwing/FarmersDelight).

## Features

- Storage crates and sacks for missing vanilla foods, seeds, and saplings
- Cutting-board uncrafts for FD rope/canvas/net/fences and knives
- Pack-compat cutting recipes for Supplementaries fiber/decor, Quark rope, More Delight knives, and Handcrafted sheets

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
A C P
K   S
```

- `A` = any axe (`#minecraft:axes`)
- `C` = any crate (`#fd_storage_compat:crates` — Farmer's Delight crates/rice bag + this mod's food crates)
- `P` = any pickaxe (`#minecraft:pickaxes`)
- `K` = any knife (`#c:tools/knife`)
- `S` = shears (`minecraft:shears`)

Hopper-fed **auto cutting board** (tools built into the machine — no tool slot in the GUI): insert from top/sides, extract from bottom. Right-click for a GUI.

Processing priority:
1. **Planks → wooden slabs** (1 plank → 2 matching slabs)
2. **Wooden slabs → sticks** (1 slab → 1 stick; run wood through twice for the old stick rate)
3. **Beds → 3 matching wool + 3 oak planks**
4. **Mob heads → matching spawn eggs** (1 head → 1 egg; vanilla heads plus modded `_head`/`_skull` names when the egg exists)
5. **All Farmer's Delight cutting-board recipes** (matched by input item only)
6. Reverse crafting as fallback for items with no cutting recipe

Crafting recipes auto-unlock in the recipe book when you obtain the required ingredients (or a crate/sack for unpack recipes).
