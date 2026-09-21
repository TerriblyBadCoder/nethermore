package net.atired.nethermore.mixin;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import net.atired.nethermore.accessors.BiomeSourceExpandAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;
import java.util.function.Supplier;

@Mixin(BiomeSource.class)
public class NMBiomeSourceNoiseMixin implements BiomeSourceExpandAccessor {
    @Mutable
    @Shadow @Final private Supplier<Set<Holder<Biome>>> possibleBiomes;
    private boolean expanded=false;

    @Override
    public void nm$appendBiomes(Set<Holder<Biome>> newGenBiomes) {
        if(!this.expanded){
            ImmutableSet.Builder<Holder<Biome>> builder = ImmutableSet.builder();
            //OLD STUFF
            builder.addAll(this.possibleBiomes.get());
            //MY AWESOME STUFF
            builder.addAll(newGenBiomes);

            //THREAD SAFE CONCEPT I FOUND WHEN RESEARCHING SUPPLIERS
            this.possibleBiomes = Suppliers.memoize(builder::build);
            this.expanded = true;
        }
    }
}
