# Farmer's Delight Tweaks (`fd_storage_compat`)

NeoForge 1.21.1 add-on for [Farmer's Delight](https://github.com/vectorwing/FarmersDelight).

## Features

- Storage crates and sacks for missing vanilla foods, seeds, and saplings
- Modded seed/sapling sacks + produce crates for Cultural / Veggies / Expanded / Fruits / Crabber's Delight, Quark blossom+ancient saplings, and Supplementaries flax seeds (recipes gated with `neoforge:mod_loaded`)
- Cutting-board uncrafts for FD rope/canvas/net/fences and knives
- Pack-compat cutting recipes for Supplementaries fiber/decor, Quark rope, More Delight knives, and Handcrafted sheets
- Cutting-board / Decrafter uncrafts for Create kinetics/machines/components when Create is loaded
- Cutting-board / Decrafter uncrafts for Sophisticated Backpacks + Sophisticated Storage (backpacks, storage tiers, upgrades) when those mods are loaded
- Decrafter Upgrade for Sophisticated Backpacks (optional). It reverses any crafting recipe at full ingredient counts into the backpack. The cutting board and Decrafter block stay partial.

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

When `sophisticatedbackpacks` / `sophisticatedstorage` are installed, cutting-board recipes (also used by the Decrafter) salvage backpacks, storage containers, and upgrades back toward previous tiers / main materials. Recipes match by item id. The Decrafter block and the Decrafter Upgrade hand back everything stored inside first (see Stored contents below). The cutting board cannot return stored items, so it refuses to cut a container that still holds something; empty it first. Optional Chipped/Sawmill upgrades and creative infinity upgrades are skipped.

Crafting recipes auto-unlock in the recipe book when you obtain the required ingredients (or a crate/sack for unpack recipes).


### Stored contents

The Decrafter block and the Decrafter Upgrade never destroy what is inside a container. When they decraft one, its stored items come out first, then its own decraft results. If all of that does not fit (the block's 9 output slots, or the backpack), nothing is used up and the container waits. In the Decrafter block, a container whose contents could never fit in 9 empty slots (a full shulker box) passes through whole.

Read and emptied: shulker boxes, bundles, decorated pots, charged crossbows, pick-block copies of chests, barrels, furnaces, cabinets and baskets, Sophisticated Backpacks and Storage (inventory and upgrades, including Sophisticated Emerald Upgrade storage), Sophisticated upgrades with an inventory, Create toolboxes, Supplementaries safe, sack, presents, jar, urn, quiver and lunch basket, Farmer's Delight and Miner's Delight cooking pots and the skillet, Tide rods and the fish satchel, Construction Wand cores and the void sack. Create packages are unwrapped: the contents come out and the package is used up.

Passed through unchanged: Create minecart contraptions, Some Assembly Required sandwiches, Sophisticated Storage in Motion carts and boats, and anything holding a fluid, a mob, an unrolled loot table, pick-block block entity data, a linked Sophisticated endpoint, or item data this mod cannot read. The rules are in `fd_storage_compat/container_rules.json`; `./gradlew check` runs the JUnit tests and a coverage check against the modpack scan, and `./gradlew runGameTestServer` runs in-world Decrafter tests.

### Decrafter Upgrade

When Sophisticated Backpacks is installed, one upgrade is added: **Decrafter Upgrade** (`fd_storage_compat:decrafter_upgrade`).

Craft it shapeless with 4 leather and 1 Decrafter. Only one fits in a backpack.

Open the backpack, open the upgrade, and put an item in the input slot. If that item has a crafting recipe, the full ingredient counts are inserted into the backpack. Sophisticated Backpacks tier upgrades are included, so a diamond backpack returns 8 diamonds and the gold backpack it was crafted from. Tag ingredients use the same item this mod already picked in its full-uncraft data, or the first registered item in the tag. Items with no crafting recipe are left alone, except uncrafts this mod already defines. Damaged tools are uncrafted anyway. If the ingredients (and anything stored inside the item) do not fit, the input stays. There is no take-by-hand grid.

Containers match by item id even when they have contents. Stored items are moved into the backpack first, then the craft ingredients, and the container is emptied only after that insert succeeds (see Stored contents above). The cutting board and Decrafter block still do not return full counts, and they still do not uncraft the upgrade.

The cutting board and the Decrafter block keep their partial salvage. Full counts exist only through this upgrade, including a full return of the upgrade itself (4 leather + 1 Decrafter). The cutting board has no recipe for it, and the Decrafter block will not reverse-craft it, so you need a Decrafter Upgrade already installed and a spare upgrade in its input.
