package dev.renzo.fdstoragecompat.compat.sb;

import dev.renzo.fdstoragecompat.FdStorageCompat;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

public final class SbUncrafterSetup {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FdStorageCompat.MOD_ID);

    public static final DeferredHolder<Item, SimpleUncrafterUpgradeItem> SIMPLE_UNCRAFTER =
            ITEMS.register("simple_uncrafter_upgrade", SimpleUncrafterUpgradeItem::new);
    public static final DeferredHolder<Item, AdvancedUncrafterUpgradeItem> ADVANCED_UNCRAFTER =
            ITEMS.register("advanced_uncrafter_upgrade", AdvancedUncrafterUpgradeItem::new);

    public static final UpgradeContainerType<UncrafterUpgradeWrapper, UncrafterUpgradeContainer> SIMPLE_CONTAINER =
            new UpgradeContainerType<>(UncrafterUpgradeContainer::new);
    public static final UpgradeContainerType<UncrafterUpgradeWrapper, UncrafterUpgradeContainer> ADVANCED_CONTAINER =
            new UpgradeContainerType<>(UncrafterUpgradeContainer::new);

    private SbUncrafterSetup() {}

    public static void init(IEventBus modBus) {
        ITEMS.register(modBus);
        modBus.addListener(SbUncrafterSetup::registerContainers);
        FdStorageCompat.LOGGER.info("Simple and Advanced Uncrafter upgrades registered");
    }

    private static void registerContainers(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.MENU)) {
            return;
        }
        UpgradeContainerRegistry.register(SIMPLE_UNCRAFTER.getId(), SIMPLE_CONTAINER);
        UpgradeContainerRegistry.register(ADVANCED_UNCRAFTER.getId(), ADVANCED_CONTAINER);
    }
}
