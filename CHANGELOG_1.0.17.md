# 1.0.17

- The Decrafter Upgrade was skipping Sophisticated Backpacks tier crafts because those recipes are marked special (they only copy the backpack's contents onto the result). A diamond backpack now returns the real craft: 8 diamonds and 1 gold backpack.
- The same lookup keeps every grid cell, so counts are not collapsed to one, and a shorter ingredient list no longer beats the full pattern. Smithing upgrades (netherite backpack) return the template, the previous backpack, and the ingot.
- Stored contents are still moved into the backpack first, then the craft ingredients, and only if all of it fits. The cutting board and Decrafter block are unchanged.
