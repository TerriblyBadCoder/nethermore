package net.atired.nethermore.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MorbidPiglinEntity extends ZombifiedPiglin {
    public int jumpCD = 10;
    public double speedLower = 0.0f;
    public MorbidPiglinEntity(EntityType<? extends ZombifiedPiglin> entityType, Level level) {
        super(entityType, level);
    }
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        //this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }
    public static AttributeSupplier.Builder createMorbidAttributes() {
        return ZombifiedPiglin.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.19).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.KNOCKBACK_RESISTANCE,0.66);
    }
    @Override
    public void tick() {
        speedLower*=0.8f;
        if(getTarget()!=null){
            jumpCD-=1;
            double dist=distanceTo(getTarget());
            if(jumpCD<=0&&dist>3&&dist<5&&onGround()){
                jumpCD=80+(int)(Math.random()*30);
                lookAt(getTarget(),180f,180f);
                addDeltaMovement(getViewVector(1).multiply(1,0,1).yRot((float)(Math.random()-0.5)/1.5f).normalize().scale(0.4+Math.random()/3.0f).add(0,0.2+Math.random()/3.0f,0));
                getNavigation().stop();
                speedLower=1.0f;
            }
            if(speedLower>0.1f){
                this.getMoveControl().setWantedPosition(getTarget().getX(),getTarget().getY(),getTarget().getZ(),1.2f);
            }
        }
        super.tick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }
}
