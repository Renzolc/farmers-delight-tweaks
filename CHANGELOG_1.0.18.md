# 1.0.18

- Fixed a crash when an item was placed in the Decrafter Upgrade (server "Ticking entity" and client "Rendering screen" NullPointerException in `CraftUncraft.resolve`). The upgrade compares crafting, smithing and full_uncraft results; most items have only some of these, and 1.0.17 put the missing ones as null into `List.of(...)`, which throws. The missing sources are now skipped.
- A recipe that cannot be read (null result, broken ingredient, odd modded recipe) is skipped and logged once instead of crashing the server or the backpack screen. The Decrafter block gets the same per-recipe guards.
- Pass-through: an item that cannot be decrafted no longer gets stuck.
  - Decrafter block: the item moves to the output slots unchanged so hoppers and pipes carry it on. If a recipe needs a bigger stack (for example 4 torches) the Decrafter waits while the stack is still growing, then passes the short stack through after about 5 seconds with no new items.
  - Decrafter Upgrade: the item moves into the backpack unchanged (whatever fits), clearing the input slot.
- Decrafting results are otherwise unchanged.
