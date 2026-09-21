package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.renderers.models.TarlingEntityModel;
import net.atired.nethermore.entity.TarlingEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;

public class TarlingEntityRenderer extends MobRenderer<TarlingEntity, TarlingEntityModel<TarlingEntity>> {
    private static final ResourceLocation TARLING_LOCATION = Nethermore.getId("textures/entity/tarling.png");

    public TarlingEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new TarlingEntityModel<>(context.bakeLayer(TarlingEntityModel.LAYER_LOCATION)), 0.5f);

        this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(TarlingEntity machinationEntity) {
        return TARLING_LOCATION;
    }

    @Override
    public void render(TarlingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    protected void scale(TarlingEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        float f = 0.999F;
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float f1 = (float)livingEntity.getSize();
        float f2 = Mth.lerp(partialTickTime, livingEntity.oSquish, livingEntity.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2*5.0f + 1.0F);
        float scale2=(f2*f2*2.4f + 1.0F);
        poseStack.scale(f3 * f1*scale2, 1.0F / f3 * f1, f3 * f1*scale2);
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
