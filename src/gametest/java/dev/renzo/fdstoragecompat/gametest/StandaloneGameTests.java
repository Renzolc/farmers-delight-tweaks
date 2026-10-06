package dev.renzo.fdstoragecompat.gametest;

import java.util.List;

import dev.renzo.fdstoragecompat.FdStorageCompat;
import dev.renzo.fdstoragecompat.ModBlocks;
import dev.renzo.fdstoragecompat.contents.CuttingBoardGuard;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Farmer's Delight Tweaks 1.1.0 on its own (no Disassembly Delight in the test game). Run with
 * {@code ./gradlew runGameTestServer}.
 */
@GameTestHolder(FdStorageCompat.MOD_ID)
@PrefixGameTestTemplate(false)
public class StandaloneGameTests {
    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(FdStorageCompat.MOD_ID, path);
    }

    @GameTest(template = "empty")
    public static void decrafterIsGoneButStorageBlocksRemain(GameTestHelper helper) {
        helper.assertFalse(ModList.get().isLoaded(FdStorageCompat.DISASSEMBLY_DELIGHT), "test game runs without Disassembly Delight");
        helper.assertTrue(BuiltInRegistries.BLOCK.get(id("decrafter")) == Blocks.AIR, "the Decrafter moved to Disassembly Delight");
        helper.assertTrue(BuiltInRegistries.ITEM.get(id("decrafter")) == Items.AIR, "the Decrafter item moved to Disassembly Delight");
        helper.assertTrue(BuiltInRegistries.ITEM.get(id("decrafter_upgrade")) == Items.AIR, "the upgrade moved to Disassembly Delight");
        helper.assertTrue(BuiltInRegistries.RECIPE_SERIALIZER.get(id("full_uncraft")) == null, "full_uncraft moved to Disassembly Delight");
        helper.assertTrue(BuiltInRegistries.BLOCK.get(id("apple_crate")) == ModBlocks.APPLE_CRATE.get(), "crates still registered");
        helper.assertTrue(BuiltInRegistries.ITEM.get(id("wheat_seed_sack")) != Items.AIR, "sacks still registered");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void cuttingBoardGuardStillSeesContents(GameTestHelper helper) {
        ItemStack full = new ItemStack(Items.SHULKER_BOX);
        full.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(new ItemStack(Items.DIAMOND, 3))));
        helper.assertTrue(CuttingBoardGuard.holdsItems(helper.getLevel(), full), "a shulker box with diamonds must not be cut");
        helper.assertFalse(CuttingBoardGuard.holdsItems(helper.getLevel(), new ItemStack(Items.SHULKER_BOX)), "an empty one may be cut");
        helper.succeed();
    }
}
