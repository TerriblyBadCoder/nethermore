package net.atired.nethermore.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.accessors.GameRendererResourceManagerAccessor;
import net.atired.nethermore.accessors.PostChainDepthPassAccessor;
import net.atired.nethermore.client.particles.SoulParticle;
import net.atired.nethermore.client.particles.TarParticle;
import net.atired.nethermore.client.particles.TarSlopParticle;
import net.atired.nethermore.client.particles.WhisperParticle;
import net.atired.nethermore.client.renderers.*;
import net.atired.nethermore.client.renderers.models.*;
import net.atired.nethermore.entity.ObserverEntity;
import net.atired.nethermore.init.NMEntityInit;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.joml.Matrix4f;

import java.io.IOException;

@Mod(value = Nethermore.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Nethermore.MODID, value = Dist.CLIENT)
public class NethermoreClient {
    public static NMClientProxy PROXY = new NMClientProxy();
    public static ResourceLocation TARREDEFFECT = Nethermore.getId("shaders/post/tarred.json");
    public static PostChain TARRED = null;
    public static ResourceLocation TUMOREFFECT = Nethermore.getId("shaders/post/tumor.json");
    public static PostChain TUMOR = null;
    public static ResourceLocation SCRAMBLEDEFFECT = Nethermore.getId("shaders/post/scrambled.json");
    public static PostChain SCRAMBLED = null;
    public NethermoreClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

    }
    public static int[] SIZED = {160,90};
    protected static final ResourceLocation CLOUDS_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/clouds.png");
    public static void renderHauntedCragsSky(PoseStack poseStack){

        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, CLOUDS_LOCATION);
        float light = (float)Math.pow(PROXY.lightNess,0.5);
        float off = (float)Math.pow(PROXY.lightNess,0.2);
        float timed=-(float)Math.pow(PROXY.lightNess,1.0)/20.0f;

        Tesselator tesselator = Tesselator.getInstance();
        for(int i = 5; i >=0; --i) {
            poseStack.pushPose();
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
            Matrix4f matrix4f = poseStack.last().pose();
            BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            float v = -10.0F - i * 5;
            bufferbuilder.addVertex(matrix4f, -200.0F, v, -200.0F).setUv(0.0F+i/8.0f+timed, 0.0F+i/8.0f+timed).setColor(0,0.0f,0,light);
            bufferbuilder.addVertex(matrix4f, -200.0F, v, 200.0F).setUv(0.0F+i/8.0f+timed, 1.0F+i/8.0f+timed).setColor(0,0.0f,0,light);
            bufferbuilder.addVertex(matrix4f, 200.0F, v, 200.0F).setUv(1.0F+i/8.0f+timed, 1.0F+i/8.0f+timed).setColor(0,0.0f,0,light);
            bufferbuilder.addVertex(matrix4f, 200.0F, v, -200.0F).setUv(1.0F+i/8.0f+timed, 0.0F+i/8.0f+timed).setColor(0,0.0f,0,light);
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
            poseStack.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    @SubscribeEvent
    public static void world(RenderLevelStageEvent renderLevelStageEvent){
        if(renderLevelStageEvent.getStage()== RenderLevelStageEvent.Stage.AFTER_SKY&&PROXY.lightNess>0.0){
//            PoseStack posestack = new PoseStack();
//            posestack.mulPose(renderLevelStageEvent.getModelViewMatrix());
//            renderHauntedCragsSky(posestack);
        }
        if(renderLevelStageEvent.getStage()== RenderLevelStageEvent.Stage.AFTER_ENTITIES&&DEPTH_TARGET!=null){
            PostChain[] chains = {TUMOR,SCRAMBLED};
            DEPTH_TARGET.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());

            //RESET
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
            for(PostChain i : chains){
                if(i instanceof PostChainDepthPassAccessor accessor){
                    accessor.depthEmPostPasses("TrueDepthSampler",DEPTH_TARGET);
                }
            }

            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
//            if( NethermoreClient.PROXY.blueNess>0.01f){
//
//            }
        }
    }
    public static RenderTarget DEPTH_TARGET = null;
    public static RenderTarget ARM_DEPTH_TARGET = null;
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NMEntityInit.UNPHEASANT.get(), UnpheasantEntityRenderer::new);

        event.registerEntityRenderer(NMEntityInit.NOO.get(), NooEntityRenderer::new);
        event.registerEntityRenderer(NMEntityInit.EGO_MASK.get(), EgoMaskEntityRenderer::new);
        event.registerEntityRenderer(NMEntityInit.EGO.get(), EgoEntityRenderer::new);
        event.registerEntityRenderer(NMEntityInit.PYLON.get(), PylonEntityRenderer::new);
        event.registerEntityRenderer(NMEntityInit.SOUL.get(), SoulProjEntityRenderer::new);
        event.registerEntityRenderer(NMEntityInit.ONLOOKER.get(), OnlookerEntityRenderer::new);
        event.registerEntityRenderer(NMEntityInit.BEHOLDER.get(), ObserverEntityRenderer::new);

        event.registerEntityRenderer(NMEntityInit.TARLING.get(), TarlingEntityRenderer::new);
        event.registerEntityRenderer(NMEntityInit.MORBID_PIGLIN.get(), MorbidPiglinEntityRenderer::new);
        event.registerEntityRenderer(NMEntityInit.DISGUSTLING.get(), DisgustlingEntityRenderer::new);
        event.registerEntityRenderer(NMEntityInit.DISGUSTLING_HEAD.get(), EmptyEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(NMParticleInit.TAR_POP_PARTICLE.get(), TarParticle.Provider::new);
        event.registerSpriteSet(NMParticleInit.TAR_SLOP_PARTICLE.get(), TarSlopParticle.Provider::new);
        event.registerSpriteSet(NMParticleInit.WHISPER_PARTICLE.get(), WhisperParticle.Provider::new);
        event.registerSpriteSet(NMParticleInit.SOUL_PARTICLE.get(), SoulParticle.Provider::new);
    }
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(UnpheasantEntityModel.LAYER_LOCATION, UnpheasantEntityModel::createBodyLayer);
        event.registerLayerDefinition(NooEntityModel.LAYER_LOCATION, NooEntityModel::createBodyLayer);
        event.registerLayerDefinition(OnlookerEntityModel.LAYER_LOCATION, OnlookerEntityModel::createBodyLayer);
        event.registerLayerDefinition(ObserverEntityModel.LAYER_LOCATION, ObserverEntityModel::createBodyLayer);
        event.registerLayerDefinition(EgoEntityModel.LAYER_LOCATION, EgoEntityModel::createBodyLayer);
        event.registerLayerDefinition(PylonEntityModel.LAYER_LOCATION, PylonEntityModel::createBodyLayer);
        event.registerLayerDefinition(TarlingEntityModel.LAYER_LOCATION, TarlingEntityModel::createBodyLayer);
        event.registerLayerDefinition(MorbidPiglinEntityModel.LAYER_LOCATION, MorbidPiglinEntityModel::createBodyLayer);
        event.registerLayerDefinition(DisgustlingEntityModel.LAYER_LOCATION, DisgustlingEntityModel::createBodyLayer);
    }
    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        if (Minecraft.getInstance().gameRenderer instanceof GameRendererResourceManagerAccessor accessor) {
            SIZED= new int[]{Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight()};
            TARRED= new PostChain(Minecraft.getInstance().getTextureManager(), accessor.nethermore$myPrecious(), Minecraft.getInstance().getMainRenderTarget(), TARREDEFFECT);
            TARRED.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
            SCRAMBLED= new PostChain(Minecraft.getInstance().getTextureManager(), accessor.nethermore$myPrecious(), Minecraft.getInstance().getMainRenderTarget(), SCRAMBLEDEFFECT);
            SCRAMBLED.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
            TUMOR= new PostChain(Minecraft.getInstance().getTextureManager(), accessor.nethermore$myPrecious(), Minecraft.getInstance().getMainRenderTarget(), TUMOREFFECT);
            TUMOR.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
            TUMOR.addTempTarget("depthtumortarget",Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
            DEPTH_TARGET=TUMOR.getTempTarget("depthtumortarget");
            TUMOR.addTempTarget("armdepthtumortarget",Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
            ARM_DEPTH_TARGET=TUMOR.getTempTarget("armdepthtumortarget");

        }
    }
    @SubscribeEvent
    public static void clientTickEvent(ClientTickEvent.Pre post) throws IOException {
        if((SIZED[0]!=Minecraft.getInstance().getWindow().getWidth()||
                SIZED[1]!=Minecraft.getInstance().getWindow().getHeight())&&
                TUMOR!=null) {
            SIZED[0] = Minecraft.getInstance().getWindow().getWidth();
            SIZED[1] = Minecraft.getInstance().getWindow().getHeight();
            TUMOR.resize(SIZED[0],SIZED[1]);
            TARRED.resize(SIZED[0],SIZED[1]);
            SCRAMBLED.resize(SIZED[0],SIZED[1]);
            if(DEPTH_TARGET!=null){

                ARM_DEPTH_TARGET.resize(SIZED[0],SIZED[1],true);
                DEPTH_TARGET.resize(SIZED[0],SIZED[1],true);
            }

        }
    }



    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

    }
}
