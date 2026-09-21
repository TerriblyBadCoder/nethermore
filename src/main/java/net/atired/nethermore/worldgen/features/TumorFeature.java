package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class TumorFeature extends Feature<NoneFeatureConfiguration> {
    public TumorFeature(Codec codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(!level.getBlockState(pos).isEmpty()){
            boolean yeaGoAhead=false;
            for(Direction direction : Direction.values()){
                BlockPos pos1 = pos.relative(direction,1);
                if(level.getBlockState(pos1).isEmpty()){
                    yeaGoAhead=true;
                    break;
                }
            }
            if(yeaGoAhead){
                boolean madeMouth=false;
                Direction ranDir=Direction.fromYRot(Math.random()*360.0f*2.0f);

                for (int x = -8; x <= 8; x++) {
                    for (int y = -8;y <= 8; y++) {
                        for (int z = -8; z <= 8; z++) {
                            Vec3 dir = new Vec3(x,y,z);
                            double len = dir.length();
                            dir=dir.normalize();
                            float sins=Mth.sin((float)(dir.x+dir.y*0.6f)*4.0f+pos.getY())*2- Mth.cos((float)(dir.y*0.6f+dir.z)*4.0f+pos.getY())*2;
                            if(len<6+sins){
                                if(len<5+sins){
                                    BlockPos pos1 = pos.offset(x,y,z);
                                    Block block=level.getBlockState(pos1).getBlock();
                                    if(block!=NMBlockInit.MAW_TUMOR_BLOCK.get()){
                                        double randumb = Math.random();
                                        boolean placeFat= Math.abs(NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY(),pos1.getZ(),110))<0.01&&Math.abs(NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY(),pos1.getZ(),80))>0.13f;
                                        if(randumb>0.1f){
                                            boolean placetumor=true;
                                            if(len>5&&pos1.getY()%2==0&&randumb>0.4f&&!madeMouth){
                                                Direction direction=Direction.getNearest(dir.multiply(1,0,1).normalize());
                                                if(ranDir==direction){
                                                    BlockState state = NMBlockInit.MAW_TUMOR_BLOCK.get().defaultBlockState();
                                                    state.setValue(BlockStateProperties.FACING,direction);
                                                    BlockState state2 = state.setValue(BlockStateProperties.FACING,direction);
                                                    level.setBlock(pos1,state2,2);

                                                    state2 = state.setValue(BlockStateProperties.FACING,direction.getClockWise().getOpposite());
                                                    level.setBlock(pos1.offset(direction.getClockWise().getNormal()),state2,2);
                                                    level.setBlock(pos1.offset(direction.getOpposite().getNormal()).offset(direction.getClockWise().getNormal()),NMBlockInit.TUMOR_BLOCK.get().defaultBlockState(),2);

                                                    state2 = state.setValue(BlockStateProperties.FACING,direction.getCounterClockWise().getOpposite());
                                                    level.setBlock(pos1.offset(direction.getCounterClockWise().getNormal()),state2,2);
                                                    level.setBlock(pos1.offset(direction.getOpposite().getNormal()).offset(direction.getCounterClockWise().getNormal()),NMBlockInit.TUMOR_BLOCK.get().defaultBlockState(),2);
                                                    ranDir=Direction.fromYRot(Math.random()*360.0f*2.0f);
                                                    placetumor=false;
                                                }

                                            }
                                            if(placetumor){
                                                level.setBlock(pos1, placeFat?NMBlockInit.FAT_TUMOR_BLOCK.get().defaultBlockState():NMBlockInit.TUMOR_BLOCK.get().defaultBlockState(),2);
                                            }
                                        }else{
                                            level.setBlock(pos1, placeFat?NMBlockInit.FAT_TUMOR_BLOCK.get().defaultBlockState():NMBlockInit.EYE_TUMOR_BLOCK.get().defaultBlockState(),2);
                                        }
                                    }

                                }else {
                                    BlockPos offPos=pos.offset(x,y,z);
                                    yeaGoAhead=false;
                                    for(Direction direction : Direction.values()){
                                        BlockPos pos1 = offPos.relative(direction,1);
                                        Block block=level.getBlockState(pos1).getBlock();
                                        if(!level.getBlockState(pos1).isEmpty()&&block!=NMBlockInit.EYE_TUMOR_BLOCK.get()&&block!=NMBlockInit.FAT_TUMOR_BLOCK.get()&&block!=NMBlockInit.MAW_TUMOR_BLOCK.get()&&block!=NMBlockInit.TUMOR_BLOCK.get()&&block!=Blocks.EMERALD_BLOCK){
                                            yeaGoAhead=true;
                                            break;
                                        }
                                    }
                                    if(!level.getBlockState(offPos).isEmpty()||yeaGoAhead){
                                        BlockPos pos1 = pos.offset(x,y,z);
                                        level.setBlock(pos1, Blocks.EMERALD_BLOCK.defaultBlockState(),2);
                                    }
                                }
                            }
                        }
                    }
                }
                for (int x = -8; x <= 8; x++) {
                    for (int y = -8;y <= 8; y++) {
                        for (int z = -8; z <= 8; z++) {

                            BlockPos pos1 = pos.offset(x,y,z);
                            Block block=level.getBlockState(pos1).getBlock();
                            if(block==Blocks.EMERALD_BLOCK){
                                level.setBlock(pos1, Blocks.BLACKSTONE.defaultBlockState(),2);

                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
