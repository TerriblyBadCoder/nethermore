package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.init.NMItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

import javax.annotation.Nullable;

import static net.atired.nethermore.init.NMBlockInit.FRUITING;

public class CreepingIvBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<CreepingIvBlock> CODEC = simpleCodec(CreepingIvBlock::new);

    protected static final VoxelShape SHAPE = Block.box(2.0, 5.0, 2.0, 14.0, 16.0, 14.0);

    public MapCodec<CreepingIvBlock> codec() {
        return CODEC;
    }

    public CreepingIvBlock(BlockBehaviour.Properties p_154966_) {
        super(p_154966_, Direction.DOWN, SHAPE, false, 0.1);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, 0).setValue(FRUITING,false));

    }

    protected int getBlocksToGrowWhenBonemealed(RandomSource p_222680_) {
        return NetherVines.getBlocksToGrowWhenBonemealed(p_222680_);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if ((Boolean)state.getValue(FRUITING)) {
            Block.popResource(level, pos, new ItemStack(NMItemInit.CREEPING_IV_CHERRY.get(), 1));
            float f = Mth.randomBetween(level.random, 0.5F, 0.9F);
            level.playSound((Player)null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, f);
            BlockState blockstate = (BlockState)state.setValue(FRUITING, false);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(Math.random()>0.93){
            level.setBlock(pos,state.setValue(FRUITING,true),2);
        }
        super.randomTick(state,level,pos,random);

    }
    public BlockState getStateForPlacement(LevelAccessor level) {
        return (BlockState)this.defaultBlockState().setValue(AGE, level.getRandom().nextInt(25)).setValue(FRUITING,false);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE,FRUITING});
    }

    protected Block getBodyBlock() {
        return NMBlockInit.CREEPING_IV_PLANT.get();
    }

    protected boolean canGrowInto(BlockState p_154971_) {
        return NetherVines.isValidGrowthState(p_154971_);
    }
}
