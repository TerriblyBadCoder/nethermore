package net.atired.nethermore.effects;

import net.atired.nethermore.init.NMAchievements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MorbidEffect extends MobEffect {
    public MorbidEffect() {
        super(MobEffectCategory.NEUTRAL, 0x3f5a4c);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return super.shouldApplyEffectTickThisTick(duration, amplifier);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity instanceof ServerPlayer serverPlayer) NMAchievements.MORBID.get().trigger(serverPlayer);
        return super.applyEffectTick(livingEntity, amplifier);
    }
}
