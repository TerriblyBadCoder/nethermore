package net.atired.nethermore.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.accessors.LivingEntityTarAccessor;
import net.atired.nethermore.init.NMBiomeInit;
import net.atired.nethermore.init.NMFluidInit;
import net.atired.nethermore.init.NMSoundInit;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = Nethermore.MODID,value = Dist.CLIENT)
public class NethermoreClientEvents {
    private static final ResourceLocation OIL_SLICK_LOCATION = Nethermore.getId("textures/entity/oil_slick.png");

    @SubscribeEvent
    public static void livingRender(RenderLivingEvent.Post renderLivingEvent){
        if(renderLivingEvent.getEntity() instanceof LivingEntityTarAccessor accessor&&(accessor.getTarred()>0.01f||!(renderLivingEvent.getEntity()instanceof Player))){

            if(renderLivingEvent.getEntity() instanceof LivingEntity living&&living.isInFluidType(NMFluidInit.TAR_FLUID_TYPE.get())){
                VertexConsumer vertexConsumer = renderLivingEvent.getMultiBufferSource().getBuffer(RenderType.entityTranslucent(OIL_SLICK_LOCATION));
                PoseStack poseStack = renderLivingEvent.getPoseStack();
                poseStack.pushPose();
                PoseStack.Pose pose = poseStack.last();
                float scaled = Math.min(1.0f,accessor.getTarred()==0.0f?1.0f:accessor.getTarred());
                float off = (float) living.getFluidTypeHeight(NMFluidInit.TAR_FLUID_TYPE.get());
                float aged =(renderLivingEvent.getPartialTick()+living.tickCount)/9.0f;
                for (int i = 0; i < 4; i++) {
                    Vec3 dir1 = new Vec3(0.7f,0,0).yRot(-3.14f/4.0f+i/2.0f*3.14f);
                    Vec3 dir2 = new Vec3(0.7f,0,0).yRot(3.14f/4.0f+i/2.0f*3.14f);
                    vertex(pose,vertexConsumer,dir1.x,(1.7f+Mth.sin(aged+i)/5.0f+off/4)*scaled,dir1.z,0,off/1.7f,0,0,-1,renderLivingEvent.getPackedLight(),0.5f);
                    vertex(pose,vertexConsumer,dir1.x,(off/2)*scaled,dir1.z,0,1,0,0,-1,renderLivingEvent.getPackedLight(),1);
                    vertex(pose,vertexConsumer,dir2.x,(off/2)*scaled,dir2.z,1,1,0,0,-1,renderLivingEvent.getPackedLight(),1);
                    vertex(pose,vertexConsumer,dir2.x,(1.7f+Mth.sin(aged+i+1)/5.0f+off/4)*scaled,dir2.z,1,off/1.7f,0,0,-1,renderLivingEvent.getPackedLight(),0.5f);

                }
                poseStack.popPose();
            }
        }
    }
    public static void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
    @SubscribeEvent
    public static void clTick(ClientTickEvent.Pre tickEvent){
        NethermoreClient.PROXY.whispered*=0.95f;
        NethermoreClient.PROXY.redNess=Math.max(0.0f,Math.max(NethermoreClient.PROXY.redNess-0.03f,NethermoreClient.PROXY.redNess*0.75f));
        NethermoreClient.PROXY.redNess2=Math.max(0.0f,Math.max(NethermoreClient.PROXY.redNess2-0.03f,NethermoreClient.PROXY.redNess2*0.75f));

        NethermoreClient.PROXY.lightNess=Math.max(0.0f,Math.max(NethermoreClient.PROXY.lightNess-0.015f,NethermoreClient.PROXY.lightNess*0.8f));
        if(Minecraft.getInstance().player!=null&&Minecraft.getInstance().player.isInFluidType(NMFluidInit.TAR_FLUID_TYPE.get())){
            NethermoreClient.PROXY.tarNess= Mth.lerp(0.1f,NethermoreClient.PROXY.tarNess,1.0f);
        }else{
            NethermoreClient.PROXY.tarNess*=0.8f;
        }
        if(Minecraft.getInstance().player!=null&&Minecraft.getInstance().level.getBiome(Minecraft.getInstance().player.getOnPos()).is(NMBiomeInit.HAUNTED_CRAGS)) {
            NethermoreClient.PROXY.blueNess= Mth.lerp(0.1f,NethermoreClient.PROXY.blueNess,1.0f);
            if( NethermoreClient.PROXY.blueCD<=0){
                if(Math.random()>0.9){
                    NethermoreClient.PROXY.lightNess=0.99f;
                    Minecraft.getInstance().player.playSound(SoundEvents.LIGHTNING_BOLT_IMPACT,0.2f,0.6f-(float)Math.random()/8.0f);
                    Minecraft.getInstance().player.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER,1.1f,0.6f-(float)Math.random()/8.0f);
                    NethermoreClient.PROXY.blueCD=300+(int)(Math.random()*100);
                }
            }else{
                NethermoreClient.PROXY.blueCD-=1;
            }

        }else{
            NethermoreClient.PROXY.blueNess*=0.86f;
        }
        if(Minecraft.getInstance().player!=null&&Minecraft.getInstance().level.getBiome(Minecraft.getInstance().player.getOnPos()).is(NMBiomeInit.SCRAMBLED_PITS)) {
            NethermoreClient.PROXY.nihiloNess= Mth.lerp(0.1f,NethermoreClient.PROXY.nihiloNess,1.0f);

        }else{
            NethermoreClient.PROXY.nihiloNess*=0.96f;
        }
    }

    @SubscribeEvent
    public static void fog2(ViewportEvent.RenderFog computeFogColor){
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        if(Minecraft.getInstance().level!=null&&Minecraft.getInstance().level.getFluidState(cam.getBlockPosition()).getFluidType()==(NMFluidInit.TAR_FLUID_TYPE.get())){
            computeFogColor.scaleNearPlaneDistance(0.03f);
            computeFogColor.scaleFarPlaneDistance(0.03f);
            computeFogColor.setCanceled(true);
        }
        if(NethermoreClient.PROXY.blueNess>0.01f) {
            computeFogColor.scaleNearPlaneDistance(1.0f-NethermoreClient.PROXY.blueNess*1.1f+NethermoreClient.PROXY.lightNess*0.8f);
            computeFogColor.scaleFarPlaneDistance(1.0f-NethermoreClient.PROXY.blueNess*0.2f+NethermoreClient.PROXY.lightNess*0.08f);
            computeFogColor.setCanceled(true);
        }
        if(NethermoreClient.PROXY.nihiloNess>0.01f) {
            computeFogColor.scaleNearPlaneDistance(1.0f-NethermoreClient.PROXY.nihiloNess*2.6f);
            computeFogColor.scaleFarPlaneDistance(1.0f-NethermoreClient.PROXY.nihiloNess*0.25f);
            computeFogColor.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void fog(ViewportEvent.ComputeFogColor computeFogColor){
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        if(Minecraft.getInstance().level!=null&&Minecraft.getInstance().level.getFluidState(cam.getBlockPosition()).getFluidType()==(NMFluidInit.TAR_FLUID_TYPE.get())){
            computeFogColor.setRed(computeFogColor.getRed()*0.1f);
            computeFogColor.setBlue(computeFogColor.getBlue()*0.1f);
            computeFogColor.setGreen(computeFogColor.getGreen()*0.1f);
        }
        if(NethermoreClient.PROXY.lightNess>0.01f){
            computeFogColor.setBlue(computeFogColor.getBlue()*(1.0f+2*NethermoreClient.PROXY.lightNess)+NethermoreClient.PROXY.lightNess/2.0f);
            computeFogColor.setGreen(computeFogColor.getGreen()*(1.0f+NethermoreClient.PROXY.lightNess*5f)+NethermoreClient.PROXY.lightNess/2.0f);
            computeFogColor.setRed(computeFogColor.getRed()*(1.0f+NethermoreClient.PROXY.lightNess*5f)+NethermoreClient.PROXY.lightNess/2.0f);
        }
        if(NethermoreClient.PROXY.nihiloNess>0.01f&&Minecraft.getInstance().level!=null){
            float toAdd = NethermoreClient.PROXY.nihiloNess*Minecraft.getInstance().level.getRainLevel((float)computeFogColor.getPartialTick());
            computeFogColor.setBlue(computeFogColor.getBlue()*(1.0f+toAdd));
            computeFogColor.setGreen(computeFogColor.getGreen()*(1.0f+toAdd));
            computeFogColor.setRed(computeFogColor.getRed()*(1.0f+toAdd));
        }
//        if(NethermoreClient.PROXY.redNess>0.01f) {
//            computeFogColor.setBlue(computeFogColor.getBlue()*(1.0f+0.2f*NethermoreClient.PROXY.redNess));
//            computeFogColor.setGreen(computeFogColor.getGreen()*(1.0f-NethermoreClient.PROXY.redNess*0.2f));
//            computeFogColor.setRed(computeFogColor.getRed()*(1.0f-NethermoreClient.PROXY.redNess*0.2f));
//        }

    }
}
