package com.alexkrolick.fdstoragecompat;

import java.util.function.Supplier;

import com.alexkrolick.fdstoragecompat.menu.DecrafterMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, FdStorageCompat.MOD_ID);

    public static final Supplier<MenuType<DecrafterMenu>> DECRAFTER =
            MENUS.register("decrafter", () -> IMenuTypeExtension.create(DecrafterMenu::new));

    private ModMenus() {}
}
