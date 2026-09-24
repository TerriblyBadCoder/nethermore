
package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.client.NMRenderLayers;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.entity.MorbidPiglinEntity;
import net.atired.nethermore.entity.SlitherEntity;
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
public class SlitherEntityModel<T extends SlitherEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("slitherentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart root;

	public SlitherEntityModel(ModelPart root) {
		super(NMRenderLayers::entityTarCull);
		this.root=root;
		this.body = root.getChild("body");
		this.bone = this.body.getChild("bone");
		this.head = this.body.getChild("head");
		this.hat = this.head.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone = body.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 30).addBox(-3.0F, -14.5F, -3.0F, 6.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 30).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -2.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.2F));

		PartDefinition cube_r2 = hat.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(-6.0F, 2.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(SlitherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.body.yScale=1.0f-(entity.lerpedSlide*0.95f);
		this.head.xRot=headPitch/180.0f*3.14f/4.0f;
		this.head.yRot=netHeadYaw/180.0f*3.14f/4.0f;
		this.body.zRot=Mth.sin(ageInTicks/12.0f)/12.0f;
		float flip = (1.0f-this.body.yScale);
		this.body.xRot=Mth.cos(ageInTicks/12.0f)/12.0f-flip*0.4f;
		this.head.zRot=Mth.sin(ageInTicks/12.0f)/12.0f;
		this.head.xRot+=Mth.cos(ageInTicks/12.0f)/12.0f;
		this.hat.xScale=1.0f+flip*0.5f;
		this.hat.zScale=1.0f+flip*0.5f;
		this.head.yScale=1.0f/this.body.yScale;
	}


	@Override
	public ModelPart root() {
		return this.root;
	}
}