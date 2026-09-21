package net.atired.nethermore.worldgen.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.atired.nethermore.init.NMStructureInit;
import net.atired.nethermore.worldgen.structures.piece.HauntedCragsStructurePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;
import java.util.function.Consumer;

public class HauntedCragsStructure extends AbstractCaveGenerationStructure{
    private static final int BOWL_WIDTH_RADIUS = 20;
    private static final int BOWL_HEIGHT_RADIUS = 30;

    public static final int BOWL_Y_CENTER = 60;

    public static final int MAX_TOTAL_STRUCTURE_RANGE = 128;
    public static final MapCodec<Structure> CODEC =simpleCodec(HauntedCragsStructure::new);



    public HauntedCragsStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public HolderSet<Biome> biomes() {
        return super.biomes();
    }

    public Optional<GenerationStub> findGenerationPoint(GenerationContext p_227636_) {
        return super.findGenerationPoint(p_227636_);
    }

//    @Override
//    protected Optional<GenerationStub> atYCaveBiomePoint(GenerationContext context, Heightmap.Types heightMap, Consumer<StructurePiecesBuilder> builderConsumer) {
//        ChunkPos $$1 = context.chunkPos();
//        int $$2 = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
//        BlockPos $$3 = new BlockPos($$1.getMinBlockX(), $$2, $$1.getMinBlockZ());
//        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
//        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
//        ChunkPos chunkpos = context.chunkPos();
//        int i = chunkpos.getMiddleBlockX();
//        int j = chunkpos.getMiddleBlockZ();
//        int k = getGenerateYHeight(context.random(), i, j);
//        return Optional.of(new GenerationStub(new BlockPos(i, k, j), builderConsumer.andThen(JigsawPlacement.addPieces(context, this.startPool, this.startJigsawName, this.maxDepth, $$3, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter).get().generator().orThrow())));
//    }

    @Override
    protected StructurePiece createPiece(BlockPos offset, BlockPos center, int heightBlocks, int widthBlocks, RandomState randomState) {
        return new HauntedCragsStructurePiece(offset, center, heightBlocks, widthBlocks);
    }

    @Override
    public int getGenerateYHeight(WorldgenRandom random, int x, int y) {
        return BOWL_Y_CENTER;
    }
    @Override
    public int getWidthChunks(){
        return 5;
    }
    @Override
    public int getWidthRadius(WorldgenRandom random) {
        return 15;
    }

    @Override
    public int getHeightRadius(WorldgenRandom random, int seaLevel) {
        return 90;
    }

    @Override
    public StructureType<?> type() {
        return NMStructureInit.HAUNTED_CRAGS.get();
    }
}
