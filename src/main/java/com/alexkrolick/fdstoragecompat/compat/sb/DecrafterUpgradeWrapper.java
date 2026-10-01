package com.alexkrolick.fdstoragecompat.compat.sb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.alexkrolick.fdstoragecompat.recipe.CraftUncraft;

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
 * One input slot. Any crafting recipe (or this mod's full_uncraft) is reversed into the backpack.
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
        // Contents first, then craft ingredients. All of it fits, or the input stays.
        if (!insertAll(backpack, op.results())) {
            return false;
        }
        op.clearContents().run();
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
        return resolveStack(level, input);
    }

    /** Client preview of what the next uncraft will insert. Not a container slot. */
    public List<ItemStack> preview(Level level) {
        ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
        return resolveStack(level, input).map(Resolved::results).orElse(List.of());
    }

    private static Optional<Resolved> resolveStack(Level level, ItemStack input) {
        if (input.isEmpty() || level == null) {
            return Optional.empty();
        }
        Optional<CraftUncraft.Result> crafted = CraftUncraft.resolve(level, input);
        if (crafted.isEmpty()) {
            return Optional.empty();
        }
        Optional<StoredContents.Extraction> contents = StoredContents.extract(level, input);
        if (contents.isEmpty()) {
            return Optional.empty();
        }
        List<ItemStack> results = new ArrayList<>();
        results.addAll(contents.get().stacks());
        results.addAll(crafted.get().results());
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Resolved(crafted.get().consume(), results, contents.get().clearAfterInsert()));
    }

    private record Resolved(int consume, List<ItemStack> results, Runnable clearContents) {}
}
