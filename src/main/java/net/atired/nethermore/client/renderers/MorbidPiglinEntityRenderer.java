package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.renderers.models.MorbidPiglinEntityModel;
import net.atired.nethermore.entity.MorbidPiglinEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MorbidPiglinEntityRenderer extends MobRenderer<MorbidPiglinEntity, MorbidPiglinEntityModel<MorbidPiglinEntity>> {
    private static final ResourceLocation MORBID_PIGLIN_LOCATION = Nethermore.getId("textures/entity/morbid_piglin.png");

    public MorbidPiglinEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new MorbidPiglinEntityModel<>(context.bakeLayer(MorbidPiglinEntityModel.LAYER_LOCATION)), 0.5f);

        this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(MorbidPiglinEntity machinationEntity) {
        return MORBID_PIGLIN_LOCATION;
    }

    @Override
    public void render(MorbidPiglinEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
