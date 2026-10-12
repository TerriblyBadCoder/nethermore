package net.atired.nethermore.entity;

import net.atired.nethermore.init.NMEntityInit;
import net.atired.nethermore.init.NMItemInit;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.commands.arguments.EntityAnchorArgument;
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
import net.minecraft.world.phys.Vec3;

public class AshBallEntity extends Snowball {

    public Vec3[] positions = new Vec3[4];
    public int posTracker = 0;
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
        if(this.posTracker<4){
            this.positions[this.posTracker]=position();

            this.posTracker+=1;
        }
        else{
            for (int i = 1; i < 4; i++) {
                this.positions[i-1]=this.positions[i];
            }
            this.positions[3]=position();
        }
        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(result.getEntity()!=null){
            if(level() instanceof ServerLevel serverLevel&&result.getEntity() instanceof LivingEntity living){
                SoulProjEntity projEntity = new SoulProjEntity(serverLevel,getX(),getY(),getZ());
                projEntity.ignoreHeart=true;
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
