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
            NethermoreClient.PROXY.cherried=1.0f;
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
