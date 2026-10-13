package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class ScrambledPitsMonsterRoom extends Feature<NoneFeatureConfiguration> {
    public ScrambledPitsMonsterRoom(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }


    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(level.isEmptyBlock(pos)||Math.random()>0.2)return false;
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                for (int y = 0; y < 9; y++) {
                    BlockPos pos1 = pos.offset(x,y,z);
                    if(!level.isEmptyBlock(pos1)&&NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()*3.0f,pos1.getZ(),5.0f)*1.3f>-3+new Vec3(x,y,z).multiply(0.7,1,0.7).length()/2.0f){
                        Block block = Blocks.POLISHED_BLACKSTONE_BRICKS;
                        if(Math.abs(x)==4||Math.abs(z)==4||y==0||y==9){
                            if(NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()*6.0f,pos1.getZ(),12.0f)>0.2){
                                block=Blocks.BLACKSTONE;
                            }
                        }else{
                            if(x==0&&z==0&&y==1){
                                level.setBlock(pos1,Blocks.IRON_TRAPDOOR.defaultBlockState(),2);
                                continue;
                            }
                            block=Blocks.AIR;
                            if((pos1.getX()+pos.getX())%12==3&&(pos1.getZ()+pos.getZ())%12>5){
                                block=Blocks.BLACKSTONE_WALL;
                            }
                            else if((pos1.getZ()+pos.getZ())%12==3&&(pos1.getX()+pos.getX())%12>5){
                                block=Blocks.BLACKSTONE_WALL;
                            }
                        }
                        level.setBlock(pos1, block.defaultBlockState(),2);
                    }
                }
            }
        }
        return false;
    }
}
