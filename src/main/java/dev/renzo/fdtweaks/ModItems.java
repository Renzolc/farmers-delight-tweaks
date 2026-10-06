package dev.renzo.fdtweaks;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FdStorageCompat.MOD_ID);

    static {
        for (DeferredBlock<Block> block : ModBlocks.ALL) {
            ITEMS.registerSimpleBlockItem(block);
        }
    }

    private ModItems() {}
}
