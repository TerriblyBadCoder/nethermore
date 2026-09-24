package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.blocks.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NMBlockInit {

    public static final DeferredRegister.Blocks
            BLOCKS = DeferredRegister.createBlocks(Nethermore.MODID);
    public static final DeferredBlock<Block> BILE_GROWTH = registerBlock("bile_growth",
            () -> new GrowthBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_ROOTS).mapColor(MapColor.COLOR_YELLOW)));
    public static final DeferredBlock<Block> BILESTONE = registerBlock("bilestone",
            () -> new SoulSandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE).mapColor(MapColor.COLOR_YELLOW)));
    public static final DeferredBlock<Block> TOPPED_BILESTONE = registerBlock("topped_bilestone",
            () -> new SoulSandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE).mapColor(MapColor.COLOR_YELLOW)));

    public static final DeferredBlock<Block> SOUL_LINING_BLOCK = registerBlock("soul_lining",
            () -> new SoulSandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).lightLevel((p_50872_) -> {
                return 12;
            }).mapColor(MapColor.COLOR_LIGHT_BLUE).sound(SoundType.AMETHYST)));
    public static final DeferredBlock<Block> SOUL_CRYSTAL_BLOCK = registerBlock("soul_crystal",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).lightLevel((p_50872_) -> {
                return 12;
            }).mapColor(MapColor.COLOR_LIGHT_BLUE).sound(SoundType.AMETHYST)));
    public static final DeferredBlock<Block> BLUE_ASH = registerBlock("blue_ash",
            () -> new BlueAshLayerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SOIL).mapColor(MapColor.SNOW)));
    public static final DeferredBlock<Block> BLUE_ASH_BLOCK = registerBlock("blue_ash_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SOIL).mapColor(MapColor.SNOW)));
    public static final DeferredBlock<Block> BLUE_SLAG_BLOCK = registerBlock("blue_slag",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SOIL).mapColor(MapColor.SNOW)));


    public static final DeferredBlock<Block> TUMOR_BLOCK = registerBlock("tumor_block",
            () -> new TumorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK).mapColor(MapColor.TERRACOTTA_CYAN).sound(SoundType.HONEY_BLOCK)));
    public static final DeferredBlock<Block> EYE_TUMOR_BLOCK = registerBlock("eye_tumor_block",
            () -> new TumorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK).mapColor(MapColor.TERRACOTTA_CYAN).sound(SoundType.MOSS)));
    public static final DeferredBlock<Block> FUSED_TAR_BLOCK = registerBlock("fused_tar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE).mapColor(MapColor.COLOR_BLACK).sound(SoundType.DRIPSTONE_BLOCK)));
    public static final DeferredBlock<Block> SOFT_TAR_BLOCK = registerBlock("soft_tar",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK).randomTicks().strength(0.5F).mapColor(MapColor.COLOR_BLACK).sound(SoundType.DRIPSTONE_BLOCK)));
    public static final DeferredBlock<Block> COLD_FUSED_TAR_BLOCK = registerBlock("cold_fused_tar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACKSTONE).mapColor(MapColor.COLOR_BLACK).sound(SoundType.DRIPSTONE_BLOCK)));
    public static final DeferredBlock<Block> FAT_TUMOR_BLOCK = registerBlock("fat_tumor_block",
            () -> new TumorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK).mapColor(MapColor.GLOW_LICHEN).sound(SoundType.HONEY_BLOCK).lightLevel((p_50872_) -> {
                return 9;
            })));
    public static final DeferredBlock<Block> MAW_TUMOR_BLOCK = registerBlock("maw_tumor_block",
            () -> new MawTumorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERRACK).mapColor(MapColor.TERRACOTTA_WHITE).strength(0.6F)));
    public static final DeferredBlock<LiquidBlock> TAR = registerBlockNoItem("tar",()-> new TarBlock(NMFluidInit.TAR_FLUID_SOURCE.get(),BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static final DeferredBlock<Block> WHISPERING_THORN_BLOCK = registerBlock("whispering_thorn",
            () -> new WhisperingThornBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STEM).randomTicks().mapColor(MapColor.COLOR_PURPLE)));
    public static final DeferredBlock<Block> SLIVERS_BLOCK = registerBlock("slivers",
            () -> new SliversBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_PETALS).mapColor(MapColor.COLOR_PURPLE)));

    public static final DeferredBlock<Block> WHISPERING_PLANKS = registerBlock("whispering_planks",
            () -> new WhisperingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_PLANKS).mapColor(MapColor.COLOR_GREEN)));
    public static final DeferredBlock<Block> WHISPERING_SLAB = registerBlock("whispering_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_SLAB).mapColor(MapColor.COLOR_GREEN)));
    public static final DeferredBlock<Block> WHISPERING_STAIRS = registerBlock("whispering_stairs",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STAIRS).mapColor(MapColor.COLOR_GREEN)));
    public static final DeferredBlock<Block> WHISPERING_FENCE = registerBlock("whispering_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_FENCE).mapColor(MapColor.COLOR_GREEN)));
    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    public static <T extends Block> DeferredBlock<T> registerBlockNoItem(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        return toReturn;
    }
    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block)
    {
        return NMItemInit.ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties()));
    }
}
