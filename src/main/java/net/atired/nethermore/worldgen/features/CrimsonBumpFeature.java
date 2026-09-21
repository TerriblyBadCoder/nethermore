package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class CrimsonBumpFeature  extends Feature<NoneFeatureConfiguration> {
    public CrimsonBumpFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(!level.getBlockState(pos).isEmpty()&&level.getBlockState(pos).isCollisionShapeFullBlock(level,pos)&&level.getBlockState(pos.above()).isEmpty()){
            for (int x = -8; x <= 8; x++) {
                for (int z = -8; z <= 8; z++) {
                    BlockPos pos2 = pos.offset(x,1,z);
                    int count =12;
                    while (level.getBlockState(pos2).isEmpty()){
                        pos2=pos2.offset(0,-1,0);
                        count-=1;
                    }
                    for (int y = 0; y <= 16; y++) {
                        BlockPos pos1 = pos2.offset(0,y,0);

                        float len = (float)Math.pow( new Vec3(x,0,z).length(),2)/7;
                        int gottenY=(pos1.getY());
                        gottenY-=gottenY%3;
                        float noisy = NMsimplexNoise.sampleNoise3D(pos1.getX(),gottenY/5.0f,pos1.getZ(),7.0f);
                        float noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX(),((pos1.getY()+1)%3)/5.0f,pos1.getZ(),7.0f);
                        if(!level.getBlockState(pos1).isEmpty()){
                            continue;
                        }
                        if(noisy*5.0f-len-(y*y)/16.0>-2.5){
                            level.setBlock(pos1,Blocks.NETHERRACK.defaultBlockState(), 2);
                            if(noisy2*5.0f-len-((y+1)*(y+1))/16.0<=-2.5||y>4){
                                noisy = NMsimplexNoise.sampleNoise3D(pos1.getX(),(pos1.getY()-pos1.getY()%5)*26,pos1.getZ(),55);
                                boolean doesNoisy = (Math.abs(noisy)%0.2f)>0.09f;
                                level.setBlock(pos1.above(),doesNoisy?Blocks.CRIMSON_NYLIUM.defaultBlockState():Blocks.WARPED_NYLIUM.defaultBlockState(),2);
                                if(Math.random()>0.8){
                                    performBonemeal(level,featurePlaceContext.random(),pos1.above(),featurePlaceContext.chunkGenerator());
                                }
                                break;
                            }
                        }

                    }
                }
            }
            return true;
        }

        return false;
    }
    public static void performBonemeal(WorldGenLevel level, RandomSource random, BlockPos pos, ChunkGenerator chunkgenerator) {
        BlockState blockstate = level.getBlockState(pos);
        BlockPos blockpos = pos.above();
        Registry<ConfiguredFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
        if (blockstate.is(Blocks.CRIMSON_NYLIUM)) {
            place(registry, NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL, level, chunkgenerator, random, blockpos);
        } else if (blockstate.is(Blocks.WARPED_NYLIUM)) {
            place(registry, NetherFeatures.WARPED_FOREST_VEGETATION_BONEMEAL, level, chunkgenerator, random, blockpos);
            place(registry, NetherFeatures.NETHER_SPROUTS_BONEMEAL, level, chunkgenerator, random, blockpos);
            if (random.nextInt(8) == 0) {
                place(registry, NetherFeatures.TWISTING_VINES_BONEMEAL, level, chunkgenerator, random, blockpos);
            }
        }

    }
    private static void place(Registry<ConfiguredFeature<?, ?>> featureRegistry, ResourceKey<ConfiguredFeature<?, ?>> featureKey, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
        featureRegistry.getHolder(featureKey).ifPresent((p_255920_) -> {
            ((ConfiguredFeature)p_255920_.value()).place(level, chunkGenerator, random, pos);
        });
    }
}
