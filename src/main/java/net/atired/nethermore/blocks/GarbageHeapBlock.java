package net.atired.nethermore.blocks;

import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.init.NMAchievements;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.init.NMItemInit;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class GarbageHeapBlock extends Block {
    public GarbageHeapBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        if(level instanceof ServerLevel serverLevel){
            Vec3 center = pos.getCenter();
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state),center.x,center.y,center.z,14,0.25,0.25,.25,0.4);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state),center.x,center.y,center.z,14,0.25,0.25,.25,0.2);
        }
        super.spawnDestroyParticles(level, player, pos, state);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {

        if(fallDistance>0.2&&level instanceof ServerLevel serverLevel){
            if(entity instanceof ServerPlayer player){

            }
            if(entity instanceof LivingEntity && !(entity instanceof Monster)){
                level.destroyBlock(pos,true);
            }
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }



    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {

        if(player instanceof ServerPlayer serverPlayer) NMAchievements.GARBAGE.get().trigger(serverPlayer);
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> items = super.getDrops(state,params);
        if(items.isEmpty()){
            items= new ArrayList<>();
            if(Math.random()>0.66){
                ItemStack paper = new ItemStack(Items.PAPER);
                paper.setCount((int)(4*Math.random()));
                items.add(paper);
            }
            if(Math.random()>0.66){
                ItemStack paper = new ItemStack(Items.STRING);
                paper.setCount((int)(2*Math.random()));
                items.add(paper);
            }
            ItemStack rot = new ItemStack(Items.ROTTEN_FLESH);
            rot.setCount((int)(1*Math.random()));
            items.add(rot);
            rot = new ItemStack(NMItemInit.MORBID_PIECE.get());
            rot.setCount((int)(1*Math.random()));
            rot = new ItemStack(Items.CANDLE);
            rot.setCount((int)(1*Math.random()));
            items.add(rot);
        }
        return items;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (Direction dir : Direction.values()){
            if(dir.getAxis().isHorizontal()){
                if(Math.random()>0.85*(1.0f-NethermoreClient.PROXY.lightNess)){
                    Direction dir2 = dir.getClockWise();
                    Vec3 vec = pos.getCenter().add(dir.getStepX()*0.5f,0,dir.getStepZ()*0.5f).add(0.8f*dir2.getStepX()*(Math.random()-0.5f),0.8f*(Math.random()-0.5f),dir2.getStepZ()*(Math.random()-0.5f)*0.8f);
                    level.addParticle(NMParticleInit.RUNE_PARTICLE.get(), vec.x,vec.y,vec.z,dir.getStepX(),0,dir.getStepZ());
                }
            }
        }
        super.animateTick(state, level, pos, random);
    }
}
