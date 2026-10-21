package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.atired.nethermore.entity.SoulProjEntity;
import net.atired.nethermore.entity.TarlingEntity;
import net.atired.nethermore.init.NMEntityInit;
import net.atired.nethermore.init.NMItemInit;
import net.atired.nethermore.init.NMParticleInit;
import net.atired.nethermore.misc.NMPerlinNoise;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class MorgueCabinetBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty HAS_LOOT= BooleanProperty.create("has_loot");

    public MorgueCabinetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(HAS_LOOT,false).setValue(OPEN,false).setValue(FACING, Direction.NORTH)));
    }
    public static final MapCodec<MorgueCabinetBlock> CODEC = simpleCodec(MorgueCabinetBlock::new);
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(HAS_LOOT,false).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(OPEN,false);
    }
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }
    private void playSound(@Nullable Entity source, Level level, BlockPos pos, boolean isOpening) {
        level.playSound(source, pos, isOpening?SoundEvents.COPPER_DOOR_OPEN:SoundEvents.COPPER_DOOR_CLOSE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.5F);
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!state.getValue(OPEN)&&state.getValue(HAS_LOOT)){
            Vec3 center = pos.getCenter();
            Vec3i vec3i = state.getValue(FACING).getNormal();
            Vec3 dir = new Vec3(vec3i.getX(),0,vec3i.getZ());
            center=center.add(dir.scale(0.55));
            double randumb =Math.abs( NMsimplexNoise.sampleNoise3D(pos.getX(),pos.getY(),pos.getZ(),1))*1.01;
            if(randumb>0.7){
                for (int i = 0; i < 12; i++) {
                    level.addParticle(NMParticleInit.SOUL_PARTICLE.get(),
                            center.x+(Math.random()-0.5),center.y+(Math.random()-0.5),center.z+(Math.random()-0.5),
                            dir.x*(Math.random()*0.4+0.05),Math.random()/12.0f,dir.z*(Math.random()*.4+0.05));
                }
                level.addParticle(NMParticleInit.OPEN_PARTICLE.get(), center.x,center.y,center.z, dir.x,0,dir.z);
                if(level instanceof ServerLevel serverLevel){
                    SoulProjEntity projEntity = new SoulProjEntity(serverLevel,center.x,center.y,center.z);
                    serverLevel.addFreshEntity(projEntity);
                    projEntity.setDeltaMovement(dir.scale(-0.21).add(0,0.1,0));
                }
            }else if(randumb>0.6){
                for (int i = 0; i < 12; i++) {
                    level.addParticle(NMParticleInit.TAR_POP_PARTICLE.get(),
                            center.x+(Math.random()-0.5),center.y+(Math.random()-0.5),center.z+(Math.random()-0.5),
                            dir.x*(Math.random()*0.1+0.05),Math.random()/12.0f,dir.z*(Math.random()*.1+0.05));
                }
                if(level instanceof ServerLevel serverLevel){
                    TarlingEntity tarlingEntity = new TarlingEntity(NMEntityInit.TARLING.get(),serverLevel);
                    center=center.add(dir);
                    tarlingEntity.setPos(new Vec3(center.x,center.y,center.z));
                    serverLevel.addFreshEntity(tarlingEntity);
                    tarlingEntity.setSize(1,true);
                    tarlingEntity.setDeltaMovement(dir.scale(0.21).add(0,0.1,0));
                }
                level.addParticle(NMParticleInit.TAR_SLOP_PARTICLE.get(), center.x,center.y,center.z, dir.x*0.2,0,dir.z*0.2);
            }else{
                for (int i = 0; i < 12; i++) {
                    level.addParticle(ParticleTypes.SMOKE,
                            center.x+(Math.random()-0.5),center.y+(Math.random()-0.5),center.z+(Math.random()-0.5),
                            dir.x*(Math.random()*0.1+0.05),Math.random()/12.0f,dir.z*(Math.random()*.1+0.05));
                }
                if(level instanceof ServerLevel serverLevel){
                    int count = (int)(Math.random()*3+2);
                    for (int i = 0; i < count; i++) {
                        ItemStack stack = NMItemInit.ASH_BALL.toStack();
                        double randum = Math.random();
                        if(randum>0.8){
                            if(Math.random()>0.5){
                                stack= Items.ENDER_PEARL.getDefaultInstance();
                            }else{
                                stack= Items.BLAZE_ROD.getDefaultInstance();
                            }
                            stack.setCount((int)(Math.random()*1+1));

                        } else if(randum>0.7){
                            stack.setCount((int)(Math.random()*4+1));
                        }else if (randum>0.4) {
                            stack= Items.BONE.getDefaultInstance();
                            stack.setCount((int)(Math.random()*5+1));
                        } else if (randum>0.2) {
                            stack= Items.BONE_BLOCK.getDefaultInstance();
                            stack.setCount((int)(Math.random()*3+1));
                        } else if (randum>0.02) {
                            stack= Items.COAL.getDefaultInstance();
                            stack.setCount((int)(Math.random()*5+2));
                        } else{
                            stack= Items.NETHERITE_SCRAP.getDefaultInstance();
                            stack.setCount((int)(Math.random()*2+2));
                        }
                        Vec3 tempDir = dir.yRot((float)(Math.random()-0.5)*0.4f).scale(Math.random()*0.1f+0.05f);
                        ItemEntity item = new ItemEntity(serverLevel,center.x,center.y,center.z,stack,tempDir.x,tempDir.y,tempDir.z);
                        serverLevel.addFreshEntity(item);
                    }
                }
            }
            level.setBlock(pos,state.setValue(HAS_LOOT,false),2);
            state=level.getBlockState(pos);
        }
        this.playSound(player, level, pos, !(Boolean)state.getValue(OPEN));
        level.setBlock(pos,state.setValue(OPEN,!state.getValue(OPEN)),2);
        return InteractionResult.SUCCESS;

        //return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{OPEN,FACING,HAS_LOOT});
    }
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
