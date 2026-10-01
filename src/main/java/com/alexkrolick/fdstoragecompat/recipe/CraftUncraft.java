package com.alexkrolick.fdstoragecompat.recipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.Level;

/**
 * Full-count reverse of a crafting recipe for the Decrafter Upgrade only.
 * The cutting board and Decrafter block do not use this.
 */
public final class CraftUncraft {
    private CraftUncraft() {
    }

    public record Result(int consume, List<ItemStack> results) {
    }

    /**
     * Prefer a crafting recipe whose result is this item, including shaped recipes that mods mark
     * special only so they can copy NBT (Sophisticated Backpacks tier upgrades). full_uncraft fills
     * in items with no crafting or smithing recipe, and supplies tag representatives.
     * Damage is ignored: a damaged tool still uncrafts.
     */
    public static Optional<Result> resolve(Level level, ItemStack input) {
        if (input.isEmpty() || level == null) {
            return Optional.empty();
        }
        List<Item> preferred = preferredItems(level, input);
        Result best = null;
        for (Result candidate : List.of(
                fromCrafting(level, input, preferred).orElse(null),
                fromSmithing(level, input, preferred).orElse(null),
                fromFullUncraft(level, input).orElse(null))) {
            best = fuller(best, candidate, preferred);
        }
        if (best == null || input.getCount() < best.consume()) {
            return Optional.empty();
        }
        return Optional.of(best);
    }

    /**
     * A shorter list must not beat the real craft. Covering the known full-uncraft items
     * (the previous-tier backpack, the tag representative) wins, then the larger total count.
     */
    private static Result fuller(Result current, Result candidate, List<Item> preferred) {
        if (candidate == null) {
            return current;
        }
        if (current == null) {
            return candidate;
        }
        int preferredDelta = coverage(candidate.results(), preferred) - coverage(current.results(), preferred);
        if (preferredDelta != 0) {
            return preferredDelta > 0 ? candidate : current;
        }
        int countDelta = total(candidate.results()) - total(current.results());
        if (countDelta != 0) {
            return countDelta > 0 ? candidate : current;
        }
        return current;
    }

    private static Optional<Result> fromCrafting(Level level, ItemStack input, List<Item> preferred) {
        List<Candidate> matches = new ArrayList<>();
        for (RecipeHolder<CraftingRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
            CraftingRecipe recipe = holder.value();
            ItemStack result = recipe.getResultItem(level.registryAccess());
            // Custom recipes (dye, repair) report no result. Tier upgrades are special only to copy
            // components, but they still have a shaped pattern and a real result. Do not skip those.
            if (result.isEmpty() || result.getItem() != input.getItem() || result.getCount() < 1) {
                continue;
            }
            List<ItemStack> ingredients = ingredientsOf(ingredientList(recipe), preferred);
            if (ingredients == null || ingredients.isEmpty()) {
                continue;
            }
            matches.add(new Candidate(holder.id().toString(), result.getCount(), ingredients, coverage(ingredients, preferred), total(ingredients)));
        }
        return pick(matches);
    }

    private static Optional<Result> fromSmithing(Level level, ItemStack input, List<Item> preferred) {
        List<Candidate> matches = new ArrayList<>();
        for (RecipeHolder<SmithingRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING)) {
            if (!(holder.value() instanceof SmithingTransformRecipe smithing)) {
                continue;
            }
            ItemStack result = smithing.getResultItem(level.registryAccess());
            if (result.isEmpty() || result.getItem() != input.getItem() || result.getCount() < 1) {
                continue;
            }
            List<Ingredient> ingredients = new ArrayList<>(3);
            Ingredient template = smithingField(smithing, "template");
            Ingredient base = smithingField(smithing, "base");
            Ingredient addition = smithingField(smithing, "addition");
            if (template == null || base == null || addition == null) {
                continue;
            }
            ingredients.add(template);
            ingredients.add(base);
            ingredients.add(addition);
            List<ItemStack> resolved = ingredientsOf(ingredients, preferred);
            if (resolved == null || resolved.isEmpty()) {
                continue;
            }
            matches.add(new Candidate(holder.id().toString(), result.getCount(), resolved, coverage(resolved, preferred), total(resolved)));
        }
        return pick(matches);
    }

    private static Ingredient smithingField(SmithingTransformRecipe recipe, String name) {
        try {
            Field field = SmithingTransformRecipe.class.getDeclaredField(name);
            field.setAccessible(true);
            return (Ingredient) field.get(recipe);
        } catch (ReflectiveOperationException | ClassCastException e) {
            return null;
        }
    }

    private static Optional<Result> pick(List<Candidate> matches) {
        if (matches.isEmpty()) {
            return Optional.empty();
        }
        matches.sort(Comparator
                .comparingInt(Candidate::coverage).reversed()
                .thenComparing(Comparator.comparingInt(Candidate::total).reversed())
                .thenComparing(Candidate::id));
        Candidate best = matches.getFirst();
        return Optional.of(new Result(best.consume, copy(best.ingredients)));
    }

    private static Optional<Result> fromFullUncraft(Level level, ItemStack input) {
        Optional<FullUncraftRecipe> recipe = FullUncraftRecipe.find(level, input);
        if (recipe.isEmpty()) {
            return Optional.empty();
        }
        List<ItemStack> results = recipe.get().copyResults();
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Result(recipe.get().consume(), results));
    }

    private static List<Ingredient> ingredientList(CraftingRecipe recipe) {
        // ShapedRecipe.getIngredients() is the expanded pattern, one entry per grid cell,
        // so eight diamonds stay eight. Read the pattern directly in case a wrapper overrides it.
        if (recipe instanceof ShapedRecipe shaped) {
            return shaped.pattern.ingredients();
        }
        return recipe.getIngredients();
    }

    private static List<Item> preferredItems(Level level, ItemStack input) {
        List<Item> items = new ArrayList<>();
        for (FullUncraftRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FULL_UNCRAFT.get()).stream().map(holder -> holder.value()).toList()) {
            if (!recipe.input().test(input)) {
                continue;
            }
            for (ItemStack stack : recipe.results()) {
                if (!stack.isEmpty() && !items.contains(stack.getItem())) {
                    items.add(stack.getItem());
                }
            }
        }
        return items;
    }

    /**
     * @return ingredient stacks, or null when a tag/ingredient has no registered item
     */
    private static List<ItemStack> ingredientsOf(List<Ingredient> ingredients, List<Item> preferred) {
        List<ItemStack> out = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.isEmpty()) {
                continue;
            }
            ItemStack[] options = ingredient.getItems();
            if (options.length == 0) {
                return null;
            }
            ItemStack chosen = choose(options, preferred);
            if (chosen == null) {
                // Every option leaves a bucket or similar. It was not consumed, so it is not returned.
                continue;
            }
            boolean merged = false;
            for (ItemStack existing : out) {
                if (ItemStack.isSameItemSameComponents(existing, chosen)) {
                    existing.grow(chosen.getCount());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                out.add(chosen);
            }
        }
        return out;
    }

    private static ItemStack choose(ItemStack[] options, List<Item> preferred) {
        for (Item want : preferred) {
            for (ItemStack option : options) {
                if (!option.isEmpty() && option.is(want)) {
                    return option.copyWithCount(1);
                }
            }
        }
        for (ItemStack option : options) {
            if (!option.isEmpty() && !option.hasCraftingRemainingItem()) {
                return option.copyWithCount(1);
            }
        }
        return null;
    }

    private static int coverage(List<ItemStack> ingredients, List<Item> preferred) {
        int n = 0;
        for (Item want : preferred) {
            for (ItemStack stack : ingredients) {
                if (stack.is(want)) {
                    n++;
                    break;
                }
            }
        }
        return n;
    }

    private static int total(List<ItemStack> stacks) {
        int n = 0;
        for (ItemStack stack : stacks) {
            n += stack.getCount();
        }
        return n;
    }

    private static List<ItemStack> copy(List<ItemStack> stacks) {
        List<ItemStack> out = new ArrayList<>(stacks.size());
        for (ItemStack stack : stacks) {
            out.add(stack.copy());
        }
        return out;
    }

    private record Candidate(String id, int consume, List<ItemStack> ingredients, int coverage, int total) {
    }
}
