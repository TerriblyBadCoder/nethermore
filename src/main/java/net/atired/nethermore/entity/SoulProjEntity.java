package net.atired.nethermore.entity;

import net.atired.nethermore.accessors.RednessServerLevelAccessor;
import net.atired.nethermore.init.NMEntityInit;
import net.atired.nethermore.init.NMParticleInit;
import net.atired.nethermore.networking.payloads.VelSyncPayload;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class SoulProjEntity extends ThrowableItemProjectile {
    public Vec3[] positions = new Vec3[16];
    public float lastYaw;
    public float lastPitch;
    public int posTracker = 0;

    public boolean hitStuff = false;
    public LivingEntity toHurt = null;
    public SoulProjEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    public SoulProjEntity(Level level, LivingEntity shooter) {
        super(NMEntityInit.SOUL.get(), shooter, level);
    }

    public SoulProjEntity(Level level, double x, double y, double z) {
        super(NMEntityInit.SOUL.get(), x, y, z, level);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
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
    public void tick() {
        super.tick();
        if(level() instanceof ServerLevel serverLevel && toHurt==null){
            LivingEntity otherLiving=null;
            for(LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(7))){
                if(living!=getOwner()){
                    if(otherLiving==null){
                        otherLiving=living;
                    }else if(distanceTo(otherLiving)>distanceTo(living)){
                        otherLiving=living;
                    }
                }
            }
            if(otherLiving!=null){
                toHurt=otherLiving;
            }
        }
        if(this.tickCount<140&&toHurt!=null){
            Vec3 to = this.toHurt.getPosition(0).add(0,1.6,0).subtract(this.getPosition(0)).add(0,1.0,0);
            if(this.tickCount<40){
                to=to.add(0,2.4*(1.0f-Math.max(0.0f,(tickCount-20)/10.0f)),0);
            }
            float mul = 0.4f;
            if(level() instanceof RednessServerLevelAccessor serverLevelAccessor){
                mul+=serverLevelAccessor.nethermore$getRed()*1.4f;
                to=to.add(0,-1.0,0);
            }
            if(this.hitStuff||this.tickCount>125){
                mul*=2.3f;
                to=to.add(0,50,0);
                setDeltaMovement(getDeltaMovement().scale(1.03));
            }
            this.setDeltaMovement(evilAssRotLerp(getDeltaMovement().scale(1.01),to.normalize(), mul*0.015f*(float)Math.pow(this.tickCount+3,0.5f)));
        }
        if(this.tickCount>143){
            if(level() instanceof ServerLevel serverLevel){
                this.setDeltaMovement(getDeltaMovement().scale(0.6));

            }
            if(this.tickCount>150){
                discard();
            }
        }
        if(this.posTracker<16){
            for(int i =0;i<16;i++){
                this.positions[i]=position();
                this.posTracker+=1;
            }
        }
        else{
            for (int i = 1; i < 16; i++) {
                this.positions[i-1]=this.positions[i];
            }
            this.positions[15]=position();
        }
    }
    public Vec3 evilAssRotLerp(Vec3 original, Vec3 goTo, float lerp){
        Vec3 vec31=original.normalize();
        Vec3 vec32=goTo.normalize();
        double dot = vec31.dot(vec32);
        if(dot>0.99){
            return original;
        }
        double omega = Math.acos(dot);
        Vec3 vec = vec31.scale(Math.sin(omega*(1.0-lerp))/Math.sin(omega)).add(
                   vec32.scale(Math.sin(omega*lerp)/Math.sin(omega)));
        return vec.scale(original.length());
    }
    @Override
    protected void applyGravity() {
        return;
    }
    public void setHitStuff(){
        this.hitStuff=true;
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(this.hitStuff)return;
        if(!(level() instanceof RednessServerLevelAccessor)){
            return;
        }
        if((level() instanceof RednessServerLevelAccessor serverLevelAccessor)&&serverLevelAccessor.nethermore$getRed()<0.03f){
            return;
        }
        if(result.getEntity() instanceof LivingEntity living&&(living!=getOwner()||this.tickCount>20)&&level()instanceof ServerLevel serverLevel){
            setHitStuff();
            playSound(SoundEvents.SOUL_ESCAPE.value(),3.7f,2.4f);
            playSound(SoundEvents.VEX_CHARGE,0.37f,0.4f);
            living.setHealth(Math.max(0.01f,living.getHealth()-4f));
            Vec3 normDir = getDeltaMovement().multiply(1,0,1).normalize().scale(0.1);
            living.addDeltaMovement(normDir);
            if(living instanceof Player){
                PacketDistributor.sendToPlayersTrackingEntity(this,new VelSyncPayload(living.getId(),normDir.x,normDir.y,normDir.z));
            }
            serverLevel.sendParticles(NMParticleInit.SOUL_PARTICLE.get(),getX(),getY(),getZ(),2,0.1,0.1,0.1,0.03);
            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,getX(),getY(),getZ(),2,0.1,0.1,0.1,0.03);
            if(living.getHealth()<0.1f){
                playSound(SoundEvents.VEX_DEATH,3.7f,0.6f);
                living.hurt(damageSources().mobProjectile(this,(getOwner() instanceof LivingEntity ? (LivingEntity)getOwner() : null)),4);
            }
        }


        super.onHitEntity(result);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if(this.tickCount>20&&!this.hitStuff&&result.getType()== HitResult.Type.BLOCK&&getId()%6>0){
            setHitStuff();
            if(level() instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(ParticleTypes.SOUL,getX(),getY(),getZ(),7,0.1,0.1,0.1,0.03);
            }
        }

    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        super.move(type, pos);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.MAGMA_CREAM.asItem();
    }
}
