package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BileStoneFeature extends Feature<NoneFeatureConfiguration> {
    public BileStoneFeature(Codec<NoneFeatureConfiguration> codec) {
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
            for (int y = -12; y <= 12; y++) {
                for (int z = -12; z <= 12; z++) {

                }
            }
        }
        for (int i = 5; i > 0; i--) {
            int offX = featurePlaceContext.random().nextInt(-3,3)*(5-i);
            int offZ = featurePlaceContext.random().nextInt(-3,3)*(5-i);
            for (int x = -1; x < 1; x++) {
                for (int z = 0; z < 2; z++) {
                    for (int y = -1; y <= i+2; y++) {
                        BlockPos pos1 = pos.offset(x+offX,y,z+offZ);
                        if(level.isEmptyBlock(pos1)&&level.getBlockState(pos1.below()).isSolid()){
                            if(level.getBlockState(pos1.below()).is(NMBlockInit.TOPPED_BILESTONE)){
                                level.setBlock(pos1.below(),(NMBlockInit.BILESTONE.get()).defaultBlockState(),2);

                            }
                            if(y!=i+2){
                                level.setBlock(pos1,((level.isEmptyBlock(pos1.above())&&y==i+1)? NMBlockInit.TOPPED_BILESTONE.get():NMBlockInit.BILESTONE.get()).defaultBlockState(),2);
                            }else if(Math.random()>0.9){
                                level.setBlock(pos1,(NMBlockInit.BILE_GROWTH.get()).defaultBlockState(),2);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
