package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.blocks.HexatedBasaltBlock;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BasaltOutcropFeature  extends Feature<NoneFeatureConfiguration> {
    public BasaltOutcropFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(NMsimplexNoise.sampleNoise3D(pos.getX(),pos.getY(),pos.getZ()+2000,130.0f)>0.3){
            if(level.getBlockState(pos.below()).getBlock()!=Blocks.BASALT||(pos.getX()+pos.getY())%2!=0||(pos.getZ()+pos.getY())%2!=0){
                return false;
            }
            int count = 9+(int)(Math.random()*5.0);
            Direction dir=Math.random()>0.5?Direction.SOUTH:Direction.EAST;
            for (int j = -1; j <= 1; j++) {
                BlockPos pos1 = pos.relative(dir,j);
                float noisy = -Math.abs(j)/3.0f+NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/7.0f,pos1.getZ()+2000,4.0f)/3.0f;

                for (int i = 0; i <= count; i++) {
                    if((i+2==count||i+3==count)&&j==0){
                        continue;
                    }
                    BlockPos pos2=pos1.offset(0,i,0);
                    Block block= Blocks.BASALT;
                    if(i*3.0f/(float)count-1.5f>noisy){
                        block = NMBlockInit.HEXATED_BASALT.get();
                        if(i*3.0f/(float)count-1.5f>noisy+1.0){
                            block = NMBlockInit.HEXATED_BLOCK.get();

                            if(i*3.0f/(float)count-1.5f>noisy+1.7) {
                                continue;
                            }
                        }else{
                            level.setBlock(pos2,block.defaultBlockState().setValue(HexatedBasaltBlock.AMOUNT,
                                    level.getBlockState(pos2.below()).getBlock()!=block?0:((i+1)*3.0f/(float)(count)-1.5f>noisy+1.0)?2:1),2);

                        }

                    }
                    if(level.getBlockState(pos2).isEmpty()||!level.getBlockState(pos2).getFluidState().isEmpty()){
                        level.setBlock(pos2,block.defaultBlockState(),2);
                    }
                }
            }
            return true;

        }
        return false;

    }
}