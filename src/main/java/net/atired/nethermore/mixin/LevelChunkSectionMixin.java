package net.atired.nethermore.mixin;

import net.atired.nethermore.accessors.BiomeLevelChunkSectionAccessor;
import net.atired.nethermore.init.NMBiomeInit;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LevelChunkSection.class)
public class LevelChunkSectionMixin<T extends Biome> implements BiomeLevelChunkSectionAccessor {

    @Shadow private PalettedContainerRO<Holder<Biome>> biomes;
//    @ModifyArgs(method = "fillBiomesFromNoise",at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/PalettedContainer;getAndSetUnchecked(IIILjava/lang/Object;)Ljava/lang/Object;"))
//    private void noisyNoise(Args args, BiomeResolver biomeResolver, Climate.Sampler climateSampler, int x, int y, int z){
////        if (args.get(3) instanceof Holder holder && holder.is(NMBiomeInit.HAUNTED_CRAGS)) {
////            args.set(3,biomeResolver.getNoiseBiome(0, 0, 0, climateSampler));
////        }
//
//    }

    @Override
    public void setBiome(PalettedContainerRO<Holder<Biome>> biome) {
        this.biomes=biome;
    }
}
