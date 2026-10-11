package net.atired.nethermore.misc;

import net.atired.nethermore.blocks.BlueAshLayerBlock;
import net.atired.nethermore.blocks.SoulLiningBlock;
import net.atired.nethermore.init.NMBlockInit;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;
import java.util.concurrent.BlockingQueue;

public class NMBlockConversions {
    @Nullable
    public static BlockState convert(BlockState from){
        if(from.getBlock() instanceof SoulLiningBlock){
            return Blocks.AMETHYST_BLOCK.defaultBlockState();
        }
        if(from.getBlock() instanceof BlueAshLayerBlock ){
            return Blocks.SNOW.defaultBlockState().setValue(BlockStateProperties.LAYERS,from.getValue(BlockStateProperties.LAYERS));
        }
        if(from.getBlock()== NMBlockInit.BLUE_ASH_BLOCK.get()||from.getBlock()== NMBlockInit.BLUE_SLAG_BLOCK.get()){
            return Blocks.SNOW_BLOCK.defaultBlockState();
        }
        if(from.getBlock()==Blocks.SOUL_SAND){
            return Blocks.DIRT.defaultBlockState();
        }
        if(from.getBlock()==Blocks.SOUL_SOIL){
            return Blocks.COARSE_DIRT.defaultBlockState();
        }
        if(from.getBlock()==Blocks.BLACKSTONE){
            return Blocks.GRANITE.defaultBlockState();
        }
        if(from.getBlock()==Blocks.NETHERRACK){
            return Blocks.STONE.defaultBlockState();
        }
        return null;
    }
}
