package net.atired.nethermore.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.atired.nethermore.init.NMBiomeInit;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SurfaceRuleData.class)
public class SurfaceRuleDataMixin {

//    @Shadow @Final private static SurfaceRules.RuleSource SOUL_SAND;
//
//    @Shadow @Final private static SurfaceRules.RuleSource SOUL_SOIL;
//
//    @Shadow @Final private static SurfaceRules.RuleSource GRAVEL;
//
//    @ModifyArgs(method = "Lnet/minecraft/data/worldgen/SurfaceRuleData;nether()Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;",at= @At(value = "INVOKE",ordinal = 1, target = "Lnet/minecraft/world/level/levelgen/SurfaceRules;isBiome([Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/level/levelgen/SurfaceRules$ConditionSource;"))
//    private static void modifySurfaceRule(Args args){
//        ResourceKey[] keys = new ResourceKey[args.size()+1];
//        int count = 0;
//        if(args.get(0) instanceof ResourceKey[] resourceKeys){
//            for (ResourceKey<Biome> biomeResourceKey : resourceKeys){
//                keys[count]=biomeResourceKey;
//                count+=1;
//            }
//        }
//        keys[count]=NMBiomeInit.SCRAMBLED_PITS;
//        System.out.println(keys + " bbb");
//
//        args.set(0,keys);
//    }
//    @ModifyReturnValue(method = "Lnet/minecraft/data/worldgen/SurfaceRuleData;nether()Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;",at= @At(value = "RETURN"))
//    private static SurfaceRules.RuleSource modifyReturn(SurfaceRules.RuleSource original){
//        SurfaceRules.ConditionSource surfacerules$conditionsource2 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(30), 0);
//        SurfaceRules.ConditionSource surfacerules$conditionsource3 = SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(35), 0));
//        SurfaceRules.ConditionSource surfacerules$conditionsource8 = SurfaceRules.noiseCondition(Noises.PATCH, -0.012);
//        SurfaceRules.ConditionSource surfacerules$conditionsource11 = SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.0);
//        SurfaceRules.RuleSource surfacerules$rulesource = SurfaceRules.ifTrue(
//                surfacerules$conditionsource8, SurfaceRules.ifTrue(surfacerules$conditionsource2, SurfaceRules.ifTrue(surfacerules$conditionsource3, GRAVEL))
//        );
//        SurfaceRules.RuleSource scrambledPits = SurfaceRules.ifTrue(
//                SurfaceRules.isBiome(NMBiomeInit.SCRAMBLED_PITS),
//                SurfaceRules.sequence(
//                        SurfaceRules.ifTrue(
//                                SurfaceRules.UNDER_CEILING, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, SOUL_SAND), SOUL_SOIL)
//                        ),
//                        SurfaceRules.ifTrue(
//                                SurfaceRules.UNDER_FLOOR,
//                                SurfaceRules.sequence(surfacerules$rulesource, SurfaceRules.ifTrue(surfacerules$conditionsource11, SOUL_SAND), SOUL_SOIL)
//                        )
//                )
//        );
//        System.out.println("I AM LOSING IT!");
//        return scrambledPits;
//    }
}
