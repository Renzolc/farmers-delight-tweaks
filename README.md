# Farmer's Delight Tweaks (`fd_storage_compat`)

NeoForge 1.21.1 add-on for [Farmer's Delight](https://github.com/vectorwing/FarmersDelight).

> ## ⚠️ Updating from 1.0.x? Install Disassembly Delight too
>
> **Since 1.1.0 the Decrafter and the Decrafter Upgrade are no longer part of this mod.** They moved to a separate mod, **[Disassembly Delight](https://github.com/Renzolc/disassembly-delight)**, where they are the **Disassembly Table** and the **Disassembly Table Upgrade**.
>
> If your world has Decrafters, or Decrafter Upgrades in backpacks, install **Disassembly Delight 1.0.0+ together with Farmer's Delight Tweaks 1.1.0 before you open the world**. Disassembly Delight maps the old ids (`fd_storage_compat:decrafter`, `fd_storage_compat:decrafter_upgrade`) onto its own, so placed Decrafters keep their contents and upgrades stay in their backpacks.
>
> **If you update to 1.1.0 without Disassembly Delight, every Decrafter and Decrafter Upgrade in the world is deleted when it loads.** Farmer's Delight Tweaks logs a warning at startup when Disassembly Delight is missing. Keep a backup.
>
> Disassembly Delight refuses to load next to Farmer's Delight Tweaks 1.0.x (two copies of the same machine), so update both together.

## Features

- Storage crates and sacks for missing vanilla foods, seeds, and saplings
- Modded seed/sapling sacks + produce crates for Cultural / Veggies / Expanded / Fruits / Crabber's Delight, Quark blossom+ancient saplings, and Supplementaries flax seeds (recipes gated with `neoforge:mod_loaded`)
- Cutting-board uncrafts for FD rope/canvas/net/fences and knives
- Pack-compat cutting recipes for Supplementaries fiber/decor, Quark rope, More Delight knives, and Handcrafted sheets
- Cutting-board uncrafts for Create kinetics/machines/components when Create is loaded
- Cutting-board uncrafts for Sophisticated Backpacks + Sophisticated Storage (backpacks, storage tiers, upgrades) when those mods are loaded
- A cutting-board guard: a container that still holds items is never cut (see below)

The automatic machine that runs these cutting-board recipes (the old Decrafter) and the backpack upgrade that returns full crafting ingredients are in [Disassembly Delight](https://github.com/Renzolc/disassembly-delight). Disassembly Delight works without this mod, and this mod works without it.

## Integrations

See [INTEGRATIONS.md](INTEGRATIONS.md) for Renzo-pack FD addon tag/recipe bridges (squid/calamari, cheese, bread slices, cut veggies, SAR ingredients).

### Create

When `create` is installed, cutting-board recipes salvage common kinetics, machines, logistics parts, casings, and tools. High-ratio cheap crafts (e.g. shafts ×8) are skipped on the cutting board. Decorative palette blocks and most dye variants are skipped.

### Sophisticated Backpacks / Storage

When `sophisticatedbackpacks` / `sophisticatedstorage` are installed, cutting-board recipes salvage backpacks, storage containers, and upgrades back toward previous tiers / main materials. Recipes match by item id. Optional Chipped/Sawmill upgrades and creative infinity upgrades are skipped.

Crafting recipes auto-unlock in the recipe book when you obtain the required ingredients (or a crate/sack for unpack recipes).

### Cutting board and stored contents

The cutting board cannot hand back what is inside a container, so it refuses to cut one that still holds something (a Sophisticated chest or backpack, a Create toolbox, a shulker box...). You get a message and the item stays on the board: take it off and empty it first. Empty containers cut as before. Anything whose contents this mod cannot read (fluids, mobs, unrolled loot tables, pick-block block entity data, unknown item data) is treated as full.

The rules are in `fd_storage_compat/container_rules.json`. `./gradlew check` runs the JUnit tests and a coverage check against the modpack scan (skipped when the scan folder is absent), and `./gradlew runGameTestServer` runs the in-world tests.

## Build

```bash
./gradlew build
```

Jar: `build/libs/farmersdelight_tweaks-*.jar`

## License

MIT

## Attribution

Crate bottom / bag layout textures are derived from Farmer's Delight assets (vectorwing) for personal/compat use. Vanilla item icons used for compositing are Mojang assets.
