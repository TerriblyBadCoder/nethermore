package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.blocks.MorgueCabinetBlock;
import net.atired.nethermore.blocks.SyringeBlock;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
        boolean didLantern=false;
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                for (int y = 0; y < 9; y++) {
                    BlockPos pos1 = pos.offset(x,y,z);
                    if(!level.isEmptyBlock(pos1)&&NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()*3.0f,pos1.getZ(),5.0f)*1.3f>-3+new Vec3(x,y,z).multiply(0.7,1,0.7).length()/2.0f){
                        Block block = Blocks.POLISHED_BLACKSTONE_BRICKS;
                        if(Math.abs(x)==4||Math.abs(z)==4||y==0||y==8){
                            if(NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()*6.0f,pos1.getZ(),12.0f)>0.2){
                                block=Blocks.BLACKSTONE;
                            }
                            double randumb =Math.abs( NMsimplexNoise.sampleNoise3D(pos1.getX()-pos1.getX()%2,pos1.getY(),pos1.getZ()-pos1.getZ()%2,0.2f))*1.01;
                            if(randumb>0.17&&y>2&&y<6&&(pos1.getX()+pos1.getY()+pos1.getZ())%2==0){
                                block= NMBlockInit.MORGUE_CABINET_BLOCK.get();
                                Direction dir = Direction.NORTH;
                                if(z==-4){dir = Direction.SOUTH;}
                                if(x==-4){dir = Direction.EAST;}
                                if(x==4){dir = Direction.WEST;}
                                level.setBlock(pos1, block.defaultBlockState().setValue(MorgueCabinetBlock.HAS_LOOT,true).setValue(BlockStateProperties.HORIZONTAL_FACING,dir),2);
                                continue;
                            }
                        }else{
                            if(x==0&&z==0&&y==1){
                                level.setBlock(pos1,Blocks.IRON_TRAPDOOR.defaultBlockState(),2);
                                continue;
                            }
                            if(!didLantern&&x==0&&z==0&&NMsimplexNoise.sampleNoise3D(pos1.getX(),(pos1.getY()+1)*3.0f,pos1.getZ(),5.0f)*1.3f<-3+new Vec3(x,y+1,z).multiply(0.7,1,0.7).length()/2.0f){
                                level.setBlock(pos1,NMBlockInit.SYRINGE.get().defaultBlockState().setValue(SyringeBlock.FACING,Direction.DOWN),2);
                                didLantern=true;
                                continue;
                            }
                            block=Blocks.AIR;
                            if((pos1.getX()+pos.getX())%12==3&&(pos1.getZ())%2==1&&(pos1.getZ()+pos.getZ())%12>5){
                                block=Blocks.BLACKSTONE_WALL;
                            }
                            else if((pos1.getZ()+pos.getZ())%12==3&&(pos1.getX())%2==1&&(pos1.getX()+pos.getX())%12>5){
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
