package net.atired.nethermore.entity;

import net.atired.nethermore.init.NMEntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DisgustlingEntity extends Monster {
    private static final EntityDataAccessor<Integer> HEAD_ID= SynchedEntityData.defineId(DisgustlingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> WINDUP= SynchedEntityData.defineId(DisgustlingEntity.class, EntityDataSerializers.FLOAT);
    public Vec3 headPos=new Vec3(0,0,0);
    public DisgustlingHeadEntity head = null;
    public float diffMove = 0.0f;
    public int lungeDelay = 200;
    public Vec3 lockedPos = new Vec3(0,0,0);
    public Vec3 dirPos = new Vec3(0,0,0);
    public DisgustlingEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if(level() instanceof ServerLevel serverLevel){
            if(this.head==null){
                DisgustlingHeadEntity headEntity = new DisgustlingHeadEntity(NMEntityInit.DISGUSTLING_HEAD.get(),serverLevel);
                headEntity.setPos(getPosition(1));
                serverLevel.addFreshEntity(headEntity);
                this.head=headEntity;
                this.setHeadId(this.head.getId());
                this.head.setParent(getId());
            }else if(this.head.getRemovalReason()==null){
                if(getWindup()>0.0f){
                    setWindup(getWindup()+0.16f);
                    if(getWindup()>2.0f){
                        setWindup(0.0f);
                        this.lockedPos=new Vec3(0,0,0);
                    }
                }

                Vec3 posFrom = getPosition(1).add(0,1.1,0);
                Vec3 dir = new Vec3(0.6,0.75+ Mth.cos(tickCount/5.0f)/16.0f,0).yRot(getId()*1.6f+tickCount/10.0f).scale(1.5);
                boolean setLockedPos = false;
                if(getTarget()!=null){
                    if(onGround()&&this.lungeDelay<65&&this.head.getPosition(1).distanceTo(getTarget().getPosition(1).add(0,getTarget().getBbHeight()/2.0f,0))<2&&getWindup()<=0.05f){
                        this.jumpFromGround();
                        this.lungeDelay=80;
                        this.dirPos=this.head.getViewVector(1).scale(0.5);
                        setWindup(1.1f);
                        setLockedPos=true;
                        this.addDeltaMovement(getViewVector(1).multiply(1,0,1).normalize().scale(0.4));
                    }
                    this.lungeDelay-=1;
                    float delta=(1.0f+Mth.sin(tickCount/6.0f))/2.0f;
                    dir=dir.add(posFrom).lerp(getTarget().getPosition(1).add(0,1.2f,0).add(dir),delta*0.8f).subtract(posFrom);
                    dir = dir.normalize().scale(Math.min(dir.length(),6.0f));
                    if(delta>0.95f&&this.lungeDelay<0&&getWindup()<=0.05f){
                        this.lungeDelay=80;
                        this.dirPos=this.head.getViewVector(1);
                        playSound(SoundEvents.CHICKEN_AMBIENT,1.0f,0.2f);
                        setWindup(0.1f);
                        setLockedPos=true;
                    }
                }
                BlockHitResult result = level().clip(new ClipContext(
                        posFrom,
                        posFrom.add(dir),
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        this));

                Vec3 resultVec = result.getLocation().add(result.getLocation().vectorTo(dir).normalize().scale(0.1));
                if(setLockedPos){
                    this.lockedPos = result.getLocation().add(result.getLocation().vectorTo(dir).normalize().scale(0.1));
                }
                if(this.lockedPos.length()>0.01f){
                    resultVec=this.lockedPos.add(this.dirPos.scale(Math.max(this.getWindup()-0.7f,0.0f)*1.5f*4.0f));
                }
                this.headPos =resultVec;
                this.head.setPos(this.head.getPosition(1).lerp(this.headPos,0.25f));
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    public void setWindup(float id){
        entityData.set(WINDUP,id);
    }
    public float getWindup(){
        return entityData.get(WINDUP);
    }
    public void setHeadId(int id){
        entityData.set(HEAD_ID,id);
    }

    public DisgustlingHeadEntity getHead(){
        if(level().getEntity(entityData.get(HEAD_ID)) instanceof DisgustlingHeadEntity){
            return (DisgustlingHeadEntity)level().getEntity(entityData.get(HEAD_ID));
        }
        return null;
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HEAD_ID,-1);
        builder.define(WINDUP,0.0f);
        super.defineSynchedData(builder);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.2));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));

        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        super.registerGoals();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }


    public static AttributeSupplier.Builder createUnpheasantAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.09).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

}
