package net.atired.nethermore.worldgen;
import net.atired.nethermore.Nethermore;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class NMPlacedFeatures {

    public static final ResourceKey<PlacedFeature> TUMOR_PK = registerKey("tumor_placed");
    public static final ResourceKey<PlacedFeature> SMALL_TUMOR_PK = registerKey("small_tumor_placed");
    public static final ResourceKey<PlacedFeature> TUMOR_PILLAR_PK = registerKey("tumor_pillar_placed");
    public static final ResourceKey<PlacedFeature> BASALT_LINES_PK = registerKey("basalt_lines_placed");
    public static final ResourceKey<PlacedFeature> CRIMSON_BUMP_PK = registerKey("crimson_bump_placed");
    public static final ResourceKey<PlacedFeature> WHISPERING_THORN_PK = registerKey("whispering_thorn_placed");
    public static final ResourceKey<PlacedFeature> SOUL_BUMP_PK = registerKey("soul_bump_placed");
    public static final ResourceKey<PlacedFeature> SHATTER_PK = registerKey("shatter_placed");
    public static final ResourceKey<PlacedFeature> BRICK_ROAD_PK = registerKey("brick_road_placed");
    public static final ResourceKey<PlacedFeature> SLAG_PK = registerKey("slag_placed");
    public static final ResourceKey<PlacedFeature> BILESTONE_PK = registerKey("bilestone_placed");
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, SMALL_TUMOR_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.SMALL_TUMOR_KEY),
                commonPlacement(26, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(50), VerticalAnchor.belowTop(27))));
        register(context, TUMOR_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.TUMOR_KEY),
                commonPlacement(32, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(67), VerticalAnchor.belowTop(7))));
        register(context, BASALT_LINES_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.BASALT_LINES_KEY),
                typicalPlacement(CountPlacement.of(UniformInt.of(32,48)), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(31), VerticalAnchor.belowTop(45))));
        register(context, CRIMSON_BUMP_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.CRIMSON_BUMP_KEY),
                typicalPlacement(CountPlacement.of(UniformInt.of(8,12)), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(33), VerticalAnchor.belowTop(55))));
        register(context, WHISPERING_THORN_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.WHISPERING_THORN_KEY),
                typicalPlacement(CountPlacement.of(UniformInt.of(1,6)), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(53), VerticalAnchor.belowTop(35))));
        register(context, TUMOR_PILLAR_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.TUMOR_PILLAR_KEY),
                typicalPlacement(CountPlacement.of(UniformInt.of(12,16)), HeightRangePlacement.triangle(VerticalAnchor.belowTop(100), VerticalAnchor.belowTop(4))));
        register(context, SOUL_BUMP_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.SOUL_BUMP_KEY),
                commonPlacement(64, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(40), VerticalAnchor.belowTop(30))));
        register(context, SHATTER_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.SHATTER_KEY),
                commonPlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(86), VerticalAnchor.belowTop(5))));
        register(context, BRICK_ROAD_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.BRICK_ROAD_KEY),
                commonPlacement(64, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(42), VerticalAnchor.belowTop(20))));
        register(context, SLAG_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.SLAG_KEY),
                commonPlacement(32, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(47), VerticalAnchor.belowTop(20))));
        register(context, BILESTONE_PK, configuredFeatures.getOrThrow(NMConfiguredFeatures.BILESTONE_KEY),
                commonPlacement(48, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(34), VerticalAnchor.aboveBottom(43))));

    }
    public static List<PlacementModifier> typicalPlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonPlacement(int pCount, PlacementModifier pHeightRange) {
        return typicalPlacement(CountPlacement.of(pCount), pHeightRange);
    }

    public static List<PlacementModifier> rarePlacement(int pChance, PlacementModifier pHeightRange) {
        return typicalPlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }
    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Nethermore.getId(name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
