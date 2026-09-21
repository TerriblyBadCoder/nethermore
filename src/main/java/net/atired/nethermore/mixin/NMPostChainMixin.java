package net.atired.nethermore.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.atired.nethermore.accessors.PostChainDepthPassAccessor;
import net.atired.nethermore.client.NethermoreClient;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
@Mixin(PostChain.class)
public class NMPostChainMixin implements PostChainDepthPassAccessor {
    @Shadow
    @Final
    private List<PostPass> passes;

    @Override
    public List<PostPass> getDemPostPasses() {
        return this.passes;
    }

    @Override
    public void depthEmPostPasses(String name, RenderTarget target) {
        if(target !=null)
            for(PostPass i : this.passes){
                i.getEffect().setSampler(name, target::getDepthTextureId);
            }
    }
}
