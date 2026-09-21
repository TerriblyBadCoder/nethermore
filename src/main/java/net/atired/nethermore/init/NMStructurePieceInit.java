package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.worldgen.structures.piece.HauntedCragsStructurePiece;
import net.atired.nethermore.worldgen.structures.piece.ScrambledPitsStructurePiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NMStructurePieceInit {
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, Nethermore.MODID);
    public static final DeferredHolder<StructurePieceType,StructurePieceType> HAUNTED_CRAGS = STRUCTURE_PIECES.register("haunted_crags", () -> HauntedCragsStructurePiece::new);
    public static final DeferredHolder<StructurePieceType,StructurePieceType> SCRAMBLED_PITS = STRUCTURE_PIECES.register("scrambled_pits", () -> ScrambledPitsStructurePiece::new);

}
