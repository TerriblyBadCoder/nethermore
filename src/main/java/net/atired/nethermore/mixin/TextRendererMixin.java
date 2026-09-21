package net.atired.nethermore.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.client.NethermoreClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Font.class)
public abstract class TextRendererMixin {
    private float timed = 0.0f;
    @Unique
    private float xOff = 0.0f;
    @Unique
    private float yOff = 0.0f;

    @Inject(method = "renderChar",at=@At("HEAD"))
    private void test(BakedGlyph glyph, boolean bold, boolean italic, float boldOffset, float x, float y, Matrix4f matrix, VertexConsumer buffer, float red, float green, float blue, float alpha, int packedLight, CallbackInfo ci){
        if(NethermoreClient.PROXY.whispered>0&&Minecraft.getInstance().level!=null){
            this.timed=(Minecraft.getInstance().level.getGameTime()%24000+Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false));
            this.xOff=Mth.sin(this.timed/4.0f+y/2.0f+x/3.0f)*NethermoreClient.PROXY.whispered*1.6f;
            this.yOff=Mth.cos(x/2.0f+this.timed/4.0f)*NethermoreClient.PROXY.whispered*3.6f;
            matrix.translate(this.xOff,this.yOff,0);
        }
    }
    @Inject(method = "renderChar",at=@At("TAIL"))
    private void test2(BakedGlyph glyph, boolean bold, boolean italic, float boldOffset, float x, float y, Matrix4f matrix, VertexConsumer buffer, float red, float green, float blue, float alpha, int packedLight, CallbackInfo ci){
        if(NethermoreClient.PROXY.whispered>0&&Minecraft.getInstance().level!=null){
            matrix.translate(-this.xOff,-this.yOff,0);

           }
    }
}
