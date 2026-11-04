package net.atired.nethermore.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.atired.nethermore.client.NMClientProxy;
import net.atired.nethermore.client.NMRenderLayers;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.init.NMItemComponentInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ItemRenderer.class)
public class NMItemRendererMixin {
    @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",at= @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;",ordinal = 1))
    private void yeaj(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel p_model, CallbackInfo ci){
        if(itemStack.has(NMItemComponentInit.INFUSED)){
            NethermoreClient.PROXY.infuseNext=true;
            Color col = new Color(itemStack.get(NMItemComponentInit.INFUSED).getColor());
            NMRenderLayers.INFUSION_SHADER_INSTANCE.safeGetUniform("InfusedColorModulator").set(new Vector4f(col.getRed()/255.0f,col.getGreen()/255.0f,col.getBlue()/255.0f,1.0f));

        }else{
            NethermoreClient.PROXY.infuseNext=false;
        }
    }
    @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",at= @At(value = "INVOKE",shift = At.Shift.BEFORE, target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
    private void yeaj2(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel p_model, CallbackInfo ci){
        if(NethermoreClient.PROXY.infuseNext) {
            NMClientProxy.getInfusionSource().endBatch();
        }
    }
    @WrapMethod(method = "getFoilBufferDirect(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;")
    private static VertexConsumer yeah(MultiBufferSource bufferSource, RenderType renderType, boolean noEntity, boolean withGlint, Operation<VertexConsumer> original){
        VertexConsumer consumer = original.call(bufferSource,renderType,noEntity,withGlint);
        if(NethermoreClient.PROXY.infuseNext){
            return withGlint?VertexMultiConsumer.create(bufferSource.getBuffer(RenderType.glint()), NMClientProxy.getInfusionSource().getBuffer(NMRenderLayers.infusion(TextureAtlas.LOCATION_BLOCKS))):NMClientProxy.getInfusionSource().getBuffer(NMRenderLayers.infusion(TextureAtlas.LOCATION_BLOCKS));
        }
        return consumer;
    }
}
