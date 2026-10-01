package com.alexkrolick.fdstoragecompat.compat.sb;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

public class UncrafterUpgradeContainer extends UpgradeContainerBase<UncrafterUpgradeWrapper, UncrafterUpgradeContainer> {
    public UncrafterUpgradeContainer(Player player, int upgradeContainerId, UncrafterUpgradeWrapper upgradeWrapper,
            UpgradeContainerType<UncrafterUpgradeWrapper, UncrafterUpgradeContainer> type) {
        super(player, upgradeContainerId, upgradeWrapper, type);
        slots.add(new SlotSuppliedHandler(supplyFromWrapper(UncrafterUpgradeWrapper::getInventory), UncrafterUpgradeWrapper.INPUT_SLOT, -100, -100) {
            @Override
            public void setChanged() {
                super.setChanged();
                if (!player.level().isClientSide) {
                    upgradeWrapper.process(player.level(), player.blockPosition());
                }
            }
        });
        if (!upgradeWrapper.isAdvanced()) {
            for (int i = 0; i < UncrafterUpgradeWrapper.OUTPUT_SLOTS; i++) {
                int slot = 1 + i;
                slots.add(new SlotSuppliedHandler(supplyFromWrapper(UncrafterUpgradeWrapper::getInventory), slot, -100, -100) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public void handlePacket(CompoundTag data) {
        // Processing is server-ticked off the input slot. No extra packets.
    }
}
