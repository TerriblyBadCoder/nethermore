package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.init.NMFluidInit;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class BasaltLinesFeature  extends Feature<NoneFeatureConfiguration> {
    public BasaltLinesFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        boolean doThat=false;
        for (int i = 0; i < 10; i++) {

            BlockPos pos1 = pos.offset(0,i,0);
            doThat=!level.getBlockState(pos1).isEmpty()&&level.getBlockState(pos1).isCollisionShapeFullBlock(level,pos1)&&level.getBlockState(pos1.above()).isEmpty();
            if(doThat)break;
        }
        if(doThat){
            for (int x = -11; x <= 11; x++) {
                for (int z = -11; z <= 11; z++) {
                    for (int y = -1; y <= 14; y++) {
                        BlockPos pos1 = pos.offset(x,y,z);
                        if(!level.getFluidState(pos1).isEmpty()|| level.getBlockState(pos1).getBlock()==Blocks.LAVA||pos1.getY()<31){
                            continue;
                        }
                        float len = (float)Math.pow( new Vec3(x,0,z).length(),0.66)+2.0f;
                        float noisy = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/7.0f,pos1.getZ(),20.0f);
                        float noisy2 =0;
                        noisy=Math.abs(noisy*(1.0f-Math.abs(y)/10.0f*len/2.0f));
                        noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX(),pos1.getY()/10.0f,pos1.getZ(),50.0f);
                        boolean makeTumor=false;
                        if(!level.getBlockState(pos1).isEmpty()){
                            if(noisy>0.3||pos.getY()<61){
                                makeTumor=true;
                            }
                            noisy*=0.6f;
                            if(noisy2>0.1){
                                noisy*=0.7f;
                            }
                        }

                        if(!level.getBlockState(pos1.below()).isEmpty()&&level.getBlockState(pos1.below()).isCollisionShapeFullBlock(level,pos1)){
                            if(makeTumor){
                                if(pos.getY()<61){
                                    boolean dirs = true;
                                    for(Direction dir : Direction.values()){
                                        if(dir.getStepY()==0){
                                            if(!level.getBlockState(pos1.offset(dir.getNormal())).isCollisionShapeFullBlock(level,pos1.offset(dir.getNormal()))&&!(level.getFluidState(pos1.offset(dir.getNormal())).getFluidType()== NMFluidInit.TAR_FLUID_TYPE.get())){
                                                dirs=false;
                                            }
                                        }
                                    }
                                    FluidState state = level.getBlockState(pos1).getFluidState();
                                    if(level.getBlockState(pos1.above()).isEmpty()&&new Vec3(x,y,z).length()<13&&dirs&&(state.isEmpty()||state.getFluidType()== NMFluidInit.TAR_FLUID_TYPE.get())){
                                        level.setBlock(pos1, NMBlockInit.TAR.get().defaultBlockState(),2);
                                    }else if(noisy<0.16f){
                                        noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX()/4.0f,pos1.getY()/3.0f,pos1.getZ()/4.0f,8.0f)-y/5.0f;

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
                            }else if(noisy<0.34f){
                                 noisy2 = NMsimplexNoise.sampleNoise3D(pos1.getX()/4.0f,pos1.getY()/3.0f,pos1.getZ()/4.0f,4.0f)*1.4f-y/5.0f;
                                 if(noisy2>0.01){
                                     if(noisy2>0.1){
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
            }
            return true;
        }

        return false;
    }
}
