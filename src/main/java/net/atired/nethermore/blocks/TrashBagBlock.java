package net.atired.nethermore.blocks;

import net.atired.nethermore.init.NMAchievements;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.init.NMItemInit;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TrashBagBlock extends Block {
    public TrashBagBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        if(level instanceof ServerLevel serverLevel){
            Vec3 center = pos.getCenter();
            serverLevel.sendParticles(NMParticleInit.PAPER_TRAIL_PARTICLE.get(),center.x,center.y,center.z,7,0.25,0.25,.25,0.4);
            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, NMBlockInit.TRASH_BAG.asItem().getDefaultInstance()),center.x,center.y,center.z,14,0.25,0.25,.25,0.8);
            serverLevel.sendParticles(NMParticleInit.PAPER_TRAIL_PARTICLE.get(),center.x,center.y,center.z,7,0.25,0.25,.25,0.8);
        }
        super.spawnDestroyParticles(level, player, pos, state);
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
                paper.setCount((int)(5*Math.random()));
                items.add(paper);
            }
            if(Math.random()>0.66){
                ItemStack paper = new ItemStack(Items.STRING);
                paper.setCount((int)(3*Math.random()));
                items.add(paper);
            }
            ItemStack rot = new ItemStack(Items.ROTTEN_FLESH);
            rot.setCount((int)(2*Math.random()));
            items.add(rot);
            rot = new ItemStack(NMItemInit.MORBID_PIECE.get());
            rot.setCount((int)(2*Math.random()));
            items.add(rot);
        }
        return items;
    }
}
