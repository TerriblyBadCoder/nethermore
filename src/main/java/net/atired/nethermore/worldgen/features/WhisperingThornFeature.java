package net.atired.nethermore.worldgen.features;

import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class WhisperingThornFeature extends Feature<NoneFeatureConfiguration> {
    public WhisperingThornFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(pos.getCenter().length()<2000){
            return false;
        }
        if(!level.getBlockState(pos).isEmpty()&&
                (level.getBlockState(pos).is(Blocks.CRIMSON_NYLIUM)||level.getBlockState(pos).is(NMBlockInit.WHISPERING_THORN_BLOCK))&&
                level.getBlockState(pos.above()).isEmpty()) {
            int offY = 0;
            int randHeight=featurePlaceContext.random().nextInt(4,6)+1;
            for (int i = 0; i < randHeight; i++) {
                offY+=1;
                level.setBlock(pos.above(offY),NMBlockInit.WHISPERING_THORN_BLOCK.get().defaultBlockState(),2);
            }
            for (Direction dir : Direction.values()){
                if(dir.getStepY()==0){
                    if(Math.random()>0.1){
                        level.setBlock(pos.offset(dir.getNormal()),NMBlockInit.WHISPERING_THORN_BLOCK.get().defaultBlockState(),2);
                        if(Math.random()>0.5){
                            level.setBlock(pos.offset(dir.getNormal()).above(),NMBlockInit.WHISPERING_THORN_BLOCK.get().defaultBlockState(),2);
                        }
                    }
                    if(Math.random()>0.2){
                        int height = randHeight-featurePlaceContext.random().nextInt(0,2)-1;
                        BlockPos pos2=pos.above(height);
                        int rand=featurePlaceContext.random().nextInt(2,4);
                        for (int i = 0; i < rand; i++) {
                            level.setBlock(pos2.offset(dir.getNormal().multiply((i+1))),NMBlockInit.WHISPERING_THORN_BLOCK.get().defaultBlockState().setValue(BlockStateProperties.AXIS, dir.getAxis()),2);
                        }
                        pos2=pos2.offset(dir.getNormal().multiply(rand));
                        rand=featurePlaceContext.random().nextInt(2,4);
                        for (int i = 0; i < rand; i++) {
                            level.setBlock(pos2.above(i+1),NMBlockInit.WHISPERING_THORN_BLOCK.get().defaultBlockState(),2);
                        }
                    }
                }
            }
            for (int x = -11; x <= 11; x++) {
                for (int z = -11; z <= 11; z++) {
                    for (int y = -7; y <= 7; y++) {
                        BlockPos pos1 = pos.offset(x, y, z);
                        BlockState state=level.getBlockState(pos1);
                        double dist = new Vec3(x,y,z).length();
                        if(level.getBlockState(pos1.above()).isEmpty()&&(state.is(Blocks.CRIMSON_NYLIUM)||state.is(NMBlockInit.WHISPERING_THORN_BLOCK))){
                            float noise =NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY(),pos1.getZ()+200.0f,12.0f)*2.0f;
                            if(dist/2.0f<1.5f+noise){
                                float noisy =NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY(),pos1.getZ()+200.0f,3.0f);
                                if(noisy>0.86&&Math.random()>0.1){
                                    level.setBlock(pos1.above(),NMBlockInit.WHISPERING_THORN_BLOCK.get().defaultBlockState(),2);
                                }else{
                                    level.setBlock(pos1.above(),NMBlockInit.SLIVERS_BLOCK.get().defaultBlockState().setValue(BlockStateProperties.FLOWER_AMOUNT,(int)(Math.clamp(5.0f-dist/2.0f+noise*4.0+NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY(),pos1.getZ()+200.0f,2.0f),1,4))),2);

                                }

                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
