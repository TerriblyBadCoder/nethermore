package net.atired.nethermore.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.EnumSet;
import java.util.UUID;

public class OnlookerEntity extends Monster {
    public LivingEntity eyeOwner = null;
    private static final EntityDataAccessor<Vector3f> SOURCE= SynchedEntityData.defineId(OnlookerEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Integer> ENTITY_ID= SynchedEntityData.defineId(OnlookerEntity.class, EntityDataSerializers.INT);

    public OnlookerEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new OnlookerMoveControl(this);

    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        if(getSource().length()<0.01f){
            return;
        }
        float mul = Math.clamp(6.0f-getSource().distance(pos.add(getPosition(1)).toVector3f()),-0.0f,1.0f);
        pos=getPosition(1).subtract(new Vec3(getSource())).scale(-1).normalize().lerp(pos,mul);
        super.move(type, pos);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    private UUID safeKeepingUUID=null;
    @Override
    public void load(CompoundTag compound) {
        if(compound.hasUUID("skeletontieduuid")){
            safeKeepingUUID=(compound.getUUID("skeletontieduuid"));
        }
        super.load(compound);
    }

    @Override
    public boolean save(CompoundTag compound) {
        if(this.eyeOwner!=null)
        compound.putUUID("skeletontieduuid",this.eyeOwner.getUUID());
        return super.save(compound);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{AbstractSkeleton.class, OnlookerEntity.class})).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class,true));

        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        super.registerGoals();
    }

    @Override
    public void tick() {
        if(this.eyeOwner==null&&getEyeId()!=-1&&this.level().isClientSide()&&level().getEntity(getEyeId()) instanceof LivingEntity living){
            this.eyeOwner=living;
        }
        else if(getEyeId()==-1&&this.level().isClientSide()){
            this.eyeOwner=null;
        }
        if(level() instanceof ServerLevel serverLevel && safeKeepingUUID!=null&&this.tickCount<7){
            this.eyeOwner= (LivingEntity) serverLevel.getEntities().get(safeKeepingUUID);
        }
        if(this.eyeOwner!=null&&this.eyeOwner.isAlive()&&!this.level().isClientSide()){
            setEyeId(this.eyeOwner.getId());
            setSource(eyeOwner.getEyePosition().add(eyeOwner.getViewVector(1).multiply(1,0,1).normalize().scale(0.07)).add(0,-0.1,0).toVector3f());

        }
        else if(this.tickCount>1&&getSource().length()<=0.01f&&level() instanceof ServerLevel&&this.eyeOwner==null){
            setSource(getPosition(1).toVector3f());
            setEyeId(-1);
        }
        super.tick();
    }
    public static AttributeSupplier.Builder createOnlookerAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.39).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MAX_HEALTH,8);
    }
    public void setEyeId(int eye){
         this.entityData.set(ENTITY_ID,eye);
    }

    public int getEyeId(){
        return this.entityData.get(ENTITY_ID);
    }

    public void setSource(Vector3f vec){
        this.entityData.set(SOURCE,vec);
    }
    public Vector3f getSource(){
        return this.entityData.get(SOURCE);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SOURCE,new Vector3f(0,0,0));
        builder.define(ENTITY_ID,-1);
        super.defineSynchedData(builder);
    }
    static class RandomFloatAroundGoal extends Goal {
        private final OnlookerEntity onlooker;

        public RandomFloatAroundGoal(OnlookerEntity ghast) {
            this.onlooker = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.onlooker.getMoveControl();
            if (!movecontrol.hasWanted()||(this.onlooker.getTarget()!=null)) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.onlooker.getX();
                double d1 = movecontrol.getWantedY() - this.onlooker.getY();
                double d2 = movecontrol.getWantedZ() - this.onlooker.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.onlooker.getRandom();
            if(this.onlooker.getTarget()!=null){
                this.onlooker.moveControl.setWantedPosition(this.onlooker.getTarget().getX(),this.onlooker.getTarget().getY()+1.6,this.onlooker.getTarget().getZ(),1.5f);
            }else{

                double d0 = this.onlooker.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d1 = this.onlooker.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.1F) * 12.0F);
                double d2 = this.onlooker.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.onlooker.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            }
        }
    }
    static class OnlookerMoveControl extends MoveControl {
        private final OnlookerEntity onlooker;
        private int collisionCheckCooldown;

        public OnlookerMoveControl(OnlookerEntity onlooker) {
            super(onlooker);
            this.onlooker = onlooker;
        }

        public void tick() {

            MoveControl moveControl = this.onlooker.getMoveControl();

            if (this.operation == Operation.MOVE_TO) {
                if(this.onlooker.getTarget()!=null||(
                        this.onlooker.getDeltaMovement().length()>0.1&&this.onlooker.getPosition(1).distanceTo(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()))>1.2)){
                    this.onlooker.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                    this.onlooker.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.onlooker.getRandom().nextInt(5) + 2;
                    Vec3 Vec3 = new Vec3(this.wantedX - this.onlooker.getX(), this.wantedY - this.onlooker.getY(), this.wantedZ - this.onlooker.getZ());
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();

                    if (this.willCollide(Vec3, Mth.ceil(d))&&!(this.mob.getTarget()==null&&this.mob.tickCount%10==0&&this.mob.getPosition(1).distanceTo(new Vec3(this.onlooker.getSource()))>5)) {

                        this.onlooker.setDeltaMovement(this.onlooker.getDeltaMovement().scale(0.6)
                                .add(Vec3.scale(this.onlooker.getSpeed()+0.1)
                                        .xRot((float)(Mth.cos(this.onlooker.tickCount*0.1f+this.onlooker.getId()))*0.8f)
                                        .yRot((float)(Mth.sin(this.onlooker.tickCount*0.1f-this.onlooker.getId()))*0.8f)));
                    } else {
                        this.operation = Operation.WAIT;
                    }
                }

            }
        }

        private boolean willCollide(Vec3 direction, int steps) {
            AABB box = this.onlooker.getBoundingBox();
            for(int i = 1; i < steps; ++i) {
                box = box.move(direction);
                if (!this.onlooker.level().noCollision(this.onlooker, box)) {
                    return false;
                }
            }

            return true;
        }
    }
}
