package net.atired.nethermore.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class NMTagInit {
    public static final TagKey<Item> SOUL_SHARDS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("nethermore", "soul_shards")
    );
}
