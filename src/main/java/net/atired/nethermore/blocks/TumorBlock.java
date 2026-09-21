package net.atired.nethermore.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TumorBlock extends Block {
    public TumorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        int count = level.getRandom().nextInt(5,9);
        for (int i = 0; i < count; i++) {
            Direction dir = Direction.getRandom(level.getRandom());
            BlockPos other=pos.relative(dir);
            if(level.getBlockState(other).getBlock() instanceof TumorBlock tumorBlock){
                level.scheduleTick(other,tumorBlock,8+level.getRandom().nextInt(0,8));
            }
        }

        super.destroy(level, pos, state);
    }
    public void destroyWeaker(LevelAccessor level, BlockPos pos, BlockState state) {
        int count = level.getRandom().nextInt(0,3);
        for (int i = 0; i < count; i++) {
            Direction dir = Direction.getRandom(level.getRandom());
            BlockPos other=pos.relative(dir);
            if(Math.random()>0.2&&level.getBlockState(other).getBlock() instanceof TumorBlock tumorBlock){
                level.scheduleTick(other,tumorBlock,12+level.getRandom().nextInt(0,32));
            }
        }

    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        level.destroyBlock(pos,true);
        destroyWeaker(level,pos,state);
    }
}
