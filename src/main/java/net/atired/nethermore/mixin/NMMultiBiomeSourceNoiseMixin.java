package net.atired.nethermore.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.atired.nethermore.accessors.BiomeSourceAccessor;
import net.atired.nethermore.init.NMBiomeInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = {MultiNoiseBiomeSource.class},priority = -30000)
public abstract class NMMultiBiomeSourceNoiseMixin implements BiomeSourceAccessor {

    @Shadow public abstract Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler);

    boolean doNext=false;
    @WrapMethod(method = "getNoiseBiome(IIILnet/minecraft/world/level/biome/Climate$Sampler;)Lnet/minecraft/core/Holder;")
    private Holder<Biome> test1(int x, int y, int z, Climate.Sampler sampler, Operation<Holder<Biome>> original){
        Holder<Biome> called = original.call(x,y,z,sampler);
        boolean should = false;
        should = NMsimplexNoise.sampleNoise2D(x,z,2000)>0.0;
        if(new Vec3(x,0,z).length()>500 && should){
            if(called.is(Biomes.BASALT_DELTAS))
                return map.get(NMBiomeInit.HAUNTED_CRAGS);
            if(called.is(Biomes.SOUL_SAND_VALLEY))
                return map.get(NMBiomeInit.SCRAMBLED_PITS);
        } else if(new Vec3(x,0,z).length()<=492){
            if(called.is(NMBiomeInit.HAUNTED_CRAGS))
                return map.get(Biomes.BASALT_DELTAS);
            if(called.is(NMBiomeInit.SCRAMBLED_PITS))
                return map.get(Biomes.SOUL_SAND_VALLEY);
        }
        return called;
    }
//    @Inject(method = "getNoiseBiome(IIILnet/minecraft/world/level/biome/Climate$Sampler;)Lnet/minecraft/core/Holder;",at = @At(value = "TAIL"),cancellable = true)
//    private void test2(int x, int y, int z, Climate.Sampler sampler, CallbackInfoReturnable<Holder<Biome>> cir){
//    }

    private Map<ResourceKey<Biome>, Holder<Biome>> map = new HashMap<>();

    @Override
    public void setResourceKeyMap(Map<ResourceKey<Biome>, Holder<Biome>> map) {
        this.map = map;
    }

    @Override
    public Map<ResourceKey<Biome>, Holder<Biome>> getResourceKeyMap() {
        return map;
    }
    private boolean expanded=false;

}
