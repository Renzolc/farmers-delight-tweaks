package com.alexkrolick.fdstoragecompat.blockentity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.alexkrolick.fdstoragecompat.ModBlockEntities;
import com.alexkrolick.fdstoragecompat.ModBlocks;
import com.alexkrolick.fdstoragecompat.menu.DecrafterMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

public class DecrafterBlockEntity extends BlockEntity implements MenuProvider {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOTS = 9;
    public static final int TOTAL_SLOTS = 1 + OUTPUT_SLOTS;
    public static final int PROCESS_INTERVAL = 20; // 1 second

    private final ItemStackHandler items = new ItemStackHandler(TOTAL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return !stack.isEmpty();
        }
    };

    /** Top/sides: insert into input only; no extract. */
    private final IItemHandler inputHandler = new RangedWrapper(items, INPUT_SLOT, INPUT_SLOT + 1) {
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    };

    /** Bottom: extract from outputs only; no insert. */
    private final IItemHandler outputHandler = new RangedWrapper(items, INPUT_SLOT + 1, TOTAL_SLOTS) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }
    };

    private int progress;

    public DecrafterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DECRAFTER.get(), pos, state);
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public IItemHandler getHandlerForSide(@Nullable Direction side) {
        if (side == null) {
            return items;
        }
        if (side == Direction.DOWN) {
            return outputHandler;
        }
        return inputHandler;
    }

    public NonNullList<ItemStack> getDrops() {
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stack = items.getStackInSlot(i);
            if (!stack.isEmpty()) {
                list.add(stack.copy());
            }
        }
        return list;
    }

    public int getRedstoneSignal() {
        int filled = 0;
        int total = 0;
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stack = items.getStackInSlot(i);
            total += items.getSlotLimit(i);
            filled += stack.getCount();
        }
        if (total == 0) {
            return 0;
        }
        return (int) Math.floor(1 + (14.0 * filled) / total);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DecrafterBlockEntity be) {
        be.progress++;
        if (be.progress < PROCESS_INTERVAL) {
            return;
        }
        be.progress = 0;
        be.tryDecraft();
    }

    private void tryDecraft() {
        if (level == null || level.isClientSide) {
            return;
        }
        ItemStack input = items.getStackInSlot(INPUT_SLOT);
        if (input.isEmpty()) {
            return;
        }
        // Never decraft the Decrafter itself
        if (input.is(ModBlocks.DECRAFTER.asItem())) {
            return;
        }
        // Skip damaged tools/armor (would produce full undamaged ingredients unfairly)
        if (input.isDamageableItem() && input.isDamaged()) {
            return;
        }

        Optional<List<ItemStack>> results = resolveDecraft(input);
        if (results.isEmpty()) {
            return;
        }
        List<ItemStack> outputs = results.get();
        if (!canInsertAll(outputs)) {
            return;
        }

        // Consume input: for planks special-case consume 1; for recipes consume result count
        int consume = input.is(ItemTags.PLANKS) ? 1 : resolveConsumeCount(input);
        if (consume <= 0 || input.getCount() < consume) {
            return;
        }
        input.shrink(consume);
        items.setStackInSlot(INPUT_SLOT, input.isEmpty() ? ItemStack.EMPTY : input);

        for (ItemStack out : outputs) {
            ItemStack remaining = ItemHandlerHelper.insertItemStacked(outputInsertView(), out.copy(), false);
            // Should be empty if canInsertAll passed; drop safety not needed in BE
            if (!remaining.isEmpty()) {
                // Extremely unlikely — leave remainder in void rather than duplicate input
            }
        }
        setChanged();
    }

    private int resolveConsumeCount(ItemStack input) {
        return findBestRecipe(input)
                .map(r -> r.value().getResultItem(level.registryAccess()).getCount())
                .orElse(1);
    }

    private Optional<List<ItemStack>> resolveDecraft(ItemStack input) {
        // Special-case: any plank -> 2 sticks (prefer over reverse-craft to logs)
        if (input.is(ItemTags.PLANKS)) {
            return Optional.of(List.of(new ItemStack(Items.STICK, 2)));
        }
        return findBestRecipe(input).map(holder -> ingredientsOf(holder.value()));
    }

    private Optional<RecipeHolder<CraftingRecipe>> findBestRecipe(ItemStack input) {
        List<RecipeHolder<CraftingRecipe>> matches = new ArrayList<>();
        for (RecipeHolder<CraftingRecipe> holder : level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
            CraftingRecipe recipe = holder.value();
            if (recipe.isSpecial()) {
                continue;
            }
            ItemStack result = recipe.getResultItem(level.registryAccess());
            // Match by item type (ignore NBT/components differences on crafted results)
            if (result.isEmpty() || result.getItem() != input.getItem()) {
                continue;
            }
            if (input.getCount() < result.getCount()) {
                continue;
            }
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            if (ingredients.isEmpty()) {
                continue;
            }
            // Skip unsafe container leftovers (e.g. recipes that would invent buckets)
            if (hasUnsafeRemainingItems(ingredients)) {
                continue;
            }
            // Skip recipe results that are damageable tools if we're somehow matching damaged — already gated
            List<ItemStack> resolved = ingredientsOf(recipe);
            if (resolved.isEmpty()) {
                continue;
            }
            matches.add(holder);
        }
        if (matches.isEmpty()) {
            return Optional.empty();
        }
        // Prefer fewest non-empty ingredients; stable by recipe id
        matches.sort(Comparator
                .comparingInt((RecipeHolder<CraftingRecipe> h) -> countIngredients(h.value()))
                .thenComparing(h -> h.id().toString()));
        return Optional.of(matches.getFirst());
    }

    private static int countIngredients(CraftingRecipe recipe) {
        int n = 0;
        for (Ingredient ing : recipe.getIngredients()) {
            if (!ing.isEmpty()) {
                n++;
            }
        }
        return n;
    }

    private static boolean hasUnsafeRemainingItems(NonNullList<Ingredient> ingredients) {
        for (Ingredient ing : ingredients) {
            if (ing.isEmpty()) {
                continue;
            }
            ItemStack[] stacks = ing.getItems();
            if (stacks.length == 0) {
                return true;
            }
            // If every option leaves a crafting remainder (bucket etc.), skip — would duplicate containers
            boolean allRemain = true;
            for (ItemStack s : stacks) {
                if (!s.hasCraftingRemainingItem()) {
                    allRemain = false;
                    break;
                }
            }
            if (allRemain) {
                return true;
            }
        }
        return false;
    }

    private static List<ItemStack> ingredientsOf(CraftingRecipe recipe) {
        List<ItemStack> out = new ArrayList<>();
        for (Ingredient ing : recipe.getIngredients()) {
            if (ing.isEmpty()) {
                continue;
            }
            ItemStack[] options = ing.getItems();
            if (options.length == 0) {
                return List.of(); // unresolved tag
            }
            // Prefer first option that does not have a crafting remainder
            ItemStack chosen = options[0];
            for (ItemStack opt : options) {
                if (!opt.hasCraftingRemainingItem()) {
                    chosen = opt;
                    break;
                }
            }
            ItemStack stack = chosen.copy();
            stack.setCount(1);
            // Merge identical stacks
            boolean merged = false;
            for (ItemStack existing : out) {
                if (ItemStack.isSameItemSameComponents(existing, stack)) {
                    existing.grow(1);
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                out.add(stack);
            }
        }
        return out;
    }

    private boolean canInsertAll(List<ItemStack> outputs) {
        // Simulate insert into a copy of output slots
        ItemStack[] sim = new ItemStack[OUTPUT_SLOTS];
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            sim[i] = items.getStackInSlot(INPUT_SLOT + 1 + i).copy();
        }
        for (ItemStack out : outputs) {
            ItemStack remaining = out.copy();
            // Fill existing stacks first
            for (int i = 0; i < OUTPUT_SLOTS && !remaining.isEmpty(); i++) {
                if (!sim[i].isEmpty() && ItemStack.isSameItemSameComponents(sim[i], remaining)) {
                    int space = Math.min(sim[i].getMaxStackSize(), items.getSlotLimit(INPUT_SLOT + 1 + i)) - sim[i].getCount();
                    if (space > 0) {
                        int move = Math.min(space, remaining.getCount());
                        sim[i].grow(move);
                        remaining.shrink(move);
                    }
                }
            }
            // Then empty slots
            for (int i = 0; i < OUTPUT_SLOTS && !remaining.isEmpty(); i++) {
                if (sim[i].isEmpty()) {
                    int move = Math.min(remaining.getMaxStackSize(), remaining.getCount());
                    sim[i] = remaining.copyWithCount(move);
                    remaining.shrink(move);
                }
            }
            if (!remaining.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /** View used for actual inserts into output slots only. */
    private IItemHandler outputInsertView() {
        return new RangedWrapper(items, INPUT_SLOT + 1, TOTAL_SLOTS);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Items", items.serializeNBT(registries));
        tag.putInt("Progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Items")) {
            items.deserializeNBT(registries, tag.getCompound("Items"));
        }
        progress = tag.getInt("Progress");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.fd_storage_compat.decrafter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new DecrafterMenu(containerId, playerInventory, this);
    }

    public boolean stillValid(Player player) {
        return net.minecraft.world.Container.stillValidBlockEntity(this, player);
    }
}
