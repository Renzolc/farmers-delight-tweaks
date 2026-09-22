# Modded storage candidates (Renzo NeoForge 1.21.1)

Research-only list of **crops / seeds / saplings / similar farmable produce** that lack Farmer's Delight–style crates or seed/sapling sacks in the Renzo delight ecosystem. **Do not implement recipes yet** — parent agent will.

## Scope & method

| Source | Notes |
| --- | --- |
| Jar cache | `/workspace/renzo-mods-scan/jars` (27 delight-focused jars previously copied from Renzo) |
| Existing fd_storage_compat | `ModBlocks.java` + recipes under `data/fd_storage_compat/recipe/` |
| Cross-check | Farmer's Delight, Crate Delight, Storage Delight (furniture only), Quark compressed crops, and per-addon crates (Cultural / Veggies / Fruits / Expanded / Rustic / Crabber's / My Nether's / Miner's / End's) |
| Remote mods folder | `Shell` with `machineId `6bb91937-bb6c-414f-9c54-703abcae940a`` was **not usable from this executor** (commands stayed on the box). Full Renzo `mods/` listing (Create, Atmosphere, Evercrops, Sable Companion, Chipped, etc.) was **not** re-scanned live. Re-run jar listing on the user machine before treating this as exhaustive. |

**Out of scope for this pass:** food-tag bridges; cooked dishes; decorative wood furniture; Quark/Crate Delight duplicates of vanilla items already handled by fd_storage_compat or Quark.

### Already covered (do not re-add)

**fd_storage_compat:** apple, sweet berry, glow berry, cocoa, sugar cane, nether wart, chorus fruit crates; wheat/pumpkin/melon/beetroot/torchflower/cabbage/tomato seed sacks; oak/spruce/birch/jungle/acacia/dark oak/cherry sapling sacks + mangrove propagule sack.

**Farmer's Delight:** carrot/potato/beetroot/cabbage/tomato/onion crates; rice bag/bale; straw bale.

**Crate Delight (vanilla + Expanded peanut/cinnamon/salt):** wheat/pumpkin/melon/beetroot seed bags; apple/berry/glowberry/cocoa/mushroom/egg/fish/etc.; `expandeddelight:peanut` crate; `ground_cinnamon` bag; salt bag.

**Cultural Delights:** avocado, corn cob, cucumber, eggplant, white eggplant, pickle crates (+ avocado bundle).

**Veggies Delight:** bellpepper, broccoli, cauliflower, garlic, sweet potato, turnip, zucchini crates.

**Fruits Delight:** crates for apple/bayberry/blueberry/cranberry/fig/hawberry/kiwi/lemon/lychee/mango/mangosteen/orange/peach/pear/persimmon/pineapple (not durian; not seeds/saplings).

**Expanded Delight:** asparagus, chili pepper, sweet potato crates; cranberry bag.

**Rustic Delight:** colored bell pepper crates; cotton boll crate; coffee bean bags; cotton/bell-pepper seed bags.

**Others:** Crabber's coconut + sea pickle crates + palm wood (no palm sapling sack); My Nether's bullet pepper crate; Miner's cave carrot crate; End's chorus fruit crate; Supplementaries `flax_block` (9× flax); Quark blossom trees have saplings but no sacks (see candidates).

---

## Candidate table

Suggested types match fd_storage_compat conventions: **crate** (9× produce), **seed_sack** (9× seeds/plantables), **sapling_sack** (9× saplings).

| mod id | item id | suggested storage | already covered? | notes |
| --- | --- | --- | --- | --- |
| culturaldelights | cucumber_seeds | seed_sack | **no** | In `#c:seeds`; crop crate exists |
| culturaldelights | eggplant_seeds | seed_sack | **no** | |
| culturaldelights | corn_kernels | seed_sack | **no** | Tagged as corn seed; cob has crate |
| culturaldelights | avocado_pit | seed_sack | **no** | Tagged `#c:seeds`; also plantable via sapling |
| culturaldelights | avocado_sapling | sapling_sack | **no** | In `#minecraft:saplings` |
| culturaldelights | cinnamon | crate | **no** | From jungle log cutting; unused `cinnamon_bag_*` textures exist in jar (no block) |
| veggiesdelight | bellpepper_seeds | seed_sack | **no** | Produce crate exists |
| veggiesdelight | broccoli_seeds | seed_sack | **no** | |
| veggiesdelight | cauliflower_seeds | seed_sack | **no** | |
| veggiesdelight | turnip_seeds | seed_sack | **no** | |
| veggiesdelight | zucchini_seeds | seed_sack | **no** | |
| veggiesdelight | garlic_clove | seed_sack | **no** | Plantable (`villager_plantable_seeds`); garlic bulb already has crate |
| expandeddelight | asparagus_seeds | seed_sack | **no** | |
| expandeddelight | chili_pepper_seeds | seed_sack | **no** | |
| expandeddelight | cinnamon_sapling | sapling_sack | **no** | Full cinnamon wood set |
| expandeddelight | cinnamon_stick | crate | **no** | Harvest product; Crate Delight only bags **ground** cinnamon |
| expandeddelight | cinnamon | crate | optional | Ground/processed form — prefer stick; skip if redundant with stick crate |
| fruitsdelight | apple_sapling | sapling_sack | **no** | Distinct from oak; fruit crate exists |
| fruitsdelight | bayberry_sapling | sapling_sack | **no** | |
| fruitsdelight | fig_sapling | sapling_sack | **no** | |
| fruitsdelight | hawberry_sapling | sapling_sack | **no** | |
| fruitsdelight | kiwi_sapling | sapling_sack | **no** | |
| fruitsdelight | lychee_sapling | sapling_sack | **no** | |
| fruitsdelight | mango_sapling | sapling_sack | **no** | |
| fruitsdelight | mangosteen_sapling | sapling_sack | **no** | |
| fruitsdelight | orange_sapling | sapling_sack | **no** | |
| fruitsdelight | peach_sapling | sapling_sack | **no** | |
| fruitsdelight | pear_sapling | sapling_sack | **no** | |
| fruitsdelight | persimmon_sapling | sapling_sack | **no** | |
| fruitsdelight | pineapple_sapling | sapling_sack | **no** | |
| fruitsdelight | durian_sapling | sapling_sack | **no** | Lang: "Durian Seed" |
| fruitsdelight | lemon_seeds | seed_sack | **no** | Lemon crate exists |
| fruitsdelight | hamimelon_seeds | seed_sack | **no** | Melon-like crop; no seed bag |
| fruitsdelight | durian | crate | **no** | Whole fruit; flesh/jams exist; **no** `durian_crate` in Fruits Delight |
| fruitsdelight | durian_flesh | crate | optional | Prefer whole `durian` crate first |
| fruitsdelight | hamimelon_slice | crate | optional | Analogous to Crate Delight pumpkin_slice crate |
| crabbersdelight | palm_sapling | sapling_sack | **no** | Coconut already has crate |
| quark | ancient_sapling | sapling_sack | **no** | Ashen tree |
| quark | blue_blossom_sapling | sapling_sack | **no** | Frosty Trumpet |
| quark | lavender_blossom_sapling | sapling_sack | **no** | Serene Trumpet |
| quark | orange_blossom_sapling | sapling_sack | **no** | Warm Trumpet |
| quark | red_blossom_sapling | sapling_sack | **no** | Fiery Trumpet |
| quark | yellow_blossom_sapling | sapling_sack | **no** | Sunny Trumpet |
| quark | ancient_fruit | crate | optional | Enchanted Fruit — rare; low farm volume |
| supplementaries | flax_seeds | seed_sack | **no** | `flax_block` already stores flax fiber |
| mynethersdelight | powdery_cane | crate | optional | Nether cane-like; pepper already crated |
| minersdelight | copper_carrot | crate | skip/low | Crafted novelty, not a crop |

### Explicit non-candidates (false positives / already stored)

| item | reason |
| --- | --- |
| culturaldelights popcorn / creamed_corn | cooked/processed |
| farmersdelight hot_cocoa / mushroom_rice / rotten_tomato | food / trash |
| veggiesdelight garlic_rice_with_cauliflower | cooked |
| expandeddelight cinnamon_rice / cinnamon_apples | cooked |
| ends_delight chorus_fruit_grain / ender_* | processed grains |
| tide spore_stalker* | mob, not crop |
| supplementaries flax | has `flax_block` |
| peanut, coffee_beans, cotton_*, bell peppers (rustic), all veggies/cultural produce crates | upstream crates/bags |
| storagedelight:* | cabinets/drawers only |

---

## Prioritized implementation list (Renzo high-value)

Implement in this order for maximum farm QoL with least duplication:

### P0 — Seed sacks for delight crops that already have produce crates (highest ROI)

1. `culturaldelights:corn_kernels` → seed_sack  
2. `culturaldelights:cucumber_seeds` → seed_sack  
3. `culturaldelights:eggplant_seeds` → seed_sack  
4. `veggiesdelight:bellpepper_seeds` → seed_sack  
5. `veggiesdelight:broccoli_seeds` → seed_sack  
6. `veggiesdelight:cauliflower_seeds` → seed_sack  
7. `veggiesdelight:zucchini_seeds` → seed_sack  
8. `veggiesdelight:turnip_seeds` → seed_sack  
9. `veggiesdelight:garlic_clove` → seed_sack  
10. `expandeddelight:asparagus_seeds` → seed_sack  
11. `expandeddelight:chili_pepper_seeds` → seed_sack  
12. `fruitsdelight:lemon_seeds` → seed_sack  
13. `fruitsdelight:hamimelon_seeds` → seed_sack  
14. `supplementaries:flax_seeds` → seed_sack  

### P1 — Sapling sacks (orchard / tree farms)

15. `culturaldelights:avocado_sapling` (+ optional `avocado_pit` seed_sack)  
16. `expandeddelight:cinnamon_sapling`  
17. All **14** `fruitsdelight:*_sapling` (apple, bayberry, durian, fig, hawberry, kiwi, lychee, mango, mangosteen, orange, peach, pear, persimmon, pineapple)  
18. `crabbersdelight:palm_sapling`  
19. Quark blossom/ancient saplings (6) — nice-to-have cosmetics  

### P2 — Missing produce crates

20. `fruitsdelight:durian` → crate  
21. `expandeddelight:cinnamon_stick` → crate (and/or `culturaldelights:cinnamon`)  
22. Optional: `fruitsdelight:hamimelon_slice` crate  

### P3 — After live Renzo mods scan

Re-list ``C:\Users\alexm\curseforge\minecraft\Instances\Renzo\mods`` on machine `6bb91937-bb6c-414f-9c54-703abcae940a` and diff for farming mods **not** in the jar cache, especially: Create (no crops), Atmosphere / Evercrops / Sable Companion / Farmers Respite / Ube's Delight / Croptopia-likes / any extra wood saplings. Add rows here before coding those.

---

## Counts

| Bucket | Count |
| --- | --- |
| **Implement candidates (recommended, excl. optional/skip)** | **46** |
| — seed_sack | 17 (incl. avocado_pit + garlic_clove) |
| — sapling_sack | 23 (14 Fruits + avocado + cinnamon + palm + 6 Quark) |
| — crate | 2+ (durian, cinnamon_stick; + optional slice/cultural cinnamon) |
| Optional / low priority rows in table | ~6 |
| Explicit non-candidates documented | ~15+ |

**Strict “new storage blocks to add” if following P0–P2 only (no Quark, no optional):**  
14 seed sacks (P0) + 1 avocado sapling (+pit) + 1 cinnamon sapling + 14 Fruits saplings + 1 palm + 2 crates ≈ **34** blocks.

**With Quark saplings:** ≈ **40** blocks.

---

## Notes for implementer

- Gate all recipes with `neoforge:mod_loaded` for the source mod.  
- Prefer **not** duplicating items that Cultural/Veggies/Fruits/Expanded/Rustic/Crate Delight already crate.  
- fd_storage_compat already overlaps Crate Delight/Quark on some vanilla items — keep that pattern only where Renzo needs FD-style textures; for modded seeds/saplings there is little upstream coverage.  
- Cultural Delights ships cinnamon bag **textures** without a registered bag block — safe to add our own sack/crate.  
- Garlic: crate the bulb (done upstream); sack the **clove** (plantable).  
- Peanut is plantable as `expandeddelight:peanut` and already has Crate Delight crate — no seed item.

