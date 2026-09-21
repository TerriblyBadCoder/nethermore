// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NethermoreClient;
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

public class NooEntityModel<T extends NooEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("nooentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart head;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart root;

	public NooEntityModel(ModelPart root) {
		this.root=root;
		this.body = root.getChild("body");
		this.right_arm = this.body.getChild("right_arm");
		this.left_arm = this.body.getChild("left_arm");
		this.head = this.body.getChild("head");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.6F, -7.0F, -1.1F, 4.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.4F, 18.0F, -0.4F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(14, 10).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.6F, -6.0F, 0.4F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(14, 10).mirror().addBox(0.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.4F, -6.0F, 0.4F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -5.0F, -2.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 18.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 18.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(NooEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.right_leg.xRot= Mth.sin(limbSwing)*limbSwingAmount;
		this.left_leg.xRot=-Mth.sin(limbSwing)*limbSwingAmount;
		this.body.zRot=Mth.sin(ageInTicks/7.0f)/15.0f;
		this.body.xRot=Mth.cos(ageInTicks/7.0f)/35.0f;
		float sinused=Mth.sin( NethermoreClient.PROXY.redNess2*3.14f);
		this.body.xRot+=limbSwingAmount/4.0f-sinused/5.0f;
		this.right_arm.xRot= Mth.sin(limbSwing)*limbSwingAmount/4.0f+.67f;
		this.left_arm.xRot=-Mth.sin(limbSwing)*limbSwingAmount/4.0f+.67f;
		this.right_arm.zRot= Mth.sin(ageInTicks/5.0f)/8.0f+.37f;
		this.left_arm.zRot=-Mth.sin(ageInTicks/5.0f)/8.0f-.37f;
		this.head.xRot=headPitch/180.0f*3.14f;
		this.head.yRot=netHeadYaw/180.0f*3.14f;
		this.head.xScale=1.0f+sinused/3.0f;
		this.head.yScale=1.0f+sinused/3.0f;
		this.head.zScale=1.0f+sinused/3.0f;
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}