package dev.renzo.fdstoragecompat.compat.sb;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import dev.renzo.fdstoragecompat.recipe.FullUncraftRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;

/**
 * One input slot. A matching full-uncraft recipe moves those ingredients into the backpack.
 */
public class DecrafterUpgradeWrapper extends UpgradeWrapperBase<DecrafterUpgradeWrapper, DecrafterUpgradeItem>
        implements ITickableUpgrade {
    public static final int INPUT_SLOT = 0;

    private final StatefulComponentItemHandler inventory;
    private boolean processing;

    public DecrafterUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        inventory = new StatefulComponentItemHandler(upgrade, ModCoreDataComponents.LENIENT_CONTAINER.get(), 1) {
            @Override
            protected void onContentsChanged(int slot, ItemStack oldStack, ItemStack newStack) {
                super.onContentsChanged(slot, oldStack, newStack);
                save();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                // Sophisticated Core syncs every slot with setStackInSlot, including air to clear it.
                // setStackInSlot throws if isItemValid is false, so empty stacks must be accepted.
                return stack.isEmpty() || super.isItemValid(slot, stack);
            }
        };
    }

    public StatefulComponentItemHandler getInventory() {
        return inventory;
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

    public boolean process(Level level, BlockPos ignoredPos) {
        if (processing || level == null || level.isClientSide || !isEnabled()) {
            return false;
        }
        processing = true;
        try {
            return depositIntoBackpack(level);
        } finally {
            processing = false;
        }
    }

    private boolean depositIntoBackpack(Level level) {
        Optional<Resolved> resolved = resolve(level);
        if (resolved.isEmpty()) {
            return false;
        }
        Resolved op = resolved.get();
        IItemHandler backpack = storageWrapper.getInventoryForUpgradeProcessing();
        if (!insertAll(backpack, op.results())) {
            return false;
        }
        inventory.extractItem(INPUT_SLOT, op.consume(), false);
        return true;
    }

    /**
     * Commits through the backpack's own inserter. If any result does not fit, the inventory is restored
     * and the input is left alone.
     */
    private static boolean insertAll(IItemHandler backpack, List<ItemStack> results) {
        if (!(backpack instanceof IItemHandlerModifiable modifiable)) {
            return false;
        }
        ItemStack[] before = new ItemStack[backpack.getSlots()];
        for (int i = 0; i < before.length; i++) {
            before[i] = backpack.getStackInSlot(i).copy();
        }
        boolean fitted = true;
        for (ItemStack stack : results) {
            ItemStack leftover = InventoryHelper.insertIntoInventory(stack.copy(), backpack, false);
            if (!leftover.isEmpty()) {
                fitted = false;
                break;
            }
        }
        if (!fitted) {
            for (int i = 0; i < before.length; i++) {
                modifiable.setStackInSlot(i, before[i]);
            }
        }
        return fitted;
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

    /** Client preview of what the next uncraft will insert. Not a container slot. */
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

    private record Resolved(int consume, List<ItemStack> results) {}
}
