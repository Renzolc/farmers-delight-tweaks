package com.alexkrolick.fdstoragecompat.compat.sb;

import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

public class AdvancedUncrafterUpgradeItem extends SimpleUncrafterUpgradeItem {
    private static final UpgradeType<UncrafterUpgradeWrapper> TYPE = new UpgradeType<>(UncrafterUpgradeWrapper::new);

    @Override
    public UpgradeType<UncrafterUpgradeWrapper> getType() {
        return TYPE;
    }
}
