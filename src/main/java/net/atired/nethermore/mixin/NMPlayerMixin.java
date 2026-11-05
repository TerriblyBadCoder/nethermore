package net.atired.nethermore.mixin;

import net.atired.nethermore.accessors.PlayerXPAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class NMPlayerMixin extends LivingEntity implements PlayerXPAccessor {
    protected NMPlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract void giveExperiencePoints(int xpPoints);

    private int delayedXP=0;
    @Override
    public int getDelayedXP() {
        return this.delayedXP;
    }
    @Inject(method = "tick()V",at=@At("HEAD"))
    private void tickTock(CallbackInfo ci){
        if(this.delayedXP>0&&!level().isClientSide()){
            int ceiled = (int)Math.ceil(this.delayedXP*0.2f);
            this.delayedXP=this.delayedXP-ceiled;
            this.giveExperiencePoints(ceiled);
        }
    }
    @Override
    public void setDelayedXP(int delayedXP) {
        this.delayedXP=delayedXP;
    }
}
