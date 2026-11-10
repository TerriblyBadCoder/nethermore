package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class SaltOutcropFeature extends Feature<NoneFeatureConfiguration> {
    public SaltOutcropFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(Math.abs(pos.getX())+Math.abs(pos.getZ())<2000|| NMsimplexNoise.sampleNoise3D(4000+pos.getX(),pos.getY(),pos.getZ(),200.0f)<0.3){
            return false;
        }
        float rot = (float)(Math.random()*3.14f*2.0f);
        Vec3 forward = new Vec3(1,0,0).yRot(rot);
        Vec3 side = new Vec3(1,0,0).yRot(rot+3.14f/2.0f);
        Vec3 upward = new Vec3(0,1,0).zRot((float)(Math.random()*3.14f*2.0f)/25.0f).yRot(rot+3.14f/2.0f);
        boolean falsity=false;
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 20; i++) {
                Vec3 real = upward.scale(-5+i*0.5f).add(forward.scale(-6)).add(side.scale(k));
                for (int j = 0; j < 24; j++) {
                    real=real.add(forward.scale(0.5).yRot(j/24.0f*1.57f));
                    BlockPos pos1 = pos.offset((int)(real.x),(int)(real.y),(int)(real.z));
                    if(level.getBlockState(pos1).getBlock()== Blocks.NETHERRACK){
                        level.setBlock(pos1, NMBlockInit.SMOKED_SALT_BLOCK.get().defaultBlockState(),2);
                        falsity=true;
                    }
                }
            }
        }
        return falsity;

    }
}
