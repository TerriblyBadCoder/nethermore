package net.atired.nethermore.blocks;

import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WhisperingBlock extends Block {
    public WhisperingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(random.nextFloat()>0.8f){
            Vec3 center = pos.getCenter();
            level.addParticle(NMParticleInit.WHISPER_PARTICLE.get(),center.x()-0.5f+random.nextFloat(),center.y()+0.4f+Math.random()/5.0f,center.z()-0.5f+random.nextFloat(),(Math.random()-0.5)/6.0,0.01,(Math.random()-0.5)/6.0);
        }
        super.animateTick(state, level, pos, random);
    }
}
