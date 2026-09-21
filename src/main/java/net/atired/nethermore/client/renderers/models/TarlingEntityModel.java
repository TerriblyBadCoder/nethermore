// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.entity.MorbidPiglinEntity;
import net.atired.nethermore.entity.TarlingEntity;
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

public class TarlingEntityModel<T extends TarlingEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("tarlingentitymodel"), "main");
	private final ModelPart bone;
	private final ModelPart root;

	public TarlingEntityModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.root=root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(TarlingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.bone.xRot=entity.getSpin()*1.25f;
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}