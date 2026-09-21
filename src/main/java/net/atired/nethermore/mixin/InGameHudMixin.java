package net.atired.nethermore.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.init.NMMobEffectInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {
    @Unique
    private static final ResourceLocation MORBID_HEART_LOCATION = Nethermore.getId("hud/morbid_heart");
    @Unique
    private static final ResourceLocation MORBID_HEART_BLINK_LOCATION = Nethermore.getId("hud/morbid_heart_blinking");
    @Inject(method= "renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
            at = @At(value = "TAIL"))
    private void healthRenderAgain(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, boolean hardcore, boolean halfHeart, boolean blinking, CallbackInfo ci){
        if(heartType!= Gui.HeartType.CONTAINER&&!blinking&&Minecraft.getInstance().player!=null&&Minecraft.getInstance().player.hasEffect(NMMobEffectInit.MORBID)){
            RenderSystem.enableBlend();
            guiGraphics.blitSprite(halfHeart?MORBID_HEART_BLINK_LOCATION:MORBID_HEART_LOCATION, x+1, y-1, 9, 9);

            RenderSystem.disableBlend();

        }
    }
}
