package net.atired.nethermore.worldgen;
import net.atired.nethermore.Nethermore;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class NMConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> TUMOR_KEY = registerKey("tumor");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_TUMOR_KEY = registerKey("small_tumor");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TUMOR_PILLAR_KEY = registerKey("tumor_pillar");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BASALT_LINES_KEY = registerKey("basalt_lines");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSON_BUMP_KEY = registerKey("crimson_bump");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WHISPERING_THORN_KEY = registerKey("whispering_thorn");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SOUL_BUMP_KEY = registerKey("soul_bump");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHATTER_KEY = registerKey("shatter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BRICK_ROAD_KEY = registerKey("brick_road");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SLAG_KEY = registerKey("slag");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BILESTONE_KEY = registerKey("bilestone");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, TUMOR_KEY,NMFeatures.TUMOR_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, SMALL_TUMOR_KEY,NMFeatures.SMALL_TUMOR_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, TUMOR_PILLAR_KEY,NMFeatures.TUMOR_PILLAR_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, BASALT_LINES_KEY,NMFeatures.BASALT_LINES_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, CRIMSON_BUMP_KEY,NMFeatures.CRIMSON_BUMP_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, WHISPERING_THORN_KEY,NMFeatures.WHISPERING_THORN_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, SOUL_BUMP_KEY,NMFeatures.SOUL_BUMP_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, SHATTER_KEY,NMFeatures.SHATTER_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, BRICK_ROAD_KEY,NMFeatures.BRICK_ROAD_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, SLAG_KEY,NMFeatures.SLAG_FEATURE.get(), new NoneFeatureConfiguration());
        register(context, BILESTONE_KEY,NMFeatures.BILESTONE_FEATURE.get(), new NoneFeatureConfiguration());

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,  Nethermore.getId(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
