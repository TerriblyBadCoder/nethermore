package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class BrickRoadFeature extends Feature<NoneFeatureConfiguration> {
    public BrickRoadFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(level.isEmptyBlock(pos)||!level.isEmptyBlock(pos.above())){
            return false;
        }
        for (int x = -15; x <= 15; x++) {
            for (int y = -1; y <= 12; y++) {
                for (int z = -15; z <= 15; z++) {
                    BlockPos pos1 = pos.offset(x,y,z);
                    float noisy = NMsimplexNoise.sampleNoise3D(x,y,z,16.0f);
                    double dist = new Vec3(x,y,z).length();
                    if(level.getBlockState(pos1).isEmpty()&&dist<7-y/4.0f){
                        float noisy2 = NMsimplexNoise.sampleNoise3D(x,y,z,2.0f)*9.0f;
                        if(Math.abs(noisy)<0.3&&!level.getBlockState(pos1.below()).isEmpty()&&level.getBlockState(pos1.below()).isCollisionShapeFullBlock(level,pos1.below())){
                            if(Math.abs(noisy)<0.08&&noisy2+y/2.0f<18.0f){
                                noisy2 = NMsimplexNoise.sampleNoise3D(x,y/40.0f,z,3.0f);
                                level.setBlock(pos1, noisy2>0.2?NMBlockInit.SOUL_CRYSTAL_BLOCK.get().defaultBlockState():NMBlockInit.SOUL_LINING_BLOCK.get().defaultBlockState(),2);

                            }
                            else{
                                Block block = level.getBlockState(pos1.below()).getBlock();
                                if(level.getBlockState(pos1.below()).isCollisionShapeFullBlock(level,pos1.below())){
                                    noisy2 = NMsimplexNoise.sampleNoise3D(x,0,z,1.0f)*3.0f;
                                    int layerLevel=Math.clamp((int)((0.3-Math.abs(noisy))/0.2f*9.0f+noisy2),1,8);
                                    level.setBlock(pos1, NMBlockInit.BLUE_ASH.get().defaultBlockState().setValue(BlockStateProperties.LAYERS,layerLevel),2);

                                }
                            }

                        }else if(Math.abs(noisy)>0.5){
                            Block block = level.getBlockState(pos1.below()).getBlock();
                            if(block==Blocks.SOUL_SAND||block==Blocks.SOUL_SOIL||(block== NMBlockInit.BLUE_ASH.get()&&level.getBlockState(pos1.below()).getValue(BlockStateProperties.LAYERS)==8)){
                                noisy2 = NMsimplexNoise.sampleNoise3D(x,0,z,1.0f)*3.0f;
                                int layerLevel=Math.clamp((int)((Math.abs(noisy)-0.5)/0.2f*9.0f+noisy2),1,8);
                                level.setBlock(pos1, NMBlockInit.BLUE_ASH.get().defaultBlockState().setValue(BlockStateProperties.LAYERS,layerLevel),2);

                            }
                        }

                    }
                }
            }
        }
        return false;
    }
}
