package net.atired.nethermore.items;

import net.atired.nethermore.client.NethermoreClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CreepingCherryItem extends Item {
    public CreepingCherryItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if(level.isClientSide()&&livingEntity== Minecraft.getInstance().player){
            if(NethermoreClient.PROXY.cherried<0.5)
                NethermoreClient.PROXY.cherried=1.0f;
        }
        livingEntity.heal(5);
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
