package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.entity.EgoMaskEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EgoMaskEntityRenderer extends EntityRenderer<EgoMaskEntity> {
    private static final ResourceLocation MASK_LOCATION = Nethermore.getId("textures/entity/mask.png");
    private static final ResourceLocation MASK_CHAIN_LOCATION = Nethermore.getId("textures/entity/mask_chain.png");

    public EgoMaskEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EgoMaskEntity egoMaskEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(egoMaskEntity.maskOwner==null)return;
        float reddened = Mth.sin(NethermoreClient.PROXY.redNess2*3.14f);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(MASK_CHAIN_LOCATION));
        Vector3f vector3f = egoMaskEntity.maskOwner.getEyePosition(partialTick).subtract(0,0.6+reddened*0.3,0.0).toVector3f().sub(egoMaskEntity.getPosition(partialTick).toVector3f(),new Vector3f());
        vector3f=vector3f.add((egoMaskEntity.maskOwner.getViewVector(partialTick).multiply(1,0,1).normalize().scale(0.5*reddened)).toVector3f(),new Vector3f());
        poseStack.pushPose();
        poseStack.translate(0,0.6,0);
        PoseStack.Pose posed = poseStack.last();
        float length = Math.min(1.0f,vector3f.length()/4.0f);
        vertex(posed,consumer,0f,0.5f/4.0f,0.0f,0.0f,0,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,0f,-0.5f/4.0f,-0.0f,0.0f,1,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,vector3f.x,vector3f.y+0.5f/4.0f,vector3f.z,length,0,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,vector3f.x,vector3f.y-0.5f/4.0f,vector3f.z,length,1,-1,0,0,packedLight,1.0f);

        poseStack.popPose();

        consumer = bufferSource.getBuffer(RenderType.entityTranslucent(MASK_LOCATION));
        poseStack.pushPose();
        poseStack.scale(1.5f,1.5f,1.5f);
        poseStack.translate(0,0.6,0);
        poseStack.mulPose(new Quaternionf().rotationXYZ(0,-entityYaw/180.0f*3.14f, Mth.sin((egoMaskEntity.tickCount+partialTick)/8.0f)/12.0f));
        posed = poseStack.last();
        vertex(posed,consumer,-0.3f,0.5f,0.0f,0.2f,0,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,0.3f,0.5f,-0.0f,0.8f,0,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,0.3f,-0.5f,-0.0f,0.8f,1,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,-0.3f,-0.5f,0.0f,0.2f,1,-1,0,0,packedLight,1.0f);

        vertex(posed,consumer,-0.3f,0.5f,-0.2f,0.0f,0,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,-0.3f,0.5f,0.0f,0.2f,0,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,-0.3f,-0.5f,0.0f,0.2f,1,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,-0.3f,-0.5f,-0.2f,0.0f,1,-1,0,0,packedLight,1.0f);

        vertex(posed,consumer,0.3f,0.5f,0.f,0.8f,0,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,0.3f,0.5f,-0.2f,1f,0,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,0.3f,-0.5f,-0.2f,1f,1,-1,0,0,packedLight,1.0f);
        vertex(posed,consumer,0.3f,-0.5f,0.f,0.8f,1,-1,0,0,packedLight,1.0f);

        poseStack.popPose();
        super.render(egoMaskEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1,1,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
    @Override
    public ResourceLocation getTextureLocation(EgoMaskEntity entity) {
        return MASK_LOCATION;
    }
}
