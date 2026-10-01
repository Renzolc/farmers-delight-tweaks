package dev.renzo.fdstoragecompat.client;

import java.util.List;

import dev.renzo.fdstoragecompat.compat.sb.UncrafterUpgradeContainer;
import dev.renzo.fdstoragecompat.compat.sb.UncrafterUpgradeWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.Label;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.WidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;

public class UncrafterUpgradeTab extends UpgradeSettingsTab<UncrafterUpgradeContainer> {
    private final boolean advanced;

    public UncrafterUpgradeTab(UncrafterUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen, boolean advanced) {
        super(upgradeContainer, position, screen,
                Component.translatable(advanced ? "gui.fd_storage_compat.uncrafter.advanced" : "gui.fd_storage_compat.uncrafter.simple"),
                Component.translatable(advanced ? "gui.fd_storage_compat.uncrafter.advanced.tooltip" : "gui.fd_storage_compat.uncrafter.simple.tooltip"));
        this.advanced = advanced;
        if (advanced) {
            addHideableChild(new Label(new Position(x + 32, y + 24),
                    Component.translatable("gui.fd_storage_compat.uncrafter.into_backpack")));
            addHideableChild(new Preview(new Position(x + 36, y + 40), new Dimension(54, 54), upgradeContainer));
        } else {
            addHideableChild(new Label(new Position(x + 36, y + 24),
                    Component.translatable("gui.fd_storage_compat.uncrafter.take_by_hand")));
            addHideableChild(new Spacer(new Position(x + 6, y + 36), new Dimension(100, 58)));
        }
    }

    @Override
    protected void moveSlotsToTab() {
        List<Slot> slots = getContainer().getSlots();
        if (slots.isEmpty()) {
            return;
        }
        Slot input = slots.getFirst();
        input.x = x + 8 - screen.getGuiLeft();
        input.y = y + 40 - screen.getGuiTop();
        if (!advanced) {
            for (int i = 1; i < slots.size() && i <= 9; i++) {
                Slot slot = slots.get(i);
                int col = (i - 1) % 3;
                int row = (i - 1) / 3;
                slot.x = x + 37 + col * 18 - screen.getGuiLeft();
                slot.y = y + 41 + row * 18 - screen.getGuiTop();
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, minecraft, mouseX, mouseY);
        if (!isOpen) {
            return;
        }
        GuiHelper.renderSlotsBackground(guiGraphics, x + 7, y + 39, 1, 1);
        if (!advanced) {
            GuiHelper.renderSlotsBackground(guiGraphics, x + 36, y + 40, 3, 3);
        }
    }

    private static final class Spacer extends WidgetBase {
        protected Spacer(Position position, Dimension dimension) {
            super(position, dimension);
        }

        @Override
        protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        }
    }

    private static final class Preview extends WidgetBase {
        private final UncrafterUpgradeContainer container;

        protected Preview(Position position, Dimension dimension, UncrafterUpgradeContainer container) {
            super(position, dimension);
            this.container = container;
        }

        @Override
        protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
            GuiHelper.renderSlotsBackground(guiGraphics, x, y, 3, 3);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            if (minecraft.level == null || container.getSlots().isEmpty()) {
                return;
            }
            ItemStack input = container.getSlots().getFirst().getItem();
            List<ItemStack> results = dev.renzo.fdstoragecompat.recipe.FullUncraftRecipe.find(minecraft.level, input)
                    .map(recipe -> input.getCount() >= recipe.consume() ? recipe.copyResults() : List.<ItemStack>of())
                    .orElse(List.of());
            int shown = Math.min(9, results.size());
            for (int i = 0; i < shown; i++) {
                int col = i % 3;
                int row = i / 3;
                int ix = x + 1 + col * 18;
                int iy = y + 1 + row * 18;
                ItemStack stack = results.get(i);
                guiGraphics.renderItem(stack, ix, iy);
                guiGraphics.renderItemDecorations(font, stack, ix, iy);
            }
        }

        @Override
        public void renderTooltip(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
            if (!visible || minecraft.level == null || container.getSlots().isEmpty()) {
                return;
            }
            ItemStack input = container.getSlots().getFirst().getItem();
            List<ItemStack> results = dev.renzo.fdstoragecompat.recipe.FullUncraftRecipe.find(minecraft.level, input)
                    .map(recipe -> input.getCount() >= recipe.consume() ? recipe.copyResults() : List.<ItemStack>of())
                    .orElse(List.of());
            int shown = Math.min(9, results.size());
            for (int i = 0; i < shown; i++) {
                int col = i % 3;
                int row = i / 3;
                int ix = x + 1 + col * 18;
                int iy = y + 1 + row * 18;
                if (mouseX >= ix && mouseX < ix + 16 && mouseY >= iy && mouseY < iy + 16) {
                    guiGraphics.renderTooltip(font, results.get(i), mouseX, mouseY);
                    return;
                }
            }
        }
    }
}
