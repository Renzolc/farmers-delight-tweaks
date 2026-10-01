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

public final class DecrafterUpgradeSetup {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FdStorageCompat.MOD_ID);

    public static final DeferredHolder<Item, DecrafterUpgradeItem> DECRAFTER_UPGRADE =
            ITEMS.register("decrafter_upgrade", DecrafterUpgradeItem::new);

    public static final UpgradeContainerType<DecrafterUpgradeWrapper, DecrafterUpgradeContainer> CONTAINER =
            new UpgradeContainerType<>(DecrafterUpgradeContainer::new);

    private DecrafterUpgradeSetup() {}

    public static void init(IEventBus modBus) {
        ITEMS.register(modBus);
        modBus.addListener(DecrafterUpgradeSetup::registerContainers);
        FdStorageCompat.LOGGER.info("Decrafter Upgrade registered");
    }

    private static void registerContainers(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.MENU)) {
            return;
        }
        UpgradeContainerRegistry.register(DECRAFTER_UPGRADE.getId(), CONTAINER);
    }
}
