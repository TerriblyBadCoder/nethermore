package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

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
            for (int z = -12; z <= 12; z++) {
                boolean noStackingLmoa = false;
                for (int y = -12; y <= 12; y++) {
                    BlockPos pos1=pos.offset(x,y,z);
                    float noisy = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/20.0f+300,pos1.getZ(),55.0f);
                    noisy=Math.abs(noisy);
                    noisy=noisy%0.2f;
                    noisy*=5.0f;
                    float noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/20.0f-300,pos1.getZ(),45.0f);
                    noisy2=Math.abs(noisy2);
                    noisy2=noisy2%0.2f;
                    noisy2*=5.0f;
                    double len = 10.0f/new Vec3(12,12,12).length();
                    if(noisy*len>0.2){
                        if((!level.isEmptyBlock(pos1)&&(level.getBlockState(pos1).isSolid()||level.getBlockState(pos1).is(BlockTags.SOUL_FIRE_BASE_BLOCKS)))||
                                (!level.isEmptyBlock(pos1.below())&&noisy*len>0.3)){
                            if(!level.getBlockState(pos1).is(NMBlockInit.TOPPED_BILESTONE)&&!level.getFluidState(pos1).is(Fluids.LAVA)&&!level.getBlockState(pos1.below()).is(NMBlockInit.TOPPED_BILESTONE)&&!level.getBlockState(pos1.below()).is(NMBlockInit.BILE_GROWTH)){
                                if(level.isEmptyBlock(pos1.above())){
                                    noisy = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/20.0f+300+1/20.0f,pos1.getZ(),55.0f);
                                    noisy=noisy%0.2f;
                                    noisy*=5.0f;
                                    if(level.isEmptyBlock(pos1)){
                                        noisy*=0.1f;

                                        if(noisy2>0.5){
                                            if(noStackingLmoa)continue;
                                            noStackingLmoa=true;
                                        }
                                    }
                                    Block block = (noisy*len>0.3||noisy2>0.5)?NMBlockInit.BILESTONE.get():NMBlockInit.TOPPED_BILESTONE.get();
                                    level.setBlock(pos1,(block).defaultBlockState(),2);
                                    if(block==NMBlockInit.TOPPED_BILESTONE.get()&&level.isEmptyBlock(pos1.above())&&Math.random()>0.96){
                                        level.setBlock(pos1.above(),NMBlockInit.BILE_GROWTH.get().defaultBlockState(),2);
                                    }
                                }else{
                                    level.setBlock(pos1,(NMBlockInit.BILESTONE.get()).defaultBlockState(),2);
                                }
                            }
                        }
                    }
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
                        if(level.isEmptyBlock(pos1)&&level.getBlockState(pos1.below()).isSolid()&&level.getBlockState(pos1).getBlock()!=NMBlockInit.BILE_GROWTH.get()){

                            if(y!=i+2){
                                level.setBlock(pos1,((level.isEmptyBlock(pos1.above())&&y==i+1)? NMBlockInit.TOPPED_BILESTONE.get():NMBlockInit.BILESTONE.get()).defaultBlockState(),2);
                                if(level.getBlockState(pos1.below()).is(NMBlockInit.TOPPED_BILESTONE)){
                                    level.setBlock(pos1.below(),(NMBlockInit.BILESTONE.get()).defaultBlockState(),2);
                                }
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
