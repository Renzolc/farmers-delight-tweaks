package com.alexkrolick.fdstoragecompat;

import org.slf4j.Logger;

import com.alexkrolick.fdstoragecompat.blockentity.DecrafterBlockEntity;
import com.alexkrolick.fdstoragecompat.client.ClientModEvents;
import com.mojang.logging.LogUtils;

import net.minecraft.core.Direction;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod(FdStorageCompat.MOD_ID)
public class FdStorageCompat {
    public static final String MOD_ID = "fd_storage_compat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FdStorageCompat(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::registerCapabilities);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientModEvents.register(modEventBus);
        }
        LOGGER.info("Farmer's Delight Tweaks loaded");
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.DECRAFTER.get(),
                (DecrafterBlockEntity be, Direction side) -> be.getHandlerForSide(side));
    }
}
