package dev.renzo.fdstoragecompat.recipe;

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
     * Prefer a crafting recipe whose result is this item. full_uncraft is only the fallback
     * for items this mod uncrafts that have no crafting recipe (and the source of tag picks).
     * Damage is ignored: a damaged tool still uncrafts.
     */
    public static Optional<Result> resolve(Level level, ItemStack input) {
        if (input.isEmpty() || level == null) {
            return Optional.empty();
        }
        List<Item> preferred = preferredItems(level, input);
        Optional<Result> crafted = fromCrafting(level, input, preferred);
        if (crafted.isPresent()) {
            Result result = crafted.get();
            // A crafting recipe exists. Do not fall through to full_uncraft when the stack is short.
            if (input.getCount() < result.consume()) {
                return Optional.empty();
            }
            return crafted;
        }
        return fromFullUncraft(level, input);
    }

    private static Optional<Result> fromCrafting(Level level, ItemStack input, List<Item> preferred) {
        List<Candidate> matches = new ArrayList<>();
        for (RecipeHolder<CraftingRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
            CraftingRecipe recipe = holder.value();
            if (recipe.isSpecial()) {
                continue;
            }
            ItemStack result = recipe.getResultItem(level.registryAccess());
            if (result.isEmpty() || result.getItem() != input.getItem() || result.getCount() < 1) {
                continue;
            }
            List<ItemStack> ingredients = ingredientsOf(recipe, preferred);
            if (ingredients == null || ingredients.isEmpty()) {
                continue;
            }
            matches.add(new Candidate(holder.id().toString(), result.getCount(), ingredients, overlap(ingredients, preferred)));
        }
        if (matches.isEmpty()) {
            return Optional.empty();
        }
        matches.sort(Comparator
                .comparingInt(Candidate::overlap).reversed()
                .thenComparingInt(Candidate::consume)
                .thenComparingInt(c -> c.ingredients.size())
                .thenComparing(Candidate::id));
        Candidate best = matches.getFirst();
        return Optional.of(new Result(best.consume, copy(best.ingredients)));
    }

    private static Optional<Result> fromFullUncraft(Level level, ItemStack input) {
        Optional<FullUncraftRecipe> recipe = FullUncraftRecipe.find(level, input);
        if (recipe.isEmpty() || input.getCount() < recipe.get().consume()) {
            return Optional.empty();
        }
        List<ItemStack> results = recipe.get().copyResults();
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Result(recipe.get().consume(), results));
    }

    private static List<Item> preferredItems(Level level, ItemStack input) {
        List<Item> items = new ArrayList<>();
        for (FullUncraftRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FULL_UNCRAFT.get()).stream().map(h -> h.value()).toList()) {
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
    private static List<ItemStack> ingredientsOf(CraftingRecipe recipe, List<Item> preferred) {
        List<ItemStack> out = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
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

    private static int overlap(List<ItemStack> ingredients, List<Item> preferred) {
        int n = 0;
        for (ItemStack stack : ingredients) {
            if (preferred.contains(stack.getItem())) {
                n++;
            }
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

    private record Candidate(String id, int consume, List<ItemStack> ingredients, int overlap) {
    }
}
