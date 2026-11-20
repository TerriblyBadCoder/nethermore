package net.atired.nethermore.blocks;

import net.atired.nethermore.accessors.OffsetFunctionAccessor;
import net.atired.nethermore.init.NMParticleInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InflamedEyeBlock extends Block {
    public static final BooleanProperty COVERED = BooleanProperty.create("covered");

    protected static final VoxelShape Y_AXIS_AABB = Block.box(3, 0.0, 3, 13, 16.0, 13);

    public InflamedEyeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(COVERED,false));

//        if(properties instanceof OffsetFunctionAccessor accessor){
//            accessor.nethermore$setOffsetFunction((p_272565_, p_272566_, p_272567_) -> {
//                Block block = p_272565_.getBlock();
//                float f = 20.0f;
//                float x = NMsimplexNoise.sampleNoise3D(p_272567_.getX(), p_272567_.getY()/12.0f, p_272567_.getZ(),12.0f)*f;
//                float z = NMsimplexNoise.sampleNoise3D(-p_272567_.getX(), -p_272567_.getY()/12.0f+200, -p_272567_.getZ(),12.0f)*f;
//                return new Vec3(x, 0.0, z);
//            });
//        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if(neighborPos.getY()==pos.getY()+1){
            BlockState neighbor = level.getBlockState(neighborPos);
           level.setBlock(pos,
                   state.setValue(COVERED,!neighbor.isEmpty()),2);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(!state.getValue(COVERED)){
            for (int i = 0; i < 2; i++) {
                Vec3 posv = pos.getCenter().add(0,0.41,0).add(state.getOffset(level,pos)).add((Math.random()-0.5)/2.0,Math.random()/4.0f,(Math.random()-0.5)/2.0);
                level.addParticle(NMParticleInit.TEAR_PARTICLE.get(),posv.x,posv.y,posv.z,0,0.01,0);
            }
        }
        super.animateTick(state, level, pos, random);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{COVERED});
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        return (BlockState)this.defaultBlockState().setValue(COVERED,level.getBlockState(context.getClickedPos().above()).hidesNeighborFace(level,context.getClickedPos().above(),this.defaultBlockState(),Direction.DOWN));
    }
    @Override
    protected float getMaxHorizontalOffset() {
        return super.getMaxHorizontalOffset()*10.1f;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return Y_AXIS_AABB.move(vec3.x, vec3.y, vec3.z);
    }
}
