package net.atired.nethermore.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.atired.nethermore.accessors.OffsetFunctionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockBehaviour.Properties.class)
public class NMBlockBehaviourPropertiesMixin implements OffsetFunctionAccessor {


    private BlockBehaviour.OffsetFunction offsetFunction2;

    @Override
    public BlockBehaviour.@org.jetbrains.annotations.Nullable OffsetFunction nethermore$getOffsetFunction() {
        return this.offsetFunction2;
    }

    @Override
    public void nethermore$setOffsetFunction(BlockBehaviour.OffsetFunction func) {
        this.offsetFunction2=func;
    }

}
