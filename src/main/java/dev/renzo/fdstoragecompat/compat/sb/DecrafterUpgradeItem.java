package dev.renzo.fdstoragecompat.compat.sb;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

public class DecrafterUpgradeItem extends UpgradeItemBase<DecrafterUpgradeWrapper> {
    private static final UpgradeType<DecrafterUpgradeWrapper> TYPE = new UpgradeType<>(DecrafterUpgradeWrapper::new);

    public DecrafterUpgradeItem() {
        super(OnePerStorageLimit.INSTANCE);
    }

    @Override
    public UpgradeType<DecrafterUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public List<UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.DARK_GRAY));
    }
}
