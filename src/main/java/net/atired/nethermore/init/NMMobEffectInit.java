package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.effects.MorbidEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NMMobEffectInit {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Nethermore.MODID);
    public static final Holder<MobEffect> MORBID = MOB_EFFECTS.register("morbid", MorbidEffect::new
    );
}
