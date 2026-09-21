package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.RootsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrowthBlock extends BushBlock {
    public static final MapCodec<RootsBlock> CODEC = simpleCodec(RootsBlock::new);
    protected static final float AABB_OFFSET = 6.0F;
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

    public MapCodec<RootsBlock> codec() {
        return CODEC;
    }

    public GrowthBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.NYLIUM) || state.is(BlockTags.SOUL_FIRE_BASE_BLOCKS) || state.is(NMBlockInit.BILESTONE) ||state.is(NMBlockInit.TOPPED_BILESTONE) || super.mayPlaceOn(state, level, pos);
    }
}