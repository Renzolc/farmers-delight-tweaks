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
            () -> new DecrafterBlock(decrafterProps()));

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


    // Modded seed sacks / sapling sacks / produce crates (compat; recipes gated by mod_loaded)
    public static final DeferredBlock<Block> CUCUMBER_SEED_SACK = sack("cucumber_seed_sack");
    public static final DeferredBlock<Block> EGGPLANT_SEED_SACK = sack("eggplant_seed_sack");
    public static final DeferredBlock<Block> CORN_KERNEL_SEED_SACK = sack("corn_kernel_seed_sack");
    public static final DeferredBlock<Block> AVOCADO_PIT_SEED_SACK = sack("avocado_pit_seed_sack");
    public static final DeferredBlock<Block> AVOCADO_SAPLING_SACK = sack("avocado_sapling_sack");
    public static final DeferredBlock<Block> BELLPEPPER_SEED_SACK = sack("bellpepper_seed_sack");
    public static final DeferredBlock<Block> BROCCOLI_SEED_SACK = sack("broccoli_seed_sack");
    public static final DeferredBlock<Block> CAULIFLOWER_SEED_SACK = sack("cauliflower_seed_sack");
    public static final DeferredBlock<Block> TURNIP_SEED_SACK = sack("turnip_seed_sack");
    public static final DeferredBlock<Block> ZUCCHINI_SEED_SACK = sack("zucchini_seed_sack");
    public static final DeferredBlock<Block> GARLIC_CLOVE_SEED_SACK = sack("garlic_clove_seed_sack");
    public static final DeferredBlock<Block> ASPARAGUS_SEED_SACK = sack("asparagus_seed_sack");
    public static final DeferredBlock<Block> CHILI_PEPPER_SEED_SACK = sack("chili_pepper_seed_sack");
    public static final DeferredBlock<Block> CINNAMON_SAPLING_SACK = sack("cinnamon_sapling_sack");
    public static final DeferredBlock<Block> CINNAMON_STICK_CRATE = crate("cinnamon_stick_crate");
    public static final DeferredBlock<Block> APPLE_SAPLING_SACK = sack("apple_sapling_sack");
    public static final DeferredBlock<Block> BAYBERRY_SAPLING_SACK = sack("bayberry_sapling_sack");
    public static final DeferredBlock<Block> FIG_SAPLING_SACK = sack("fig_sapling_sack");
    public static final DeferredBlock<Block> HAWBERRY_SAPLING_SACK = sack("hawberry_sapling_sack");
    public static final DeferredBlock<Block> KIWI_SAPLING_SACK = sack("kiwi_sapling_sack");
    public static final DeferredBlock<Block> LYCHEE_SAPLING_SACK = sack("lychee_sapling_sack");
    public static final DeferredBlock<Block> MANGO_SAPLING_SACK = sack("mango_sapling_sack");
    public static final DeferredBlock<Block> MANGOSTEEN_SAPLING_SACK = sack("mangosteen_sapling_sack");
    public static final DeferredBlock<Block> ORANGE_SAPLING_SACK = sack("orange_sapling_sack");
    public static final DeferredBlock<Block> PEACH_SAPLING_SACK = sack("peach_sapling_sack");
    public static final DeferredBlock<Block> PEAR_SAPLING_SACK = sack("pear_sapling_sack");
    public static final DeferredBlock<Block> PERSIMMON_SAPLING_SACK = sack("persimmon_sapling_sack");
    public static final DeferredBlock<Block> PINEAPPLE_SAPLING_SACK = sack("pineapple_sapling_sack");
    public static final DeferredBlock<Block> DURIAN_SAPLING_SACK = sack("durian_sapling_sack");
    public static final DeferredBlock<Block> LEMON_SEED_SACK = sack("lemon_seed_sack");
    public static final DeferredBlock<Block> HAMIMELON_SEED_SACK = sack("hamimelon_seed_sack");
    public static final DeferredBlock<Block> DURIAN_CRATE = crate("durian_crate");
    public static final DeferredBlock<Block> PALM_SAPLING_SACK = sack("palm_sapling_sack");
    public static final DeferredBlock<Block> ANCIENT_SAPLING_SACK = sack("ancient_sapling_sack");
    public static final DeferredBlock<Block> BLUE_BLOSSOM_SAPLING_SACK = sack("blue_blossom_sapling_sack");
    public static final DeferredBlock<Block> LAVENDER_BLOSSOM_SAPLING_SACK = sack("lavender_blossom_sapling_sack");
    public static final DeferredBlock<Block> ORANGE_BLOSSOM_SAPLING_SACK = sack("orange_blossom_sapling_sack");
    public static final DeferredBlock<Block> RED_BLOSSOM_SAPLING_SACK = sack("red_blossom_sapling_sack");
    public static final DeferredBlock<Block> YELLOW_BLOSSOM_SAPLING_SACK = sack("yellow_blossom_sapling_sack");
    public static final DeferredBlock<Block> FLAX_SEED_SACK = sack("flax_seed_sack");

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

    /** Like a crafting table: breakable by hand; axe preferred via #minecraft:mineable/axe. */
    private static BlockBehaviour.Properties decrafterProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.5F)
                .sound(SoundType.WOOD)
                .ignitedByLava();
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
