package net.atired.nethermore.blocks;

import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

public class WhisperingThornBlock extends RotatedPillarBlock {
    public static final BooleanProperty BLINKING=  BooleanProperty.create("blinking");


    public WhisperingThornBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(AXIS, Direction.Axis.Y).setValue(BLINKING, false)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{BLINKING,AXIS});
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(BLINKING)) {
            level.setBlock(pos, (BlockState)state.setValue(BLINKING, false),2);
            if(Math.random()>0.3){
                level.scheduleTick(pos,this,6+random.nextInt(0,12));

            }
        }else{
            level.setBlock(pos, (BlockState)state.setValue(BLINKING, true),2);
            level.scheduleTick(pos,this,6+random.nextInt(0,12));
            level.playSound(null,pos, SoundEvents.LEASH_KNOT_BREAK, SoundSource.AMBIENT,0.1f,1.7f-random.nextFloat()/5.0f);
            Direction.Axis axis = state.getValue(AXIS);
            Vec3 center = pos.getCenter();
            Vec3 dir = new Vec3(axis== Direction.Axis.X?1:0,axis.isVertical()?1:0,axis== Direction.Axis.Z?1:0).scale(0.5);
            level.sendParticles(NMParticleInit.WHISPER_PARTICLE.get(),center.x+dir.x,center.y+dir.y,center.z+dir.z,4,0.1,0.1,0.1,0.2);
            level.sendParticles(NMParticleInit.WHISPER_PARTICLE.get(),center.x-dir.x,center.y-dir.y,center.z-dir.z,4,0.1,0.1,0.1,0.2);

            int count = level.getRandom().nextInt(1,2);
            for (int i = 0; i < count; i++) {
                Direction dir2 = Direction.getRandom(level.getRandom());
                BlockPos other=pos.relative(dir2);
                if(Math.random()>0.2&&level.getBlockState(other).getBlock() instanceof WhisperingThornBlock thornBlock&&!level.getBlockTicks().hasScheduledTick(other,thornBlock)){
                    level.scheduleTick(other,thornBlock,12+level.getRandom().nextInt(0,32));
                }
            }
        }
        super.tick(state, level, pos, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(!level.getBlockTicks().hasScheduledTick(pos,this)){
            level.setBlock(pos, (BlockState)state.setValue(BLINKING, true), 2);
            int count = level.getRandom().nextInt(1,6);
            for (int i = 0; i < count; i++) {
                Direction dir = Direction.getRandom(level.getRandom());
                BlockPos other=pos.relative(dir);
                if(Math.random()>0.2&&level.getBlockState(other).getBlock() instanceof WhisperingThornBlock thornBlock&&!level.getBlockTicks().hasScheduledTick(other,thornBlock)){
                    level.scheduleTick(other,thornBlock,12+level.getRandom().nextInt(0,32));
                }
            }
            level.scheduleTick(pos,this,6+random.nextInt(0,12));
            Direction.Axis axis = state.getValue(AXIS);
            Vec3 center = pos.getCenter();
            Vec3 dir = new Vec3(axis== Direction.Axis.X?1:0,axis.isVertical()?1:0,axis== Direction.Axis.Z?1:0).scale(0.5);
            level.sendParticles(NMParticleInit.WHISPER_PARTICLE.get(),center.x+dir.x,center.y+dir.y,center.z+dir.z,4,0.1,0.1,0.1,0.2);
            level.sendParticles(NMParticleInit.WHISPER_PARTICLE.get(),center.x-dir.x,center.y-dir.y,center.z-dir.z,4,0.1,0.1,0.1,0.2);
            level.playSound(null,pos, SoundEvents.LEASH_KNOT_BREAK, SoundSource.AMBIENT,0.4f,1.7f-random.nextFloat()/5.0f);
        }
        super.randomTick(state, level, pos, random);
    }
}
