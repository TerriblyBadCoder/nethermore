package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CorkBoredFeature extends Feature<NoneFeatureConfiguration> {
    public CorkBoredFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        //I am losing it.
        if(placeUp(featurePlaceContext)){return true;}
        if(placeSide(featurePlaceContext,true)){return true;}
        if(placeSide(featurePlaceContext,false)){return true;}
        if(placeFront(featurePlaceContext,true)){return true;}
        if(placeFront(featurePlaceContext,false)){return true;}
        return false;
    }
    public boolean placeSide(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext,boolean forward) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1;z <= 1; z++) {
                if(!level.getBlockState(pos.offset(x,z,0)).isEmpty()){
                    count+=1;
                }
            }
        }
        if(count<7){return false;}
        count = 0;
        boolean doesThat = true;
        for (int i = 1; i < 3; i++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1;z <= 1; z++) {
                    if(level.getBlockState(pos.offset(x,z,-i*(forward?-1:1))).isEmpty()){
                        count+=1;
                    }
                }
            }
        }
        if(count<13){return false;}

        for (int i = 0; i < 4; i++) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2;z <= 2; z++) {
                    BlockPos pos1 = pos.offset(x,z,-i*(forward?-1:1));
                    if(Math.abs(x)<2&&Math.abs(z)<2){
                        level.setBlock(pos1,NMBlockInit.CORKBORED_BLOCK.get().defaultBlockState(),2);
                    } else if (i<2&&level.getBlockState(pos1).isEmpty()) {
                        level.setBlock(pos1,Blocks.BLACKSTONE.defaultBlockState(),2);

                    }
                }
            }
        }
        Direction dir = forward?Direction.SOUTH:Direction.NORTH;

        for (int i = 0; i < 2; i++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1;z <= 1; z++) {
                    BlockPos pos1 = pos.offset(x,z,(-i-3)*(forward?-1:1));
                    if(level.getBlockState(pos1).isEmpty()&&Math.random()>0.7&&!level.getBlockState(pos1.relative(dir.getOpposite())).isEmpty()){
                        level.setBlock(pos1,NMBlockInit.PIN.get().defaultBlockState().setValue(BlockStateProperties.FACING,dir),2);
                    }
                }
            }
        }
        return true;
    }
    public boolean placeFront(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext,boolean forward) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1;z <= 1; z++) {
                if(!level.getBlockState(pos.offset(0,x,z)).isEmpty()){
                    count+=1;
                }
            }
        }
        if(count<7){return false;}
        count = 0;
        boolean doesThat = true;
        for (int i = 1; i < 3; i++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1;z <= 1; z++) {
                    if(level.getBlockState(pos.offset(-i*(forward?-1:1),x,z)).isEmpty()){
                        count+=1;
                    }
                }
            }
        }
        if(count<13){return false;}

        for (int i = 0; i < 4; i++) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2;z <= 2; z++) {
                    BlockPos pos1 = pos.offset(-i*(forward?-1:1),x,z);
                    if(Math.abs(x)<2&&Math.abs(z)<2){
                        level.setBlock(pos1,NMBlockInit.CORKBORED_BLOCK.get().defaultBlockState(),2);
                    } else if (i<2&&level.getBlockState(pos1).isEmpty()) {
                        level.setBlock(pos1,Blocks.BLACKSTONE.defaultBlockState(),2);

                    }
                }
            }
        }
        Direction dir = forward?Direction.EAST:Direction.WEST;
        for (int i = 0; i < 2; i++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1;z <= 1; z++) {
                    BlockPos pos1 = pos.offset((-i-3)*(forward?-1:1),x,z);
                    if(level.getBlockState(pos1).isEmpty()&&Math.random()>0.7&&!level.getBlockState(pos1.relative(dir.getOpposite())).isEmpty()){
                        level.setBlock(pos1,NMBlockInit.PIN.get().defaultBlockState().setValue(BlockStateProperties.FACING,dir),2);
                    }
                }
            }
        }
        return true;
    }
    public boolean placeUp(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1;z <= 1; z++) {
                if(!level.getBlockState(pos.offset(x,0,z)).isEmpty()){
                    count+=1;
                }
            }
        }
        if(count<7){return false;}
        count = 0;
        boolean doesThat = true;
        for (int i = 1; i < 3; i++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1;z <= 1; z++) {
                    if(level.getBlockState(pos.offset(x,-i,z)).isEmpty()){
                        count+=1;
                    }
                }
            }
        }
        if(count<13){return false;}

        for (int i = 0; i < 4; i++) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2;z <= 2; z++) {
                    BlockPos pos1 = pos.offset(x,-i,z);
                    if(Math.abs(x)<2&&Math.abs(z)<2){
                        level.setBlock(pos1,NMBlockInit.CORKBORED_BLOCK.get().defaultBlockState(),2);
                    } else if (i<2&&level.getBlockState(pos1).isEmpty()) {
                        level.setBlock(pos1,Blocks.BLACKSTONE.defaultBlockState(),2);

                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1;z <= 1; z++) {
                    BlockPos pos1 = pos.offset(x,-i-3,z);
                    if(level.getBlockState(pos1).isEmpty()&&Math.random()>0.7&&!level.getBlockState(pos1.above()).isEmpty()){
                        level.setBlock(pos1,NMBlockInit.PIN.get().defaultBlockState().setValue(BlockStateProperties.FACING,Direction.DOWN),2);
                    }
                    else if(level.getBlockState(pos1).isEmpty()&&Math.random()>0.2&&i==3&&!level.getBlockState(pos1.above()).isEmpty()){
                        level.setBlock(pos1,Blocks.LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING,true),2);

                    }
                }
            }
        }
        return true;
    }
}
