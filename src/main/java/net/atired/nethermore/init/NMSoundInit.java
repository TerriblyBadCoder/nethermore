package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NMSoundInit {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Nethermore.MODID);
    public static final Holder<SoundEvent> HEARTBEAT = SOUND_EVENTS.register(
            "entity.heartbeat",
            // Takes in the registry name
            SoundEvent::createVariableRangeEvent
    );
}
