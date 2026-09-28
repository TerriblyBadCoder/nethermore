package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.blocks.IronRafterBlock;
import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class IronRaftersFeature extends Feature<NoneFeatureConfiguration> {
    public IronRaftersFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(level.isEmptyBlock(pos)||!level.isEmptyBlock(pos.below())){
            return false;
        }
        if(pos.getY()%2==0){
            for (int x = -15; x <= 15; x++) {
                for (int z = -15; z <= 15; z++) {
                    BlockPos pos1 = pos.offset(x,0,z);
                    if(level.isEmptyBlock(pos1)){

                        if((pos1.getX()+pos1.getY()/(int)2)%4==0){
                            if(pos1.getZ()%4==0){
                                level.setBlock(pos1, NMBlockInit.IRON_RAFTER.get().defaultBlockState()
                                        .setValue(((IronRafterBlock)NMBlockInit.IRON_RAFTER.get()).AXIS, Direction.Axis.X)
                                        .setValue(((IronRafterBlock)NMBlockInit.IRON_RAFTER.get()).COMBINED, true)
                                        ,2);
                            }else{
                                level.setBlock(pos1, NMBlockInit.IRON_RAFTER.get().defaultBlockState()
                                                .setValue(((IronRafterBlock)NMBlockInit.IRON_RAFTER.get()).AXIS, Direction.Axis.X)
                                        ,2);
                            }
                        }else if((pos1.getZ()+pos1.getY()/(int)2)%4==0){
                            level.setBlock(pos1, NMBlockInit.IRON_RAFTER.get().defaultBlockState()
                                            .setValue(((IronRafterBlock)NMBlockInit.IRON_RAFTER.get()).AXIS, Direction.Axis.Z)
                                    ,2);
                        }
                    }

                }
            }
            return true;
        }
        return false;
    }
}
