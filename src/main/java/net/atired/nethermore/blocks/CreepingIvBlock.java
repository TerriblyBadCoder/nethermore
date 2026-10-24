package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.atired.nethermore.init.NMBlockInit.FRUITING;

public class CreepingIvBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<CreepingIvBlock> CODEC = simpleCodec(CreepingIvBlock::new);

    protected static final VoxelShape SHAPE = Block.box(4.0, 9.0, 4.0, 12.0, 16.0, 12.0);

    public MapCodec<CreepingIvBlock> codec() {
        return CODEC;
    }

    public CreepingIvBlock(BlockBehaviour.Properties p_154966_) {
        super(p_154966_, Direction.DOWN, SHAPE, false, 0.1);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, 0).setValue(FRUITING,false));

    }

    protected int getBlocksToGrowWhenBonemealed(RandomSource p_222680_) {
        return NetherVines.getBlocksToGrowWhenBonemealed(p_222680_);
    }

    public BlockState getStateForPlacement(LevelAccessor level) {
        return (BlockState)this.defaultBlockState().setValue(AGE, level.getRandom().nextInt(25)).setValue(FRUITING,false);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE,FRUITING});
    }

    protected Block getBodyBlock() {
        return NMBlockInit.CREEPING_IV_PLANT.get();
    }

    protected boolean canGrowInto(BlockState p_154971_) {
        return NetherVines.isValidGrowthState(p_154971_);
    }
}
