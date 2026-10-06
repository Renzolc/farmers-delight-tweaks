package dev.renzo.fdstoragecompat.contents;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

/**
 * This mod adds cutting-board recipes for storage items (Sophisticated chests/backpacks, a Create toolbox...).
 * The cutting board itself cannot hand back stored items, so cutting an item that still holds something is
 * refused: the player (or a deployer) gets a message and the item stays on the board, to be taken off and emptied.
 * Empty containers cut as before.
 */
public final class CuttingBoardGuard {
    private CuttingBoardGuard() {
    }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(CuttingBoardGuard::onRightClickBlock);
    }

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide() || event.getItemStack().isEmpty()) {
            return; // an empty hand takes the item back off the board; that is always allowed
        }
        if (!(level.getBlockEntity(event.getPos()) instanceof CuttingBoardBlockEntity board) || board.isEmpty()) {
            return;
        }
        ItemStack stored = board.getStoredItem();
        if (!holdsItems(level, stored)) {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);
        event.getEntity().displayClientMessage(
                Component.translatable("message.fd_storage_compat.cutting_board_contents", stored.getHoverName()), true);
    }

    /** True if the item holds anything, or holds something this mod cannot read. */
    public static boolean holdsItems(Level level, ItemStack stack) {
        return holdsItems(level, level.registryAccess(), stack);
    }

    /** Same as {@link #holdsItems(Level, ItemStack)}; the level may be null in tests (no Sophisticated storage). */
    public static boolean holdsItems(@Nullable Level level, HolderLookup.Provider registries, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (ContainerContents.isForcedPassThrough(stack)) {
            return true;
        }
        Optional<ContainerContents.Extraction> contents = ContainerContents.extract(level, registries, stack);
        return contents.isEmpty() || !contents.get().isEmpty();
    }
}
