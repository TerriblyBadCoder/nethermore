package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class CerebroCollumnFeature extends Feature<NoneFeatureConfiguration> {
    public CerebroCollumnFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {

        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        boolean does = true;
        for (int i = 2; i < 6; i++) {
            does=does&&level.getBlockState(pos.below(i)).isEmpty();
        }
        if(!level.getBlockState(pos).isEmpty()&&does) {
                float yRot = (float)Math.random()*3.14f*2.0f;
                for (int x = -8; x <= 8; x++) {
                    for (int y = -8; y <= 8; y++) {
                        for (int z = -8; z <= 8; z++) {
                            Vec3 dir = new Vec3(x,y,z);
                            BlockPos pos1 = pos.offset(x,y,z);
                            float noisier = NMsimplexNoise.sampleNoise3D(200+pos1.getX(),pos1.getY(),pos1.getZ(),12);

                            if(dir.length()<7&&dir.length()>5.5&&!level.getBlockState(pos1).isEmpty()){
                                float noisy = NMsimplexNoise.sampleNoise3D(200+pos1.getX(),pos1.getY(),pos1.getZ(),6);
                                if(noisy>-0.2){
                                    level.setBlock(pos1, NMBlockInit.SOUL_LINING_BLOCK.get().defaultBlockState(),2);
                                }else{
                                    level.setBlock(pos1, NMBlockInit.SOUL_CRYSTAL_BLOCK.get().defaultBlockState(),2);
                                }
                            }else if(((dir.length()<5.5&&dir.length()>4.5+noisier)||(dir.length()>7.0&&dir.length()<7.5-noisier))&&!level.getBlockState(pos1).isEmpty()){
                                float noisy = NMsimplexNoise.sampleNoise3D(200+pos1.getX(),pos1.getY(),pos1.getZ(),5);
                                if(noisy>-0.2){
                                    level.setBlock(pos1, NMBlockInit.BLUE_ASH_BLOCK.get().defaultBlockState(),2);
                                }else{
                                    level.setBlock(pos1, NMBlockInit.BLUE_SLAG_BLOCK.get().defaultBlockState(),2);
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < 12; i++) {
                    double length = 8*Math.random()+5;
                    Vec3 dir = new Vec3(1,0,0).zRot((float)(Math.random()-0.5f)*3.0f).yRot(yRot+i/12.0f*3.14f*2.0f);
                    for (double j = 0; j < length; j+=0.5) {
                        BlockPos pos1 = pos.offset((int)dir.scale(j).x(),(int)dir.scale(j).y(),(int)dir.scale(j).z());
                        if(j<length/2){
                            for (int x = -1; x <1; x++) {
                                for (int z = -1; z <1; z++) {
                                    for (int y = -1; y <1; y++) {
                                        float noisy = NMsimplexNoise.sampleNoise3D(200+pos1.getX(),pos1.getY(),pos1.getZ(),6);
                                        if(noisy>0.1){
                                            level.setBlock(pos1, NMBlockInit.SOUL_LINING_BLOCK.get().defaultBlockState(),2);
                                        }else{
                                            level.setBlock(pos1, NMBlockInit.SOUL_CRYSTAL_BLOCK.get().defaultBlockState(),2);
                                        }
                                    }
                                }
                            }
                        }else{
                            float noisy = NMsimplexNoise.sampleNoise3D(200+pos1.getX(),pos1.getY(),pos1.getZ(),6);
                            if(noisy>-0.2){
                                level.setBlock(pos1, NMBlockInit.SOUL_LINING_BLOCK.get().defaultBlockState(),2);
                            }else{
                                level.setBlock(pos1, NMBlockInit.SOUL_CRYSTAL_BLOCK.get().defaultBlockState(),2);
                            }
                        }
                    }
                }

            return true;
            }

        return false;
    }
}
