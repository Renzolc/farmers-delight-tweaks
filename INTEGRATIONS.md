# Farmer's Delight Tweaks — Renzo pack integrations (1.0.4)

Additive datapack bridges so Farmer's Delight ecosystem addons share ingredients.
Nothing from other mods is deleted or replaced (`replace: true` is never used).
Optional items/recipes are gated with `neoforge:mod_loaded`.

## Compat Delight (`compatdelight`) — already covered vs what we add

Compat Delight focuses on **non-FD** mods (Create, Ad Astra, Rats, YUNG's, etc.):
knives, exotic meats, and its own cucumber/eggplant/garlic crops under `c:vegetables/*`
and Diet tags. It does **not** unify Cultural Delights ↔ Miner's Delight squid/calamari,
More Delight ↔ SAR bread slices, or cut veggies across Cultural / Veggies Delight / SAR.

**We extend, we do not fight it.** Our tags are additive on the same `c:` namespaces.

## Unified item groups

| Group | Tag(s) | Items bridged |
| --- | --- | --- |
| Raw squid | `c:foods/squid`, `c:foods/raw_squid` | Cultural `squid` / `glow_squid`, Miner's `squid` / `glow_squid` (+ cooked where appropriate on `c:foods/squid`) |
| Cooked squid | `c:foods/cooked_squid` | Cultural `cooked_squid`, Miner's `baked_squid` |
| Raw calamari / tentacles | `c:foods/raw_calamari`, `c:foods/tentacles` | Cultural `raw_calamari` ↔ Miner's `tentacles` |
| Cooked calamari / tentacles | `c:foods/cooked_calamari`, `c:foods/tentacles` | Cultural `cooked_calamari` ↔ Miner's `baked_tentacles` |
| Cheese wedges | `c:foods/cheese`, `brewinandchewin:foods/cheese_wedge` | Cultural `cheese_wedge`, B&C flaxen/scarlet wedges |
| Bread slices | `c:foods/bread_slice`, `c:bread_slices`, `c:bread_slices/wheat` | SAR bread/toasted slices ↔ More Delight bread slice / toast |
| Tomato equivalents | `c:crops/tomato`, `c:foods/tomato` | Cultural `smoked_tomato` |
| Leafy / salad greens | `c:foods/leafy_green` | Veggies `dandelion_leaf`, Cultural cut cucumber / pickle |
| Cut vegetables | `fd_storage_compat:cut_vegetables` (+ `c:foods/vegetable`) | Cultural cuts, Veggies slices/florets/cloves, More Delight diced potatoes, SAR tomato/onion/carrot cuts, FD cabbage leaf |
| Dough | `c:foods/dough` | Cultural corn dough, FD wheat dough, Veggies sweet potato dough |
| Cabbage-roll fillers | `farmersdelight:cabbage_roll_ingredients` | Cut veggies + calamari/tentacles (legacy tag; rolls also use `c:foods/vegetable` / `c:foods/safe_raw_fish`) |
| Safe raw fish | `c:foods/safe_raw_fish` | Squid / calamari / tentacles so FD cabbage rolls accept them |

## Some Assembly Required

Registered sandwich ingredients (with `neoforge:mod_loaded`) under
`data/someassemblyrequired/someassemblyrequired/ingredients/`:

- Cultural: cut cucumber/avocado/eggplant/pickle, pickle, smoked tomato, cheese wedge, raw/cooked calamari, cooked squid
- Veggies Delight: zucchini slice, cauliflower floret, garlic clove, dandelion leaf
- More Delight: diced potatoes, bread slice, toast
- Brewin' and Chewin': flaxen & scarlet cheese wedges
- Miner's Delight: tentacles, baked tentacles, baked squid

Also adds More Delight bread/toast to `someassemblyrequired:sandwich_bread`.

## Additive recipes (`fd_storage_compat`)

| Recipe | Purpose |
| --- | --- |
| `culturaldelights/calamari_roll_from_tag` | Calamari roll accepts `#c:foods/raw_calamari` (tentacles or cultural calamari) |
| `moredelight/hamburger_with_cheese_from_tag` | Cheese burger accepts `#c:foods/cheese` |
| `moredelight/toast_with_cheese_from_tag` | Toast + any cheese wedge |
| `moredelight/tomato_sandwich_from_slices` | Tomato sandwich from SAR tomato slices |
| `moredelight/chicken_salad_cut_veggies` | Chicken salad with `#fd_storage_compat:cut_vegetables` |
| `minersdelight/squid_sandwich_from_tag` | Squid sandwich from `#c:foods/cooked_squid` |
| Cooking bridges | Cultural raw squid → Miner's baked squid; tentacles ↔ cooked/baked calamari (smelting/smoking/campfire) |
| Cutting bridges | Cultural squid ↔ Miner's tentacles; Miner's squid ↔ Cultural raw calamari |

## Mods targeted (Renzo)

Farmers Delight, Cultural Delights, Miner's Delight, More Delight, Veggies Delight,
Some Assembly Required, Brewin' and Chewin'. Other delight addons remain compatible
via shared `c:` tags when present.
