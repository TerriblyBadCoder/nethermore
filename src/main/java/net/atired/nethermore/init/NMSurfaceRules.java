package net.atired.nethermore.init;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

import static net.minecraft.world.level.levelgen.SurfaceRules.state;

public class NMSurfaceRules {

    private static SurfaceRules.RuleSource block(Block block) {
        return state(block.defaultBlockState());
    }
    public static SurfaceRules.RuleSource nether() {
        SurfaceRules.ConditionSource surfacerules$conditionsource2 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(30), 0);
        SurfaceRules.ConditionSource surfacerules$conditionsource3 = SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(35), 0));
        SurfaceRules.ConditionSource surfacerules$conditionsource8 = SurfaceRules.noiseCondition(Noises.PATCH, -0.012);
        SurfaceRules.ConditionSource surfacerules$conditionsource11 = SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.0);
        SurfaceRules.RuleSource surfacerules$rulesource = SurfaceRules.ifTrue(
                surfacerules$conditionsource8, SurfaceRules.ifTrue(surfacerules$conditionsource2, SurfaceRules.ifTrue(surfacerules$conditionsource3, block(Blocks.GRAVEL)))
        );

        SurfaceRules.RuleSource scrambledPits = SurfaceRules.ifTrue(
                SurfaceRules.isBiome(NMBiomeInit.SCRAMBLED_PITS),
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), block(Blocks.BEDROCK))
                        ,SurfaceRules.ifTrue(
                                SurfaceRules.UNDER_CEILING, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource11, block(Blocks.SOUL_SAND)), block(Blocks.SOUL_SOIL))
                        ),
                        SurfaceRules.ifTrue(
                                SurfaceRules.UNDER_FLOOR,
                                SurfaceRules.sequence(surfacerules$rulesource, SurfaceRules.ifTrue(surfacerules$conditionsource11, block(Blocks.SOUL_SAND)), block(Blocks.SOUL_SOIL))
                        )
                )
        );
        System.out.println("I AM LOSING IT!");
        return scrambledPits;
    }
}
