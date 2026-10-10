package net.atired.nethermore.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.entity.AshBallEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class AshBallEntityRenderer extends ThrownItemRenderer<AshBallEntity> {
    public AshBallEntityRenderer(EntityRendererProvider.Context context, float scale, boolean fullBright) {
        super(context, scale, fullBright);
    }

    @Override
    public void render(AshBallEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
