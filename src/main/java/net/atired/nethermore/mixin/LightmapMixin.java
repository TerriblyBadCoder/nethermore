package net.atired.nethermore.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.atired.nethermore.client.NethermoreClient;
import net.atired.nethermore.init.NMBiomeInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightTexture.class)
public class LightmapMixin {
    @WrapOperation(method = "updateLightTexture",at= @At(value = "INVOKE",ordinal = 2, target = "Lorg/joml/Vector3f;mul(F)Lorg/joml/Vector3f;"))
    private Vector3f awesomeLightmapBlue(Vector3f instance, float scalar, Operation<Vector3f> original){
        if(NethermoreClient.PROXY.blueNess>0.01f){
            Vector3f olLight = instance.mul(new Vector3f(1,1,1),new Vector3f());
            if(instance.x+instance.y+instance.z>=3.0){
                return original.call(instance,scalar);
            }
            float olZ=instance.z;
            instance.z=instance.x;
            instance.x=olZ;
            instance.x*=instance.x*instance.x;
//            instance.x*=instance.x*instance.x;
//            instance.y*=instance.y;
            instance.y*=instance.y;
            float lightA = NethermoreClient.PROXY.lightNess;
            float lightB = (float)Math.pow(lightA,0.5f);
            instance.x*=1.0f+lightB;
            instance.x=Math.min(instance.x+lightB/2.0f,1.0f);
            instance.y*=1.0f+lightB;
            instance.y=Math.min(instance.y+lightB/2.0f,1.0f);
            instance.z*=1.0f+lightA/3.0f;
            instance.z=Math.min(instance.z+lightA/12.0f,1.0f);
            instance.x= Mth.lerp(NethermoreClient.PROXY.blueNess,olLight.x,instance.x);
            instance.y= Mth.lerp(NethermoreClient.PROXY.blueNess,olLight.y,instance.y);
            instance.z= Mth.lerp(NethermoreClient.PROXY.blueNess,olLight.z,instance.z);
        }
        if(NethermoreClient.PROXY.nihiloNess>0.01f) {
            Vector3f olLight = instance.mul(new Vector3f(1, 1, 1), new Vector3f());
            float median = (olLight.x+olLight.y+olLight.z)/3.0f;
            median=(float)Math.pow(median,0.5);
            median=Math.min(1.0f,median*1.33f);
            instance.x= Mth.lerp(NethermoreClient.PROXY.nihiloNess,instance.x,median);
//            float reddened = Math.min(1.0f,NethermoreClient.PROXY.redNess*3.0f);
//            if(NethermoreClient.PROXY.redNess>0.8f){
//                reddened=Math.clamp(1.0f-NethermoreClient.PROXY.redNess,0.0f,0.2f)*6.0f;
//            }
//            median=(float)Math.pow(median,1.0+reddened);
            instance.y= Mth.lerp(NethermoreClient.PROXY.nihiloNess,instance.y,median);
            instance.z= Mth.lerp(NethermoreClient.PROXY.nihiloNess,instance.z,median);
        }
        return original.call(instance,scalar);
    }
}
