package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class SillShroomFeature extends Feature<NoneFeatureConfiguration> {
    public SillShroomFeature(Codec codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos0 = featurePlaceContext.origin();
        for (int y = -2; y <= 9; y+=3) {
            BlockPos pos =pos0.offset(0,y,0);
            if(!level.getBlockState(pos).isEmpty()){
                int count = 0;
                for(Direction direction : Direction.values()){
                    BlockPos pos1 = pos.relative(direction,1);
                    if(level.getBlockState(pos1).isEmpty()||level.getBlockState(pos1.relative(direction,1)).isEmpty()){
                        count+=1;
                    }
                }
                if(count>=2){
                    float ranRange = (int)(Math.random()*4.0f)+2.0f;
                    for (int z = -6; z <= 6; z++) {
                        for (int x = -6; x < 6; x++) {
                            BlockPos pos1 = pos.offset(x,0,z);
                            if(level.getBlockState(pos1).isEmpty()&&x*x+z*z<ranRange*ranRange){
                                level.setBlock(pos1,NMBlockInit.SILL_SHROOM_BLOCK.get().defaultBlockState(),2);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
