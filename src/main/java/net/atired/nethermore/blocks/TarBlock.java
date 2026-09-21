package net.atired.nethermore.blocks;

import net.atired.nethermore.accessors.LivingEntityTarAccessor;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

public class TarBlock extends LiquidBlock {
    public TarBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntityTarAccessor tarAccessor){
            tarAccessor.setTarred(Math.min(1.3f,tarAccessor.getTarred()+0.2f));
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(random.nextFloat()>0.4f){
            Vec3 center = pos.getCenter();
            level.addParticle(NMParticleInit.TAR_POP_PARTICLE.get(),center.x()-0.5f+random.nextFloat(),center.y()+0.3f+Math.random()/5.0f,center.z()-0.5f+random.nextFloat(),0,0.01,0);
        }
        super.animateTick(state, level, pos, random);
    }
}
