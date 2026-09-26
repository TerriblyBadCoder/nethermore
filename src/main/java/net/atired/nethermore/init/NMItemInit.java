package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NMItemInit {
    public static final DeferredRegister.Items ITEMS =DeferredRegister.createItems(Nethermore.MODID);
    public static final FoodProperties MORBID_FOOD = (new FoodProperties.Builder()).nutrition(1).alwaysEdible().saturationModifier(0.1F)
            .effect(()->{return new MobEffectInstance(NMMobEffectInit.MORBID,400,0);}, 1.0f)
            .effect(()->{return new MobEffectInstance(MobEffects.POISON,20,0);}, 1.0f).fast().build();
    public static final DeferredItem<Item> TAR_BUCKET = ITEMS.register(
            "tar_bucket",
            ()->new BucketItem(NMFluidInit.TAR_FLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final DeferredItem<Item> TAR_GLOB = ITEMS.register(
            "tar_glob",
            ()->new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> TARLING_SPAWN_EGG = ITEMS.register(
            "tarling_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.TARLING.get(),0x292127,0xa44d00,new Item.Properties())
    );
    public static final DeferredItem<Item> BEHOLDER_SPAWN_EGG = ITEMS.register(
            "beholder_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.BEHOLDER.get(),0xa0c46e,0xbb3245,new Item.Properties())
    );
    public static final DeferredItem<Item> ONLOOKER_SPAWN_EGG = ITEMS.register(
            "onlooker_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.ONLOOKER.get(),0xffeeb7,0xbb3245,new Item.Properties())
    );
    public static final DeferredItem<Item> UNPHEASANT_SPAWN_EGG = ITEMS.register(
            "unpheasant_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.UNPHEASANT.get(),0xadabad,0x5b4650,new Item.Properties())
    );
    public static final DeferredItem<Item> DISGUSTLING_SPAWN_EGG = ITEMS.register(
            "disgustling_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.DISGUSTLING.get(),0x35423c,0x938c93,new Item.Properties())
    );
    public static final DeferredItem<Item> MORBID_PIGLIN_SPAWN_EGG = ITEMS.register(
            "morbid_piglin_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.MORBID_PIGLIN.get(),0xbd9f9c,0x3a4a40,new Item.Properties())
    );
    public static final DeferredItem<Item> SLITHER_SPAWN_EGG = ITEMS.register(
            "slither_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.SLITHER.get(),0xa44d00,0x402b39,new Item.Properties())
    );
    public static final DeferredItem<Item> NOO_SPAWN_EGG = ITEMS.register(
            "noo_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.NOO.get(),0x13afb3,0x9fedef,new Item.Properties())
    );
    public static final DeferredItem<Item> EGO_SPAWN_EGG = ITEMS.register(
            "ego_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.EGO.get(),0x905767,0x7cf2f5,new Item.Properties())
    );
    public static final DeferredItem<Item> PYLON_SPAWN_EGG = ITEMS.register(
            "pylon_spawn_egg",
            ()->new SpawnEggItem(NMEntityInit.PYLON.get(),0x52dcff,0xdae3e3,new Item.Properties())
    );

    public static final DeferredItem<Item> ASH_BALL = ITEMS.register(
            "ash_ball",
            ()->new Item(new Item.Properties())
    );
//    public static final DeferredItem<Item> WAILING_SCEPTRE = ITEMS.register(
//            "wailing_sceptre",
//            ()->new Item(new Item.Properties())
//    );
//    public static final DeferredItem<Item> MASK = ITEMS.register(
//            "mask",
//            ()->new Item(new Item.Properties())
//    );
//    public static final DeferredItem<Item> CIGAR = ITEMS.register(
//            "cigar",
//            ()->new Item(new Item.Properties())
//    );
    public static final DeferredItem<Item> MORBID_PIECE = ITEMS.register(
            "morbid_piece",
            ()->new Item(new Item.Properties().food(MORBID_FOOD))
    );

}
