package net.atired.nethermore.mixin;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.atired.nethermore.client.NMRenderLayers;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBuffers.class)
public abstract class NMRenderBuffersMixin {

    @Inject(method = "lambda$new$1",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;glint()Lnet/minecraft/client/renderer/RenderType;"))
    private void moreInitYay(Object2ObjectLinkedOpenHashMap p_349871_, CallbackInfo ci){
        //p_349871_.put(NMRenderLayers.INFUSION, new ByteBufferBuilder(NMRenderLayers.INFUSION.bufferSize()));

    }
}
