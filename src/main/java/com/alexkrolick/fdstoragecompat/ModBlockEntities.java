package com.alexkrolick.fdstoragecompat;

import java.util.function.Supplier;

import com.alexkrolick.fdstoragecompat.blockentity.DecrafterBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FdStorageCompat.MOD_ID);

    public static final Supplier<BlockEntityType<DecrafterBlockEntity>> DECRAFTER = BLOCK_ENTITIES.register(
            "decrafter",
            () -> BlockEntityType.Builder.of(DecrafterBlockEntity::new, ModBlocks.DECRAFTER.get()).build(null));

    private ModBlockEntities() {}
}
