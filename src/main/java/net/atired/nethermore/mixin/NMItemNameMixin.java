package net.atired.nethermore.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.atired.nethermore.init.NMItemComponentInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Iterator;

@Mixin(ItemStack.class)
public abstract class NMItemNameMixin implements DataComponentHolder, MutableDataComponentHolder, IItemStackExtension {
    @Shadow public abstract String getDescriptionId();


    @ModifyReturnValue(method = "getHoverName",at=@At("RETURN"))
    private Component getSaltyName(Component original){
        if (this.has(DataComponents.CUSTOM_NAME)) {
            return original;
        }
        if(this.has(NMItemComponentInit.SALTED)){
            original= Component.literal("Salted ").append(original.copy()).withStyle(original.getStyle());
        }
        //shitpost generator
//        if(this.has(NMItemComponentInit.INFUSED)){
//            PotionContents contents = this.get(NMItemComponentInit.INFUSED);
//            if(contents.hasEffects()){
//                MutableComponent andAllThat = Component.empty();
//                Iterator<MobEffectInstance> instanceIterator =contents.getAllEffects().iterator();
//                while(instanceIterator.hasNext()){
//                    andAllThat=andAllThat.append(instanceIterator.next().getEffect().value().getDisplayName().copy());
//                    if(instanceIterator.hasNext()){
//                        andAllThat=andAllThat.append(Component.literal(" and "));
//                    }
//                }
//                original= andAllThat.append(Component.literal("-Infused ")).append(original.copy()).withStyle(original.getStyle());
//            }
//        }
        return original;
    }
}
