package net.atired.nethermore.items;

import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.networking.payloads.VelSyncPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class LooseEyeItem extends Item {
    public LooseEyeItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 81000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        timeCharged=Math.clamp(81000-timeCharged,5,100);
        float timed = Math.max(0.2f,(float)Math.pow(timeCharged/3.0f,0.7f));
        livingEntity.swing(livingEntity.getMainHandItem()==stack?InteractionHand.MAIN_HAND:InteractionHand.OFF_HAND);
        BlockHitResult result1 = level.clip(new ClipContext(
                livingEntity.getPosition(1).add(0,livingEntity.getBbHeight()/2,0),
                livingEntity.getPosition(1).add(0,livingEntity.getBbHeight()/2,0).add(livingEntity.getViewVector(1).scale(1+timed*2.0)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.ANY,
                livingEntity));
        if(result1.getLocation()==null)return;
        Vec3 pos = result1.getLocation().lerp(livingEntity.getPosition(1).add(0,livingEntity.getBbHeight()/2,0),0.1).add(livingEntity.getPosition(1).add(0,livingEntity.getBbHeight()/2,0).subtract(result1.getLocation()).normalize().multiply(1,3,1));
        livingEntity.teleportTo(pos.x,pos.y,pos.z);
        if(level instanceof ServerLevel serverLevel){
            Vec3 dir = livingEntity.getViewVector(1).scale(0.5*timed);
            livingEntity.setDeltaMovement(dir);
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel,livingEntity.chunkPosition(),new VelSyncPayload(livingEntity.getId(),dir.x,dir.y,dir.z));
        }
        if(livingEntity.level().isClientSide()&&NethermoreClient.PROXY!=null&& Minecraft.getInstance().player==livingEntity){
            NethermoreClient.PROXY.blink=1.0f;
        }
        livingEntity.playSound(SoundEvents.PLAYER_TELEPORT,1.0f,0.6f);
        super.releaseUsing(stack, level, livingEntity, timeCharged);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return true;
    }
}
