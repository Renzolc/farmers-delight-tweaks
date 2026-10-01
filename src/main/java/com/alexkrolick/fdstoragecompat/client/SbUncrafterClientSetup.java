package com.alexkrolick.fdstoragecompat.client;

import com.alexkrolick.fdstoragecompat.compat.sb.SbUncrafterSetup;
import com.alexkrolick.fdstoragecompat.compat.sb.UncrafterUpgradeContainer;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;

public final class SbUncrafterClientSetup {
    private SbUncrafterClientSetup() {}

    public static void init(IEventBus modBus) {
        modBus.addListener(SbUncrafterClientSetup::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            UpgradeGuiManager.registerTab(SbUncrafterSetup.SIMPLE_CONTAINER,
                    (UncrafterUpgradeContainer uc, Position p, StorageScreenBase<?> s) -> new UncrafterUpgradeTab(uc, p, s, false));
            UpgradeGuiManager.registerTab(SbUncrafterSetup.ADVANCED_CONTAINER,
                    (UncrafterUpgradeContainer uc, Position p, StorageScreenBase<?> s) -> new UncrafterUpgradeTab(uc, p, s, true));
        });
    }
}
