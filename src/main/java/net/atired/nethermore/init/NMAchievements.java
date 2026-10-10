package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.misc.DummyTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NMAchievements {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, Nethermore.MODID);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> MORBID=  TRIGGER_TYPES.register("morbid",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> HEARTY=  TRIGGER_TYPES.register("hearty",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> UNPLEASANT=  TRIGGER_TYPES.register("unpleasant",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> BABBLE=  TRIGGER_TYPES.register("babble",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> TARRED=  TRIGGER_TYPES.register("tarred",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> GARBAGE=  TRIGGER_TYPES.register("garbage",DummyTrigger::new);

}
