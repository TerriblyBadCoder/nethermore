package net.atired.nethermore.mixin;

import net.atired.nethermore.accessors.GameRendererResourceManagerAccessor;
import net.atired.nethermore.accessors.PostChainDepthPassAccessor;
import net.atired.nethermore.client.NethermoreClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class NMGameRendererMixin implements GameRendererResourceManagerAccessor {
    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Override
    public ResourceManager nethermore$myPrecious() {
        return resourceManager;
    }
    @Inject(method = "render",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V",ordinal = 0,shift= At.Shift.BEFORE))
    private void renderCFDepth(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci){
        PostChain[] chains = {NethermoreClient.TUMOR,NethermoreClient.SCRAMBLED};
        NethermoreClient.ARM_DEPTH_TARGET.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());

        //RESET
        for(PostChain i : chains){
            if(i instanceof PostChainDepthPassAccessor accessor){
                accessor.depthEmPostPasses("TrueHandDepthSampler",NethermoreClient.ARM_DEPTH_TARGET);
            }
        }

    }
    @Inject(method = "render",at= @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V",ordinal = 0,shift= At.Shift.BEFORE))
    private void renderNM(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci){
        if(Minecraft.getInstance().levelRenderer!=null&&Minecraft.getInstance().player!=null){
            if(NethermoreClient.PROXY!=null&& NethermoreClient.PROXY.blueNess>0.0f){

                PostChain chain = NethermoreClient.TUMOR;
                chain.setUniform("GameTime",(Minecraft.getInstance().level.getGameTime()%24000));
                chain.setUniform("FadeInTest",NethermoreClient.PROXY.blueNess);
                chain.setUniform("Pixelation",1.0f-NethermoreClient.PROXY.lightNess);
                chain.process(deltaTracker.getRealtimeDeltaTicks());
            }
            if(NethermoreClient.PROXY!=null&& NethermoreClient.PROXY.tarNess>0.0f){

                PostChain chain = NethermoreClient.TARRED;
                chain.setUniform("GameTime",(Minecraft.getInstance().level.getGameTime()%24000));
                chain.setUniform("FadeInTest",NethermoreClient.PROXY.tarNess);
                chain.process(deltaTracker.getRealtimeDeltaTicks());
            }
            if(NethermoreClient.PROXY!=null&& NethermoreClient.PROXY.nihiloNess>0.0f){

                PostChain chain = NethermoreClient.SCRAMBLED;
                chain.setUniform("GameTime",(Minecraft.getInstance().level.getGameTime()%24000));
                chain.setUniform("FadeInTest",NethermoreClient.PROXY.nihiloNess);
                chain.setUniform("Reddened",NethermoreClient.PROXY.redNess);
                chain.process(deltaTracker.getRealtimeDeltaTicks());
            }
        }
    }
}
