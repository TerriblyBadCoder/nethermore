package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class SoulStackedBumpFeature extends Feature<NoneFeatureConfiguration> {
    public SoulStackedBumpFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    public void placeSecond(WorldGenLevel level, BlockPos pos,int y,boolean up){
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 12; z++) {
                BlockPos pos1 = pos.offset(x,y,z);
                float noisy = NMsimplexNoise.sampleNoise3D(200+pos1.getX(),pos1.getY(),pos1.getZ(),40);
                float noisy3 = NMsimplexNoise.sampleNoise3D(200+pos1.getX(),pos1.getY(),pos1.getZ(),100);
                if(noisy>0.3){
                    double dist = new Vec3(x,up?y/3.0f:(8.0f-Math.abs(y)/2.0f),z).length();
                    float noisy2 = NMsimplexNoise.sampleNoise3D(200+pos1.getX(),pos1.getY(),pos1.getZ(),12);
                    if(dist+noisy2*4.0<8.0&&level.getBlockState(pos1).isEmpty()){
                        noisy2 = NMsimplexNoise.sampleNoise3D(200+pos1.getX(),pos1.getY()*2.0f,pos1.getZ(),12);
                        if(noisy2>0){
                            if(noisy2<0.4){
                                level.setBlock(pos1,Blocks.SOUL_SOIL.defaultBlockState(),2);
                            }
                        }else{
                            level.setBlock(pos1,Blocks.SOUL_SAND.defaultBlockState(),2);
                        }
                    }

                }
            }
        }
    }
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(!level.getBlockState(pos).isEmpty()){
            if(level.getBlockState(pos.above()).isEmpty()){
                for(int y = -6; y <= 30; y+=1){
                    placeSecond(level,pos,y,true);
                }
            } else if (level.getBlockState(pos.below()).isEmpty()) {
                for(int y = 30; y >=-6; y-=1){
                    placeSecond(level,pos,y,false);
                }
            }
        }
        return false;
    }
}
