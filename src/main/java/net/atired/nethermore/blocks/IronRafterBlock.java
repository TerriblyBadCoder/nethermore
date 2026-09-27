package net.atired.nethermore.blocks;

import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IronRafterBlock extends RotatedPillarBlock {
    public static final BooleanProperty COMBINED = BooleanProperty.create("combined");
    public static final BooleanProperty ISX = BooleanProperty.create("isx");
    
    protected static final VoxelShape Y_AXIS_AABB = Block.box(4.5, 0.0, 4.5, 11.5, 16.0, 11.5);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(4.5, 4.5, 0.0, 11.5, 11.5, 16.0);
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 4.5, 4.5, 16.0, 11.5, 11.5);
    protected static final VoxelShape XZ_AXIS_AABB = Shapes.or(X_AXIS_AABB,Z_AXIS_AABB);
    protected static final VoxelShape XY_AXIS_AABB = Shapes.or(X_AXIS_AABB,Y_AXIS_AABB);
    protected static final VoxelShape YZ_AXIS_AABB = Shapes.or(Y_AXIS_AABB,Z_AXIS_AABB);

    public IronRafterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(COMBINED,false).setValue(ISX,false));

    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AXIS,COMBINED,ISX});
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!player.isCrouching()&&stack.getItem().asItem()== NMBlockInit.IRON_RAFTER.asItem() &&!state.getValue(COMBINED)){
            Direction.Axis axis = state.getValue(AXIS);
            Direction.Axis axis2 = hitResult.getDirection().getAxis();
            if(axis2!=state.getValue(AXIS)){
                if(axis2.isVertical()){
                    level.setBlock(pos,state.setValue(COMBINED,true).setValue(ISX,axis== Direction.Axis.X).setValue(AXIS, Direction.Axis.Y),2);
                }else if(!axis.isVertical()){
                    level.setBlock(pos,state.setValue(COMBINED,true),2);
                }else{
                    level.setBlock(pos,state.setValue(COMBINED,true).setValue(ISX,axis2== Direction.Axis.X),2);
                }
                player.swing(hand);
                return ItemInteractionResult.CONSUME_PARTIAL;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(COMBINED,false).setValue(AXIS, context.getClickedFace().getAxis()).setValue(ISX,false);
    }
    protected VoxelShape getShape(BlockState state, BlockGetter p_154347_, BlockPos p_154348_, CollisionContext p_154349_) {
        if(state.getValue(COMBINED)){
            if(state.getValue(AXIS)!= Direction.Axis.Y)
                return XZ_AXIS_AABB;
            else{
                if(state.getValue(ISX)){
                    return XY_AXIS_AABB;
                }else{
                    return YZ_AXIS_AABB;
                }
            }
        }
        switch ((state.getValue(AXIS))) {
            case X:
            default:
                return X_AXIS_AABB;
            case Z:
                return Z_AXIS_AABB;
            case Y:
                return Y_AXIS_AABB;
        }
    }
}
