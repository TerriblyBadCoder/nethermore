package net.atired.nethermore.worldgen;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.init.NMBiomeInit;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.BasaltPillarFeature;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NMBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_TUMOR = registerKey("add_tumor");
    public static final ResourceKey<BiomeModifier> ADD_SMALL_TUMOR = registerKey("add_small_tumor");
    public static final ResourceKey<BiomeModifier> ADD_TUMOR_PILLAR = registerKey("add_tumor_pillar");
    public static final ResourceKey<BiomeModifier> ADD_BASALT_LINES = registerKey("add_basalt_lines");
    public static final ResourceKey<BiomeModifier> ADD_CRIMSON_BUMP = registerKey("add_crimson_bump");
    public static final ResourceKey<BiomeModifier> ADD_WHISPERING_THORN = registerKey("add_whispering_thorn");
    public static final ResourceKey<BiomeModifier> ADD_SOUL_BUMP = registerKey("add_soul_bump");
    public static final ResourceKey<BiomeModifier> ADD_SHATTER = registerKey("add_shatter");
    public static final ResourceKey<BiomeModifier> ADD_BRICK_ROAD = registerKey("add_brick_road");
    public static final ResourceKey<BiomeModifier> ADD_SLAG = registerKey("add_slag");
    public static final ResourceKey<BiomeModifier> ADD_BILESTONE = registerKey("add_bilestone");
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature>placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        context.register(ADD_TUMOR, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.HAUNTED_CRAGS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.TUMOR_PK)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_SMALL_TUMOR, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.HAUNTED_CRAGS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.SMALL_TUMOR_PK)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_BASALT_LINES, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.HAUNTED_CRAGS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.BASALT_LINES_PK)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_TUMOR_PILLAR, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.HAUNTED_CRAGS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.TUMOR_PILLAR_PK)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        context.register(ADD_CRIMSON_BUMP, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.HAUNTED_CRAGS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.CRIMSON_BUMP_PK)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_WHISPERING_THORN, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.CRIMSON_FOREST)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.WHISPERING_THORN_PK)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_SOUL_BUMP, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.SCRAMBLED_PITS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.SOUL_BUMP_PK)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_SHATTER, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.SCRAMBLED_PITS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.SHATTER_PK)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));
        context.register(ADD_BRICK_ROAD, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.SCRAMBLED_PITS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.BRICK_ROAD_PK)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));
        context.register(ADD_SLAG, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.SCRAMBLED_PITS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.SLAG_PK)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));
        context.register(ADD_BILESTONE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(NMBiomeInit.SCRAMBLED_PITS)),
                HolderSet.direct(placedFeatures.getOrThrow(NMPlacedFeatures.BILESTONE_PK)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Nethermore.getId(name));
    }
}
