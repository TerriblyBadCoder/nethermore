package net.atired.nethermore.mixin;

import net.atired.nethermore.accessors.RednessServerLevelAccessor;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.init.NMAchievements;
import net.atired.nethermore.init.NMBiomeInit;
import net.atired.nethermore.init.NMSoundInit;
import net.atired.nethermore.networking.payloads.RednessPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements RednessServerLevelAccessor {
    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Shadow public abstract List<ServerPlayer> players();

    private int nm$redCD=100;
    private float nm$redness=0.0f;
    @Inject(method = "tick",at=@At("HEAD"))
    private void nmTick(BooleanSupplier hasTimeLeft, CallbackInfo ci){
        this.nm$redness*=0.95f;
        if(this.nm$redness<0.01f)this.nm$redness=0.0f;
        if( nm$redCD<=0){
            if(Math.random()>0.9){
                nm$redness=0.99f;
                nm$redCD=400+(int)(Math.random()*17);
                if(!players().isEmpty()){
                    for(ServerPlayer serverPlayer : players()){
                        if(getBiome(serverPlayer.getOnPos()).is(NMBiomeInit.SCRAMBLED_PITS)){
                            NMAchievements.HEARTY.get().trigger(serverPlayer);
                        }
                    }
                    PacketDistributor.sendToPlayersInDimension((ServerLevel)(Object)this,new RednessPayload(1));
                }
            }
        }else{
            nm$redCD-=1;
        }
    }

    @Override
    public float nethermore$getRed() {
        return nm$redness;
    }
}
