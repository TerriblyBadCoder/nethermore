package net.atired.nethermore.blocks;

import net.atired.nethermore.accessors.OffsetFunctionAccessor;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InflamedEyeBlock extends Block {
    protected static final VoxelShape Y_AXIS_AABB = Block.box(3, 0.0, 3, 13, 16.0, 13);

    public InflamedEyeBlock(Properties properties) {
        super(properties);
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
    protected float getMaxHorizontalOffset() {
        return super.getMaxHorizontalOffset()*10.1f;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return Y_AXIS_AABB.move(vec3.x, vec3.y, vec3.z);
    }
}
