package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.blockentities.BannerPinBlockEntity;
import net.atired.nethermore.blocks.BannerPinBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NMBlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Nethermore.MODID);

    public static final Supplier<BlockEntityType<BannerPinBlockEntity>> BANNER_PIN_ENTITY = BLOCK_ENTITIES.register("banner_pin_entity",
            () -> BlockEntityType.Builder.of(BannerPinBlockEntity::new, NMBlockInit.BANNER_PIN.get()).build(null));
}
