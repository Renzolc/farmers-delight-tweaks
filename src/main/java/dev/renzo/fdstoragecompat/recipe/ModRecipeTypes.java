package dev.renzo.fdstoragecompat.recipe;

import java.util.function.Supplier;

import dev.renzo.fdstoragecompat.FdStorageCompat;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, FdStorageCompat.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, FdStorageCompat.MOD_ID);

    public static final Supplier<RecipeType<FullUncraftRecipe>> FULL_UNCRAFT = RECIPE_TYPES.register("full_uncraft",
            () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return FdStorageCompat.MOD_ID + ":full_uncraft";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FullUncraftRecipe>> FULL_UNCRAFT_SERIALIZER =
            SERIALIZERS.register("full_uncraft", FullUncraftRecipe.Serializer::new);

    private ModRecipeTypes() {}
}
