package net.atired.nethermore.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NethermoreClient;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementTab.class)
public class NMAdvancementTabMixin {
    @Shadow private double scrollX;
    @Shadow private double scrollY;
    @Shadow private DisplayInfo display;
    @Shadow private float fade;
    private float test = 0.0f;
    @Unique
    private static ResourceLocation[] SPRITE_0 = {Nethermore.getId("textures/gui/sprites/misc/advancements_bg.png"),Nethermore.getId("textures/gui/sprites/misc/advancements_bg_2.png")};
    @Unique
    private static ResourceLocation[] SPRITE_1 = {Nethermore.getId("textures/gui/sprites/misc/advancements_fg.png"),Nethermore.getId("textures/gui/sprites/misc/advancements_fg_2.png")};
    private static ResourceLocation SPRITE_2 = Nethermore.getId("textures/gui/sprites/misc/glow.png");
    @Inject(method = "drawContents",at= @At(value = "INVOKE",shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/screens/advancements/AdvancementWidget;draw(Lnet/minecraft/client/gui/GuiGraphics;II)V",ordinal = 0))
    private void drawEvil(GuiGraphics guiGraphics, int x, int y, CallbackInfo ci){
        int i = 0;
        int j = 0;
        ResourceLocation atlasLocation = (ResourceLocation)this.display.getBackground().orElse(TextureManager.INTENTIONAL_MISSING_TEXTURE);
        if(atlasLocation.getNamespace().equals("nethermore")) {
            RenderSystem.enableBlend();
            if(NethermoreClient.PROXY.nihiloNess>0.01f)RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f- NethermoreClient.PROXY.nihiloNess);
            guiGraphics.pose().translate((test/64.0f*234.0f)%234,(1.0f-this.fade)*30.0f,200);
            guiGraphics.blit(SPRITE_2, i+234*2,j,0.0f,0.0f,234,113,234,113);
            guiGraphics.blit(SPRITE_2,i+234,j,0.0f,0.0f,234,113,234,113);
            guiGraphics.blit(SPRITE_2, i,j,0.0f,0.0f,234,113,234,113);
            guiGraphics.blit(SPRITE_2,i-234,j,0.0f,0.0f,234,113,234,113);
            guiGraphics.pose().translate(-((test/64.0f*234.0f)%234),0,-200);
            RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        }


    }
        @WrapOperation(method = "drawContents",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"))
    private void drawawesomeTexture(GuiGraphics instance, ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight, Operation<Void> original,
                                    @Local(ordinal = 4) int k, @Local(ordinal = 5) int l, @Local(ordinal = 6) int m, @Local(ordinal = 7) int n){
        if(atlasLocation.getNamespace().equals("nethermore")){
            if(m==-1&&n==-1){
                Level level = Minecraft.getInstance().level;
                double yPos = Minecraft.getInstance().player!=null?Minecraft.getInstance().player.getY():0;
                test-=1.0f/10.0f*(1.0f+this.fade*3.0f);
                test%=256;
            }
            int i = Mth.floor(this.scrollX);
            int j = Mth.floor(this.scrollY);
            k = i % 64;l = j % 64;


            RenderSystem.enableBlend();
            if(NethermoreClient.PROXY.nihiloNess>0.01f)RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f- NethermoreClient.PROXY.nihiloNess);
            instance.blit(SPRITE_0[0],k+64*m,l+64*n,0.0f,test*1.0f,64,64,64,64);
            if(NethermoreClient.PROXY.nihiloNess>0.01f){
                RenderSystem.setShaderColor(1.0f,1.0f,1.0f,NethermoreClient.PROXY.nihiloNess);
                instance.blit(SPRITE_0[1],k+64*m,l+64*n,Mth.sin(test/32.0f*3.14f)*64,test*1.0f,64,64,64,64);

            }
            if(m==15&&n==8){

                for (m=-1;m<=15;m++){
                    for(n = -1; n <= 8; ++n) {
                        RenderSystem.enableBlend();

                        instance.pose().pushPose();
                        if(NethermoreClient.PROXY.nihiloNess>0.01f)RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f- NethermoreClient.PROXY.nihiloNess);
                        instance.blit(SPRITE_1[0],k+64*(m),l+64*(n),test,test/2.0f,64,64,64,64);
                        if(NethermoreClient.PROXY.nihiloNess>0.01f){
                            RenderSystem.setShaderColor(1.0f,1.0f,1.0f,NethermoreClient.PROXY.nihiloNess);
                            instance.blit(SPRITE_1[1],k+64*(m),l+64*(n),test,0,64,64,64,64);

                        }
                        instance.pose().popPose();
                    }
                }
            }

            RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
        }
        else{
            original.call(instance,atlasLocation,x,y,uOffset,vOffset,width,height,textureWidth,textureHeight);
        }
    }
}
