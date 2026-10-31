package net.atired.nethermore.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.atired.nethermore.init.NMItemComponentInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class NMItemNameMixin implements DataComponentHolder, MutableDataComponentHolder, IItemStackExtension {
    @ModifyReturnValue(method = "getHoverName",at=@At("RETURN"))
    private Component getSaltyName(Component original){
        if (this.has(DataComponents.CUSTOM_NAME)) {
            return original;
        }
        if(this.has(NMItemComponentInit.SALTED)){
            return Component.literal("Salted ").append(original.copy()).withStyle(original.getStyle());
        }
        return original;
    }
}
