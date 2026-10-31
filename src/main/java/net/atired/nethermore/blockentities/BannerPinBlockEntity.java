package net.atired.nethermore.blockentities;

import net.atired.nethermore.init.NMBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class BannerPinBlockEntity extends BlockEntity {
    public float aged = 0;
    public int height = 0;
    public float vHeight = 0;
    public boolean doHeight=true;
    public BannerPinBlockEntity(BlockPos pos, BlockState blockState) {
        super(NMBlockEntityInit.BANNER_PIN_ENTITY.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BannerPinBlockEntity blockEntity) {
        blockEntity.vHeight=Mth.lerp(0.3f,blockEntity.vHeight,blockEntity.height);
        if(blockEntity.doHeight){
            BlockState state2=level.getBlockState(pos.offset(0,-Math.max(1,blockEntity.height),0));
            if((state2.isEmpty()||state.isPathfindable(PathComputationType.AIR))&& blockEntity.height<6){
                blockEntity.height+=1;

            }
        }
        blockEntity.aged+=0.36f*(float)Math.abs(Math.cos(blockEntity.aged/16.0f+(float)Math.pow(pos.getX()+pos.getZ(),0.5f)+pos.getY()))+0.26f;
    }
}
