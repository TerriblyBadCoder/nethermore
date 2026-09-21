package net.atired.nethermore.mixin;

import net.atired.nethermore.accessors.LivingEntityTarAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityTarAccessor {
    private float tarred = 0.0f;
    @Inject(method = "baseTick",at=@At("HEAD"))
    private void tickTar(CallbackInfo ci){
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
