package net.atired.nethermore.mixin;

import net.atired.nethermore.entity.OnlookerEntity;
import net.atired.nethermore.init.NMBiomeInit;
import net.atired.nethermore.init.NMEntityInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeleton.class)
public abstract class SkeletonEntityMixin extends LivingEntity {
    protected SkeletonEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "finalizeSpawn",at=@At("RETURN"))
    private void finaliseEyes(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir){
        if(level.getBiome(getOnPos()).is(NMBiomeInit.SCRAMBLED_PITS)&&level instanceof ServerLevel serverLevel){
            for (int i = 0; i < 2; i++) {
                OnlookerEntity onlooker = new OnlookerEntity(NMEntityInit.ONLOOKER.get(),serverLevel);
                onlooker.setPos(getEyePosition());
                onlooker.eyeOwner=(LivingEntity) (Object)this;
                serverLevel.addFreshEntity(onlooker);
            }
        }
    }
}
