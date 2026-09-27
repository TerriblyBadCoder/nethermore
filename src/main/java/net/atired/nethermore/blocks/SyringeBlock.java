package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SyringeBlock extends RodBlock {
    public static final MapCodec<SyringeBlock> CODEC = simpleCodec(SyringeBlock::new);

    protected static final VoxelShape Y_AXIS_AABB = Block.box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(2.0, 2.0, 4.0, 14.0, 14.0, 16.0);
    protected static final VoxelShape X_AXIS_AABB = Block.box(4.0, 2.0, 2.0, 16.0, 14.0, 14.0);
    protected static final VoxelShape YF_AXIS_AABB = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);

    protected static final VoxelShape ZF_AXIS_AABB = Block.box(2.0, 2.0, 0.0, 14.0, 14.0, 12.0);
    protected static final VoxelShape XF_AXIS_AABB = Block.box(0.0, 2.0, 2.0, 12.0, 14.0, 14.0);

    public SyringeBlock(Properties p_154339_) {
        super(p_154339_);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.UP));

    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));
        return (BlockState)this.defaultBlockState().setValue(FACING, direction);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }
    @Override
    protected VoxelShape getShape(BlockState p_154346_, BlockGetter p_154347_, BlockPos p_154348_, CollisionContext p_154349_) {
        Direction dir = p_154346_.getValue(FACING);
        if(dir.getStepY()+dir.getStepX()+dir.getStepZ()==1){
            switch (((Direction)p_154346_.getValue(FACING)).getAxis()) {
                case X:
                default:
                    return X_AXIS_AABB;
                case Z:
                    return Z_AXIS_AABB;
                case Y:
                    return Y_AXIS_AABB;
            }
        }else{
            switch (((Direction)p_154346_.getValue(FACING)).getAxis()) {
                case X:
                default:
                    return XF_AXIS_AABB;
                case Z:
                    return ZF_AXIS_AABB;
                case Y:
                    return YF_AXIS_AABB;
            }
        }
    }

    @Override
    protected MapCodec<? extends RodBlock> codec() {
        return CODEC;
    }
}
