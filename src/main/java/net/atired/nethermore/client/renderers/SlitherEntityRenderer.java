package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NMClientProxy;
import net.atired.nethermore.client.NMRenderLayers;
import net.atired.nethermore.client.renderers.models.SlitherEntityModel;
import net.atired.nethermore.entity.SlitherEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SlitherEntityRenderer extends MobRenderer<SlitherEntity, SlitherEntityModel<SlitherEntity>> {
    private static final ResourceLocation SLITHER_LOCATION = Nethermore.getId("textures/entity/slither.png");

    public SlitherEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SlitherEntityModel<>(context.bakeLayer(SlitherEntityModel.LAYER_LOCATION)), 0.5f);

    }

    @Override
    public ResourceLocation getTextureLocation(SlitherEntity machinationEntity) {
        return SLITHER_LOCATION;
    }

    @Override
    public void render(SlitherEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float slide = entity.getSlide();
//		if(slide>0.0f){
//			slide-=(ageInTicks%1)/10.0f;
//		}
        slide=(float)Math.min(Mth.sin(slide*3.14f)*1.5f,1.0F);
        if(NMRenderLayers.TAR_SHADER_INSTANCE!=null){
            NMRenderLayers.TAR_SHADER_INSTANCE.safeGetUniform("Tarred").set(slide);
        }
        super.render(entity, entityYaw, partialTicks, poseStack, NMClientProxy.getSlitherSource(), packedLight);
        NMClientProxy.getSlitherSource().endBatch();
    }

    @Override
    protected void scale(SlitherEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        float slide = livingEntity.getSlide();
        slide=(float)Math.min(Mth.sin(slide*3.14f)*1.5f,1.0F);
        poseStack.scale(1.0f+slide*1.3f,1.0f-slide*0.92f,1.0f+slide*1.3f);
        super.scale(livingEntity, poseStack, partialTickTime);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
