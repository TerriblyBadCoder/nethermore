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
        BlockPos pos = featurePlaceContext.origin();
        if(!level.getBlockState(pos).isEmpty()){
            int count = 0;
            for(Direction direction : Direction.values()){
                BlockPos pos1 = pos.relative(direction,1);
                if(level.getBlockState(pos1).isEmpty()){
                    count+=1;
                }
            }
            if(count>=2){
                boolean madeMouth=false;
                Direction ranDir=Direction.fromYRot(Math.random()*360.0f*2.0f);
                float ranRange = (float)Math.random()*6.0f+2.0f;
                for (int z = -8; z <= 8; z++) {
                    for (int x = -8; x < 8; x++) {
                        BlockPos pos1 = pos.offset(x,0,z);
                    }
                }
            }
        }
        return false;
    }
}
