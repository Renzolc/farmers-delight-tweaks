package com.alexkrolick.fdstoragecompat.client;

import com.alexkrolick.fdstoragecompat.ModMenus;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public final class ClientModEvents {
    private ClientModEvents() {}

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientModEvents::registerScreens);
    }

    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.DECRAFTER.get(), DecrafterScreen::new);
    }
}
