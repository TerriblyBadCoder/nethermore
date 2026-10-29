package net.atired.nethermore.blocks;

import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class HexatedBasaltBlock extends RotatedPillarBlock {
    public static final IntegerProperty AMOUNT =  IntegerProperty.create("basalt_amount",0,2);
    public HexatedBasaltBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AXIS, Direction.Axis.Y).setValue(AMOUNT,1));
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = (BlockState)super.getStateForPlacement(context);
        if(state!=null){
            if(context.getLevel().getBlockState(context.getClickedPos().above()).isEmpty()||context.getLevel().getBlockState(context.getClickedPos().above()).getBlock()== NMBlockInit.HEXATED_BLOCK.get()){
                state=state.setValue(AMOUNT,2);
            }
            if(context.getLevel().getBlockState(context.getClickedPos().below()).getBlock()!=this){
                state=state.setValue(AMOUNT,0);
            }
        }
        return state;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if(state!=null){
            state=state.setValue(AMOUNT,1);
            if(level.getBlockState(pos.above()).isEmpty()||level.getBlockState(pos.above()).getBlock()== NMBlockInit.HEXATED_BLOCK.get()){
                state=state.setValue(AMOUNT,2);
            }
            if(level.getBlockState(pos.below()).getBlock()!=this){
                state=state.setValue(AMOUNT,0);
            }

            level.setBlock(pos,state,2);
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AXIS,AMOUNT});
    }
}
