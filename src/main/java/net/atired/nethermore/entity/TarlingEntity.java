package net.atired.nethermore.entity;

import com.google.common.annotations.VisibleForTesting;
import net.atired.nethermore.init.NMItemInit;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TarlingEntity extends Slime {
    private static final EntityDataAccessor<Float> SPIN= SynchedEntityData.defineId(TarlingEntity.class, EntityDataSerializers.FLOAT);
    public int jumpAmount = 3;
    public int jumpDelay = 0;
    private boolean wasOnGroundBefore = false;
    private boolean wasJumpBefore = false;
    public TarlingEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setJumping(boolean jumping) {
        if(this.jumpAmount<=0&&onGround()&&this.jumpDelay>0){
            return;
        }
        super.setJumping(jumping);
    }

    @Override
    public void tick() {
        boolean doJump =false;
        float olSquish = this.oSquish;
        if(onGround()&&this.getDeltaMovement().multiply(1,0,1).length()>0.05){
            this.level().addParticle(NMParticleInit.TAR_POP_PARTICLE.get(), this.getX()+(Math.random()-0.5)*getSize(), this.getY()+0.01f+this.getDeltaMovement().y+(Math.random())/2*getSize(), this.getZ()+(Math.random()-0.5)*getSize(), (Math.random()-0.5)/2.0f, 0.0, (Math.random()-0.5)/2.0f);

        }
        if(this.jumpDelay>0){
            if(this.jumpAmount<=0&&onGround()){
                Vec3 dir =this.getDeltaMovement().multiply(1.1,1,1.1).add(getViewVector(1).multiply(1,0,1));
                this.setDeltaMovement(dir.normalize().scale(0.05*this.jumpDelay*0.17));
                this.entityData.set(SPIN,getSpin()+0.01f+0.2f*(float)dir.length()*this.jumpDelay*0.02f);
            }
            if(this.jumpAmount>0&&onGround()){
                this.entityData.set(SPIN,0f);

                this.jumpDelay=40;
                doJump=true;
            }
            this.jumpDelay-=1;
        }else{
            this.jumpAmount=3;
        }
        super.tick();
        if (this.onGround() && !this.wasOnGroundBefore) {
            float f = this.getDimensions(this.getPose()).width() * 2.0F;
            float f1 = f / 2.0F;
            if (!this.spawnCustomParticles()&&this.level()!=null) {
                this.level().addParticle(NMParticleInit.TAR_SLOP_PARTICLE.get(), this.getX(), this.getY()+0.01f+this.getDeltaMovement().y, this.getZ(), (Math.random()-0.5)/2.0f, 0.0, (Math.random()-0.5)/2.0f);
                this.level().addParticle(NMParticleInit.TAR_SLOP_PARTICLE.get(), this.getX(), this.getY()+0.01f+this.getDeltaMovement().y, this.getZ(), (Math.random()-0.5)/7.0f, 0.0, (Math.random()-0.5)/7.0f);
                for (int i = 0; i < 2; i++) {
                    this.level().addParticle(NMParticleInit.TAR_POP_PARTICLE.get(), this.getX()+(Math.random()-0.5)*getSize(), this.getY()+0.01f+this.getDeltaMovement().y+(Math.random())/2*getSize(), this.getZ()+(Math.random()-0.5)*getSize(), (Math.random()-0.5)/2.0f, 0.0, (Math.random()-0.5)/2.0f);
                    this.level().addParticle(NMParticleInit.TAR_POP_PARTICLE.get(), this.getX()+(Math.random()-0.5)*getSize(), this.getY()+0.01f+this.getDeltaMovement().y+(Math.random())/2*getSize(), this.getZ()+(Math.random()-0.5)*getSize(), (Math.random()-0.5)/7.0f, 0.0, (Math.random()-0.5)/7.0f);
                }
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
        }
        if(doJump&&onGround()&&this.wasJumpBefore){
            this.jumpAmount-=1;
            this.getJumpControl().jump();
            if(this.jumpAmount<=0){
                this.jumpDelay=40;
            }
        }
        this.wasOnGroundBefore=onGround();
        this.wasJumpBefore=doJump;
    }

    @Override
    protected ParticleOptions getParticleType() {
        return new ItemParticleOption(ParticleTypes.ITEM, NMItemInit.TAR_GLOB.toStack());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }


    protected void dealDamage(LivingEntity entity) {
        if (this.isAlive() && this.isWithinMeleeAttackRange(entity) && this.hasLineOfSight(entity)) {
            if(getSpin()>0) {
                this.setDeltaMovement(getViewVector(1).multiply(1, 0, 1).normalize().scale(-0.8).add(0, 0.1, 0));
            }else{
                this.setDeltaMovement(getViewVector(1).multiply(1, 0, 1).normalize().scale(-0.4));

            }
        }
        super.dealDamage(entity);
    }

    @Override
    public void jumpFromGround() {
        if(this.jumpAmount>0){
            this.jumpDelay=40;
        }else{
            this.setDeltaMovement(0,0,0);
            return;
        }
        super.jumpFromGround();
        this.addDeltaMovement(getViewVector(1).multiply(1,0,1).normalize().scale(0.6));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SPIN,0.0f);
        super.defineSynchedData(builder);
    }
    public float getSpin(){
        return this.entityData.get(SPIN);
    }
    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }
    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }
    @VisibleForTesting
    public void setSize(int size, boolean resetHealth) {
        int i = Mth.clamp(size, 1, 127);
        super.setSize(size,resetHealth);

        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(i * i*1.5f)+2);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.12F * (float)i));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)(i-1)*3+1.5);
        this.xpReward = i;
    }
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }


}
