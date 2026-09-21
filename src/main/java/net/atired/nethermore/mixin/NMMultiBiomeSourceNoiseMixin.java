package net.atired.nethermore.mixin;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(value = {MultiNoiseBiomeSource.class},priority = -30000)
public abstract class NMMultiBiomeSourceNoiseMixin implements BiomeSourceAccessor {

    @Shadow public abstract Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler);

    boolean doNext=false;

    @Inject(method = "getNoiseBiome(IIILnet/minecraft/world/level/biome/Climate$Sampler;)Lnet/minecraft/core/Holder;",at = @At(value = "HEAD"),cancellable = true)
    private void test1(int x, int y, int z, Climate.Sampler sampler, CallbackInfoReturnable<Holder<Biome>> cir){

        if(doNext){
            
            doNext=false;
            boolean should = false;

            should = NMsimplexNoise.sampleNoise2D(x,z,2000)>0.0;


            if(new Vec3(x,0,z).length()>500 && should){
                Holder<Biome> biomeHolder = getNoiseBiome(x,y,z,sampler);
                doNext=false;
                Holder<Biome> biomeHolder2 = getNoiseBiome(x,y+2,z,sampler);
                if(biomeHolder.is(Biomes.BASALT_DELTAS))
                    cir.setReturnValue(map.get(NMBiomeInit.HAUNTED_CRAGS));
                if(biomeHolder.is(Biomes.SOUL_SAND_VALLEY)||biomeHolder2.is(Biomes.SOUL_SAND_VALLEY))
                    cir.setReturnValue(map.get(NMBiomeInit.SCRAMBLED_PITS));
            }
        }else{
            doNext=true;
        }
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
