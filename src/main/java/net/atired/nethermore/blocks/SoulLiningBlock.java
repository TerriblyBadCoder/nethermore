package net.atired.nethermore.blocks;

import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SoulLiningBlock extends Block {
    public SoulLiningBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(random.nextFloat()>0.4f&&!(level.getBlockState(pos.below()).getBlock() instanceof SoulLiningBlock))
            level.addParticle(NMParticleInit.SOUL_PARTICLE.get(), pos.getX()+Math.random(),pos.getY()-Math.random()/12.0f,pos.getZ()+Math.random(),0,-Math.random()/4.0f-0.04f,0);
        super.animateTick(state, level, pos, random);
    }
}
