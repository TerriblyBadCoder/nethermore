package net.atired.nethermore.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class UnpheasantEntity extends Monster {
    private static final EntityDataAccessor<Boolean> HEADFUL= SynchedEntityData.defineId(UnpheasantEntity.class, EntityDataSerializers.BOOLEAN);

    public Vec3[] legPositions={new Vec3(0,0,0),new Vec3(0,0,0)};
    public Vec3[] oldLegPositions={new Vec3(0,0,0),new Vec3(0,0,0)};
    public float[] legFloats={0.0f,0.0f};
    public UnpheasantEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new UnpheasantMoveControl(this);
    }

    @Override
    public void tick() {
        if(tickCount%3==1){
            for (int j = 0; j < 1; j++) {
                int i = tickCount%6>2?1:0;
                Vec3 dir = new Vec3(0.05,-0.8,0).normalize().yRot(3.14f*(i+0.5f)-getYRot()/180.0f*3.14f);
                Vec3 posFrom = getPosition(1).add(0,1.0,0);

                BlockHitResult result = level().clip(new ClipContext(
                        posFrom,
                        posFrom.add(dir.scale(12)),
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        this));
                dir = new Vec3(0,0,1).yRot(-getYRot()/180.0f*3.14f).scale(Math.min(getDeltaMovement().multiply(1,0,1).length()*8.0,1.4));
                if(result.getLocation().add(dir).subtract(oldLegPositions[i]).length()>1){
                    legPositions[i]=result.getLocation().add(dir);
                    legFloats[i]=1.0f;
                }

            }
        }
        for (int i = 0; i < 2; i++) {
            if(legFloats[i]>0){
                legFloats[i]=Math.max(0.0f,legFloats[i]-0.15f);
            }
            oldLegPositions[i]=oldLegPositions[i].lerp(legPositions[i],0.5f);
        }
        if(level()!=null) {
            BlockHitResult result = null;
            float wantedOff = 0.0f;
            if(getMoveControl().hasWanted()&&getMoveControl().getWantedY()>getY()-0.1){
                wantedOff=1.0f;
            }
            for (int i = 0; i < 4; i++) {
                BlockHitResult result1 = level().clip(new ClipContext(
                        getPosition(1),
                        getPosition(1).add(new Vec3(0.2, -0.4-wantedOff + Mth.sin(this.tickCount / 10.0f) / 8.0f, 0).yRot(i * 3.14f / 2.0f)),
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        this));
                if (result == null || result1.getLocation().distanceTo(getPosition(1)) < result.getLocation().distanceTo(getPosition(1))) {
                    result = result1;
                }
            }
            if (result.getType() != HitResult.Type.MISS || isInWater()) {

                float to = (float) (1.0f - Math.clamp((result.getLocation().distanceTo(getPosition(1))-wantedOff*0.9f - 0.3f)*4.0, 0.0f, 1.0f));
                if (getTarget() != null && getTarget().getY() > getY()) {
                    to += (float) (1.8f + Math.abs(getTarget().getY() - getY()) / 20.0f);
                    if (isInWater()) {
                        to += 0.5f;
                    }
                } else if (getTarget() != null && getTarget().getY() < getY() && isInWater()) {
                    to -= 3.0f;
                } else if (isInWater()) {
                    to -= 1.3f;
                }
                if (getTarget() != null && getPosition(1).multiply(1, 0, 1).distanceTo(getTarget().getPosition(1).multiply(1, 0, 1)) < 1) {
                    to = -1.4f;
                }
                resetFallDistance();
                setDeltaMovement(new Vec3(getDeltaMovement().x, 0.06 * to + getDeltaMovement().y * 0.5, getDeltaMovement().z));
            }
        }
        super.tick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(2,new MeleeAttackGoal(this,1.0f,true));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class,true));

    }

    @Override
    public boolean save(CompoundTag compound) {
        compound.putBoolean("has_real_head",true);
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.entityData.set(HEADFUL,compound.getBoolean("has_real_head"));
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return super.isWithinMeleeAttackRange(entity);
    }

    @Override
    protected AABB getAttackBoundingBox() {
        if(!this.hasHead()){
            return super.getAttackBoundingBox().inflate(-0.1f);
        }
        return super.getAttackBoundingBox();
    }

    public static AttributeSupplier.Builder createUnpheasantAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.09).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected float getKnockback(Entity attacker, DamageSource damageSource) {
        if(!this.hasHead()){
            return super.getKnockback(attacker, damageSource)*1.5f;
        }
        return super.getKnockback(attacker, damageSource);
    }

    public boolean hasHead(){
        return this.entityData.get(HEADFUL);
    }
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if(Math.random()>0.8f){
            this.entityData.set(HEADFUL,true);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HEADFUL,false);
        super.defineSynchedData(builder);
    }

    static class RandomFloatAroundGoal extends Goal {
        private final UnpheasantEntity crab;

        public RandomFloatAroundGoal(UnpheasantEntity ghast) {
            this.crab = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.crab.getMoveControl();
            if (!movecontrol.hasWanted()||(this.crab.getTarget()!=null)) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.crab.getX();
                double d1 = movecontrol.getWantedY() - this.crab.getY();
                double d2 = movecontrol.getWantedZ() - this.crab.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.crab.getRandom();
            if(this.crab.getTarget()!=null){
                this.crab.moveControl.setWantedPosition(this.crab.getTarget().getX(),this.crab.getTarget().getY()+1.6,this.crab.getTarget().getZ(),1.5f);
            }else{

                double d0 = this.crab.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d1 = this.crab.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.1F) * 12.0F);
                double d2 = this.crab.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.crab.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            }
        }
    }
    static class UnpheasantMoveControl extends MoveControl {
        private final UnpheasantEntity crab;
        private int collisionCheckCooldown;
        private int jumpCD=0;

        public UnpheasantMoveControl(UnpheasantEntity mosqo) {
            super(mosqo);
            this.crab = mosqo;
        }

        public void tick() {
            MoveControl moveControl = this.crab.getMoveControl();

            if (this.operation==Operation.MOVE_TO) {
                this.jumpCD-=1;
                if(this.crab.getTarget()!=null||(
                        this.crab.getDeltaMovement().length()>0.1&&this.crab.getPosition(1).distanceTo(new Vec3(moveControl.getWantedX(),this.crab.getY(),moveControl.getWantedZ()))>1.2)){
                    this.crab.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                    this.crab.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.crab.getRandom().nextInt(5) + 2;
                    this.wantedY=this.crab.getY();

                    Vec3 Vec3 = new Vec3(this.wantedX - this.crab.getX(), 0, this.wantedZ - this.crab.getZ());
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();
                    if (this.willCollide(Vec3, Mth.ceil(d))) {
                        if(this.crab.getTarget()==null&& Math.random()>0.92&this.jumpCD>0){
                            this.crab.navigation.stop();
                            this.operation=Operation.WAIT;
                        }
                        if(this.crab.getTarget()!=null){
                            this.wantedX=this.crab.getTarget().getX();
                            this.wantedY=this.crab.getTarget().getY()+2;
                            this.wantedZ=this.crab.getTarget().getZ();
                        }
                        if(this.jumpCD<=0&&this.crab.tickCount%4==0){
                            BlockHitResult result = this.crab.level().clip(new ClipContext(
                                    this.crab.getPosition(1).add(0,0.3,0),
                                    this.crab.getPosition(1).add(0,0.3,0).add(this.crab.getViewVector(1).scale(0.7)),
                                    ClipContext.Block.COLLIDER,
                                    ClipContext.Fluid.NONE,
                                    this.crab));
                            if(result.getType() != HitResult.Type.MISS){
                                this.jumpCD=60;
                                this.crab.addDeltaMovement(new Vec3(0,0.5,0));
                            }
                        }
                        this.crab.setDeltaMovement(this.crab.getDeltaMovement().multiply(0.3,0.9,0.3)
                                .add(Vec3.multiply(1.0,0.0,1.0).scale(this.crab.hasHead()?0.26f:Mth.sin(this.crab.getId()*31+this.crab.tickCount/30.0f)/8.0f+0.13).scale(0.8)));
                    } else {
                        this.crab.navigation.stop();
                        this.operation=Operation.WAIT;

                    }
                }

            }
        }

        private boolean willCollide(Vec3 direction, int steps) {
            AABB box = this.crab.getBoundingBox();
            if(this.crab.getTarget()!=null&&this.crab.isWithinMeleeAttackRange(this.crab.getTarget())){
                if(this.crab.doHurtTarget(this.crab.getTarget())){
                    this.crab.swing(InteractionHand.MAIN_HAND);
                    if(this.crab.hasHead()){
                        this.crab.addDeltaMovement(this.crab.getDeltaMovement().scale(1.6));
                    }
                }
            }
            if(this.crab.getTarget()==null&&new Vec3(this.getWantedX(),this.getWantedY(),this.getWantedZ()).subtract(this.crab.getPosition(1)).length()<1.5){
                return false;
            }
            return true;
        }
    }
}
