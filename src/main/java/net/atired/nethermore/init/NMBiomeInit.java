package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class NMBiomeInit {
    public static final ResourceKey<Biome> HAUNTED_CRAGS = ResourceKey.create(
            Registries.BIOME,
            Nethermore.getId("haunted_crags")
    );

    public static final ResourceKey<Biome> SCRAMBLED_PITS = ResourceKey.create(
            Registries.BIOME,
            Nethermore.getId("scrambled_pits")
    );
}
