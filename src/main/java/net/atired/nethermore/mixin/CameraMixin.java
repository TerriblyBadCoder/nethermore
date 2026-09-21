package net.atired.nethermore.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.atired.nethermore.client.NethermoreClient;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public class CameraMixin {
    @ModifyVariable(method = "setPosition(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), argsOnly = true)
    private Vec3 evilSetPos(Vec3 pos){
        float mul = NethermoreClient.PROXY.lightNess+NethermoreClient.PROXY.redNess/10.0f;
        if(mul>0.01f){
            float timed = (Minecraft.getInstance().level.getGameTime()+Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true))%2000;
            timed*=3.14f/7.0f;
            pos=pos.add(Mth.sin(timed)*mul/18.0f,Mth.cos(timed/1.5f)*mul/20.0f,-Mth.cos(timed)*mul/18.0f);
        }
        return pos;
    }
}
