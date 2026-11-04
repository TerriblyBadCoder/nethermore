package net.atired.nethermore.init;

import com.mojang.serialization.Codec;
import net.atired.nethermore.Nethermore;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NMItemComponentInit {

    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.
            createDataComponents(Registries.DATA_COMPONENT_TYPE, Nethermore.MODID);
    public static final Supplier<DataComponentType<Boolean>> SALTED = DATA_COMPONENT_TYPES.registerComponentType("salted", (builder) ->
        builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final Supplier<DataComponentType<PotionContents>> INFUSED = DATA_COMPONENT_TYPES.registerComponentType("infused", (p_341836_) -> {
        return p_341836_.persistent(PotionContents.CODEC).networkSynchronized(PotionContents.STREAM_CODEC).cacheEncoding();
    });
}
