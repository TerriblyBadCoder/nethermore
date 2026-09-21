package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlueAshLayerBlock extends Block {
    public static final MapCodec<BlueAshLayerBlock> CODEC = simpleCodec(BlueAshLayerBlock::new);
    public static final int MAX_HEIGHT = 8;
    public static final IntegerProperty LAYERS;
    protected static final VoxelShape[] SHAPE_BY_LAYER;
    public static final int HEIGHT_IMPASSABLE = 5;

    public MapCodec<BlueAshLayerBlock> codec() {
        return CODEC;
    }

    public BlueAshLayerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(LAYERS, 1));
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        switch (pathComputationType) {
            case LAND:
                return (Integer)state.getValue(LAYERS) < 5;
            case WATER:
                return false;
            case AIR:
                return false;
            default:
                return false;
        }
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_LAYER[(Integer)state.getValue(LAYERS)];
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_LAYER[(Integer)state.getValue(LAYERS) - 1];
    }

    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return SHAPE_BY_LAYER[(Integer)state.getValue(LAYERS)];
    }

    protected VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_LAYER[(Integer)state.getValue(LAYERS)];
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return (Integer)state.getValue(LAYERS) == 8 ? 0.2F : 1.0F;
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos.below());
        if (blockstate.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)) {
            return false;
        } else {
            return blockstate.is(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON) ? true : Block.isFaceFull(blockstate.getCollisionShape(level, pos.below()), Direction.UP) || blockstate.is(this) && (Integer)blockstate.getValue(LAYERS) == 8;
        }
    }

    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }


    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        int i = (Integer)state.getValue(LAYERS);
        if (useContext.getItemInHand().is(this.asItem()) && i < 8) {
            return useContext.replacingClickedOnBlock() ? useContext.getClickedFace() == Direction.UP : true;
        } else {
            return i == 1;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        if (blockstate.is(this)) {
            int i = (Integer)blockstate.getValue(LAYERS);
            return (BlockState)blockstate.setValue(LAYERS, Math.min(8, i + 1));
        } else {
            return super.getStateForPlacement(context);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{LAYERS});
    }

    static {
        LAYERS = BlockStateProperties.LAYERS;
        SHAPE_BY_LAYER = new VoxelShape[]{Shapes.empty(), Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
    }
}
