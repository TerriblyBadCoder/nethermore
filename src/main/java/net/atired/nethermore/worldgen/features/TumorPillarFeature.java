package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class TumorPillarFeature extends Feature<NoneFeatureConfiguration> {
    public TumorPillarFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        if (worldgenlevel.isEmptyBlock(blockpos) && !worldgenlevel.isEmptyBlock(blockpos.above())) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos.mutable();
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = blockpos.mutable();
            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;
            int countForTumor=0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
            int counted = (int) (15+Math.random()*6);
            for(int i = -3; i < 4; ++i) {
                for(int j = -3; j < 4; ++j) {
                    int k = Mth.abs(i) * Mth.abs(j);
                    if (randomsource.nextInt(10) < 10 - k) {
                        blockpos$mutableblockpos2.set(blockpos$mutableblockpos.offset(i, 0, j));
                        int l = 3;

                        while(worldgenlevel.isEmptyBlock(blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos2, Direction.DOWN))) {
                            blockpos$mutableblockpos2.move(Direction.DOWN);
                            --l;
                            if (l <= 0) {
                                break;
                            }
                        }

                        if (!worldgenlevel.isEmptyBlock(blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos2, Direction.DOWN))) {
                            worldgenlevel.setBlock(blockpos$mutableblockpos2, Blocks.BASALT.defaultBlockState(), 2);
                        }
                    }
                }
            }
            float rotOff = (float)Math.random()*3.14f*2.0f;
            if(blockpos.getY()<63)
                for (int i = 0; i < 10; i++) {
                    Vec3 vec3 = new Vec3(i/2.0f+3,0,0).yRot(rotOff+i/5.0f*3.14f);
                    BlockPos pos2 = blockpos.offset(new Vec3i((int)vec3.x,0,(int)vec3.z));
                    int errors=9;
                    while(errors>0&&worldgenlevel.isEmptyBlock(pos2)){
                        errors-=1;
                        pos2=pos2.above();
                    }
                    if (!worldgenlevel.isEmptyBlock(pos2)) {
                        int count = (int)(Math.random()*7.0)+6;
                        for (int j = 0; j < count; j++) {
                            if(worldgenlevel.isEmptyBlock(pos2)){
                                if(j<count/2.0)
                                    worldgenlevel.setBlock(pos2, NMBlockInit.COLD_FUSED_TAR_BLOCK.get().defaultBlockState(),2);
                                else
                                    worldgenlevel.setBlock(pos2,NMBlockInit.FUSED_TAR_BLOCK.get().defaultBlockState(),2);

                            }
                            pos2=pos2.below();
                        }
                    }
                }
            int misses=5;
            while(worldgenlevel.isEmptyBlock(blockpos$mutableblockpos)||misses>0) {
                if (worldgenlevel.isOutsideBuildHeight(blockpos$mutableblockpos)) {
                    return true;
                }
                if(countForTumor>counted){
                    return true;
                }
                countForTumor+=1;
                worldgenlevel.setBlock(blockpos$mutableblockpos,countForTumor>6?((countForTumor>12&&countForTumor/18.0f>Math.random())?NMBlockInit.FAT_TUMOR_BLOCK.get().defaultBlockState():NMBlockInit.TUMOR_BLOCK.get().defaultBlockState()): Blocks.BASALT.defaultBlockState(), 2);
                flag = flag && this.placeHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.NORTH),countForTumor);
                flag1 = flag1 && this.placeHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.SOUTH),countForTumor);
                flag2 = flag2 && this.placeHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.WEST),countForTumor);
                flag3 = flag3 && this.placeHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.EAST),countForTumor);
                blockpos$mutableblockpos.move(Direction.DOWN);
                misses-=1;
            }

            return true;
        } else {
            return false;
        }
    }


    private boolean placeHangOff(LevelAccessor level, RandomSource random, BlockPos pos,int count) {
        if (random.nextInt(10) != 0&&count/6.0f-1.0f<Math.random()) {
            level.setBlock(pos, count>6?NMBlockInit.TUMOR_BLOCK.get().defaultBlockState():Blocks.BASALT.defaultBlockState(), 2);
            return true;
        } else {
            return false;
        }
    }
}
