package net.atired.nethermore.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.atired.nethermore.client.NMRenderLayers;
import net.atired.nethermore.client.NethermoreClient;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class NMLevelRendererMixin {
    @Inject(method = "renderLevel",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glint()Lnet/minecraft/client/renderer/RenderType;"))
    private void infusion(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci, @Local MultiBufferSource.BufferSource bufferSource){
//        if(NethermoreClient.PROXY.done){
//            bufferSource.endBatch(NMRenderLayers.INFUSION);
//        }

    }
}
