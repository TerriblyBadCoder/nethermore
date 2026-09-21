package net.atired.nethermore.blocks;

import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SliversBlock extends PinkPetalsBlock {
    public SliversBlock(Properties p_273335_) {
        super(p_273335_);
    }
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.CRIMSON_NYLIUM) || state.is(NMBlockInit.WHISPERING_THORN_BLOCK);
    }
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(random.nextFloat()>0.85f){
            Vec3 center = pos.getCenter();
            level.addParticle(NMParticleInit.WHISPER_PARTICLE.get(),center.x()-0.5f+random.nextFloat(),center.y()-0.3f+Math.random()/5.0f,center.z()-0.5f+random.nextFloat(),(Math.random()-0.5)/6.0,0.01,(Math.random()-0.5)/6.0);
        }
        super.animateTick(state, level, pos, random);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(level.isClientSide()){
            if(entity instanceof AbstractClientPlayer abstractClientPlayer&&abstractClientPlayer== Minecraft.getInstance().player){
                NethermoreClient.PROXY.whispered= Mth.lerp(0.1f,NethermoreClient.PROXY.whispered,1.0f);
            }
        }
        super.entityInside(state, level, pos, entity);
    }
}
