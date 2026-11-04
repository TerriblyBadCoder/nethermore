package net.atired.nethermore.blocks;

import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class HexatedBlock extends Block {
    public HexatedBlock(Properties properties) {
        super(properties);
    }
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(random.nextFloat()>0.4f&&!(level.getBlockState(pos.above()).getBlock() instanceof HexatedBlock))
            level.addParticle(NMParticleInit.HEXATED_SPIKE_PARTICLE.get(), pos.getX()+Math.random()*0.7+0.15,pos.getY()-Math.random()/12.0f+1.0f,pos.getZ()+Math.random()*0.7+0.15,0,0,0);
        super.animateTick(state, level, pos, random);
    }
}
