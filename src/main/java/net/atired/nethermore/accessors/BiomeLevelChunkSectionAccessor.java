package net.atired.nethermore.accessors;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.PalettedContainerRO;

public interface BiomeLevelChunkSectionAccessor {
    void setBiome(PalettedContainerRO<Holder<Biome>> biome);
}
