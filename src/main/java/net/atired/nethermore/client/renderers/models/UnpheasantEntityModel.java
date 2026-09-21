package net.atired.nethermore.client.renderers.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.nethermore.Nethermore;
import net.atired.nethermore.entity.MorbidPiglinEntity;
import net.atired.nethermore.entity.UnpheasantEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class UnpheasantEntityModel<T extends UnpheasantEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Nethermore.getId("unpheasantentitymodel"), "main");
	private final ModelPart body;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart head;
	private final ModelPart beak;
	private final ModelPart root;

	public UnpheasantEntityModel(ModelPart root) {
		this.root=root;
		this.body = root.getChild("body");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.head = root.getChild("head");
		this.beak = this.head.getChild("beak");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -7.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(38, 28).addBox(-3.0F, -1.0F, -7.0F, 5.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 24.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(38, 28).mirror().addBox(-2.0F, -1.0F, -7.0F, 5.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 24.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 28).addBox(-4.5F, -4.9F, -5.0F, 9.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 46).addBox(-4.5F, -4.9F, -5.0F, 9.0F, 8.0F, 10.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.9F, 0.0F));

		PartDefinition beak = head.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(38, 37).addBox(-1.5F, -2.0F, -6.0F, 3.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.9F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	@Override
	public void setupAnim(UnpheasantEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		Vec3 pos = entity.getPosition(ageInTicks%1).add(0,-0.0,0);
		pos=pos.add(0,0.1,0);
		int count=0;

		if(entity.hasHead()){
			this.head.xScale=1;
			this.head.yScale=1;
			this.head.zScale=1;
			this.head.y=8+Mth.sin(this.attackTime*3.14f)*8.5f;
			this.head.z=-6-Mth.sin(this.attackTime*3.14f)*7.5f;
			this.head.zRot=Mth.sin(ageInTicks/4.0f)/4.0f;
			this.head.xRot=Mth.cos(ageInTicks/4.0f)/13.0f+headPitch/180.0f/2.0f*3.14f+Mth.sin(this.attackTime*3.14f)*0.5f;
			this.head.yRot=netHeadYaw/180.0f*3.14f;
			this.body.xRot=-Mth.cos(ageInTicks/8.0f)/9.0f+0.4f;
		}else{
			this.head.xScale=0;
			this.head.yScale=0;
			this.head.zScale=0;
			this.body.xRot=-Mth.cos(ageInTicks/8.0f)/9.0f-0.4f+Mth.sin(this.attackTime*3.14f)*1.5f;

		}
		this.body.zRot=Mth.sin(ageInTicks/8.0f)/9.0f;
		this.body.yRot=-Mth.cos(ageInTicks/16.0f)/9.0f;
		for(Vec3 lPos : entity.oldLegPositions) {
			Vec3 off =new Vec3(0.3,-1.4,0).yRot(-entity.getYRot()/180.0f*3.14f+count*3.14f);
			Vec3 legPos = lPos.add(0, Mth.sin(entity.legFloats[count]*3.14f)/3.0f,0).subtract(pos).add(off);
			Vector3f vector3f = legPos.scale(1f*16.0f).yRot(entity.getPreciseBodyRotation(ageInTicks%1)/180.0f*3.14f).toVector3f();
			(count==0?right_leg:left_leg).setPos(vector3f.x,-vector3f.y+count/500.0f,-vector3f.z);
			count+=1;
		}
	}


	@Override
	public ModelPart root() {
		return this.root;
	}
}