package net.atired.nethermore.entity;

import net.atired.nethermore.networking.payloads.VelSyncPayload;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3f;

import java.util.List;

public class EgoMaskEntity extends LivingEntity {
    private static final EntityDataAccessor<Float> SHAKE= SynchedEntityData.defineId(EgoMaskEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> ID= SynchedEntityData.defineId(EgoMaskEntity.class, EntityDataSerializers.INT);
    public LivingEntity maskOwner = null;
    public EgoMaskEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHAKE,0.0f);
        builder.define(ID,-1);
    }
    @Override
    protected void doPush(Entity entity) {
        if(entity ==maskOwner)return;
        Vec3 dir = getViewVector(1).scale(0.5);

        entity.addDeltaMovement(dir);
        super.doPush(entity);
    }
    public int getId2(){
        return this.entityData.get(ID);
    }
    public void setId2(int id){
         this.entityData.set(ID,id);
    }
    @Override
    public void tick() {
        this.setOldPosAndRot();
        super.tick();
        noPhysics=true;
        if(this.getId2()!=-1&&level()!=null&&this.maskOwner==null) {
            this.maskOwner = (EgoEntity)level().getEntity(this.getId2());
        }
        setDeltaMovement(getDeltaMovement().scale(0.0));
        if(this.tickCount>1&&(this.maskOwner==null||!this.maskOwner.isAlive())&&level() instanceof ServerLevel serverLevel)discard();
    }
    public static AttributeSupplier.Builder createEgoMaskAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.ATTACK_DAMAGE, 0.0).add(Attributes.MAX_HEALTH,200);
    }
    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        //super.actuallyHurt(damageSource, damageAmount);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.getEntity()!=null||super.isInvulnerableTo(source);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.getEntity()!=null){
            this.doPush(source.getEntity());
        }
        return super.hurt(source, 0);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {

        return this.isAlive();
    }

}
