package net.atired.nethermore.mixin;

import net.atired.nethermore.accessors.LivingEntityTarAccessor;
import net.atired.nethermore.accessors.RednessServerLevelAccessor;
import net.atired.nethermore.init.NMAchievements;
import net.atired.nethermore.init.NMBiomeInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityTarAccessor {
    private float tarred = 0.0f;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "baseTick",at=@At("HEAD"))
    private void tickTar(CallbackInfo ci){

        if(this.tarred>0.8){
            if((Object)this instanceof ServerPlayer serverPlayer) NMAchievements.TARRED.get().trigger(serverPlayer);
        }
        this.tarred*=0.85f;
    }

    @Override
    public void setTarred(float tar) {
        this.tarred=tar;
    }

    @Override
    public float getTarred() {
        return this.tarred;
    }
}
