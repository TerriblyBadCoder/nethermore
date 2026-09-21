package net.atired.nethermore.accessors;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.PostPass;

import java.util.List;

public interface PostChainDepthPassAccessor {
    List<PostPass> getDemPostPasses();
    void depthEmPostPasses(String name, RenderTarget target);
}
