package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.blocks.PinBlock;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
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
        if(NMsimplexNoise.sampleNoise3D(pos.getX(),pos.getY(),pos.getZ()+2000,130.0f)>0.35){
            return false;
        }
        if(!level.getBlockState(pos).isEmpty()&&level.getBlockState(pos.above()).isEmpty()){
            int offCount = 2;
            pos=pos.below();
            for (int x = -6; x <= 6; x++) {
                for (int z = -6; z <= 6; z++) {
                    boolean didDat=false;
                    BlockPos pos2 = pos.offset(x,offCount,z);
                    offCount+=1;
                    while (level.getBlockState(pos2).isEmpty()&&offCount>0){
                        pos2=pos2.offset(0,-1,0);
                        offCount-=1;
                    }
                    if(level.getBlockState(pos2).isEmpty()){
                        continue;
                    }
                    for (int y = 0; y <= 16; y++) {
                        BlockPos pos1 = pos2.offset(0,y,0);

                        float len = (float)Math.pow( new Vec3(x,0,z).length(),1.4)/9;
                        int gottenY=(pos1.getY());
                        gottenY-=gottenY%3;
                        float noisy = NMsimplexNoise.sampleNoise3D(pos1.getX(),gottenY/5.0f,pos1.getZ(),7.0f);
                        float noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX(),((pos1.getY()+1)%3)/5.0f,pos1.getZ(),7.0f);
                        if(!level.getBlockState(pos1).isEmpty()){
                            continue;
                        }
                        double v = noisy * 2.0f - len - (y * y) / 16.0;
                        if(v >-1.8){
                            level.setBlock(pos1, v <-0.1?NMBlockInit.GARBAGE_HEAP.get().defaultBlockState():Blocks.BASALT.defaultBlockState(), 2);
                            if(y>=2&&Math.random()>0.9){
                                level.setBlock(pos1.above(),NMBlockInit.TRASH_BAG.get().defaultBlockState(), 2);
                                break;
                            }else if(y>=2&&Math.random()>0.85){
                                level.setBlock(pos1.above(),Blocks.CANDLE.defaultBlockState().setValue(CandleBlock.LIT,true).setValue(CandleBlock.CANDLES,featurePlaceContext.random().nextIntBetweenInclusive(1,4)), 2);
                                break;
                            }
                            else if(y>=2&&Math.random()>0.92){
                                level.setBlock(pos1.above(),NMBlockInit.PIN.get().defaultBlockState().setValue(PinBlock.FACING, Direction.UP), 2);
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
