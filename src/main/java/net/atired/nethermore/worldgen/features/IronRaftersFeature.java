package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.blocks.IronRafterBlock;
import net.atired.nethermore.blocks.SyringeBlock;
import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

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
            IronRafterBlock block = ((IronRafterBlock)NMBlockInit.IRON_RAFTER.get());
            for (int x = -15; x <= 15; x++) {
                for (int z = -15; z <= 15; z++) {
                    BlockPos pos1 = pos.offset(x,0,z);
                    if(level.isEmptyBlock(pos1)&&new Vec3(x,0,z).length()<15){

                        if((pos1.getX()+pos1.getY()/(int)2)%4==0){
                            if((pos1.getZ()+pos1.getY()/(int)2)%4==0){
                                level.setBlock(pos1, NMBlockInit.IRON_RAFTER.get().defaultBlockState()
                                        .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                                        .setValue(IronRafterBlock.COMBINED, true)
                                        ,2);

                            }else{
                                if(Math.random()>0.9f&&(pos1.getZ()+pos1.getY()/(int)2)%4==2&&level.getBlockState(pos1.below()).isEmpty()){
                                    level.setBlock(pos1.below(),NMBlockInit.SYRINGE.get().defaultBlockState().setValue(
                                            BlockStateProperties.FACING,Direction.DOWN),2);
                                    level.setBlock(pos1, NMBlockInit.IRON_RAFTER.get().defaultBlockState()
                                                    .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                                                    .setValue(IronRafterBlock.COMBINED, true)
                                                    .setValue(IronRafterBlock.ISX, false)
                                            ,2);
                                }
                                else{
                                    level.setBlock(pos1, NMBlockInit.IRON_RAFTER.get().defaultBlockState()
                                                    .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                                            ,2);
                                }
                            }
                        }else if((pos1.getZ()+pos1.getY()/(int)2)%4==0){
                            if(Math.random()>0.9f&&(pos1.getX()+pos1.getY()/(int)2)%4==2&&level.getBlockState(pos1.below()).isEmpty()){
                                level.setBlock(pos1.below(),NMBlockInit.SYRINGE.get().defaultBlockState().setValue(
                                        BlockStateProperties.FACING,Direction.DOWN),2);
                                level.setBlock(pos1, NMBlockInit.IRON_RAFTER.get().defaultBlockState()
                                                .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                                                .setValue(IronRafterBlock.COMBINED, true)
                                                .setValue(IronRafterBlock.ISX, true)
                                        ,2);
                            }
                            else{
                                level.setBlock(pos1, NMBlockInit.IRON_RAFTER.get().defaultBlockState()
                                                .setValue(RotatedPillarBlock.AXIS, Direction.Axis.X)
                                        ,2);
                            }
                        }
                    }

                }
            }
            return true;
        }
        return false;
    }
}
