package net.atired.nethermore.client.renderers.blockentities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.atired.nethermore.blockentities.InfusingStandBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

public class InfusingStandBlockEntityRenderer implements BlockEntityRenderer<InfusingStandBlockEntity> {
    private static final float SIZE = 0.375F;
    private final ItemRenderer itemRenderer;

    public InfusingStandBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }
    @Override
    public void render(InfusingStandBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {

            poseStack.pushPose();
            poseStack.translate(0.55F, 0.14921875F, 0.45F);
            int seed = (int)blockEntity.getBlockPos().asLong();

            poseStack.mulPose(Axis.YP.rotationDegrees(165.0f+ (float)Math.sin(Math.abs(blockEntity.getBlockPos().getX())*3.0f+Math.abs(blockEntity.getBlockPos().getZ())*12.0f)*20.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(0.375F, 0.375F, 0.375F);
            this.itemRenderer.renderStatic(blockEntity.getItem(1), ItemDisplayContext.FIXED, i, i1, poseStack, multiBufferSource, blockEntity.getLevel(), seed);
            poseStack.popPose();

    }
}
