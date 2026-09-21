package net.atired.nethermore.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Objects;

public class ObserverEntity extends Monster {
    public int fireDelay=100;
    public Vec3[] legPositions = new Vec3[3];
    public Vec3[] oldLegPositions = new Vec3[3];
    public float[] legSin = new float[3];
    public float[] legOffset = new float[3];
    public Vec3 lastPos = new Vec3(0.0,0,0);
    public float floatAbove = 0.0f;
    private static final EntityDataAccessor<Float> BLINK= SynchedEntityData.defineId(ObserverEntity.class, EntityDataSerializers.FLOAT);

    public ObserverEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new ObserverMoveControl(this);

    }

    @Override
    public void tick() {
            if (legPositions[0] == null) {
                for (int i = 0; i < 3; i++) {
                    oldLegPositions[i] = new Vec3(getX(), getY(), getZ());
                    legPositions[i] = new Vec3(getX(), getY(), getZ());
                }
            }
            if (this.level() != null && this.tickCount % 3 == 0) {
                this.lastPos = getPosition(1);
                this.noActionTime = 0;
                int i = (this.tickCount / 3) % 3;
                if (legPositions[i] == null)
                    oldLegPositions[i] = new Vec3(getX(), getY(), getZ());

                legPositions[i] = new Vec3(getX(), getY(), getZ());
                boolean bigMisser = false;
                for (int j = 7; j > 0; j--) {

                    Vec3 dir = new Vec3(0.13, -0.1 - j / 9.0f, 0).normalize().yRot(3.14f * 2.0f * i / 3.0f + (float) (this.lastPos.length() / 32.0f) % (3.14f * 4.0f));
                    Vec3 posFrom = getPosition(1).add(0, 1.0, 0).add(getDeltaMovement().normalize().multiply(1,0.04,1).scale(-1.5));

                    BlockHitResult result = level().clip(new ClipContext(
                            posFrom,
                            posFrom.add(dir.scale(12)),
                            ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.NONE,
                            this));
                    if (legPositions[i].equals(new Vec3(getX(), getY(), getZ())) ||
                            ((legPositions[i].subtract(getPosition(1)).length() > result.getLocation().subtract(getPosition(1)).length() || Objects.equals(legPositions[i], new Vec3(getX(), getY(), getZ()))) && result.getType() != HitResult.Type.MISS)) {

                        legPositions[i] = result.getLocation();
                        if (legPositions[i].distanceTo(oldLegPositions[i]) > 0.8 || this.tickCount % 240 < 25) {
                            legSin[i] = 1.0f;
                            legOffset[i] = (float) legPositions[i].distanceTo(oldLegPositions[i]) / 3.0f + 0.3f;
                        }
                    }
                }

            }
            for (int i = 0; i < 3; i++) {
                if (legSin[i] > 0) {
                    legSin[i] = Math.max(0.0f, legSin[i] - 0.15f);
                }
            }
            if (legPositions[0] != null) {
                boolean sound = false;
                for (int i = 0; i < 3; i++) {
                    if (oldLegPositions[i].distanceTo(legPositions[i]) > 0.02) {
                        if (oldLegPositions[i].distanceTo(legPositions[i]) > 0.1)
                            sound = true;
                        oldLegPositions[i] = oldLegPositions[i].lerp(legPositions[i], 0.2);
                    }
                }
//                if (tickCount % 8 == 0 && sound) {
//                    playSound(SoundEvents.ZOMBIE_STEP, 0.8f, 0.7f);
//                }
            }

            if (level() != null) {
                BlockHitResult result = null;
                for (int i = 0; i < 4; i++) {
                    BlockHitResult result1 = level().clip(new ClipContext(
                            getPosition(1),
                            getPosition(1).add(new Vec3(0.3, -3.1 + Mth.sin(this.tickCount / 10.0f) / 1.4f, 0).yRot(i * 3.14f / 2.0f)),
                            ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.NONE,
                            this));
                    if (result == null || result1.getLocation().distanceTo(getPosition(1)) < result.getLocation().distanceTo(getPosition(1))) {
                        result = result1;
                    }
                }
                if (result.getType() != HitResult.Type.MISS || isInWater()) {

                    float to = (float) (1.0f - Math.clamp(result.getLocation().distanceTo(getPosition(1)) - 2.8f, 0.0f, 1.0f));
                    if (getTarget() != null && getTarget().getY() > getY()) {
                        to += (float) (1.8f + Math.abs(getTarget().getY() - getY()) / 20.0f);
                        if (isInWater()) {
                            to += 0.5f;
                        }
                    }


                    setDeltaMovement(new Vec3(getDeltaMovement().x, 0.06 * to + 0.08 + getDeltaMovement().y * 0.9, getDeltaMovement().z));
                }
            }
        if(getTarget()!=null){
            this.fireDelay-=1;
            if(getBlink()>0.001f){
                setBlink(getBlink()+0.05f);
                lookAt(getTarget(),360.0f,360.0f);
                this.lookControl.setLookAt(getTarget());
                if(getBlink()>=1.5f){
                    setBlink(0.0f);
                }
            }
            if(this.fireDelay<=0){
                setBlink(0.01f);
                this.fireDelay=60;
            }
        }
        super.tick();
    }
    public static AttributeSupplier.Builder createObserverAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.19).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MAX_HEALTH,25);
    }
    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new RandomFloatAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        super.registerGoals();
    }
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLINK,0.0f);
        super.defineSynchedData(builder);
    }
    public float getBlink(){
        return this.entityData.get(BLINK);
    }
    public void setBlink(float blunk){
        this.entityData.set(BLINK,blunk);
    }
    public double getSpeedy(){
        return 0.2*Math.clamp(getTarget()==null?0.5:(getTarget().getPosition(1).add(0,1,0).distanceTo(getEyePosition())-5.0),-1.0,1.0);
    }
    static class RandomFloatAroundGoal extends Goal {
        private final ObserverEntity observer;

        public RandomFloatAroundGoal(ObserverEntity ghast) {
            this.observer = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.observer.getMoveControl();
            if (!movecontrol.hasWanted()||(this.observer.getTarget()!=null)) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.observer.getX();
                double d1 = movecontrol.getWantedY() - this.observer.getY();
                double d2 = movecontrol.getWantedZ() - this.observer.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.observer.getRandom();
            if(this.observer.getTarget()!=null){
                this.observer.moveControl.setWantedPosition(this.observer.getTarget().getX(),this.observer.getTarget().getY()+1.6,this.observer.getTarget().getZ(),1.5f);
            }else{

                double d0 = this.observer.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d1 = this.observer.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.1F) * 12.0F);
                double d2 = this.observer.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.observer.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            }
        }
    }
    static class ObserverMoveControl extends MoveControl {
        private final ObserverEntity observer;
        private int collisionCheckCooldown;

        public ObserverMoveControl(ObserverEntity mosqo) {
            super(mosqo);
            this.observer = mosqo;
        }

        public void tick() {
            MoveControl moveControl = this.observer.getMoveControl();

            if (this.operation==Operation.MOVE_TO) {
                if(this.observer.getTarget()!=null||(
                        this.observer.getDeltaMovement().length()>0.5&&this.observer.getPosition(1).distanceTo(new Vec3(moveControl.getWantedX(),this.observer.getY(),moveControl.getWantedZ()))>1.2)){
                    this.observer.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                    this.observer.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.observer.getRandom().nextInt(5) + 2;
                    this.wantedY=this.observer.getY();

                    Vec3 Vec3 = new Vec3(this.wantedX - this.observer.getX(), 0, this.wantedZ - this.observer.getZ());
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();
                    if (this.willCollide(Vec3, Mth.ceil(d))) {
                        this.observer.setDeltaMovement(this.observer.getDeltaMovement().multiply(0.3,0.9,0.3)
                                .add(Vec3.multiply(1.0,0.0,1.0).scale(this.observer.getSpeedy())));
                    } else {
                        this.observer.setTarget(null);
                    }
                }

            }
        }

        private boolean willCollide(Vec3 direction, int steps) {
            AABB box = this.observer.getBoundingBox();
            if(this.observer.getTarget()!=null&&this.observer.isWithinMeleeAttackRange(this.observer.getTarget())){
                if(this.observer.doHurtTarget(this.observer.getTarget())){
                    this.observer.swing(InteractionHand.MAIN_HAND);
                }
            }
            if(this.observer.getTarget()==null&&new Vec3(this.getWantedX(),this.getWantedY(),this.getWantedZ()).subtract(this.observer.getPosition(1)).length()<1.5){
                return false;
            }
            return true;
        }
    }
}
