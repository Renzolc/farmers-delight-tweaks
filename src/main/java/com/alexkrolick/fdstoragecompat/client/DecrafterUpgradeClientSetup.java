package com.alexkrolick.fdstoragecompat.client;

import com.alexkrolick.fdstoragecompat.compat.sb.DecrafterUpgradeContainer;
import com.alexkrolick.fdstoragecompat.compat.sb.DecrafterUpgradeSetup;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;

public final class DecrafterUpgradeClientSetup {
    private DecrafterUpgradeClientSetup() {}

    public static void init(IEventBus modBus) {
        modBus.addListener(DecrafterUpgradeClientSetup::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> UpgradeGuiManager.registerTab(DecrafterUpgradeSetup.CONTAINER,
                (DecrafterUpgradeContainer uc, Position p, StorageScreenBase<?> s) -> new DecrafterUpgradeTab(uc, p, s)));
    }
}
