package dev.renzo.fdstoragecompat.compat.sb;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.renzo.fdstoragecompat.FdStorageCompat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageFluidHandler;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.linkedstorage.LinkedStorageEndpointStackState;
import net.p3pp3rf1y.sophisticatedcore.linkedstorage.LinkedStorageStackLifecycle;

/**
 * Copies stored items out of a container before it is uncrafted.
 * Empty means "do not uncraft": the contents could not be read safely.
 */
public final class StoredContents {
    private StoredContents() {
    }

    public record Extraction(List<ItemStack> stacks, Runnable clearAfterInsert) {
    }

    public static Optional<Extraction> extract(Level level, ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.of(new Extraction(List.of(), () -> {
            }));
        }
        if (stack.getItem() instanceof BackpackItem) {
            return backpack(stack);
        }
        if (ModList.get().isLoaded("sophisticatedstorage")
                && "sophisticatedstorage".equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace())) {
            return sophisticatedStorage(level, stack);
        }
        return vanilla(stack);
    }

    private static Optional<Extraction> backpack(ItemStack stack) {
        // A linked endpoint is a pointer at another backpack. Uncrafting it would take that backpack's items.
        if (LinkedStorageStackLifecycle.classifyEndpoint(stack) == LinkedStorageEndpointStackState.ENDPOINT) {
            return Optional.empty();
        }
        IBackpackWrapper wrapper = BackpackWrapper.fromStack(stack);
        if (holdsFluid(wrapper.getFluidHandler())) {
            return Optional.empty();
        }
        List<ItemStack> stacks = new ArrayList<>();
        copySlots(wrapper.getInventoryHandler(), stacks);
        copySlots(wrapper.getUpgradeHandler(), stacks);
        return Optional.of(new Extraction(stacks, () -> {
            clear(wrapper.getInventoryHandler());
            clear(wrapper.getUpgradeHandler());
        }));
    }

    private static boolean holdsFluid(Optional<IStorageFluidHandler> fluid) {
        if (fluid.isEmpty()) {
            return false;
        }
        IStorageFluidHandler handler = fluid.get();
        for (int tank = 0; tank < handler.getTanks(); tank++) {
            if (!handler.getFluidInTank(tank).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static Optional<Extraction> sophisticatedStorage(Level level, ItemStack stack) {
        try {
            Class<?> type = Class.forName("net.p3pp3rf1y.sophisticatedstorage.item.StackStorageWrapper");
            Method fromStack = type.getMethod("fromStack", net.minecraft.core.HolderLookup.Provider.class, ItemStack.class);
            Object wrapper = fromStack.invoke(null, level.registryAccess(), stack);
            if (!((boolean) type.getMethod("hasContents").invoke(wrapper))) {
                return Optional.of(new Extraction(List.of(), () -> {
                }));
            }
            if (holdsFluidReflective(wrapper)) {
                return Optional.empty();
            }
            IItemHandler inventory = (IItemHandler) wrapper.getClass().getMethod("getInventoryHandler").invoke(wrapper);
            IItemHandler upgrades = (IItemHandler) wrapper.getClass().getMethod("getUpgradeHandler").invoke(wrapper);
            List<ItemStack> stacks = new ArrayList<>();
            copySlots(inventory, stacks);
            copySlots(upgrades, stacks);
            return Optional.of(new Extraction(stacks, () -> {
                clear(inventory);
                clear(upgrades);
            }));
        } catch (ReflectiveOperationException | ClassCastException e) {
            FdStorageCompat.LOGGER.warn("Decrafter Upgrade left a storage item alone; its contents could not be read", e);
            return Optional.empty();
        }
    }

    private static boolean holdsFluidReflective(Object wrapper) {
        try {
            Object optional = wrapper.getClass().getMethod("getFluidHandler").invoke(wrapper);
            if (!(optional instanceof Optional<?> fluids) || fluids.isEmpty()) {
                return false;
            }
            Object handler = fluids.get();
            int tanks = (int) handler.getClass().getMethod("getTanks").invoke(handler);
            Method fluidInTank = handler.getClass().getMethod("getFluidInTank", int.class);
            for (int tank = 0; tank < tanks; tank++) {
                Object fluid = fluidInTank.invoke(handler, tank);
                if (!((boolean) fluid.getClass().getMethod("isEmpty").invoke(fluid))) {
                    return true;
                }
            }
            return false;
        } catch (ReflectiveOperationException e) {
            return true;
        }
    }

    private static Optional<Extraction> vanilla(ItemStack stack) {
        List<ItemStack> stacks = new ArrayList<>();
        IItemHandler capability = stack.getCapability(Capabilities.ItemHandler.ITEM);
        if (capability != null) {
            copySlots(capability, stacks);
            return Optional.of(new Extraction(stacks, () -> clear(capability)));
        }
        ItemContainerContents container = stack.get(net.minecraft.core.component.DataComponents.CONTAINER);
        if (container != null) {
            container.stream().filter(item -> !item.isEmpty()).forEach(item -> stacks.add(item.copy()));
        }
        BundleContents bundle = stack.get(net.minecraft.core.component.DataComponents.BUNDLE_CONTENTS);
        if (bundle != null) {
            bundle.itemCopyStream().filter(item -> !item.isEmpty()).forEach(stacks::add);
        }
        ItemContainerContents lenient = stack.get(ModCoreDataComponents.LENIENT_CONTAINER.get());
        if (lenient != null) {
            lenient.stream().filter(item -> !item.isEmpty()).forEach(item -> stacks.add(item.copy()));
        }
        // The input item is consumed, so these components go with it. Nothing else to clear.
        return Optional.of(new Extraction(stacks, () -> {
        }));
    }

    private static void copySlots(IItemHandler handler, List<ItemStack> into) {
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                into.add(stack.copy());
            }
        }
    }

    private static void clear(IItemHandler handler) {
        if (!(handler instanceof IItemHandlerModifiable modifiable)) {
            return;
        }
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            if (!handler.getStackInSlot(slot).isEmpty()) {
                modifiable.setStackInSlot(slot, ItemStack.EMPTY);
            }
        }
    }
}
