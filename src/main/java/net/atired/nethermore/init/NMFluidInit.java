package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.blocks.fluids.TarFluidType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NMFluidInit {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Nethermore.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Nethermore.MODID);

    private static BaseFlowingFluid.Properties tarProperties() {
        return new BaseFlowingFluid.Properties(TAR_FLUID_TYPE, TAR_FLUID_SOURCE, TAR_FLUID_FLOWING).bucket(NMItemInit.TAR_BUCKET).block(NMBlockInit.TAR);
    }

    public static final DeferredHolder<FluidType,FluidType> TAR_FLUID_TYPE = FLUID_TYPES.register("tar", () -> new TarFluidType(FluidType.Properties.create()
            .density(1024)
            .viscosity(1024)
            
            .pathType(PathType.WATER).adjacentPathType(PathType.DANGER_OTHER)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)));
    public static final DeferredHolder<Fluid, TarFluidType.SourceTar> TAR_FLUID_SOURCE = FLUIDS.register("tar", () -> new TarFluidType.SourceTar(tarProperties()));
    public static final DeferredHolder<Fluid, TarFluidType.FlowingTar> TAR_FLUID_FLOWING = FLUIDS.register("tar_flowing", () -> new TarFluidType.FlowingTar(tarProperties()));

    public static void postInit() {
        FluidInteractionRegistry.addInteraction(TAR_FLUID_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                (FluidType) NeoForgeMod.WATER_TYPE.value(),
                fluidState -> Blocks.BLACKSTONE.defaultBlockState()
        ));
        FluidInteractionRegistry.addInteraction(NeoForgeMod.WATER_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                (FluidType) TAR_FLUID_TYPE.get(),
                fluidState -> Blocks.BLACKSTONE.defaultBlockState()
        ));
        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                (FluidType) TAR_FLUID_TYPE.get(),
                fluidState -> NMBlockInit.FUSED_TAR_BLOCK.get().defaultBlockState()
        ));
        FluidInteractionRegistry.addInteraction(TAR_FLUID_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                (FluidType) NeoForgeMod.LAVA_TYPE.value(),
                fluidState -> NMBlockInit.FUSED_TAR_BLOCK.get().defaultBlockState()
        ));
    }
}
