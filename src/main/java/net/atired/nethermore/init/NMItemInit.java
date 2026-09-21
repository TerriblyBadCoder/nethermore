package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
    public static final DeferredItem<Item> ASH_BALL = ITEMS.register(
            "ash_ball",
            ()->new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> WAILING_SCEPTRE = ITEMS.register(
            "wailing_sceptre",
            ()->new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> MASK = ITEMS.register(
            "mask",
            ()->new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> CIGAR = ITEMS.register(
            "cigar",
            ()->new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> MORBID_PIECE = ITEMS.register(
            "morbid_piece",
            ()->new Item(new Item.Properties().food(MORBID_FOOD))
    );

}
