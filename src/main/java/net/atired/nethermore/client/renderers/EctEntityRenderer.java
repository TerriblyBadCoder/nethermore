package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.atired.nethermore.entity.PrimedEctEntity;
import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class EctEntityRenderer extends EntityRenderer<PrimedEctEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public EctEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    public void render(PrimedEctEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        int i = entity.getFuse();

        if ((float)i - partialTicks + 1.0F < 20.0F) {
            float par = (float)i - partialTicks + 1.0F;
            float f = Mth.sin(par/5.0f*3.14f)*(float)Math.pow((20.0f-par)/20.0f,2.0f);
            float f2 = Mth.cos(par/5.0f*3.14f)*(float)Math.pow((20.0f-par)/20.0f,2.0f);
            float f1 = 1.0F + f * 0.3F;
            poseStack.mulPose(new Quaternionf().rotationZYX(f,0,f2));
            poseStack.scale(f1, f1, f1);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, NMBlockInit.ECT.get().defaultBlockState(), poseStack, buffer, packedLight, i / 5 % 2 == 0);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public ResourceLocation getTextureLocation(PrimedEctEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
