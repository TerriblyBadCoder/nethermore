package net.atired.nethermore.entity;

import net.atired.nethermore.accessors.RednessServerLevelAccessor;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PylonEntity extends Monster {
    private static final EntityDataAccessor<Float> RELEASE= SynchedEntityData.defineId(PylonEntity.class, EntityDataSerializers.FLOAT);

    public PylonEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.2));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0f,false));

        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class,true));

        super.registerGoals();
    }

    @Override
    public float getSpeed() {
        float mul =1.0f;
        if(getTarget()!=null){
            mul =(float) Math.clamp(getEyePosition().distanceTo(getTarget().getEyePosition())-5.0f+ Mth.sin(getId()+tickCount/20.0f),-5.0f,1.0f);
        }
        if(level() instanceof RednessServerLevelAccessor serverLevelAccessor){
            mul*=(0.3f+serverLevelAccessor.nethermore$getRed()*1.0f);
        }
        return super.getSpeed()*mul;
    }

    @Override
    public void tick() {
        if(getTarget()!=null){
            if(this.tickCount%2==0&&level() instanceof ServerLevel level && level() instanceof RednessServerLevelAccessor rednessServerLevelAccessor && rednessServerLevelAccessor.nethermore$getRed()>0.1){
                playSound(SoundEvents.SOUL_SAND_BREAK,0.45f,0.2f+(float)Math.random()/3.0f);
                //level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,getX(),getY(0.5),getZ(),4,0.3,0.6,0.3,0.01);
                level.sendParticles(NMParticleInit.SOUL_PARTICLE.get(),getX(),getY(0.5),getZ(),1,0.3,0.6,0.3,0.01);
                if(this.tickCount%2==0){
                    SoulProjEntity soul = new SoulProjEntity(level, this);
                    soul.toHurt=getTarget();
                    soul.shootFromRotation(this, this.getXRot(), this.getYRot()+(Math.random()>0.5?-90:90), 0.0F, 0.46F+(float)Math.random()/25.0f, 30.0F);
                    soul.addDeltaMovement(new Vec3(0,(Math.random()-0.5)/7.0f,0));
                    double ranDumb = Math.random()/5.0f+0.8f;
                    soul.setDeltaMovement(soul.getDeltaMovement().multiply(ranDumb,1,ranDumb));
                    level.addFreshEntity(soul);
                }
            }
        }
        super.tick();
    }
    public static AttributeSupplier.Builder createPylonAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.19).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MAX_HEALTH,40);
    }
    public float getRelease(){
        return entityData.get(RELEASE);
    }
    public void setRelease(float release){
        entityData.set(RELEASE,release);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(RELEASE,0.0f);
        super.defineSynchedData(builder);
    }
}
