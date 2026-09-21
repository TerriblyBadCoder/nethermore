package net.atired.nethermore.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DisgustlingHeadEntity extends Monster {
    private static final EntityDataAccessor<Integer> PARENT_ID= SynchedEntityData.defineId(DisgustlingHeadEntity.class, EntityDataSerializers.INT);

    public DisgustlingHeadEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        DisgustlingEntity parented =  getParent();
        if(this.tickCount %20==6&&parented==null){
            discard();
        }
        setDeltaMovement(getDeltaMovement().scale(0.3));
        if(parented!=null&&parented.getTarget()!=null){
            if(parented.getWindup()>0.01f){
                Vec3 vec3 = getEyePosition().add(getViewVector(1).scale(2));
                this.lookControl.setLookAt(vec3.x,vec3.y,vec3.z);
            }else{
                this.lookControl.setLookAt(parented.getTarget());
                this.lookAt(parented.getTarget(),170,170);
            }
        }
        super.tick();
    }
    public void playerTouch(Player entity) {
        if (this.getParent()!=null&&this.getParent().getWindup()>0f) {
            this.dealDamage(entity);
        }

    }
    protected void dealDamage(LivingEntity livingEntity) {
        if (this.isAlive() && this.isWithinMeleeAttackRange(livingEntity) && this.hasLineOfSight(livingEntity)) {
            DamageSource damagesource = this.damageSources().mobAttack(this);
            if (livingEntity.hurt(damagesource, 4)) {
                Level var4 = this.level();
                if (var4 instanceof ServerLevel) {
                    ServerLevel serverlevel = (ServerLevel)var4;
                    EnchantmentHelper.doPostAttackEffects(serverlevel, livingEntity, damagesource);
                }
            }
        }

    }
    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.is( DamageTypes.IN_WALL)){
            return false;
        }
        if(getParent()!=null){
            getParent().hurt(source,amount);
            return super.hurt(source,0.01f);
        }
        return super.hurt(source, amount);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0f;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(PARENT_ID,-1);
        super.defineSynchedData(builder);
    }
    public void setParent(int id){
        entityData.set(PARENT_ID,id);
    }

    public DisgustlingEntity getParent(){
        if(level().getEntity(entityData.get(PARENT_ID)) instanceof DisgustlingEntity){
            return (DisgustlingEntity)level().getEntity(entityData.get(PARENT_ID));
        }
        return null;
    }
}
