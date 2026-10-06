package dev.renzo.fdtweaks;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod(FdStorageCompat.MOD_ID)
public class FdStorageCompat {
    public static final String MOD_ID = "fd_storage_compat";
    public static final String DISASSEMBLY_DELIGHT = "disassembly_delight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FdStorageCompat(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        // Rule C: never cut a storage item that still holds something on a Farmer's Delight cutting board.
        dev.renzo.fdtweaks.contents.CuttingBoardGuard.register();
        warnIfDisassemblyDelightMissing();
        LOGGER.info("Farmer's Delight Tweaks loaded");
    }

    /**
     * 1.1.0 moved the Decrafter and the Decrafter Upgrade into the separate Disassembly Delight mod
     * (disassembly_delight:disassembler / disassembler_upgrade), which maps the old ids onto the new ones.
     * Without it, Decrafters in existing worlds and Decrafter Upgrades in backpacks are dropped as unknown ids.
     */
    private static void warnIfDisassemblyDelightMissing() {
        if (ModList.get().isLoaded(DISASSEMBLY_DELIGHT)) {
            return;
        }
        LOGGER.warn("Farmer's Delight Tweaks 1.1.0 no longer contains the Decrafter or the Decrafter Upgrade. "
                + "They are now the Disassembly Table and the Disassembly Table Upgrade in the Disassembly Delight mod "
                + "(https://github.com/Renzolc/disassembly-delight). Install Disassembly Delight before opening a world "
                + "that has Decrafters (fd_storage_compat:decrafter) or Decrafter Upgrades (fd_storage_compat:decrafter_upgrade), "
                + "or they will be removed from that world.");
    }
}
