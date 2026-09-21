package net.atired.nethermore.init;

import net.atired.nethermore.Nethermore;
import net.atired.nethermore.worldgen.structures.HauntedCragsStructure;
import net.atired.nethermore.worldgen.structures.ScrambledPitsStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NMStructureInit {
    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Nethermore.MODID);
    public static final DeferredHolder<StructureType<?>,StructureType<?>> HAUNTED_CRAGS = STRUCTURES.register("haunted_crags",() ->  () ->  HauntedCragsStructure.CODEC);
    public static final DeferredHolder<StructureType<?>,StructureType<?>> SCRAMBLED_PITS = STRUCTURES.register("scrambled_pits",() ->  () ->  ScrambledPitsStructure.CODEC);

}
