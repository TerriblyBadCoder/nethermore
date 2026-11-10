package net.atired.nethermore.items;

import net.atired.nethermore.accessors.PlayerXPAccessor;
import net.atired.nethermore.init.NMItemComponentInit;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HexatedBowlItem extends Item {
    public HexatedBowlItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        int count = stack.getOrDefault(NMItemComponentInit.EXPETRIFIED.get(),0);
        if(count>10){
            count/=11;

            if(player.isCrouching()){
                count=Math.min(8,stack.getCount());
            }
            if(!player.hasInfiniteMaterials()){
                stack.setCount(stack.getCount()-count);
            }
            if(player instanceof PlayerXPAccessor accessor){
                accessor.setDelayedXP(accessor.getDelayedXP()+count*15);
            }
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP,0.66f,0.4f+(float)Math.random()/5.0f);
            player.causeFoodExhaustion(count*2.0f);
            level.addParticle(NMParticleInit.HEXATED_POP_CIRCLE_PARTICLE.get(), player.getX(),player.getY(0.4+Math.random()/5.0f),player.getZ(),0,0.03,0);
            for (int i = 0; i < count/4+2; i++) {
                level.addParticle(NMParticleInit.HEXATED_SPIKE_PARTICLE.get(), player.getX((Math.random()-0.5)*6.0f),player.getY(-0.03+Math.random()/12.0f),player.getZ((Math.random()-0.5)*6.0f),0,-0.01,0);

            }
            return InteractionResultHolder.success(stack);
        }
        return super.use(level,player,usedHand);
    }
}
