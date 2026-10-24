package net.atired.nethermore.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2i;

import java.util.ArrayList;

public class CreepingIvyFeature extends Feature<NoneFeatureConfiguration> {
    public CreepingIvyFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        if(Math.random()>0.5||Math.abs(NMsimplexNoise.sampleNoise3D(pos.getX(),pos.getY()/20.0f+300,pos.getZ()+200,80.0f))<-0.3f){
            return false;
        }

        ArrayList<Vector2i> vector2is = new ArrayList<>();
        Vec3 vec3 =new Vec3(0,0,0);
        Vec3 dir = new Vec3(0.5,0,0).yRot((float)(Math.random()*3.14f*4.0f));
        vec3=vec3.add(dir.scale(-5));
        for (int i = -5; i < 5; i++) {
            BlockPos pos1=pos.offset(new Vec3i((int)vec3.x,(int)vec3.y,(int)vec3.z));
            if(!vector2is.contains(new Vector2i(pos1.getX(),pos1.getZ()))){
                boolean skip=true;
                for (int j = 0; j < 6; j++) {
                    if(level.getBlockState(pos1).isEmpty()&&!level.getBlockState(pos1.above()).isEmpty()){
                        skip=false;
                        break;
                    }else{
                        pos1=pos1.above();
                    }
                }
                vector2is.add(new Vector2i(pos1.getX(),pos1.getZ()));
                if(skip)continue;
                int count = (int)((Math.random()*5.0+3)/(1+Math.abs(i)/3.0))*2-1;
                for (int j = 0; j < count; j++) {
                    if(!level.getBlockState(pos1).isEmpty()){
                        level.setBlock(pos1.above(), NMBlockInit.CREEPING_IV.get().defaultBlockState().setValue(NMBlockInit.FRUITING,Math.random()>0.87),2);
                        break;
                    }
                    if(j==count-1){
                        level.setBlock(pos1, NMBlockInit.CREEPING_IV.get().defaultBlockState().setValue(NMBlockInit.FRUITING,Math.random()>0.87),2);
                    }else{
                        level.setBlock(pos1, NMBlockInit.CREEPING_IV_PLANT.get().defaultBlockState(),2);
                    }
                    pos1=pos1.below();
                }
            }
            vec3=vec3.add(dir);
        }
        return false;
    }
}
