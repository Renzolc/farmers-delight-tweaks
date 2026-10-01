# 1.0.16

- The Decrafter Upgrade reverses any crafting recipe, not only this mod's cutting-board uncrafts. Ingredient counts are the full craft. Tag ingredients use the item already chosen in this mod's full-uncraft data when there is one, otherwise the first registered item in the tag.
- Items with no crafting recipe are skipped. If this mod already has a full uncraft for that item, that is still used. Damaged tools are uncrafted anyway.
- Backpacks, shulker boxes, bundles, and Sophisticated Storage items are matched by item id. Their stored items (and installed storage upgrades) are inserted into the backpack first, then the craft ingredients. If any of that does not fit, the input stays and nothing is cleared. Linked backpack endpoints and containers holding fluid are left alone.
- The cutting board and Decrafter block are unchanged and still return partial salvage.
- The upgrade is still crafted with 4 leather and 1 Decrafter.
