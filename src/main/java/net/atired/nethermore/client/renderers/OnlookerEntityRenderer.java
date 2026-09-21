package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.renderers.models.OnlookerEntityModel;
import net.atired.nethermore.entity.OnlookerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class OnlookerEntityRenderer extends MobRenderer<OnlookerEntity, OnlookerEntityModel<OnlookerEntity>> {
    private static final ResourceLocation ONLOOKER_LOCATION = Nethermore.getId("textures/entity/onlooker.png");
    private static final ResourceLocation ONLOOKER_VINE_LOCATION = Nethermore.getId("textures/entity/onlooker_vine.png");

    public OnlookerEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new OnlookerEntityModel<>(context.bakeLayer(OnlookerEntityModel.LAYER_LOCATION)), 0.3f);

        this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(OnlookerEntity machinationEntity) {
        return ONLOOKER_LOCATION;
    }

    @Override
    public boolean shouldRender(OnlookerEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(OnlookerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.getSource().length()>0.01f){
            poseStack.pushPose();
            poseStack.translate(0,0.16,0);
            PoseStack.Pose posed = poseStack.last();
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(ONLOOKER_VINE_LOCATION));
            Vec3 dirTo = new Vec3(0,0,0);
            if(entity.getEyeId()==-1){
                dirTo = entity.getPosition(partialTicks).subtract(new Vec3(entity.getSource())).add(0,0.13,0);
            }else if(entity.eyeOwner!=null){
                dirTo = entity.getPosition(partialTicks).subtract(entity.eyeOwner.getEyePosition(partialTicks).add(entity.eyeOwner.getViewVector(partialTicks).multiply(1,0,1).normalize().scale(0.07)).add(0,-0.1,0));
            }
            float catenary = (float)Math.pow(Math.max(0.0f,6.0f-(float)dirTo.length()),0.5f)*0.6f;
            float downO = 0.0f;
            int overlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
            Vec3 vec1 = new Vec3(0,0,0);
            Vec3 dirNor0 = dirTo.scale(-1.0f/12.0f).add(0,-Mth.sin(1/6.0f*3.14f)*catenary,0).normalize();

            float yaw0 = (float)Math.atan2(dirNor0.x,dirNor0.z);
            float pitch0 = (float)Math.asin(dirNor0.y);
            float uO=0.0f;
            float aged = (entity.tickCount+partialTicks)/8.0f;

            Vec3 olVecOff= new Vec3(0,0.25,0).yRot(yaw0+aged).xRot(-pitch0);
            Vec3 olVecOffNormal= new Vec3(0,1,0).yRot(yaw0+aged+3.14f/2.0f).xRot(-pitch0+3.14f/2.0f);
            for (int i = 1; i <= 12; i++) {
                float down = Mth.sin(i/6.0f*3.14f)*catenary;
                Vec3 vec2 = vec1.lerp(dirTo.scale(-1),Math.pow(i/12.0f,2));
                Vec3 dirNor =(vec2.subtract(vec1)).add(0,-down+downO,0).normalize();

                float yaw = (float)Math.atan2(dirNor.x,dirNor.z);
                float pitch = (float)Math.asin(dirNor.y);
                float u=uO+(float)vec1.add(0,-downO,0).distanceTo(vec2.add(0,-down,0))*0.33f;
                Vec3 vecOff =  new Vec3(0,0.25,0).zRot(yaw+u*1.14f+aged).xRot(-pitch);
                if(i>=11){
                    vecOff=vecOff.scale((12-i)/2.0);
                }
                Vec3 vecOffNormal =  new Vec3(0,1,0).zRot(yaw+u*1.14f+aged+3.14f/2.0f).xRot(-pitch+3.14f/2.0f);

                vertex(posed,consumer,vec1.x+olVecOff.x,vec1.y-downO+olVecOff.y,vec1.z+olVecOff.z,uO,0,olVecOffNormal.x,olVecOffNormal.y,olVecOffNormal.z,packedLight,1.0f,overlay);
                vertex(posed,consumer,vec2.x+vecOff.x,vec2.y-down+vecOff.y,vec2.z+vecOff.z,u,0,vecOffNormal.x,vecOffNormal.y,vecOffNormal.z,packedLight,1.0f,overlay);
                vertex(posed,consumer,vec2.x-vecOff.x,vec2.y-down-vecOff.y,vec2.z-vecOff.z,u,1,vecOffNormal.x,vecOffNormal.y,vecOffNormal.z,packedLight,1.0f,overlay);
                vertex(posed,consumer,vec1.x-olVecOff.x,vec1.y-downO-olVecOff.y,vec1.z-olVecOff.z,uO,1,olVecOffNormal.x,olVecOffNormal.y,olVecOffNormal.z,packedLight,1.0f,overlay);
                //
                downO=down;
                olVecOff=vecOff;
                vec1=vec2;
                olVecOffNormal=vecOffNormal;
                uO=u;
            }
            poseStack.popPose();
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, double normalX, double normalY, double normalZ, int packedLight, float alpha,int overlay) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
