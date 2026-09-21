package net.atired.nethermore.blocks.fluids;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.init.NMFluidInit;
import net.atired.nethermore.init.NMItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TarFluidType extends FluidType {
    public static final ResourceLocation FLUID_FLOWING  = Nethermore.getId("block/tar_flowing");

    public static final ResourceLocation FLUID_STILL = Nethermore.getId("block/tar_still");

    public TarFluidType(Properties properties) {
        super(properties);
    }

    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.3,0.9,0.3).add(0,-0.01,0));
        return super.move(state, entity, movementVector, gravity);
    }
    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return FLUID_STILL;
            }
            @Override
            public ResourceLocation getFlowingTexture() {
                return FLUID_FLOWING;
            }
        });
    }
    public static class FlowingTar extends BaseFlowingFluid.Flowing {

        public FlowingTar(Properties properties) {
            super(properties);
        }

        @Override
        public Fluid getFlowing() {
            return NMFluidInit.TAR_FLUID_FLOWING.get();
        }

        @Override
        public Fluid getSource() {
            return NMFluidInit.TAR_FLUID_SOURCE.get();
        }

        @Override
        protected int getDropOff(LevelReader worldIn) {
            return 2;
        }

        @Override
        protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluidIn, Direction direction) {
            return true;
        }

        @Override
        public int getTickDelay(LevelReader level) {
            return 12;
        }
    }
    public static class SourceTar extends BaseFlowingFluid.Source {

        public SourceTar(Properties properties) {
            super(properties);
        }

        @Override
        public Fluid getFlowing() {
            return NMFluidInit.TAR_FLUID_FLOWING.get();
        }

        @Override
        public Fluid getSource() {
            return NMFluidInit.TAR_FLUID_SOURCE.get();
        }

        @Override
        protected int getDropOff(LevelReader worldIn) {
            return 3;
        }

        @Override
        public int getTickDelay(LevelReader level) {
            return 12;
        }
    }
}
