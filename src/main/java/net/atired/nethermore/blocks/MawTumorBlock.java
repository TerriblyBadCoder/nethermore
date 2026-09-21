package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class MawTumorBlock extends TumorBlock {
    public static final DirectionProperty FACING= BlockStateProperties.FACING;;

    public static final MapCodec<MawTumorBlock> CODEC = simpleCodec(MawTumorBlock::new);

    public MawTumorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.SOUTH)));

    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    protected BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState)state.setValue(FACING, rot.rotate((Direction)state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction dir = context.getHorizontalDirection().getOpposite();
        Vec3i diri = new Vec3i(dir.getStepX(),dir.getStepY(),dir.getStepZ());
        Vec3i diri2 = new Vec3i(context.getClickedFace().getStepX(),context.getClickedFace().getStepY(),context.getClickedFace().getStepZ());
        BlockPos realPos=context.getClickedPos();
        dir=dir.getClockWise();
        int xEd=Math.abs(realPos.getX())%3;
        if(realPos.getZ()<0&&diri.getZ()==1||(realPos.getZ()>0&&diri.getZ()==1)){
            xEd=2-xEd;
        }
        int zEd=Math.abs(realPos.getZ())%3;
        if((realPos.getX()<0&&diri.getX()==-1)||(realPos.getX()>0&&diri.getX()==1)){
            zEd=2-zEd;
        }
        zEd=2-zEd;
        for (int i = 0; i <(zEd+xEd+realPos.getY())%3; i++) {
                dir=dir.getCounterClockWise();

        }
        return (BlockState)this.defaultBlockState().setValue(FACING,dir);
    }

    @Override
    protected MapCodec<? extends TumorBlock> codec() {
        return CODEC;
    }
}
