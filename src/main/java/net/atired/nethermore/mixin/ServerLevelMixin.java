package net.atired.nethermore.mixin;

import net.atired.nethermore.accessors.RednessServerLevelAccessor;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.init.NMSoundInit;
import net.atired.nethermore.networking.payloads.RednessPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements RednessServerLevelAccessor {
    @Shadow public abstract List<ServerPlayer> players();

    private int nm$redCD=100;
    private float nm$redness=0.0f;
    @Inject(method = "tick",at=@At("HEAD"))
    private void nmTick(BooleanSupplier hasTimeLeft, CallbackInfo ci){
        this.nm$redness*=0.9f;
        if(this.nm$redness<0.01f)this.nm$redness=0.0f;
        if( nm$redCD<=0){
            if(Math.random()>0.9){
                nm$redness=0.99f;
                nm$redCD=50+(int)(Math.random()*17);
                if(!players().isEmpty()){
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
