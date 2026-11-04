package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.blockentities.BannerPinBlockEntity;
import net.atired.nethermore.blockentities.InfusingStandBlockEntity;
import net.atired.nethermore.blockentities.InfusingStandMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NMBlockEntityInit {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, Nethermore.MODID);
    public static final Supplier<MenuType<InfusingStandMenu>> INFUSING_MENU = MENUS.register("my_menu", () -> new MenuType<>(InfusingStandMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Nethermore.MODID);

    public static final Supplier<BlockEntityType<BannerPinBlockEntity>> BANNER_PIN_ENTITY = BLOCK_ENTITIES.register("banner_pin_entity",
            () -> BlockEntityType.Builder.of(BannerPinBlockEntity::new, NMBlockInit.BANNER_PIN.get()).build(null));
    public static final Supplier<BlockEntityType<InfusingStandBlockEntity>> INFUSING_STAND_ENTITY = BLOCK_ENTITIES.register("infusing_stand_entity",
            () -> BlockEntityType.Builder.of(InfusingStandBlockEntity::new, NMBlockInit.INFUSING_STAND.get()).build(null));
}
