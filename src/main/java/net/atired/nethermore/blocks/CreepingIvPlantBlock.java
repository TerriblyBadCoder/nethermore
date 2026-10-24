package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.init.NMSoundInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CreepingIvPlantBlock extends GrowingPlantBodyBlock {
    public static final BooleanProperty COVERED = BooleanProperty.create("covered");

    public static final MapCodec<CreepingIvPlantBlock> CODEC = simpleCodec(CreepingIvPlantBlock::new);
    public static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public MapCodec<CreepingIvPlantBlock> codec() {
        return CODEC;
    }

    public CreepingIvPlantBlock(BlockBehaviour.Properties p_154975_) {
        super(p_154975_, Direction.DOWN, SHAPE, false);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(COVERED,false));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if(neighborPos.getY()==pos.getY()+1){
            BlockState neighbor = level.getBlockState(neighborPos);
            level.setBlock(pos,
                    state.setValue(COVERED,(!neighbor.isEmpty()&&!(neighbor.getBlock() instanceof GrowingPlantBlock))),2);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{COVERED});
    }
    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        BlockState upState = level.getBlockState(currentPos.above());
        BlockState returning = super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        if(returning.getBlock()==this){
            returning=returning.setValue(COVERED,!(upState.getBlock() instanceof GrowingPlantBlock));
        }
        return returning;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockState neighbor = level.getBlockState(context.getClickedPos().above());
        return (BlockState)this.defaultBlockState().setValue(COVERED,true);
    }
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) NMBlockInit.CREEPING_IV.get();
    }
}

