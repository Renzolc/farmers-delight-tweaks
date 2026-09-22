package com.alexkrolick.fdstoragecompat;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FdStorageCompat.MOD_ID);

    public static final Supplier<CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.fd_storage_compat"))
                    .icon(() -> new ItemStack(ModBlocks.DECRAFTER.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModBlocks.DECRAFTER.get());
                        for (DeferredBlock<Block> block : ModBlocks.ALL) {
                            output.accept(block.get());
                        }
                    })
                    .build());

    private ModCreativeTabs() {}
}
