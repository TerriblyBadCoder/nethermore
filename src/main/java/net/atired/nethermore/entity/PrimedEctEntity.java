package net.atired.nethermore.entity;

import net.atired.nethermore.init.NMEntityInit;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class PrimedEctEntity extends PrimedTnt {
    public PrimedEctEntity(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(NMEntityInit.ECT.get(), level);
    }
    public PrimedEctEntity(Level level, double x, double y, double z) {
        this(NMEntityInit.ECT.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * 6.2831854820251465;
        this.setDeltaMovement(-Math.sin(d0) * 0.05, 0.20000000298023224+Math.random()/5.0f, -Math.cos(d0) * 0.05);
        this.setYRot((float)(Math.random()-0.5f)*20.0f);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public void tick() {
        super.tick();
        this.yRotO=this.getYRot();
        this.setYRot(this.getYRot()-(float)Math.abs(this.getDeltaMovement().y)*15.0f);
    }

    @Override
    protected double getDefaultGravity() {
        return super.getDefaultGravity()*0.4f;
    }

    @Override
    protected void explode() {
        if(level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(NMParticleInit.SOUL_PARTICLE.get(),getX(),getY(0.5),getZ(),15,0.4,0.4,0.4,0.1);
            serverLevel.sendParticles(NMParticleInit.SOUL_PARTICLE.get(),getX(),getY(0.5),getZ(),15,0.4,0.4,0.4,0.4);
            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,getX(),getY(0.5),getZ(),15,0.1,0.4,0.1,0.1);
            for (int i = 0; i < 16; i++) {
                SoulProjEntity soul = new SoulProjEntity(NMEntityInit.SOUL.get(),serverLevel);
                soul.shootFromRotation(this, this.getXRot(), (float)Math.random()*360.0f, 0.0F, 0.46F+(float)Math.random()/25.0f, 30.0F);
                soul.addDeltaMovement(new Vec3(0,(Math.random()-0.5)/7.0f,0));
                double ranDumb = Math.random()/5.0f+0.8f;
                soul.setDeltaMovement(soul.getDeltaMovement().multiply(ranDumb*2,1,ranDumb*2));
                soul.setPos(new Vec3(getX((Math.random()-0.5)*4.0),getY()-0.1,getZ((Math.random()-0.5)*4.0)));
                serverLevel.addFreshEntity(soul);
            }
        }
    }
}
