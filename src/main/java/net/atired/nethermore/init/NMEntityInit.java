package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NMEntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Nethermore.MODID);
    public static final Supplier<EntityType<UnpheasantEntity>> UNPHEASANT =
            ENTITIES.register("unpheasant", () -> EntityType.Builder.of(UnpheasantEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIFIED_PIGLIN.getWidth()*1.4f, EntityType.ZOMBIFIED_PIGLIN.getHeight()/1.9f).eyeHeight(1.14F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("unpheasant"));
    public static final Supplier<EntityType<DisgustlingEntity>> DISGUSTLING =
            ENTITIES.register("disgustling", () -> EntityType.Builder.of(DisgustlingEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIFIED_PIGLIN.getWidth()*1.4f, EntityType.ZOMBIFIED_PIGLIN.getHeight()/1.9f).eyeHeight(1.14F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("disgustling"));
    public static final Supplier<EntityType<NooEntity>> NOO =
            ENTITIES.register("noo", () -> EntityType.Builder.of(NooEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIFIED_PIGLIN.getWidth()*0.4f, EntityType.ZOMBIFIED_PIGLIN.getHeight()*0.4f).eyeHeight(0.8F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("noo"));
    public static final Supplier<EntityType<EgoEntity>> EGO =
            ENTITIES.register("ego", () -> EntityType.Builder.of(EgoEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIFIED_PIGLIN.getWidth(), EntityType.ZOMBIFIED_PIGLIN.getHeight()).eyeHeight(1.7F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("ego"));

    public static final Supplier<EntityType<EgoMaskEntity>> EGO_MASK =
            ENTITIES.register("ego_mask", () -> EntityType.Builder.<EgoMaskEntity>of(EgoMaskEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.3f).eyeHeight(0.225F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("ego_mask"));
    public static final Supplier<EntityType<SoulProjEntity>> SOUL =
            ENTITIES.register("soul", () -> EntityType.Builder.<SoulProjEntity>of(SoulProjEntity::new, MobCategory.MONSTER)
                    .sized(0.45f, 0.45f).eyeHeight(0.225F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("soul"));
    public static final Supplier<EntityType<OnlookerEntity>> ONLOOKER =
            ENTITIES.register("onlooker", () -> EntityType.Builder.<OnlookerEntity>of(OnlookerEntity::new, MobCategory.MONSTER)
                    .sized(0.42f, 0.42f).eyeHeight(0.225F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("onlooker"));
    public static final Supplier<EntityType<ObserverEntity>> BEHOLDER =
            ENTITIES.register("beholder", () -> EntityType.Builder.<ObserverEntity>of(ObserverEntity::new, MobCategory.MONSTER)
                    .sized(0.72f, 0.92f).eyeHeight(0.225F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("beholder"));
    public static final Supplier<EntityType<PylonEntity>> PYLON =
            ENTITIES.register("pylon", () -> EntityType.Builder.of(PylonEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIFIED_PIGLIN.getWidth()*1.4f, EntityType.ZOMBIFIED_PIGLIN.getHeight()*0.9f).eyeHeight(0.8F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("pylon"));
    public static final Supplier<EntityType<TarlingEntity>> TARLING =
            ENTITIES.register("tarling", () -> EntityType.Builder.of(TarlingEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.SLIME.getWidth(), EntityType.SLIME.getHeight()).eyeHeight(0.8F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("tarling"));
    public static final Supplier<EntityType<DisgustlingHeadEntity>> DISGUSTLING_HEAD =
            ENTITIES.register("disgustling_head", () -> EntityType.Builder.of(DisgustlingHeadEntity::new, MobCategory.MONSTER)
                    .sized(0.55f, 0.5f).eyeHeight(0.3F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("disgustling_head"));
    public static final Supplier<EntityType<MorbidPiglinEntity>> MORBID_PIGLIN =
            ENTITIES.register("morbid_piglin", () -> EntityType.Builder.of(MorbidPiglinEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIFIED_PIGLIN.getWidth(), EntityType.ZOMBIFIED_PIGLIN.getHeight()).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("morbid_piglin"));
}
