// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.entity.MorbidPiglinEntity;
import net.atired.nethermore.entity.OnlookerEntity;
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

public class OnlookerEntityModel<T extends OnlookerEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("onlookerentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart lid_left;
	private final ModelPart lid_right;
	private final ModelPart root;

	public OnlookerEntityModel(ModelPart root) {
		this.root=root;
		this.body = root.getChild("body");
		this.lid_left = this.body.getChild("lid_left");
		this.lid_right = this.body.getChild("lid_right");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.1F, 0.1F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 21.1F, -3.1F));

		PartDefinition lid_left = body.addOrReplaceChild("lid_left", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = lid_left.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(0.0F, -2.5F, -1.9F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 0.0F));

		PartDefinition lid_right = body.addOrReplaceChild("lid_right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = lid_right.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, -2.5F, -1.9F, 0.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(OnlookerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.body.xRot=headPitch/180.0f*3.14f;
		this.body.xRot+=Mth.cos(ageInTicks/5.0f+entity.getId())/8.0f;
		this.body.yRot=Mth.cos(ageInTicks/2.0f+entity.getId())/8.0f;
		this.body.zRot=Mth.sin(ageInTicks/5.0f+entity.getId())/8.0f;
	}


	@Override
	public ModelPart root() {
		return this.root;
	}


}