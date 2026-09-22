package com.alexkrolick.fdstoragecompat.menu;

import com.alexkrolick.fdstoragecompat.ModMenus;
import com.alexkrolick.fdstoragecompat.blockentity.DecrafterBlockEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class DecrafterMenu extends AbstractContainerMenu {
    private final DecrafterBlockEntity blockEntity;

    public DecrafterMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInv, playerInv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public DecrafterMenu(int containerId, Inventory playerInv, BlockEntity be) {
        super(ModMenus.DECRAFTER.get(), containerId);
        if (!(be instanceof DecrafterBlockEntity decrafter)) {
            throw new IllegalStateException("Invalid block entity for DecrafterMenu");
        }
        this.blockEntity = decrafter;

        // Input
        this.addSlot(new SlotItemHandler(decrafter.getItems(), DecrafterBlockEntity.INPUT_SLOT, 80, 18));

        // 3x3 output (extract-only in GUI)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = DecrafterBlockEntity.INPUT_SLOT + 1 + col + row * 3;
                this.addSlot(new SlotItemHandler(decrafter.getItems(), index, 62 + col * 18, 40 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                });
            }
        }

        // Player inventory (clear of output rows ending at y=76)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 104 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 162));
        }
    }

    public DecrafterBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            final int teSlots = DecrafterBlockEntity.TOTAL_SLOTS;
            if (index < teSlots) {
                if (!this.moveItemStackTo(stack, teSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, DecrafterBlockEntity.INPUT_SLOT, DecrafterBlockEntity.INPUT_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }
}
