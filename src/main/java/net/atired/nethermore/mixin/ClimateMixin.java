package net.atired.nethermore.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Climate;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Climate.ParameterList.class)
public class ClimateMixin<T> {

}
