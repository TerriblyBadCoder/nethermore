package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NMCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Nethermore.MODID);

    public static final Supplier<CreativeModeTab> MAIN = TABS.register("nethermore", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(NMItemInit.MORBID_PIECE.get()))
                    .title(Component.translatable("creativetab.nethermore.nethermore"))
                    .displayItems((parameters, output) -> {
                        output.accept(NMItemInit.MORBID_PIECE);
                        output.accept(NMBlockInit.TUMOR_BLOCK);
                        output.accept(NMBlockInit.EYE_TUMOR_BLOCK);
                        output.accept(NMBlockInit.FAT_TUMOR_BLOCK);
                        output.accept(NMBlockInit.MAW_TUMOR_BLOCK);
                        output.accept(NMItemInit.TAR_GLOB);
                        output.accept(NMItemInit.TAR_BUCKET);
                        output.accept(NMBlockInit.SOFT_TAR_BLOCK);
                        output.accept(NMBlockInit.FUSED_TAR_BLOCK);
                        output.accept(NMBlockInit.COLD_FUSED_TAR_BLOCK);
                        output.accept(NMBlockInit.TRASH_BAG);
                        output.accept(NMItemInit.ASH_BALL);
                        output.accept(NMBlockInit.BLUE_ASH);
                        output.accept(NMBlockInit.BLUE_ASH_BLOCK);
                        output.accept(NMBlockInit.BLUE_SLAG_BLOCK);
                        output.accept(NMBlockInit.BLUE_ASH_SHINGLES);
                        output.accept(NMBlockInit.SOUL_CRYSTAL_BLOCK);
                        output.accept(NMBlockInit.SOUL_LINING_BLOCK);
                        output.accept(NMBlockInit.BILESTONE);
                        output.accept(NMBlockInit.TOPPED_BILESTONE);
                        output.accept(NMBlockInit.BILE_GROWTH);
                        output.accept(NMBlockInit.INFLAMED_EYE);
                        output.accept(NMBlockInit.BILE_BRICKS);
                        output.accept(NMBlockInit.IRON_RAFTER);
                        output.accept(NMBlockInit.SYRINGE);
                        output.accept(NMBlockInit.SLIVERS_BLOCK);
                        output.accept(NMBlockInit.WHISPERING_THORN_BLOCK);
                        output.accept(NMBlockInit.WHISPERING_PLANKS);
                        output.accept(NMBlockInit.WHISPERING_SLAB);
                        output.accept(NMBlockInit.WHISPERING_STAIRS);
                        output.accept(NMBlockInit.WHISPERING_FENCE);
                        output.accept(NMBlockInit.WHISPERING_TRAPDOOR);
                        output.accept(NMItemInit.DISGUSTLING_SPAWN_EGG.get());
                        output.accept(NMItemInit.MORBID_PIGLIN_SPAWN_EGG.get());

                        output.accept(NMItemInit.SLITHER_SPAWN_EGG.get());
                        output.accept(NMItemInit.TARLING_SPAWN_EGG.get());

                        output.accept(NMItemInit.UNPHEASANT_SPAWN_EGG.get());

                        output.accept(NMItemInit.NOO_SPAWN_EGG.get());
                        output.accept(NMItemInit.PYLON_SPAWN_EGG.get());
                        output.accept(NMItemInit.EGO_SPAWN_EGG.get());

                        output.accept(NMItemInit.ONLOOKER_SPAWN_EGG.get());
                        output.accept(NMItemInit.BEHOLDER_SPAWN_EGG.get());
                        
                    })
                    .build());

    
}

