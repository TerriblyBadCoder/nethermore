package net.atired.nethermore.mixin;

import net.atired.nethermore.init.NMItemComponentInit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(Item.class)
public class NMItemMixin {

    @Inject(method = "appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V",at=@At("HEAD"))
    private void getInfused(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if(stack.has(NMItemComponentInit.INFUSED)){

            PotionContents contents = stack.get(NMItemComponentInit.INFUSED);
            tooltipComponents.add(Component.literal("Infused with: ").withStyle(ChatFormatting.DARK_GRAY));
            contents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());

        }
    }
}
