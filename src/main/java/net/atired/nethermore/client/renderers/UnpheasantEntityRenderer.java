package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.renderers.models.UnpheasantEntityModel;
import net.atired.nethermore.entity.UnpheasantEntity;
import net.atired.nethermore.entity.UnpheasantEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class UnpheasantEntityRenderer extends MobRenderer<UnpheasantEntity, UnpheasantEntityModel<UnpheasantEntity>> {
    private static final ResourceLocation UNPHEASANT_LOCATION = Nethermore.getId("textures/entity/unpheasant.png");
    private static final ResourceLocation UNPHEASANT_LEGS_LOCATION = Nethermore.getId("textures/entity/unpheasant_legs.png");

    public UnpheasantEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new UnpheasantEntityModel<>(context.bakeLayer(UnpheasantEntityModel.LAYER_LOCATION)), 0.5f);

    }

    @Override
    public ResourceLocation getTextureLocation(UnpheasantEntity machinationEntity) {
        return UNPHEASANT_LOCATION;
    }

    @Override
    public void render(UnpheasantEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.tickCount>3){
            poseStack.pushPose();
            int overlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));;
            Vec3 pos = entity.getPosition(partialTicks).add(0,-0.0,0);
            poseStack.translate(-pos.x(),-pos.y(),-pos.z());
            pos=pos.add(0,0.1,0);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(UNPHEASANT_LEGS_LOCATION));
            int count=0;
            for(Vec3 lPos : entity.oldLegPositions) {
                Vec3 pos1=new Vec3(0.125,0,0).yRot(-entityYaw/180.0f*3.14f);
                Vec3 pos2=new Vec3(0.125,0,0).yRot(-entityYaw/180.0f*3.14f+3.14f);
                Vec3 off =new Vec3(0.3,0,0).yRot(-entityYaw/180.0f*3.14f+count*3.14f);
                poseStack.pushPose();
                poseStack.translate(off.x,off.y,off.z);
                Vec3 legPos = lPos.add(0, Mth.sin(entity.legFloats[count]*3.14f)/3.0f,0);
                PoseStack.Pose pose = poseStack.last();
                Vec3 oPos1=pos;
                float legDist = (float) pos.distanceTo(legPos);
                legDist=(1.0f-Mth.clamp(legDist,0.0f,3.5f)/3.5f);
                Vec3 forth=new Vec3(0.5,0,0).yRot(-entityYaw/180.0f*3.14f+3.14f/2.0f).scale(legDist);
                for (int i = 0; i <= 6; i++) {
                    Vec3 oPos2 = pos.lerp(legPos,(i+1)/7.0f).add(i%2==1?forth:new Vec3(0,0,0));
                    vertex(pose,consumer,pos1.x+oPos1.x,oPos1.y,oPos1.z+pos1.z,0,(i)/7.0f,0,1,0,packedLight,1.0f,overlay);
                    vertex(pose,consumer,pos2.x+oPos1.x,oPos1.y,oPos1.z+pos2.z,1,(i)/7.0f,0,1,0,packedLight,1.0f,overlay);

                    vertex(pose,consumer,pos2.x+oPos2.x,oPos2.y,oPos2.z+pos2.z,1,(i+1)/7.0f,0,1,0,packedLight,1.0f,overlay);
                    vertex(pose,consumer,pos1.x+oPos2.x,oPos2.y,oPos2.z+pos1.z,0,(i+1)/7.0f,0,1,0,packedLight,1.0f,overlay);
                    oPos1=oPos2;
                }
                poseStack.popPose();
                count+=1;
            }
            poseStack.popPose();
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha,int overlay) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(overlay).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
