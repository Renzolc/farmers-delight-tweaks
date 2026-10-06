# 1.1.0

## ⚠️ Read before updating: the Decrafter moved to Disassembly Delight

The **Decrafter** block and the **Decrafter Upgrade** (Sophisticated Backpacks) are no longer in Farmer's Delight Tweaks. They are now the **Disassembly Table** and the **Disassembly Table Upgrade** in a separate mod, **[Disassembly Delight](https://github.com/Renzolc/disassembly-delight)** (1.0.0).

- **Install Disassembly Delight together with this update before opening an existing world.** It maps `fd_storage_compat:decrafter` and `fd_storage_compat:decrafter_upgrade` onto its own blocks and items, so placed Decrafters keep their inventory and upgrades stay in their backpacks.
- **Without Disassembly Delight, every Decrafter and Decrafter Upgrade in the world is removed when the world loads.** Farmer's Delight Tweaks now logs a warning at startup when Disassembly Delight is not installed. Back up your world first.
- Disassembly Delight will not load next to Farmer's Delight Tweaks 1.0.x, so update both mods at the same time.
- Datapacks that add `fd_storage_compat:full_uncraft` recipes keep working with Disassembly Delight installed (the recipe type is mapped too).

## Changes

- Removed: Decrafter block, block entity, menu and screen; Decrafter Upgrade; the `fd_storage_compat:full_uncraft` recipe type and all full_uncraft recipes; the Decrafter and Decrafter Upgrade crafting recipes. The upgrade-only access transformer is gone too.
- Kept: every crate and sack, all cutting-board recipes (Farmer's Delight, Create, Sophisticated Backpacks/Storage, pack compat), the tag and Some Assembly Required integrations.
- The cutting-board guard stays: a container that still holds items is not cut. It keeps the read-only part of the container-contents rules (`fd_storage_compat/container_rules.json`).
- The creative tab icon is now the Apple Crate.
- Tests: JUnit tests for the contents reader and the guard, GameTests that check the mod works on its own (no Decrafter ids registered, crates and sacks present, guard sees contents), and the container coverage check.
