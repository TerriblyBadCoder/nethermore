package net.atired.nethermore.items;

import net.atired.nethermore.init.NMItemComponentInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SmokedSaltItem extends Item {
    public SmokedSaltItem(Properties properties) {

        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("desc.nethermore.salt").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if(action==ClickAction.SECONDARY){
            if(!slot.getItem().isEmpty()&&slot.getItem().has(DataComponents.FOOD)&&!slot.getItem().has(NMItemComponentInit.SALTED)){
                FoodProperties properties = slot.getItem().get(DataComponents.FOOD);
                properties = new FoodProperties((int)(properties.nutrition()*1.5f),properties.saturation()*1.5f,properties.canAlwaysEat(),properties.eatSeconds(),properties.usingConvertsTo(),properties.effects());
                int min = Math.min(slot.getItem().getCount(),stack.getCount());
                stack.setCount(stack.getCount()-min);

                if(slot.getItem().getCount()==1){
                    slot.getItem().set(NMItemComponentInit.SALTED,true);
                    slot.getItem().set(DataComponents.FOOD,properties);
                }else{
                    ItemStack clone = slot.getItem().copyWithCount(min);
                    clone.set(DataComponents.FOOD,properties);
                    clone.set(NMItemComponentInit.SALTED,true);
                    slot.getItem().setCount(slot.getItem().getCount()-min);
                    if(!player.addItem(clone)){
                        player.drop(clone,true,false);
                    }

                }
                return true;
            }
        }
        return super.overrideStackedOnOther(stack, slot, action, player);
    }
}
