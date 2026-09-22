package com.alexkrolick.fdstoragecompat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.alexkrolick.fdstoragecompat.block.DecrafterBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FdStorageCompat.MOD_ID);

    /** All storage blocks in creative/tab order. */
    public static final List<DeferredBlock<Block>> ALL = new ArrayList<>();

    
    public static final DeferredBlock<Block> DECRAFTER = BLOCKS.register("decrafter",
            () -> new DecrafterBlock(storageProps()));

    // Food crates
    public static final DeferredBlock<Block> APPLE_CRATE = crate("apple_crate");
    public static final DeferredBlock<Block> SWEET_BERRY_CRATE = crate("sweet_berry_crate");
    public static final DeferredBlock<Block> GLOW_BERRY_CRATE = crate("glow_berry_crate");
    public static final DeferredBlock<Block> COCOA_BEAN_CRATE = crate("cocoa_bean_crate");
    public static final DeferredBlock<Block> SUGAR_CANE_CRATE = crate("sugar_cane_crate");
    public static final DeferredBlock<Block> NETHER_WART_CRATE = crate("nether_wart_crate");
    public static final DeferredBlock<Block> CHORUS_FRUIT_CRATE = crate("chorus_fruit_crate");

    // Seed sacks
    public static final DeferredBlock<Block> WHEAT_SEED_SACK = sack("wheat_seed_sack");
    public static final DeferredBlock<Block> PUMPKIN_SEED_SACK = sack("pumpkin_seed_sack");
    public static final DeferredBlock<Block> MELON_SEED_SACK = sack("melon_seed_sack");
    public static final DeferredBlock<Block> BEETROOT_SEED_SACK = sack("beetroot_seed_sack");
    public static final DeferredBlock<Block> TORCHFLOWER_SEED_SACK = sack("torchflower_seed_sack");
    public static final DeferredBlock<Block> CABBAGE_SEED_SACK = sack("cabbage_seed_sack");
    public static final DeferredBlock<Block> TOMATO_SEED_SACK = sack("tomato_seed_sack");

    // Sapling sacks
    public static final DeferredBlock<Block> OAK_SAPLING_SACK = sack("oak_sapling_sack");
    public static final DeferredBlock<Block> SPRUCE_SAPLING_SACK = sack("spruce_sapling_sack");
    public static final DeferredBlock<Block> BIRCH_SAPLING_SACK = sack("birch_sapling_sack");
    public static final DeferredBlock<Block> JUNGLE_SAPLING_SACK = sack("jungle_sapling_sack");
    public static final DeferredBlock<Block> ACACIA_SAPLING_SACK = sack("acacia_sapling_sack");
    public static final DeferredBlock<Block> DARK_OAK_SAPLING_SACK = sack("dark_oak_sapling_sack");
    public static final DeferredBlock<Block> CHERRY_SAPLING_SACK = sack("cherry_sapling_sack");
    public static final DeferredBlock<Block> MANGROVE_PROPAGULE_SACK = sack("mangrove_propagule_sack");

    private ModBlocks() {}

    private static DeferredBlock<Block> crate(String name) {
        return register(name, () -> new Block(storageProps()));
    }

    private static DeferredBlock<Block> sack(String name) {
        return register(name, () -> new Block(storageProps()));
    }

    private static DeferredBlock<Block> register(String name, Supplier<Block> supplier) {
        DeferredBlock<Block> block = BLOCKS.register(name, supplier);
        ALL.add(block);
        return block;
    }

    private static BlockBehaviour.Properties storageProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava();
    }
}
