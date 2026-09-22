# Farmer's Delight Tweaks

NeoForge 1.21.1 companion for [Farmer's Delight](https://www.curseforge.com/minecraft/mc-mods/farmers-delight). Adds missing storage, cutting-board uncrafts, pack-friendly ingredient bridges, and a hopper-fed **Decrafter**.

## Features

### Storage
- Food crates (9↔1): apple, sweet berry, glow berry, cocoa, sugar cane, nether wart, chorus fruit
- Seed sacks (9↔1): wheat, pumpkin, melon, beetroot, torchflower, cabbage, tomato
- Sapling sacks (9↔1): oak, spruce, birch, jungle, acacia, dark oak, cherry + mangrove propagule

### Cutting board
- Uncraft FD rope, canvas, rope fences/gates, and safety net into straw (+ sticks where relevant)
- Salvage FD / More Delight knives with a **pickaxe**
- Extra uncrafts for common pack decor (e.g. Supplementaries fiber items)

### Decrafter (1.0.7)
Craft with **axe + crate + pickaxe / knife + shears** (`ACP` / `K S`) — tools are built into the machine.
- Hopper in from top/sides, out from bottom (unchanged)
- Right-click GUI
- **Auto cutting board**: runs every Farmer's Delight cutting recipe by input item (no tool in GUI)
- Wood chain: **planks → 2 slabs**, then **slabs → 1 stick** (not planks→sticks in one step)
- Beds: **1 bed → 3 matching wool + 3 oak planks**
- Reverse crafting kept as fallback when no cutting recipe matches

### Recipe book
Crafting recipes auto-unlock when you pick up the required items (crates/sacks unlock their unpack recipes).

### Pack integrations
Shared tags/recipes so Cultural Delight, Miner's Delight, More Delight, Veggies Delight, Brewin' and Chewin', and Some Assembly Required ingredients interchange where it makes sense (squid/calamari, cheese, bread slices, cut veggies, dough/tomato bridges). Works **alongside** Compat Delight — we fill FD-addon gaps, not Create/Ad Astra bridges.

## Requirements
- Minecraft **1.21.1**
- **NeoForge** 21.1.x
- **Farmer's Delight**

## Links
- Source: https://github.com/Renzolc/farmers-delight-tweaks
- Releases: https://github.com/Renzolc/farmers-delight-tweaks/releases

## License
**MIT**

Not affiliated with Farmer's Delight / vectorwing — a community compat & QoL add-on.
