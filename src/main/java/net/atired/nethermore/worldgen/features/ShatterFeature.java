package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class ShatterFeature extends Feature<NoneFeatureConfiguration> {
    public ShatterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(level.isEmptyBlock(pos)||!level.isEmptyBlock(pos.above())){
            return false;
        }
        for (int x = -12; x <= 12; x++) {
            for (int y = -12; y <= 18; y++) {
                for (int z = -12; z <= 12; z++) {
                    BlockPos pos1 = pos.offset(x, y, z);
                    float noisy = NMsimplexNoise.sampleNoise3D(x, y/40.0f, z, 35.0f);
                    float noisy2 = NMsimplexNoise.sampleNoise3D(x, y, z, 4.0f)*0.5f;
                    double dist = new Vec3(x,y*2.0,z).length();
                    if(level.getBlockState(pos1.below()).isCollisionShapeFullBlock(level,pos1.below())&&(level.isEmptyBlock(pos1)||((level.getBlockState(pos1).is(BlockTags.SOUL_FIRE_BASE_BLOCKS)||level.getBlockState(pos1).is(Blocks.NETHERRACK))&&level.getBlockState(pos1).isSolid()))){
                        boolean fullOnly=false;
                        if((level.getBlockState(pos1).is(BlockTags.SOUL_FIRE_BASE_BLOCKS)||level.getBlockState(pos1).is(Blocks.NETHERRACK))){
                            dist*=0.7f;
                            fullOnly=true;
                        }
                        if((Math.abs(noisy)%0.5>(dist/5.0/8.0+0.24))){
                            level.setBlock(pos1, NMBlockInit.BLUE_SLAG_BLOCK.get().defaultBlockState(),2);
                        }else if (Math.abs(noisy)%0.5>(dist/5.0/8.0)){
                            if(fullOnly){
                                level.setBlock(pos1, NMBlockInit.BLUE_ASH_BLOCK.get().defaultBlockState(),2);
                                continue;
                            }
                            int layer = (int) Math.clamp((int)((Math.abs(noisy)%0.5-(dist/5.0/8.0))*10.0*8.0*1.4),1,8);
                            level.setBlock(pos1, NMBlockInit.BLUE_ASH.get().defaultBlockState().setValue(BlockStateProperties.LAYERS,layer),2);

                        }
                    }

                }
            }
        }
        return true;
    }
}
