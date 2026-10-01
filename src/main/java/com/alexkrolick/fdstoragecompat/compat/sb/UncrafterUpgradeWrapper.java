package com.alexkrolick.fdstoragecompat.compat.sb;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.alexkrolick.fdstoragecompat.recipe.FullUncraftRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;

/**
 * Shared logic for both Uncrafter upgrades.
 * Simple fills output slots the player takes by hand.
 * Advanced inserts the same results straight into the backpack.
 */
public class UncrafterUpgradeWrapper extends UpgradeWrapperBase<UncrafterUpgradeWrapper, SimpleUncrafterUpgradeItem>
        implements ITickableUpgrade {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOTS = 9;
    public static final int TOTAL_SLOTS = 1 + OUTPUT_SLOTS;

    private final StatefulComponentItemHandler inventory;

    public UncrafterUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        inventory = new StatefulComponentItemHandler(upgrade, ModCoreDataComponents.LENIENT_CONTAINER.get(), TOTAL_SLOTS) {
            @Override
            protected void onContentsChanged(int slot, ItemStack oldStack, ItemStack newStack) {
                super.onContentsChanged(slot, oldStack, newStack);
                save();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return slot == INPUT_SLOT;
            }
        };
    }

    public StatefulComponentItemHandler getInventory() {
        return inventory;
    }

    public boolean isAdvanced() {
        return upgrade.getItem() instanceof AdvancedUncrafterUpgradeItem;
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }

    @Override
    public void tick(@Nullable Entity entity, Level level, BlockPos pos) {
        if (level.isClientSide || !isEnabled() || isInCooldown(level)) {
            return;
        }
        boolean worked = process(level, pos);
        setCooldown(level, worked ? 5 : 10);
    }

    /** Also called when the input slot changes so an open GUI does not wait on the next world tick. */
    public boolean process(Level level, BlockPos dropPos) {
        if (level == null || level.isClientSide || !isEnabled()) {
            return false;
        }
        return isAdvanced() ? depositIntoBackpack(level, dropPos) : depositIntoOutputSlots(level);
    }

    private boolean depositIntoOutputSlots(Level level) {
        if (!outputsEmpty()) {
            return false;
        }
        Optional<Resolved> resolved = resolve(level);
        if (resolved.isEmpty()) {
            return false;
        }
        Resolved op = resolved.get();
        if (op.results().size() > OUTPUT_SLOTS) {
            return false;
        }
        inventory.extractItem(INPUT_SLOT, op.consume(), false);
        for (int i = 0; i < op.results().size(); i++) {
            inventory.setStackInSlot(1 + i, op.results().get(i));
        }
        return true;
    }

    private boolean depositIntoBackpack(Level level, BlockPos dropPos) {
        Optional<Resolved> resolved = resolve(level);
        if (resolved.isEmpty()) {
            return false;
        }
        Resolved op = resolved.get();
        IItemHandler backpack = storageWrapper.getInventoryForUpgradeProcessing();
        if (!canFit(backpack, op.results())) {
            return false;
        }
        inventory.extractItem(INPUT_SLOT, op.consume(), false);
        for (ItemStack stack : op.results()) {
            ItemStack leftover = InventoryHelper.insertIntoInventory(stack, backpack, false);
            if (!leftover.isEmpty()) {
                net.minecraft.world.Containers.dropItemStack(level, dropPos.getX() + 0.5, dropPos.getY() + 0.5, dropPos.getZ() + 0.5, leftover);
            }
        }
        return true;
    }

    private Optional<Resolved> resolve(Level level) {
        ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
        if (input.isEmpty() || (input.isDamageableItem() && input.isDamaged())) {
            return Optional.empty();
        }
        Optional<FullUncraftRecipe> recipe = FullUncraftRecipe.find(level, input);
        if (recipe.isEmpty()) {
            return Optional.empty();
        }
        int consume = recipe.get().consume();
        if (input.getCount() < consume) {
            return Optional.empty();
        }
        List<ItemStack> results = recipe.get().copyResults();
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Resolved(consume, results));
    }

    private boolean outputsEmpty() {
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            if (!inventory.getStackInSlot(1 + i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    static boolean canFit(IItemHandler inventory, List<ItemStack> outputs) {
        ItemStack[] sim = new ItemStack[inventory.getSlots()];
        for (int i = 0; i < sim.length; i++) {
            sim[i] = inventory.getStackInSlot(i).copy();
        }
        for (ItemStack out : outputs) {
            ItemStack remaining = out.copy();
            for (int i = 0; i < sim.length && !remaining.isEmpty(); i++) {
                if (sim[i].isEmpty() || !ItemStack.isSameItemSameComponents(sim[i], remaining)) {
                    continue;
                }
                int limit = Math.min(inventory.getSlotLimit(i), sim[i].getMaxStackSize());
                int space = limit - sim[i].getCount();
                if (space <= 0) {
                    continue;
                }
                int move = Math.min(space, remaining.getCount());
                sim[i].grow(move);
                remaining.shrink(move);
            }
            for (int i = 0; i < sim.length && !remaining.isEmpty(); i++) {
                if (!sim[i].isEmpty() || !inventory.isItemValid(i, remaining)) {
                    continue;
                }
                int move = Math.min(Math.min(inventory.getSlotLimit(i), remaining.getMaxStackSize()), remaining.getCount());
                if (move <= 0) {
                    continue;
                }
                sim[i] = remaining.copyWithCount(move);
                remaining.shrink(move);
            }
            if (!remaining.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private record Resolved(int consume, List<ItemStack> results) {}

    /** Exposed for the client preview. */
    public List<ItemStack> preview(Level level) {
        ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
        if (input.isEmpty() || level == null) {
            return List.of();
        }
        return FullUncraftRecipe.find(level, input).map(recipe -> {
            if (input.getCount() < recipe.consume()) {
                return List.<ItemStack>of();
            }
            return recipe.copyResults();
        }).orElse(List.of());
    }

}
