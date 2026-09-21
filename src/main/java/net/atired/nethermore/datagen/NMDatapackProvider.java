package net.atired.nethermore.datagen;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.worldgen.NMBiomeModifiers;
import net.atired.nethermore.worldgen.NMConfiguredFeatures;
import net.atired.nethermore.worldgen.NMPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class NMDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()

            .add(Registries.CONFIGURED_FEATURE, NMConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, NMPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NMBiomeModifiers::bootstrap);

    public NMDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Nethermore.MODID));
    }
}
