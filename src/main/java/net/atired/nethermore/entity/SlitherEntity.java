package net.atired.nethermore.entity;

import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

public class SlitherEntity extends Monster {
    public float lerpedSlide = 0.0f;
    private int slideCount = 3;
    private int slideDelay=100;
    private Vec3 oldDir = new Vec3(0,0,0);
    private static final EntityDataAccessor<Float> SLIDE = SynchedEntityData.defineId(SlitherEntity.class, EntityDataSerializers.FLOAT);

    public SlitherEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.2));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0f,false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        super.registerGoals();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (SLIDE.equals(key)) {
            this.refreshDimensions();
        }
    }

    @Override
    public void tick() {
        if(level() instanceof ServerLevel serverLevel){
            if(this.getSlide()>0){
                this.navigation.stop();
                this.addDeltaMovement(this.oldDir.scale(0.5*Math.pow(this.getSlide(),2.0)).scale(this.slideCount==1?(-0.8f):1.0f));
                this.setSlide(Math.max(getSlide()-0.1f,0.0f));
                if(this.getSlide()<0.8){
                    this.lookAt(EntityAnchorArgument.Anchor.EYES,this.oldDir.scale(200).add(getEyePosition()));
                    this.lookControl.setLookAt(this.oldDir.scale(200).add(getEyePosition()));
                }else if(this.getTarget()!=null){

                    this.lookAt(getTarget(),120,120);
                    this.lookControl.setLookAt(getTarget());
                }
                this.oldDir=getViewVector(1).multiply(1,0,1).normalize().yRot((this.random.nextFloat()*0.5f+0.4f)*0.1f*(Math.random()>0.5?(-1.0f):(1.0f)));

                if(this.getSlide()<=0.01f){
                    this.setDeltaMovement(0,0.01,0);
                    this.setSlide(0);
                    this.slideCount-=1;
                    if(this.slideCount==0){
                        this.slideCount=5;
                        this.slideDelay=0;
                        this.setDeltaMovement(new Vec3(0,0.0,0));
                    }
                    if(this.getTarget()!=null){
                        this.lookAt(getTarget(),180.0f,180.0f);
                        this.navigation.moveTo(getTarget(),1.0);
                    }
                }
            }
            if(getTarget()!=null){
                this.slideDelay-=1;
                if(this.slideDelay<=0&&onGround()){
                    this.slideDelay=21;
                    this.setSlide(1.0f);
                    this.lookAt(getTarget(),180.0f,180.0f);
                    this.oldDir=getViewVector(1).multiply(1,0,1).normalize().yRot((this.random.nextFloat()*0.5f+0.4f)*0.1f*(Math.random()>0.5?(-1.0f):(1.0f)));
                    this.navigation.stop();
                }
            }
        }else if(level()!=null){
            if(onGround()&&getSlide()>0.1){
                if((this.tickCount+getId())%2==0)
                    level().addParticle(NMParticleInit.TAR_POP_PARTICLE.get(),getX((Math.random()-0.5)*3.0),getY(0.02),getZ((Math.random()-0.5)*3.0),0,0.01,0);
                if((this.tickCount+getId())%2==0&&getSlide()>0.8)
                    level().addParticle(NMParticleInit.TAR_SLOP_PARTICLE.get(),getX(Math.random()),getY(0.02),getZ(Math.random()),(Math.random()-0.5)*0.1,0.01,(Math.random()-0.5)*0.1);

            }
        }
        super.tick();
    }

    @Override
    public float getSpeed() {
        return super.getSpeed()*0.1f;
    }

    public EntityDimensions getDefaultDimensions(Pose pose) {
        return super.getDefaultDimensions(pose).scale(1.0f, (float)Math.pow(1.0f-(float)this.getSlide()*0.92f,2.0f));
    }

    public static AttributeSupplier.Builder createSlitherAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.19).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MAX_HEALTH,25);
    }
    public float getSlide(){
        return this.entityData.get(SLIDE);
    }
    public void setSlide(float slide){
        this.entityData.set(SLIDE,slide);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SLIDE,0.0f);
        super.defineSynchedData(builder);
    }
}
