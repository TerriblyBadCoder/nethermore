package net.atired.nethermore.blocks;

import net.atired.nethermore.init.NMParticleInit;
import net.atired.nethermore.misc.NMBlockConversions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class SoulLiningBlock extends Block {
    public SoulLiningBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
//        if(player!=null){
//            for (int x = -5; x <= 5; x++) {
//                for (int y = -5; y <= 5; y++) {
//                    for (int z = -5; z <= 5; z++) {
//                        if(new Vec3(x,y,z).length()<6){
//                            BlockPos pos1 =pos.offset(x,y,z);
//                            BlockState state1=NMBlockConversions.convert(level.getBlockState(pos1));
//                            if(state1!=null){
//                                level.setBlock(pos1,state1,2);
//                            }
//                        }
//                    }
//                }
//            }
//        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(random.nextFloat()>0.4f&&!(level.getBlockState(pos.below()).getBlock() instanceof SoulLiningBlock))
            level.addParticle(NMParticleInit.SOUL_PARTICLE.get(), pos.getX()+Math.random(),pos.getY()-Math.random()/12.0f,pos.getZ()+Math.random(),0,-Math.random()/4.0f-0.04f,0);
        super.animateTick(state, level, pos, random);
    }
}
