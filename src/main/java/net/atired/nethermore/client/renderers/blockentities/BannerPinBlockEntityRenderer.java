package net.atired.nethermore.client.renderers.blockentities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.blockentities.BannerPinBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class BannerPinBlockEntityRenderer implements BlockEntityRenderer<BannerPinBlockEntity> {
    private static final ResourceLocation BANNER_LOCATION = Nethermore.getId("textures/entity/pin_banner.png");
    public BannerPinBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }
    @Override
    public void render(BannerPinBlockEntity bannerPinBlock, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        float yRot = 0;
        float offX = 0.17f;
        RenderSystem.enableDepthTest();
        if(bannerPinBlock.getBlockState().getOptionalValue(BlockStateProperties.FACING).isPresent()){
            Direction dir = bannerPinBlock.getBlockState().getOptionalValue(BlockStateProperties.FACING).get();
            if(dir.getStepZ()!=0){
                yRot=1.57f;
            }
            if(dir.getStepX()-dir.getStepZ()<0){
                offX*=-1.0f;
            }
            if(dir.getStepY()!=0){
                if(dir.getStepY()==1){
                    return;
                }
                offX=0;
                yRot=(bannerPinBlock.getBlockPos().getX()+bannerPinBlock.getBlockPos().getZ())%2==0?0.0f:1.57f;
            }
        }
        poseStack.pushPose();
        poseStack.translate(0.5,0.5,0.5);
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(BANNER_LOCATION));

        Vec3 oDir1 = new Vec3(0.25+offX,0,0).yRot(yRot);
        Vec3 oDir2 = new Vec3(-0.25+offX,0,0).yRot(yRot);
        int lightCol = i;
        for (int j = 0; j < 12; j++) {
            float rotated = Mth.cos(bannerPinBlock.aged/3.0f+j*3.14f/4.0f+Mth.sin((float)bannerPinBlock.getBlockPos().getX()/300.0f*6.28f+bannerPinBlock.getBlockPos().getZ()/300.0f*6.28f)*6.28f*2.0f)/50.0f*(1+j/2.0f);
            float rotated2 = Mth.cos(bannerPinBlock.aged/3.0f+j*3.14f*6.0f/4.0f+Mth.sin((float)bannerPinBlock.getBlockPos().getX()/300.0f*6.28f+bannerPinBlock.getBlockPos().getZ()/300.0f*6.28f)*6.28f*2.0f)/10.0f*(1+j/16.0f);
            Vec3 dir1;
            if(yRot>0){
                dir1=oDir1.add(new Vec3(0,-0.5,0).xRot(rotated).zRot(rotated2));
            }else{
                dir1=oDir1.add(new Vec3(0,-0.5,0).zRot(rotated).xRot(rotated2));
            }
            Vec3 dir2 = dir1.subtract(oDir1).add(oDir2);
            vertex(pose,consumer,oDir2.x,oDir2.y,oDir2.z,0,j/12.0f,0,0,1,lightCol,1.0f);
            vertex(pose,consumer,oDir1.x,oDir1.y,oDir1.z,1,j/12.0f,0,0,1,lightCol,1.0f);
            lightCol=getLightColor((j+1)/12.0f,i);
            vertex(pose,consumer,dir1.x,dir1.y,dir1.z,1,(j+1)/12.0f,0,0,1,lightCol,1.0f);
            vertex(pose,consumer,dir2.x,dir2.y,dir2.z,0,(j+1)/12.0f,0,0,1,lightCol,1.0f);
            oDir1=dir1;
            oDir2=dir2;
        }
        poseStack.popPose();

    }
    public int getLightColor(float f,int i) {
        f = Mth.clamp(f, 0.0F, 1.0F);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1,1,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
}
