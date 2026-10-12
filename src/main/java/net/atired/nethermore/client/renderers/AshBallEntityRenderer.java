package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.entity.AshBallEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class AshBallEntityRenderer extends ThrownItemRenderer<AshBallEntity> {
    public AshBallEntityRenderer(EntityRendererProvider.Context context, float scale, boolean fullBright) {
        super(context, scale, fullBright);
    }
    public AshBallEntityRenderer(EntityRendererProvider.Context context) {
        this(context, 1.0F, false);
    }

    private static final ResourceLocation ASH_TRAIL_LOCATION = Nethermore.getId("textures/entity/ash_ball_trail.png");

    @Override
    public void render(AshBallEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        Vec3 pos = entity.getPosition(partialTicks);
        poseStack.translate(-pos.x(),-pos.y()+0.1f,-pos.z());
        PoseStack.Pose posed = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(ASH_TRAIL_LOCATION));

        float sinused = 1.0f;
        float ud = 0.0f;
        for(int i = entity.posTracker;i>0;i--){
            Vec3 first = new Vec3(0,0,0);
            Vec3 second =  entity.positions[i-1];
            if(i<entity.posTracker){
                first = entity.positions[i];
            }else{
                first=entity.getPosition(partialTicks);
            }
            float a = 1;
            vertex(posed,consumer, first.x,first.y+0.25f*sinused,first.z,ud,0.0f,0,-1,0,packedLight,a);
            vertex(posed,consumer, first.x,first.y-0.25f*sinused,first.z,ud,1.0f,0,-1,0,packedLight,a);

            a = 1;
            ud+=(float)first.distanceTo(second)/2.0f;
            ud=Math.min(1.0f,ud);

            vertex(posed,consumer, second.x,second.y-0.25f*sinused,second.z,ud,1.0f,0,-1,0,packedLight,a);
            vertex(posed,consumer, second.x,second.y+0.25f*sinused,second.z,ud,0.0f,0,-1,0,packedLight,a);
            if(ud>=1.0f){
                break;
            }
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
}
