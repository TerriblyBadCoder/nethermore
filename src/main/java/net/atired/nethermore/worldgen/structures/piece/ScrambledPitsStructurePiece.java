package net.atired.nethermore.worldgen.structures.piece;


import net.atired.nethermore.init.NMBiomeInit;
import net.atired.nethermore.init.NMBlockInit;
import net.atired.nethermore.init.NMStructurePieceInit;
import net.atired.nethermore.misc.NMsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ScrambledPitsStructurePiece extends AbstractCaveGenerationStructurePiece{
    private BlockPos centric = holeCenter;
    public ScrambledPitsStructurePiece(BlockPos chunkCorner, BlockPos holeCenter, int bowlHeight, int bowlRadius) {
        super(NMStructurePieceInit.SCRAMBLED_PITS.get(), chunkCorner, holeCenter, bowlHeight, bowlRadius);
    }

    public ScrambledPitsStructurePiece(CompoundTag tag) {
        super(NMStructurePieceInit.SCRAMBLED_PITS.get(), tag);
    }

    public ScrambledPitsStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
        this(tag);
    }

    public void postProcess(WorldGenLevel level, StructureManager featureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        int cornerX = this.chunkCorner.getX();
        int cornerY = this.chunkCorner.getY();
        int cornerZ = this.chunkCorner.getZ();
        BlockPos.MutableBlockPos carve = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos carveBelow = new BlockPos.MutableBlockPos();
        carve.set(cornerX, cornerY, cornerZ);

        boolean didStuff= new Vec3(cornerX, cornerY, cornerZ).distanceTo(this.holeCenter.getCenter()) < 120;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                MutableBoolean doFloor = new MutableBoolean(false);
                for (int y = 15; y >= 0; y--) {
                    Vec3 dir =new Vec3(cornerX+x,cornerY+y,cornerZ+z).vectorTo(this.holeCenter.getCenter()).normalize();
                    float sinused =(float)Math.cos(Math.acos(dir.y)*6.0f);
                    float cosinused = (float)Math.sin(Math.atan2(dir.x,dir.z)*12.0f+(cornerY+y)/12.0f)*(float)dir.multiply(1,0.6,1).length();
                    carve.set(cornerX + x, Mth.clamp(cornerY + y, level.getMinBuildHeight(), level.getMaxBuildHeight()), cornerZ + z);
                    if(carve.getY()<level.dimensionType().logicalHeight()){
                        int circled = inCircle(carve, level,sinused,cosinused);
                        if (circled>0&&(!level.getBlockState(carve).isEmpty()||circled==3)) {
                            if(circled==1)
                            {
                                checkedSetBlock(level, carve, Blocks.CAVE_AIR.defaultBlockState());
                            }
                            else if(circled==3){
                                boolean noisy = NMsimplexNoise.sampleNoise3D(cornerX+x,cornerY+y,cornerZ+z,15)%0.2>0.1;
                                checkedSetBlock(level, carve, (noisy?NMBlockInit.SOUL_LINING_BLOCK.get():NMBlockInit.SOUL_CRYSTAL_BLOCK.get()).defaultBlockState());
                            }
                            else{
                               checkedSetBlock(level, carve, Blocks.SOUL_SAND.defaultBlockState());
                            }
                            didStuff=true;
                            //surroundCornerOfLiquid(level, carve);
                            carveBelow.set(carve.getX(), carve.getY() - 1, carve.getZ());
//                            if(carve.getY()%5==0)
//                                doFloor.setTrue();
                        }
//                        else if (doFloor.isTrue()) {
//                            break;
//                        }
                    }


                }
//                if (doFloor.isTrue() && !checkedGetBlock(level, carveBelow).isAir()&&checkedGetBlock(level, carve).is(Blocks.NETHERRACK)) {
//                    float noisy = NMsimplexNoise.sampleNoise3D(carveBelow.getX(),(carveBelow.getY()-carveBelow.getY()%5)*26,carveBelow.getZ(),55);
//                    boolean doesNoisy = (Math.abs(noisy)%0.2f)>0.09f;
//                    checkedSetBlock(level, carveBelow,doesNoisy?Blocks.CRIMSON_NYLIUM.defaultBlockState():Blocks.WARPED_NYLIUM.defaultBlockState());
//                    if(Math.random()>0.80f){
//                        performBonemeal(level,random,carveBelow,Blocks.CRIMSON_NYLIUM.defaultBlockState(),chunkGen);
//                    }
//                    doFloor.setFalse();
//                }
            }
        }
        carve.set(cornerX+1, cornerY+1, cornerZ+1);
        if(didStuff){
            Holder<Biome> biomeHolder = level.getBiome(carve);
            if(biomeHolder.is(Biomes.SOUL_SAND_VALLEY))
                replaceBiomes(level, NMBiomeInit.SCRAMBLED_PITS);
        }
    }
    public static void performBonemeal(WorldGenLevel level, RandomSource random, BlockPos pos, BlockState state,ChunkGenerator chunkgenerator) {
        BlockState blockstate = level.getBlockState(pos);
        BlockPos blockpos = pos.above();
        Registry<ConfiguredFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
        if (blockstate.is(Blocks.CRIMSON_NYLIUM)) {
            place(registry, NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL, level, chunkgenerator, random, blockpos);
        } else if (blockstate.is(Blocks.WARPED_NYLIUM)) {
            place(registry, NetherFeatures.WARPED_FOREST_VEGETATION_BONEMEAL, level, chunkgenerator, random, blockpos);
            place(registry, NetherFeatures.NETHER_SPROUTS_BONEMEAL, level, chunkgenerator, random, blockpos);
            if (random.nextInt(8) == 0) {
               place(registry, NetherFeatures.TWISTING_VINES_BONEMEAL, level, chunkgenerator, random, blockpos);
            }
        }

    }
    private static void place(Registry<ConfiguredFeature<?, ?>> featureRegistry, ResourceKey<ConfiguredFeature<?, ?>> featureKey, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
        featureRegistry.getHolder(featureKey).ifPresent((p_255920_) -> {
            ((ConfiguredFeature)p_255920_.value()).place(level, chunkGenerator, random, pos);
        });
    }
    private void surroundCornerOfLiquid(WorldGenLevel level, BlockPos.MutableBlockPos center) {
        BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            offset.set(center);
            offset.move(dir);
            BlockState state = checkedGetBlock(level, offset);
            if (!state.getFluidState().isEmpty()) {
                checkedSetBlock(level, offset, Blocks.BASALT.defaultBlockState());
            }
        }
    }
    private int inCircle(BlockPos.MutableBlockPos carve, WorldGenLevel level,float sin,float cos) {
        int otherPillarNoise = 1;
        int logical = level.dimensionType().logicalHeight();
        int x = (carve.getX());
        int y = (carve.getY());
        int noiseY = (carve.getY());
        float yOff=(carve.getY()%5)/15.5f-0.15f;
        int z = (carve.getZ());
        BlockPos pos = new BlockPos(x,y,z);
        double distToCenter = Math.pow(pos.distToLowCornerSqr(this.holeCenter.getX(), this.holeCenter.getY(), this.holeCenter.getZ()),0.5);
//        float verticalNoise = (NMsimplexNoise.sampleNoise2D(carve.getX(), carve.getZ(), 50) + 1.0F) * 0.2F - (NMsimplexNoise.smin(NMsimplexNoise.sampleNoise2D(carve.getX(), carve.getZ(), 24), -0.5F, 0.1F) + 0.5F) * 0.3F*0.2f*(float)Math.pow(distToCenter,0.5);
//        float rawHeight = (float) Math.pow( Math.abs(this.holeCenter.getY() - carve.getY()) / (float) (height*2 * 0.5F),0.5f);
        float pillarNoise = (NMsimplexNoise.sampleNoise3D(x, (int) (noiseY * 7F+z+x), z, 10)*2.5f + 3.0F)*0.5f;
//        float holenoise = (NMsimplexNoise.sampleNoise3D(x*3, (int) (noiseY*20+z*3), z*3, 54) + 1.0F)*0.5f;
//        pillarNoise/= (float) (1.0f+(float)Math.min(12.0f,distToCenter)/12.0f*(1.5f-Math.clamp(logical-y,0.0f,5f)/3.333f));
//        boolean returnal = (pillarNoise > (0.9)*(distToCenter/50-0.1)+yOff || holenoise > (0.7+yOff*0.8)) && distToCenter-5 < 50+canyonStep(carve.getY(),5)*5 && rawHeight < 1 - verticalNoise;
        double hordistToCenter = Math.pow(pos.distToLowCornerSqr(this.holeCenter.getX(), pos.getY(), this.holeCenter.getZ()),0.5);

        float absed = Math.max(0.0f,this.holeCenter.getY()-y);
        float nonabsed = Math.max(0.0f,-this.holeCenter.getY()+y);
        boolean returnal = (2.0f+sin/16.0f+cos*1.2f)*distToCenter<56f-absed*5.0;
        boolean returnal2 = pillarNoise*(hordistToCenter+10-Math.pow(nonabsed+12,0.5)*3.0)<1f-absed*5.0;
        boolean returnal3 = (2.0f+sin/16.0f+cos*0.3f)*distToCenter<56f-absed*5.0;
        return returnal2?3:(returnal?1:(returnal3?2:0));
    }
    private float getHeightOf(BlockPos.MutableBlockPos carve) {
        int halfHeight = this.height / 2*2;
        if (carve.getY() > this.holeCenter.getY() + halfHeight + 1 || carve.getY() < this.holeCenter.getY() - halfHeight) {
            return 0.0F;
        } else {
            return 1F - ((this.holeCenter.getY() + halfHeight - carve.getY()) / (float) (height * 2*2));
        }
    }

    private float canyonStep(float heightScale, int scaleTo) {
        int clampTo100 = (int) ((heightScale) * scaleTo * scaleTo);
        return Mth.clamp((float) (Math.round(clampTo100 / (float) scaleTo)) / (float) scaleTo, 0F, 1F);
    }


}
