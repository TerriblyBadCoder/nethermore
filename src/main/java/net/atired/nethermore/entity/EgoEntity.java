package net.atired.nethermore.entity;

import net.atired.nethermore.accessors.RednessServerLevelAccessor;
import net.atired.nethermore.init.NMEntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EgoEntity extends Monster {
    public int otherAge = 0;
    public float spinTime = 0.0f;
    public EgoMaskEntity[] masks = new EgoMaskEntity[4];
    public EgoEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        this.otherAge+=1;
        this.spinTime+=0.05f;
        double dist = 0.8;
        float reddened=0.0f;
        if(level() instanceof RednessServerLevelAccessor accessor){
             reddened = (float)Math.pow(Math.sin(accessor.nethermore$getRed()*3.14f),0.33f);
            this.spinTime+=reddened/8.0f;
            dist+=reddened*2.1;
        }
        if(this.masks[0]!=null){
            int count = 0;
            for(EgoMaskEntity maskEntity : this.masks){
//                if(getTarget()!=null) {
//                    dist= Mth.lerp(reddened,dist,Math.pow(getTarget().getPosition(1).multiply(1,0,1).distanceTo(getPosition(1).multiply(1,0,1))+3,0.66));
//                }
                Vec3 off = new Vec3(dist,0,0).yRot(this.spinTime+count/4.0f*3.14f*2.0f);
                maskEntity.setYRot(-(this.spinTime+count/4.0f*3.14f*2.0f)*180.0f/3.14f-90);
                maskEntity.setYHeadRot(-(this.spinTime+count/4.0f*3.14f*2.0f)*180.0f/3.14f-90);
                if(getTarget()!=null){
                    double dotted = Math.max(maskEntity.getViewVector(1).dot(getTarget().getPosition(1).subtract(getPosition(1)).multiply(1,0,1).normalize()),0.0f);
                    dotted*=dotted;
                    off=off.add(0,dotted*(getTarget().getY(0.8)-maskEntity.getY(0.5)),0);
                    off=off.add(getTarget().getPosition(1).multiply(1,0,1).subtract(getPosition(1).multiply(1,0,1)).scale(dotted*reddened*0.6));
                }
                Vec3 deltaed = maskEntity.getEyePosition().subtract(getEyePosition().subtract(0.0,0.44+reddened/1.1,0).add(off)).scale(-0.4);
                maskEntity.setDeltaMovement(deltaed);

                count+=1;
            }
        }
        else{
            if(level() instanceof ServerLevel serverLevel){
                for (int i = 0; i < 4; i++) {
                    EgoMaskEntity mask = new EgoMaskEntity(NMEntityInit.EGO_MASK.get(),serverLevel);
                    mask.setPos(getEyePosition());
                    serverLevel.addFreshEntity(mask);
                    mask.maskOwner=this;
                    mask.setId2(getId());
                    this.masks[i]=mask;
                }
            }
        }
        super.tick();
    }
    public static AttributeSupplier.Builder createEgoAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.MAX_HEALTH,25);
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

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public float getSpeed() {
        return super.getSpeed();
    }

    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

}
