package net.atired.nethermore.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.atired.nethermore.Nethermore;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.function.Function;
@EventBusSubscriber(modid = Nethermore.MODID,value = Dist.CLIENT)

public class NMRenderLayers {
    public static ShaderInstance TAR_SHADER_INSTANCE = null;
    public static ShaderInstance getTarShaderInstance(){return TAR_SHADER_INSTANCE;}
    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_TAR_CULL_SHADER = new RenderStateShard.ShaderStateShard
            (NMRenderLayers::getTarShaderInstance);
    private static final ResourceLocation TAR =Nethermore.getId("textures/block/tar.png");
    public static final Function<ResourceLocation, RenderType> ENTITY_TAR_CULL = Util.memoize(
            p_286169_ -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TAR_CULL_SHADER)
                        .setTextureState(new RenderStateShard.EmptyTextureStateShard(()->{
                            TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
                            texturemanager.getTexture(p_286169_).setFilter(false, false);
                            RenderSystem.setShaderTexture(0, p_286169_);
                            RenderSystem.setShaderTexture(4,Minecraft.getInstance().getTextureManager().getTexture(TAR).getId());
                        },()->{}))
                        .setCullState(RenderType.NO_CULL)
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(true);
                return RenderType.create("entity_tar", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, rendertype$compositestate);
            }
    );

    public static RenderType entityTarCull(ResourceLocation location) {
        return ENTITY_TAR_CULL.apply(location);
    }
    public static ShaderInstance MONOCHROME_SHADER_INSTANCE = null;
    public static ShaderInstance getMonochromeShaderInstance(){return MONOCHROME_SHADER_INSTANCE;}
    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_MONOCHROME_CULL_SHADER = new RenderStateShard.ShaderStateShard
            (NMRenderLayers::getMonochromeShaderInstance);
    public static final Function<ResourceLocation, RenderType> ENTITY_MONOCHROME_CULL = Util.memoize(
            p_286169_ -> {
                RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_MONOCHROME_CULL_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(p_286169_, false, false))
                        .setCullState(RenderType.NO_CULL)
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(true);
                return RenderType.create("entity_monochrome", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, rendertype$compositestate);
            }
    );

    public static RenderType entityMonochromeCull(ResourceLocation location) {
        return ENTITY_MONOCHROME_CULL.apply(location);
    }
    @SubscribeEvent
    public static void loadShaders(RegisterShadersEvent registerShadersEvent) throws IOException {
        registerShadersEvent.registerShader(
                new ShaderInstance(registerShadersEvent.getResourceProvider(), Nethermore.getId("rendertype_entity_monochrome"), DefaultVertexFormat.NEW_ENTITY)
                , (a) -> {
                    MONOCHROME_SHADER_INSTANCE = a;
                });
        registerShadersEvent.registerShader(
                new ShaderInstance(registerShadersEvent.getResourceProvider(), Nethermore.getId("rendertype_entity_tar"), DefaultVertexFormat.NEW_ENTITY)
                , (a) -> {
                    TAR_SHADER_INSTANCE = a;
                });
    }
}
