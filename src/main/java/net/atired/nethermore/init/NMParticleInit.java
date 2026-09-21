package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NMParticleInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE,  Nethermore.MODID);
    public static final Supplier<SimpleParticleType> WHISPER_PARTICLE = PARTICLE_TYPES.register(
            "whisper",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> SOUL_PARTICLE = PARTICLE_TYPES.register(
            "soul",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> TAR_POP_PARTICLE = PARTICLE_TYPES.register(
            "tar_pop",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> TAR_SLOP_PARTICLE = PARTICLE_TYPES.register(
            "tar_slop",
            () -> new SimpleParticleType(false)
    );
}
