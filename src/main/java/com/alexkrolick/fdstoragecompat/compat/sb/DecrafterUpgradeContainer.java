package com.alexkrolick.fdstoragecompat.compat.sb;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

public class DecrafterUpgradeContainer extends UpgradeContainerBase<DecrafterUpgradeWrapper, DecrafterUpgradeContainer> {
    public DecrafterUpgradeContainer(Player player, int upgradeContainerId, DecrafterUpgradeWrapper upgradeWrapper,
            UpgradeContainerType<DecrafterUpgradeWrapper, DecrafterUpgradeContainer> type) {
        super(player, upgradeContainerId, upgradeWrapper, type);
        // Only the input slot. Output slots used to sync air and crash StatefulComponentItemHandler.
        slots.add(new SlotSuppliedHandler(supplyFromWrapper(DecrafterUpgradeWrapper::getInventory), DecrafterUpgradeWrapper.INPUT_SLOT, -100, -100) {
            @Override
            public void setChanged() {
                super.setChanged();
                if (!player.level().isClientSide) {
                    upgradeWrapper.process(player.level(), player.blockPosition());
                }
            }
        });
    }

    @Override
    public void handlePacket(CompoundTag data) {
    }
}
