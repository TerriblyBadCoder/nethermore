package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.atired.nethermore.blockentities.BannerPinBlockEntity;
import net.atired.nethermore.init.NMBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BannerPinBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final MapCodec<BannerPinBlock> CODEC = simpleCodec(BannerPinBlock::new);

    protected static final VoxelShape Y_AXIS_AABB = Block.box(4.5, 6.0, 4.5, 11.5, 16.0, 11.5);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(4.5, 4.5, 6.0, 11.5, 11.5, 16.0);
    protected static final VoxelShape X_AXIS_AABB = Block.box(6.0, 4.5, 4.5, 16.0, 11.5, 11.5);
    protected static final VoxelShape YF_AXIS_AABB = Block.box(4.5, 0.0, 4.5, 11.5, 10.0, 11.5);
    protected static final VoxelShape ZF_AXIS_AABB = Block.box(4.5, 4.5, 0.0, 11.5, 11.5, 10.0);
    protected static final VoxelShape XF_AXIS_AABB = Block.box(0.0, 4.5, 4.5, 10.0, 11.5, 11.5);
    public BannerPinBlock(Properties p_154339_) {
        super(p_154339_);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.UP));

    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));
        return (BlockState)this.defaultBlockState().setValue(FACING, direction);
    }
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
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
    protected MapCodec<? extends BannerPinBlock> codec() {
        return CODEC;
    }


    protected BlockState rotate(BlockState p_154354_, Rotation p_154355_) {
        return (BlockState)p_154354_.setValue(FACING, p_154355_.rotate((Direction)p_154354_.getValue(FACING)));
    }

    protected BlockState mirror(BlockState p_154351_, Mirror p_154352_) {
        return (BlockState)p_154351_.setValue(FACING, p_154352_.mirror((Direction)p_154351_.getValue(FACING)));
    }

    protected boolean isPathfindable(BlockState p_154341_, PathComputationType p_154344_) {
        return false;
    }
    public static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, NMBlockEntityInit.BANNER_PIN_ENTITY.get(), BannerPinBlockEntity::tick);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new BannerPinBlockEntity(blockPos,blockState);
    }

}
