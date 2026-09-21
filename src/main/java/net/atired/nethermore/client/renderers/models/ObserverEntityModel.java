package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.entity.DisgustlingEntity;
import net.atired.nethermore.entity.DisgustlingHeadEntity;
import net.atired.nethermore.entity.MorbidPiglinEntity;
import net.atired.nethermore.entity.ObserverEntity;
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

public class ObserverEntityModel<T extends ObserverEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("observerentitymodel"), "main");
	private final ModelPart core;
	private final ModelPart front;
	private final ModelPart eye;

	private final ModelPart root;
	private final ModelPart pupil;

	public ObserverEntityModel(ModelPart root) {
		this.core = root.getChild("core");
		this.front = this.core.getChild("front");
		this.eye = this.front.getChild("eye");
		this.root = root;
		this.pupil = this.eye.getChild("pupil");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition core = partdefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -15.0F, -3.0F, 10.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition front = core.addOrReplaceChild("front", CubeListBuilder.create().texOffs(30, 22).addBox(-11.0F, -8.0F, -3.0F, 12.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -8.0F, -2.0F));

		PartDefinition cube_r1 = front.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(0.0F, -7.0F, 0.0F, 6.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.5672F, 0.0F));

		PartDefinition cube_r2 = front.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 45).addBox(-6.0F, -7.0F, 0.0F, 6.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, 0.0F, -3.0F, 0.0F, -0.5672F, 0.0F));

		PartDefinition eye = front.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(36, 0).addBox(-3.0F, -3.0F, -2.9F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, -4.1F));

		PartDefinition pupil = eye.addOrReplaceChild("pupil", CubeListBuilder.create().texOffs(43, 14).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(ObserverEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float bloink = 1.0f;
		if(entity.getBlink()<1.3f){
			bloink+=(float)Math.pow(entity.getBlink()/1.3f,0.33f);
		}else{
			bloink=2.0f-(entity.getBlink()-1.3f)/0.2f;
		}
		this.pupil.xScale=bloink;
		this.pupil.yScale=bloink;
		this.pupil.zScale=bloink;
		this.front.zRot=Mth.sin(ageInTicks/6.0f)/4.0f;
		this.front.xRot=Mth.cos(ageInTicks/6.0f)/4.0f;
		this.core.zRot=Mth.sin(ageInTicks/17.0f)/8.0f;
		this.core.xRot=Mth.cos(ageInTicks/17.0f)/8.0f;
		this.eye.zRot=-this.front.zRot;
		this.eye.xRot=-this.front.xRot+headPitch/180.0f*3.14f/1.0f;
		this.eye.yRot=netHeadYaw/180.0f*3.14f/3.0f;
	}



	@Override
	public ModelPart root() {
		return this.root;
	}
}