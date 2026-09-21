package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.entity.DisgustlingEntity;
import net.atired.nethermore.entity.DisgustlingHeadEntity;
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
import org.joml.Vector3f;

public class DisgustlingEntityModel<T extends DisgustlingEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("disgustlingentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart neck;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart head;
	private final ModelPart beak;
	private final ModelPart right_beak;
	private final ModelPart left_beak;
	private final ModelPart root;

	public DisgustlingEntityModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.neck = this.body.getChild("neck");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.head = root.getChild("head");
		this.beak = this.head.getChild("beak");
		this.right_beak = this.beak.getChild("right_beak");
		this.left_beak = this.beak.getChild("left_beak");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 23).addBox(-7.0F, -12.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -9.5F, 0.0F));

		PartDefinition cube_r1 = neck.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -3.5F, -8.0F, 16.0F, 7.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(56, 23).mirror().addBox(-3.0F, -1.0F, -7.0F, 5.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 24.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(56, 23).addBox(-2.0F, -1.0F, -7.0F, 5.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 24.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 51).addBox(-4.5F, -4.9F, -5.0F, 9.0F, 8.0F, 10.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.9F, 0.0F));

		PartDefinition beak = head.addOrReplaceChild("beak", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -5.9F));

		PartDefinition right_beak = beak.addOrReplaceChild("right_beak", CubeListBuilder.create().texOffs(56, 32).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.0F, 2.0F));

		PartDefinition left_beak = beak.addOrReplaceChild("left_beak", CubeListBuilder.create().texOffs(56, 32).mirror().addBox(-1.0F, -1.0F, -8.0F, 2.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -1.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(DisgustlingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entity.getHead()!=null){
			DisgustlingHeadEntity headEntity = entity.getHead();
			float yaw=entity.getPreciseBodyRotation(ageInTicks%1)/180.0f*3.14f;
			Vector3f pos = headEntity.getPosition(ageInTicks%1)
					.subtract(entity.getPosition(ageInTicks%1))
					.yRot(yaw)
					.scale(16.0f)
					.toVector3f();
			this.head.setPos(pos.x,-pos.y+20f,-pos.z);
			this.head.xRot=entity.getHead().getViewXRot(ageInTicks%1)/180.0f*3.14f;
			this.head.yRot=-yaw+entity.getHead().getViewYRot(ageInTicks%1)/180.0f*3.14f;
		}
		this.left_leg.xRot=Mth.sin(limbSwing*3.14f)*limbSwingAmount;
		this.left_leg.y=Mth.clamp(Mth.sin(limbSwing)*limbSwingAmount*3.0f,-3.0f,0.0f)+24;
		this.right_leg.xRot=-Mth.sin(limbSwing*3.14f)*limbSwingAmount;
		this.right_leg.y=Mth.clamp(-Mth.sin(limbSwing)*limbSwingAmount*3.0f,-3.0f,0.0f)+24;
		this.neck.xScale=1.0f+Mth.sin(ageInTicks/2.0f)/20.0f;
		this.neck.zScale=1.0f-Mth.sin(ageInTicks/2.0f)/20.0f;
		this.neck.yScale=1.0f+Mth.cos(ageInTicks/3.0f)/20.0f;
		this.body.xScale=1.0f+Mth.sin(ageInTicks/4.0f)/20.0f;
		this.body.zScale=1.0f-Mth.sin(ageInTicks/4.0f)/20.0f;
		this.body.yScale=1.0f+Mth.cos(ageInTicks/6.0f)/20.0f;
		this.body.zRot=Mth.sin(ageInTicks/12.0f)/5.0f;
		this.body.xRot=Mth.cos(ageInTicks/12.0f)/5.0f;
		float beakSin = Math.min(Mth.sin(Math.max(entity.getWindup()-0.5f,0.0f)*1.3f/2.0f*3.14f)*1.0f,1.0f);
		this.left_beak.yRot=-beakSin/2.0f;
		this.right_beak.yRot=beakSin/2.0f;
		this.head.zRot=beakSin;
	}


	@Override
	public ModelPart root() {
		return this.root;
	}
}