package net.atired.nethermore.entity;

import net.atired.nethermore.init.NMEntityInit;
import net.atired.nethermore.init.NMItemInit;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class AshBallEntity extends Snowball {
    public AshBallEntity(EntityType<? extends Snowball> entityType, Level level) {
        super(entityType, level);
    }
    protected AshBallEntity(EntityType<? extends Snowball> entityType, double x, double y, double z, Level level) {
        this(entityType, level);
        this.setPos(x, y, z);
    }

    protected AshBallEntity(EntityType<? extends Snowball> entityType, LivingEntity shooter, Level level) {
        this(entityType, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), level);
        this.setOwner(shooter);
    }

    public AshBallEntity(Level level, LivingEntity shooter) {
        this(NMEntityInit.ASH_BALL.get(), shooter, level);
    }

    @Override
    protected double getDefaultGravity() {
        return super.getDefaultGravity()*0.66;
    }

    @Override
    public void tick() {
        if(this.tickCount%2==1&&this.tickCount>10){
            //level().addParticle(NMParticleInit.SOUL_PARTICLE.get(),getX(Math.random()-0.5),getY(Math.random()),getZ(Math.random()-0.5) ,0,0,0);
            level().addParticle(ParticleTypes.SNOWFLAKE,getX(Math.random()-0.5),getY(Math.random()),getZ(Math.random()-0.5),0,0,0);
        }
        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(result.getEntity()!=null){
            if(level() instanceof ServerLevel serverLevel&&result.getEntity() instanceof LivingEntity living){
                SoulProjEntity projEntity = new SoulProjEntity(serverLevel,getX(),getY(),getZ());
                serverLevel.addFreshEntity(projEntity);
                projEntity.setPos(getPosition(1));
                projEntity.setDeltaMovement(getDeltaMovement().scale(-0.1));
            }
            result.getEntity().addDeltaMovement(getDeltaMovement().scale(0.5));
        }
        super.onHitEntity(result);
    }

    @Override
    protected Item getDefaultItem() {
        return NMItemInit.ASH_BALL.asItem();
    }
}
