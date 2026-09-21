package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.renderers.models.DisgustlingEntityModel;
import net.atired.nethermore.entity.DisgustlingEntity;
import net.atired.nethermore.entity.DisgustlingHeadEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class DisgustlingEntityRenderer extends MobRenderer<DisgustlingEntity, DisgustlingEntityModel<DisgustlingEntity>> {
    private static final ResourceLocation DISGUSTLING_LOCATION = Nethermore.getId("textures/entity/disgustling.png");
    private static final ResourceLocation DISGUSTLING_NECK_LOCATION = Nethermore.getId("textures/entity/disgustling_neck.png");

    public DisgustlingEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DisgustlingEntityModel<>(context.bakeLayer(DisgustlingEntityModel.LAYER_LOCATION)), 0.5f);

    }

    @Override
    public ResourceLocation getTextureLocation(DisgustlingEntity machinationEntity) {
        return DISGUSTLING_LOCATION;
    }

    @Override
    public void render(DisgustlingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.tickCount>3&&entity.getHead()!=null){
            float aged = (entity.tickCount+partialTicks)/10.0f;
            DisgustlingHeadEntity headEntity = entity.getHead();
            Vec3 pos = entity.getPosition(partialTicks).add(0,0.86,0);
            Vec3 headDir = headEntity.getPosition(partialTicks)
                    .subtract(pos)
                    .yRot(0);
            Vec3 vecDir = headDir.normalize();
            Vec3 vecLeft = headDir.multiply(1,0,1).normalize().yRot(3.14f/2.0f);
            Vec3 vecFor = vecDir.cross(vecLeft.normalize()).normalize();
            double olY=vecFor.y*0.1875;
            float yaw = (float)Mth.atan2(vecLeft.normalize().x,vecLeft.normalize().z);
            vecFor=new Vec3(0.1875,Mth.cos(-yaw)*olY,0.0);
            vecLeft=new Vec3(0,Mth.sin(-yaw)*olY,0.1875);
            poseStack.pushPose();
            int overlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
            poseStack.translate(-pos.x(),-pos.y(),-pos.z());
            pos=pos.add(0,1.16,0);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(DISGUSTLING_NECK_LOCATION));
            vertexes(aged,poseStack,vecLeft,vecFor,pos,headDir,consumer,overlay,packedLight);
            vertexes(aged,poseStack,vecFor,vecLeft,pos,headDir,consumer,overlay,packedLight);
            vertexes(aged,poseStack,vecLeft.scale(-1),vecFor.scale(-1),pos,headDir,consumer,overlay,packedLight);
            vertexes(aged,poseStack,vecFor.scale(-1),vecLeft.scale(-1),pos,headDir,consumer,overlay,packedLight);

            poseStack.popPose();
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    public void vertexes(float aged, PoseStack poseStack,Vec3 vecLeft,Vec3 vecFor,Vec3 pos,Vec3 headPos,VertexConsumer consumer,int overlay,int light) {
        PoseStack.Pose pose = poseStack.last();
        pos = pos;
        Vec3 posTo = pos.add(headPos);
        Vec3 posToButY =new Vec3(posTo.x,pos.y,posTo.z);
        float lenMul = 1.0f;
        if(headPos.length()>3){
            lenMul=(float)headPos.length()/3.0f;
        }
        Vec3 pos1 = pos.add(vecFor);
        double scaled = 1.0f;
        float length2 = 1.0f-Math.clamp((lenMul*3.0f-6.0f)/2.0f,0.0f,1.0f);
        for (int i = 1; i <= 6; i++) {
            float clamp1= Math.clamp((-Mth.sin((i)/6.0f*3.14f*2.0f)-0.2f)*4.0f,-0.5f,1.f);
            float sink = (1.0f-lenMul)*clamp1*0.5f*length2;
            clamp1=Math.clamp((-Mth.sin((i+1)/6.0f*3.14f*2.0f)-0.2f)*4.0f,-0.5f,1.f);
            float sink2 = (1.0f-lenMul)*clamp1*0.5f*length2;
            double scal2d = Mth.sin(-i/1.5f*3.14f+aged*2.0f)/4.0f+1.0f;
            if(i==6){
                scal2d=1.0f;
                sink2=0.0f;
            }
            if(i==1){
                scaled=0.0f;
                sink=0.0f;
            }
            float powered = (float)Math.pow(i/6.0f,2.0f);
            Vec3 pos2=pos.lerp(posToButY,powered).lerp(posTo,powered).add(vecFor.scale(scal2d));
            powered = (float)Math.pow((6-i)/6.0f,0.66f)*lenMul;
            vertex(pose,consumer,pos2.x+vecLeft.x*scal2d,pos2.y+vecLeft.y*scal2d+sink2,pos2.z+vecLeft.z*scal2d,0,powered,0,0,-1,light,1.0f,overlay);
            vertex(pose,consumer,pos2.x-vecLeft.x*scal2d,pos2.y-vecLeft.y*scal2d+sink2,pos2.z-vecLeft.z*scal2d,1,powered,0,0,-1,light,1.0f,overlay);
            powered = (float)Math.pow((6-i+1)/6.0f,0.66f)*lenMul;

            vertex(pose,consumer,pos1.x-vecLeft.x*scaled,pos1.y-vecLeft.y*scaled+sink,pos1.z-vecLeft.z*scaled,1,powered,0,0,-1,light,1.0f,overlay);
            vertex(pose,consumer,pos1.x+vecLeft.x*scaled,pos1.y+vecLeft.y*scaled+sink,pos1.z+vecLeft.z*scaled,0,powered,0,0,-1,light,1.0f,overlay);
            pos1=pos2;
            scaled=scal2d;
        }
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha,int overlay) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(overlay).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
