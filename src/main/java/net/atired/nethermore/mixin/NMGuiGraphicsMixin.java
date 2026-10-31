package net.atired.nethermore.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.init.NMItemComponentInit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class NMGuiGraphicsMixin {
    @Shadow public abstract void blit(ResourceLocation atlasLocation, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight);

    @Shadow public abstract void blitSprite(ResourceLocation sprite, int x, int y, int blitOffset, int width, int height);

    @Shadow @Final private PoseStack pose;
    private static final ResourceLocation SALT_LOCATION = Nethermore.getId("hud/salted");

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",at=@At("TAIL"))
    private void salting(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci){
        if(stack.getOrDefault(NMItemComponentInit.SALTED,false)){
            RenderSystem.enableBlend();
            pose.pushPose();
            pose.translate(x+8,y+8,Math.abs(guiOffset)+152);
            pose.mulPose(new Quaternionf().rotationZ(Mth.sin((x+y)/12.0f)/5.0f));
            pose.translate(-8,-8,0);
            blitSprite(SALT_LOCATION,0,0,0,16,16);
            pose.popPose();
            RenderSystem.disableBlend();
        }
    }
}
