package net.atired.nethermore;

import com.terraformersmc.biolith.api.surface.SurfaceGeneration;
import com.terraformersmc.biolith.impl.surface.SurfaceRuleCollector;
import net.atired.nethermore.init.*;
import net.atired.nethermore.worldgen.NMFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import static net.minecraft.world.level.levelgen.SurfaceRules.state;

@Mod(Nethermore.MODID)
public class Nethermore {
    public static final String MODID = "nethermore";
    public static final Logger LOGGER = LogUtils.getLogger();



    public Nethermore(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::loadComplete);
        SurfaceRuleCollector.NETHER.addFromMods(ResourceLocation.fromNamespaceAndPath("minecraft", "rules/nether"), NMSurfaceRules.nether());

        NMItemInit.ITEMS.register(modEventBus);
        NMBlockInit.BLOCKS.register(modEventBus);
        NMFluidInit.FLUIDS.register(modEventBus);
        NMSoundInit.SOUND_EVENTS.register(modEventBus);
        NMFluidInit.FLUID_TYPES.register(modEventBus);
        NMEntityInit.ENTITIES.register(modEventBus);
        NMParticleInit.PARTICLE_TYPES.register(modEventBus);
        NMStructureInit.STRUCTURES.register(modEventBus);
        NMStructurePieceInit.STRUCTURE_PIECES.register(modEventBus);
        NMFeatures.FEATURES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    public void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(NMFluidInit::postInit);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }
    public static ResourceLocation getId(String string){
        return ResourceLocation.fromNamespaceAndPath(MODID,string);
    }

}
