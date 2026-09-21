package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class BasaltLinesFeature  extends Feature<NoneFeatureConfiguration> {
    public BasaltLinesFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();

        if(!level.getBlockState(pos).isEmpty()&&level.getBlockState(pos).isCollisionShapeFullBlock(level,pos)&&level.getBlockState(pos.above()).isEmpty()){
            for (int x = -11; x <= 11; x++) {
                for (int z = -11; z <= 11; z++) {
                    for (int y = -7; y <= 7; y++) {
                        BlockPos pos1 = pos.offset(x,y,z);

                        float len = (float)Math.pow( new Vec3(x,0,z).length(),0.66)+2.0f;
                        float noisy = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/15.0f,pos1.getZ(),10.0f);
                        float noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/30.0f,pos1.getZ(),2.0f);
                        noisy=Math.abs(noisy*(1.0f+Math.abs(y)/10.0f*len/2.0f))*(2.0f-Math.abs(noisy2/2.0f));
                        noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/30.0f,pos1.getZ(),50.0f);
                        boolean makeTumor=false;
                        if(!level.getBlockState(pos1).isEmpty()){
                            if(noisy>0.3||pos.getY()<61){
                                makeTumor=true;
                            }
                            noisy*=0.2f;
                            if(noisy2>0.1){
                                noisy*=0.3f;
                            }
                        }

                        if(noisy<0.14f&&!level.getBlockState(pos1.below()).isEmpty()&&level.getBlockState(pos1.below()).isCollisionShapeFullBlock(level,pos1)){
                            if(makeTumor){
                                if(pos.getY()<61){
                                    boolean dirs = true;
                                    for(Direction dir : Direction.values()){
                                        if(dir.getStepY()==0){
                                            if(level.getBlockState(pos1.offset(dir.getNormal())).isEmpty()){
                                                dirs=false;
                                            }
                                        }
                                    }
                                    if(level.getBlockState(pos1.above()).isEmpty()&&dirs&&level.getBlockState(pos1).getFluidState().isEmpty()){
                                        level.setBlock(pos1, NMBlockInit.TAR.get().defaultBlockState(),2);
                                    }else{
                                        noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/3.0f,pos1.getZ(),8.0f);
                                        if(noisy2>0.3){
                                            if(noisy2>0.6){
                                                level.setBlock(pos1, NMBlockInit.SOFT_TAR_BLOCK.get().defaultBlockState(),2);
                                            }else{
                                                level.setBlock(pos1, NMBlockInit.FUSED_TAR_BLOCK.get().defaultBlockState(),2);
                                            }
                                        }

                                    }
                                }else{
                                    boolean placeFat= Math.abs(NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY(),pos1.getZ(),110))<0.01;
                                    level.setBlock(pos1, placeFat?NMBlockInit.FAT_TUMOR_BLOCK.get().defaultBlockState():NMBlockInit.TUMOR_BLOCK.get().defaultBlockState(),2);
                                }

                            }else{
                                 noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/3.0f,pos1.getZ(),3.0f);

                                if(noisy2>0.1&&pos.getY()<65){
                                    if(noisy2>0.5){
                                        level.setBlock(pos1, NMBlockInit.FUSED_TAR_BLOCK.get().defaultBlockState(),2);
                                    }else{
                                        level.setBlock(pos1, NMBlockInit.COLD_FUSED_TAR_BLOCK.get().defaultBlockState(),2);
                                    }
                                }else{
                                    level.setBlock(pos1, Blocks.BASALT.defaultBlockState(),2);
                                }
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
