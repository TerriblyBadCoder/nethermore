package net.atired.nethermore;

import com.google.common.collect.ImmutableSet;
import net.atired.nethermore.accessors.BiomeSourceAccessor;
import net.atired.nethermore.accessors.BiomeSourceExpandAccessor;
import net.atired.nethermore.datagen.NMDatapackProvider;
import net.atired.nethermore.entity.*;
import net.atired.nethermore.init.*;
import net.atired.nethermore.networking.payloads.RednessPayload;
import net.atired.nethermore.networking.payloads.VelSyncPayload;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Nethermore.MODID)
public class NethermoreEvents {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(true, new NMDatapackProvider(packOutput, lookupProvider));
    }
    private static SpawnPlacementType IN_TAR = (p_325672_, p_325673_, p_325674_) -> {
        if (p_325674_ != null && p_325672_.getWorldBorder().isWithinBounds(p_325673_)) {
            BlockPos blockpos = p_325673_.above();
            return p_325672_.getFluidState(p_325673_).getFluidType()==(NMFluidInit.TAR_FLUID_TYPE.get()) && !p_325672_.getBlockState(blockpos).isRedstoneConductor(p_325672_, blockpos);
        } else {
            return false;
        }
    };
    @SubscribeEvent
    public static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(NMEntityInit.NOO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(NMEntityInit.PYLON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(NMEntityInit.EGO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

        event.register(NMEntityInit.BEHOLDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ObserverEntity::checkObserverSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

        event.register(NMEntityInit.DISGUSTLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                DisgustlingEntity::checkTumorSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(NMEntityInit.MORBID_PIGLIN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MorbidPiglinEntity::checkTumorSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

        event.register(NMEntityInit.SLITHER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SlitherEntity::checkTarSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(NMEntityInit.TARLING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                TarlingEntity::checkTarSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

        event.register(NMEntityInit.UNPHEASANT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

    }
        @SubscribeEvent // on the mod event bus
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(NMItemInit.DISGUSTLING_SPAWN_EGG.get());
            event.accept(NMItemInit.MORBID_PIGLIN_SPAWN_EGG.get());

            event.accept(NMItemInit.SLITHER_SPAWN_EGG.get());
            event.accept(NMItemInit.TARLING_SPAWN_EGG.get());

            event.accept(NMItemInit.UNPHEASANT_SPAWN_EGG.get());

            event.accept(NMItemInit.NOO_SPAWN_EGG.get());
            event.accept(NMItemInit.PYLON_SPAWN_EGG.get());
            event.accept(NMItemInit.EGO_SPAWN_EGG.get());

            event.accept(NMItemInit.ONLOOKER_SPAWN_EGG.get());
            event.accept(NMItemInit.BEHOLDER_SPAWN_EGG.get());

        }
    }
    @SubscribeEvent
    public static void healEvent(LivingHealEvent event){
        if(event.getEntity().hasEffect(NMMobEffectInit.MORBID)){
            if(event.getEntity() instanceof Player player){
                FoodData foodData=player.getFoodData();

                if(foodData.needsFood()){
                    player.level().playSound(null
                            , player.getX(), player.getY(), player.getZ(),SoundEvents.PLAYER_BURP, SoundSource.PLAYERS,1.7f,0.4f);
                    foodData.setFoodLevel(foodData.getFoodLevel() + (int)event.getAmount());
                }else if(foodData.getSaturationLevel()>0.0f){
                    foodData.setSaturation(foodData.getSaturationLevel() + event.getAmount());
                }
            }
            event.setAmount(0);
        }
    }
    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(NMEntityInit.MORBID_PIGLIN.get(), MorbidPiglinEntity.createMorbidAttributes().build());
        event.put(NMEntityInit.UNPHEASANT.get(), UnpheasantEntity.createUnpheasantAttributes().build());
        event.put(NMEntityInit.DISGUSTLING_HEAD.get(), DisgustlingEntity.createUnpheasantAttributes().build());
        event.put(NMEntityInit.DISGUSTLING.get(), DisgustlingEntity.createUnpheasantAttributes().build());
        event.put(NMEntityInit.TARLING.get(), Monster.createMonsterAttributes().build());
        event.put(NMEntityInit.SLITHER.get(), SlitherEntity.createSlitherAttributes().build());
        event.put(NMEntityInit.NOO.get(), NooEntity.createNooAttributes().build());
        event.put(NMEntityInit.EGO_MASK.get(), EgoMaskEntity.createEgoMaskAttributes().build());
        event.put(NMEntityInit.EGO.get(), EgoEntity.createEgoAttributes().build());
        event.put(NMEntityInit.ONLOOKER.get(), OnlookerEntity.createOnlookerAttributes().build());
        event.put(NMEntityInit.BEHOLDER.get(), ObserverEntity.createObserverAttributes().build());
        event.put(NMEntityInit.PYLON.get(), PylonEntity.createPylonAttributes().build());
    }
    @SubscribeEvent // on the mod event bus
    public static void register(RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                VelSyncPayload.TYPE,
                VelSyncPayload.STREAM_CODEC,
                VelSyncPayload::handleData
        );
        registrar.playToClient(
                RednessPayload.TYPE,
                RednessPayload.STREAM_CODEC,
                RednessPayload::handleData
        );
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Pre event) {
        //later
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        RegistryAccess registryAccess = event.getServer().registryAccess();


        Registry<Biome> biomes = registryAccess.registryOrThrow(Registries.BIOME);

        Map<ResourceKey<Biome>, Holder<Biome>> biomeMap = new HashMap<>();

//        for (ResourceKey<Biome> biomeResourceKey : biomes.registryKeySet()) {
//            Optional<Holder.Reference<Biome>> holderOptional = biomes.getHolder(biomeResourceKey);
//            if(holderOptional.isPresent()){
//                biomeMap.put(biomeResourceKey,holderOptional.get());
//            }
//        }
        biomeMap.put(NMBiomeInit.HAUNTED_CRAGS,biomes.getHolder(NMBiomeInit.HAUNTED_CRAGS).get());
        biomeMap.put(NMBiomeInit.SCRAMBLED_PITS,biomes.getHolder(NMBiomeInit.SCRAMBLED_PITS).get());
        Registry<LevelStem> levelStems = registryAccess.registryOrThrow(Registries.LEVEL_STEM);

        for (ResourceKey<LevelStem> levelStemResourceKey : levelStems.registryKeySet()) {
            Optional<Holder.Reference<LevelStem>> holderOptional = levelStems.getHolder(levelStemResourceKey);
            if (holderOptional.isPresent() && holderOptional.get().value().generator().getBiomeSource() instanceof BiomeSourceAccessor expandedBiomeSource) {
                expandedBiomeSource.setResourceKeyMap(biomeMap);

                if(holderOptional.get().value().generator().getBiomeSource() instanceof BiomeSourceExpandAccessor expandAccessor&&levelStemResourceKey.equals(LevelStem.NETHER)){
                    ImmutableSet.Builder<Holder<Biome>> biomeHolders = ImmutableSet.builder();
                    biomes.getHolder(NMBiomeInit.HAUNTED_CRAGS).ifPresent(biomeHolders::add);
                    biomes.getHolder(NMBiomeInit.SCRAMBLED_PITS).ifPresent(biomeHolders::add);
                    expandAccessor.nm$appendBiomes(biomeHolders.build());
                }
            }
        }
    }

}
