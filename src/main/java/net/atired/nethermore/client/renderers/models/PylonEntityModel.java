// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NMRenderLayers;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.entity.MorbidPiglinEntity;
import net.atired.nethermore.entity.PylonEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

import net.atired.nethermore.entity.NooEntity;

public class PylonEntityModel<T extends PylonEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("pylonentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart right_hind_leg;
	private final ModelPart left_hind_leg;
	private final ModelPart root;

	public PylonEntityModel(ModelPart root) {
		super(NMRenderLayers::entityMonochromeCull);
		this.body = root.getChild("body");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.right_hind_leg = root.getChild("right_hind_leg");
		this.left_hind_leg = root.getChild("left_hind_leg");
		this.root=root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -23.0F, -6.0F, 12.0F, 25.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 37).addBox(-3.0F, -2.0F, -3.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 15.0F, -4.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(-2.0F, -2.0F, -3.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 15.0F, -4.0F));

		PartDefinition right_hind_leg = partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create(), PartPose.offset(-5.0F, 18.0F, 5.0F));

		PartDefinition cube_r1 = right_hind_leg.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 37).addBox(-3.0F, -2.0F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition left_hind_leg = partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create(), PartPose.offset(5.0F, 18.0F, 5.0F));

		PartDefinition cube_r2 = left_hind_leg.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 37).mirror().addBox(-2.0F, -2.0F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(PylonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.body.yRot=Mth.sin(ageInTicks/16.0f)/8.0f+netHeadYaw/180.0f*3.14f/4.0f;
		if(NethermoreClient.PROXY.redNess2>0.0f){
			this.body.yRot-=(float)Math.pow(Math.max(0.0f,NethermoreClient.PROXY.redNess2-0.1f)/0.9f,2.0f)*3.14f*2.0f;
		}
		this.body.xRot=Mth.cos(ageInTicks/8.0f)/20.0f+headPitch/180.0f*3.14f/20.0f;
		this.body.xRot+=Mth.sin(ageInTicks/8.0f)/14.0f-0.1f+Mth.sin(this.attackTime*3.14f)*1.1f;
		this.left_leg.xRot=Mth.sin(limbSwing)*limbSwingAmount+Mth.sin(ageInTicks/12.0f)/20.0f;
		this.right_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount-Mth.sin(ageInTicks/12.0f)/20.0f;
		this.left_hind_leg.xRot=Mth.cos(limbSwing)*limbSwingAmount+Mth.cos(ageInTicks/14.0f)/10.0f;
		this.right_hind_leg.xRot=-Mth.cos(limbSwing)*limbSwingAmount-Mth.cos(ageInTicks/14.0f)/10.0f;
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}