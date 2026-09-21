
package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NMRenderLayers;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.entity.SoulProjEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SoulProjEntityRenderer extends EntityRenderer<SoulProjEntity> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
    private static final ResourceLocation BLANK_LOCATION = Nethermore.getId("textures/entity/soultrail.png");

    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;

    public SoulProjEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = 1;
        this.fullBright = true;
    }


    public void render(SoulProjEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        float aged = (entity.tickCount+partialTicks)/5.0f+entity.getId();

        poseStack.pushPose();
        double d0 = Mth.lerp((double)partialTicks, entity.xOld, entity.getX());
        double d1 = Mth.lerp((double)partialTicks, entity.yOld, entity.getY());
        double d2 = Mth.lerp((double)partialTicks, entity.zOld, entity.getZ());
        Vec3 pos = entity.getPosition(partialTicks);
        poseStack.translate(-pos.x(),-pos.y(),-pos.z());
        PoseStack.Pose posed = poseStack.last();

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(BLANK_LOCATION));
        float scaled = Mth.clamp(30.0f-(entity.tickCount+partialTicks)/5.0f,0.0f,1.0f);
        float scaled2 =Mth.clamp(30.0f-(entity.tickCount+partialTicks)/5.0f,0.0f,1.0f);

        float reddened = NethermoreClient.PROXY.redNess2;
        float evilTickCount = Math.clamp((entity.tickCount+partialTicks-1)/8.0f,0.0f,1.0f)*(0.05f+(float)Math.pow(reddened,0.2f)*0.95f);
            float ud = 0;
            float a =2.0f;
            for(int i = entity.posTracker-2;i>1;i--){
                Vec3 second =  entity.positions[i-1].lerp(entity.positions[i],partialTicks);
                Vec3 first = entity.positions[i].lerp(entity.positions[i+1],partialTicks);
                Vec3 zeroth = entity.positions[i+1].lerp(i==entity.posTracker-2?pos:entity.positions[i+2],partialTicks);
                //
                Vec3 to = zeroth.subtract(first).multiply(1,1,1).normalize();
                Vec3 toHor = to.multiply(1,0,1).normalize();
                float yaw = (float)Math.atan2(toHor.x,toHor.z);
                float pitch = (float)Math.asin(to.y);
                Vec3 dirVec = new Vec3(1f,0,0).xRot(-pitch).yRot(yaw+3.14f*0.2f*reddened);
                Vec3 dirCross = dirVec.cross(to).scale(0.5);
                dirVec=dirVec.scale(0.5);
                //
                to =  first.subtract(second).multiply(1,1,1).normalize();
                toHor = to.multiply(1,0,1).normalize();
                yaw = (float)Math.atan2(toHor.x,toHor.z);
                pitch = (float)Math.asin(to.y);
                Vec3 dirVec2 = new Vec3(1f,0,0).xRot(-pitch).yRot(yaw+3.14f*0.2f*reddened);
                Vec3 dirCross2 = dirVec2.cross(to).scale(0.5);
                dirVec2=dirVec2.scale(0.5);

                float ud2=ud+(float)first.add(0,0,0).distanceTo(second.add(0,0,0))/4.0f;
                float a2 = (float)Math.pow(Math.clamp( Math.clamp(((i-1)/((float)(entity.posTracker-2))),0f,1f)-0.5f,0.0f,0.5f),3.0f)*18.0f;
                if(ud2>1.0f){
                    ud2=1.01f;
                }
                vertex(posed, consumer,
                        first.x + dirVec.x * scaled, first.y + dirVec.y * scaled+a*scaled, first.z + dirVec.z * scaled,
                        0.0f, ud, 0, 0, 1, packedLight,evilTickCount);
                vertex(posed,consumer,
                        second.x+dirVec2.x*scaled2,second.y+dirVec2.y*scaled2+a2*scaled2,second.z+dirVec2.z*scaled2,
                        0.0f,ud2,0,0,1,packedLight,evilTickCount);
                vertex(posed,consumer,
                        second.x-dirVec2.x*scaled2,second.y-dirVec2.y*scaled2+a2*scaled2,second.z-dirVec2.z*scaled2,
                        1.0f,ud2,0,0,1,packedLight,evilTickCount);
                vertex(posed,consumer,
                        first.x-dirVec.x*scaled,first.y-dirVec.y*scaled+a*scaled,first.z-dirVec.z*scaled
                        ,1.0f,ud,0,0,1,packedLight,evilTickCount);

                vertex(posed, consumer,
                        first.x + dirCross.x * scaled, first.y + dirCross.y * scaled+a*scaled, first.z + dirCross.z * scaled,
                        0.0f, ud, 0, 0, 1, packedLight,evilTickCount);
                vertex(posed,consumer,
                        second.x+dirCross2.x*scaled2,second.y+dirCross2.y*scaled2+a2*scaled2,second.z+dirCross2.z*scaled2,
                        0.0f,ud2,0,0,1,packedLight,evilTickCount);
                vertex(posed,consumer,
                        second.x-dirCross2.x*scaled2,second.y-dirCross2.y*scaled2+a2*scaled2,second.z-dirCross2.z*scaled2,
                        1.0f,ud2,0,0,1,packedLight,evilTickCount);
                vertex(posed,consumer,
                        first.x-dirCross.x*scaled,first.y-dirCross.y*scaled+a*scaled,first.z-dirCross.z*scaled
                        ,1.0f,ud,0,0,1,packedLight,evilTickCount);
                ud=ud2;
                a =a2;
                if(ud2>=1.0f){
                    break;
                }
            }

        poseStack.popPose();

    }

    @Override
    public boolean shouldRender(SoulProjEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1,1,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
    public ResourceLocation getTextureLocation(SoulProjEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
