// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.entity.EgoEntity;
import net.atired.nethermore.entity.MorbidPiglinEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

import net.atired.nethermore.entity.NooEntity;

public class EgoEntityModel<T extends EgoEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("egoentitymodel"), "main");
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart head;
	private final ModelPart left_head;
	private final ModelPart right_head;
	private final ModelPart root;

	public EgoEntityModel(ModelPart root) {
		this.root=root;
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.body = root.getChild("body");
		this.right_arm = this.body.getChild("right_arm");
		this.left_arm = this.body.getChild("left_arm");
		this.head = this.body.getChild("head");
		this.left_head = this.head.getChild("left_head");
		this.right_head = this.head.getChild("right_head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 32).addBox(-2.9F, -1.8F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.1F, -10.2F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 32).mirror().addBox(-0.1F, -1.8F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.1F, -10.2F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 20).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

		PartDefinition left_head = head.addOrReplaceChild("left_head", CubeListBuilder.create(), PartPose.offsetAndRotation(1.4F, 0.0F, 3.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition cube_r1 = left_head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, -5.0F, 6.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition right_head = head.addOrReplaceChild("right_head", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.4F, 0.0F, 3.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition cube_r2 = right_head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -6.0F, -5.0F, 6.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, -0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(EgoEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot=headPitch/180.0f*3.14f;
		this.head.yRot=netHeadYaw/180.0f*3.14f;
		float reddened = Mth.sin(NethermoreClient.PROXY.redNess2*3.14f);
		this.left_head.yRot=-reddened/2.0f;
		this.right_head.yRot=reddened/2.0f;
		this.left_head.zRot=-reddened/16.0f;
		this.right_head.zRot=reddened/16.0f;
		this.head.xRot+=Mth.sin(ageInTicks/12.0f)/12.0f-reddened/2.5f;
		this.head.zRot=Mth.cos(ageInTicks/12.0f)/12.0f;
		this.body.xRot=Mth.sin(ageInTicks/18.0f)/9.0f+reddened/2.0f;
		this.body.zRot=Mth.cos(ageInTicks/18.0f)/9.0f;
		this.right_leg.xRot= Mth.sin(limbSwing)*limbSwingAmount;
		this.left_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount;
		this.right_arm.xRot= Mth.sin(limbSwing)*limbSwingAmount/1.0f+Mth.sin(ageInTicks/3.0f)/12.0f;
		this.left_arm.xRot=-Mth.sin(limbSwing)*limbSwingAmount/1.0f-Mth.sin(ageInTicks/3.0f)/12.0f;
		this.right_arm.zRot=Mth.cos(ageInTicks/3.0f)/12.0f;
		this.left_arm.zRot=-Mth.cos(ageInTicks/3.0f)/12.0f;
	}


	@Override
	public ModelPart root() {
		return root;
	}
}