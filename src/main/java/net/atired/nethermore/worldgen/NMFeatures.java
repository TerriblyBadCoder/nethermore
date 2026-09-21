package net.atired.nethermore.worldgen;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.worldgen.features.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NMFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Nethermore.MODID);
    public static final Supplier<Feature<NoneFeatureConfiguration>> TUMOR_FEATURE =
            FEATURES.register("tumor_feature", () -> new TumorFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> SMALL_TUMOR_FEATURE =
            FEATURES.register("small_tumor_feature", () -> new TumorFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> TUMOR_PILLAR_FEATURE =
            FEATURES.register("tumor_pillar_feature", () -> new TumorPillarFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> BASALT_LINES_FEATURE =
            FEATURES.register("basalt_lines_feature", () -> new BasaltLinesFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> CRIMSON_BUMP_FEATURE =
            FEATURES.register("crimson_bump_feature", () -> new CrimsonBumpFeature(NoneFeatureConfiguration.CODEC));

    public static final Supplier<Feature<NoneFeatureConfiguration>> WHISPERING_THORN_FEATURE =
            FEATURES.register("whispering_thorn_feature", () -> new WhisperingThornFeature(NoneFeatureConfiguration.CODEC));

    public static final Supplier<Feature<NoneFeatureConfiguration>> SOUL_BUMP_FEATURE =
            FEATURES.register("soul_bump_feature", () -> new SoulStackedBumpFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> SHATTER_FEATURE =
            FEATURES.register("shatter_feature", () -> new CerebroCollumnFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> BRICK_ROAD_FEATURE =
            FEATURES.register("brick_road_feature", () -> new BrickRoadFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> SLAG_FEATURE =
            FEATURES.register("slag_feature", () -> new ShatterFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> BILESTONE_FEATURE =
            FEATURES.register("bilestone_feature", () -> new BileStoneFeature(NoneFeatureConfiguration.CODEC));

}
