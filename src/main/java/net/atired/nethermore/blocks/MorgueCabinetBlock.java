package net.atired.nethermore.blocks;

import com.mojang.serialization.MapCodec;
import net.atired.nethermore.init.NMParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class MorgueCabinetBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public MorgueCabinetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(OPEN,false).setValue(FACING, Direction.NORTH)));
    }
    public static final MapCodec<MorgueCabinetBlock> CODEC = simpleCodec(MorgueCabinetBlock::new);
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(OPEN,false);
    }
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }
    private void playSound(@Nullable Entity source, Level level, BlockPos pos, boolean isOpening) {
        level.playSound(source, pos, isOpening?SoundEvents.COPPER_DOOR_OPEN:SoundEvents.COPPER_DOOR_CLOSE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.5F);
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!state.getValue(OPEN)){
            Vec3 center = pos.getCenter();
            Vec3i vec3i = state.getValue(FACING).getNormal();
            Vec3 dir = new Vec3(vec3i.getX(),0,vec3i.getZ());
            center=center.add(dir.scale(0.55));
            double randumb = Math.random();
            if(randumb>0.8){
                for (int i = 0; i < 12; i++) {
                    level.addParticle(NMParticleInit.SOUL_PARTICLE.get(),
                            center.x+(Math.random()-0.5),center.y+(Math.random()-0.5),center.z+(Math.random()-0.5),
                            dir.x*(Math.random()*0.4+0.05),Math.random()/12.0f,dir.z*(Math.random()*.4+0.05));
                }
                level.addParticle(NMParticleInit.OPEN_PARTICLE.get(), center.x,center.y,center.z, dir.x,0,dir.z);
            }else if(randumb>0.6){
                for (int i = 0; i < 12; i++) {
                    level.addParticle(NMParticleInit.TAR_POP_PARTICLE.get(),
                            center.x+(Math.random()-0.5),center.y+(Math.random()-0.5),center.z+(Math.random()-0.5),
                            dir.x*(Math.random()*0.1+0.05),Math.random()/12.0f,dir.z*(Math.random()*.1+0.05));
                }
                level.addParticle(NMParticleInit.TAR_SLOP_PARTICLE.get(), center.x,center.y,center.z, dir.x,0,dir.z);
            }
            }
        this.playSound(player, level, pos, !(Boolean)state.getValue(OPEN));
        level.setBlock(pos,state.setValue(OPEN,!state.getValue(OPEN)),2);
        return InteractionResult.SUCCESS;

        //return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{OPEN,FACING});
    }
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
