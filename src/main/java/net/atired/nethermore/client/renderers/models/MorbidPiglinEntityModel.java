package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.Nethermore;
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

public class MorbidPiglinEntityModel<T extends MorbidPiglinEntity> extends HierarchicalModel<T> implements ArmedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("morbidpiglinentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart real_head;
	private final ModelPart ear;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	private final ModelPart root;
	public MorbidPiglinEntityModel(ModelPart root) {
		this.root=root;
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.real_head = this.head.getChild("real_head");
		this.ear = this.real_head.getChild("ear");
		this.left_arm = this.body.getChild("left_arm");
		this.right_arm = this.body.getChild("right_arm");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 2.4F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 29).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, -2.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, -2.4F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -1.0F, -4.0F, 15.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(36, 13).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -2.0F, 2.0F, -0.0381F, 0.1704F, -0.2214F));

		PartDefinition real_head = head.addOrReplaceChild("real_head", CubeListBuilder.create().texOffs(0, 13).addBox(-5.0F, -7.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(56, 42).addBox(-2.0F, -3.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition cube_r4 = real_head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 29).addBox(-3.0F, -4.0F, -3.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4F, -5.0F, -1.9F, 0.0F, 0.7854F, 0.0F));

		PartDefinition ear = real_head.addOrReplaceChild("ear", CubeListBuilder.create(), PartPose.offsetAndRotation(4.7F, -4.5F, 0.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition cube_r5 = ear.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(52, 26).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -11.0F, -2.4F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(52, 26).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, -11.0F, -2.4F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(40, 43).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 60).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(2.1F, 13.0F, 2.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(40, 60).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(40, 43).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.1F, 13.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(MorbidPiglinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.right_leg.xRot= Mth.sin(limbSwing)*limbSwingAmount;
		this.left_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount;
		this.right_arm.xRot= Mth.sin(limbSwing)*limbSwingAmount/4.0f-1.37f;
		this.left_arm.xRot=-Mth.sin(limbSwing)*limbSwingAmount/4.0f-1.37f;
		if(entity.isAggressive()){
			this.right_arm.xRot*=0.24f;
			this.left_arm.xRot*=0.24f;
			this.right_arm.xRot-=1.14f;
			this.left_arm.xRot-=1.14f;
		}
		this.left_arm.yRot=-Mth.sin(ageInTicks/8.0f)/14.0f+0.1f-Mth.sin(this.attackTime*3.14f)/1.5f;
		this.right_arm.yRot=Mth.sin(ageInTicks/8.0f)/14.0f-0.1f+Mth.sin(this.attackTime*3.14f)/1.5f;
		this.head.yRot=netHeadYaw/180.0f*3.14f/2.0f;
		this.real_head.yRot=netHeadYaw/180.0f*3.14f/2.0f;
		this.real_head.xRot=headPitch/180.0f*3.14f;
		this.ear.zRot=Mth.sin(ageInTicks/6.0f)/12.0f-0.4f;
	}



	@Override
	public ModelPart root() {
		return this.root;
	}

	protected ModelPart getArm(HumanoidArm side) {
		return side == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
	}
	@Override
	public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
		poseStack.translate(0.02,0.8,.2);
		this.getArm(humanoidArm).translateAndRotate(poseStack);
	}
}