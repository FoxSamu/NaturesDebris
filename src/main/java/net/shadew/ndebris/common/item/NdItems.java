package net.shadew.ndebris.common.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

import net.shadew.ndebris.common.NaturesDebris;
import net.shadew.ndebris.common.block.NdBlocks;
import net.shadew.ndebris.common.sound.NdSoundEvents;

public abstract class NdItems {

    //
    // BLOCKS
    //

    public static final BlockItem ROCK = block(NdBlocks.ROCK, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK = block(NdBlocks.DARKROCK, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE = block(NdBlocks.LIMESTONE, NdItemGroup.BUILDING);
    public static final BlockItem SUMESTONE = block(NdBlocks.SUMESTONE, NdItemGroup.BUILDING);
    public static final BlockItem DARK_SUMESTONE = block(NdBlocks.DARK_SUMESTONE, NdItemGroup.BUILDING);
    public static final BlockItem MURKY_DIRT = block(NdBlocks.MURKY_DIRT, NdItemGroup.BUILDING);
    public static final BlockItem MURKY_GRASS_BLOCK = block(NdBlocks.MURKY_GRASS_BLOCK, NdItemGroup.BUILDING);
    public static final BlockItem MURKY_COARSE_DIRT = block(NdBlocks.MURKY_COARSE_DIRT, NdItemGroup.BUILDING);
    public static final BlockItem MURKY_HUMUS = block(NdBlocks.MURKY_HUMUS, NdItemGroup.BUILDING);
    public static final BlockItem LEAFY_HUMUS = block(NdBlocks.LEAFY_HUMUS, NdItemGroup.BUILDING);
    public static final BlockItem MURKY_PODZOL = block(NdBlocks.MURKY_PODZOL, NdItemGroup.BUILDING);
    public static final BlockItem MURKY_SAND = block(NdBlocks.MURKY_SAND, NdItemGroup.BUILDING);
    public static final BlockItem MURKY_CLAY = block(NdBlocks.MURKY_CLAY, NdItemGroup.BUILDING);
    public static final BlockItem MURKY_TERRACOTTA = block(NdBlocks.MURKY_TERRACOTTA, NdItemGroup.BUILDING);
    public static final BlockItem MURKY_GRASS_PATH = block(NdBlocks.MURKY_GRASS_PATH, NdItemGroup.BUILDING);


    public static final BlockItem BLACKWOOD_LOG = block(NdBlocks.BLACKWOOD_LOG, NdItemGroup.BUILDING);
    public static final BlockItem INVER_LOG = block(NdBlocks.INVER_LOG, NdItemGroup.BUILDING);
    public static final BlockItem BLACKWOOD = block(NdBlocks.BLACKWOOD, NdItemGroup.BUILDING);
    public static final BlockItem INVER_WOOD = block(NdBlocks.INVER_WOOD, NdItemGroup.BUILDING);
    public static final BlockItem STRIPPED_BLACKWOOD_LOG = block(NdBlocks.STRIPPED_BLACKWOOD_LOG, NdItemGroup.BUILDING);
    public static final BlockItem STRIPPED_INVER_LOG = block(NdBlocks.STRIPPED_INVER_LOG, NdItemGroup.BUILDING);
    public static final BlockItem STRIPPED_BLACKWOOD = block(NdBlocks.STRIPPED_BLACKWOOD, NdItemGroup.BUILDING);
    public static final BlockItem STRIPPED_INVER_WOOD = block(NdBlocks.STRIPPED_INVER_WOOD, NdItemGroup.BUILDING);

    public static final BlockItem BLACKWOOD_PLANKS = block(NdBlocks.BLACKWOOD_PLANKS, NdItemGroup.BUILDING);
    public static final BlockItem INVER_PLANKS = block(NdBlocks.INVER_PLANKS, NdItemGroup.BUILDING);
    public static final BlockItem BLACKWOOD_SLAB = block(NdBlocks.BLACKWOOD_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem INVER_SLAB = block(NdBlocks.INVER_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem BLACKWOOD_STAIRS = block(NdBlocks.BLACKWOOD_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem INVER_STAIRS = block(NdBlocks.INVER_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem BLACKWOOD_STEP = block(NdBlocks.BLACKWOOD_STEP, NdItemGroup.BUILDING);
    public static final BlockItem INVER_STEP = block(NdBlocks.INVER_STEP, NdItemGroup.BUILDING);
    public static final BlockItem BLACKWOOD_FENCE = block(NdBlocks.BLACKWOOD_FENCE, NdItemGroup.BUILDING);
    public static final BlockItem INVER_FENCE = block(NdBlocks.INVER_FENCE, NdItemGroup.BUILDING);


    public static final BlockItem MOSSY_ROCK = block(NdBlocks.MOSSY_ROCK, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_BRICKS = block(NdBlocks.ROCK_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_BRICKS = block(NdBlocks.MOSSY_ROCK_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_BRICKS = block(NdBlocks.CRACKED_ROCK_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_TILES = block(NdBlocks.ROCK_TILES, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_TILES = block(NdBlocks.MOSSY_ROCK_TILES, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_TILES = block(NdBlocks.CRACKED_ROCK_TILES, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_ROCK = block(NdBlocks.SMOOTH_ROCK, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_ROCK = block(NdBlocks.POLISHED_ROCK, NdItemGroup.BUILDING);
    public static final BlockItem CHISELED_ROCK = block(NdBlocks.CHISELED_ROCK, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_PILLAR = block(NdBlocks.ROCK_PILLAR, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_LANTERN = block(NdBlocks.ROCK_LANTERN, NdItemGroup.BUILDING);

    public static final BlockItem ROCK_SLAB = block(NdBlocks.ROCK_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_SLAB = block(NdBlocks.MOSSY_ROCK_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_BRICKS_SLAB = block(NdBlocks.ROCK_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_BRICKS_SLAB = block(NdBlocks.MOSSY_ROCK_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_BRICKS_SLAB = block(NdBlocks.CRACKED_ROCK_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_TILES_SLAB = block(NdBlocks.ROCK_TILES_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_TILES_SLAB = block(NdBlocks.MOSSY_ROCK_TILES_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_TILES_SLAB = block(NdBlocks.CRACKED_ROCK_TILES_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_ROCK_SLAB = block(NdBlocks.SMOOTH_ROCK_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_ROCK_SLAB = block(NdBlocks.POLISHED_ROCK_SLAB, NdItemGroup.BUILDING);

    public static final BlockItem ROCK_STAIRS = block(NdBlocks.ROCK_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_STAIRS = block(NdBlocks.MOSSY_ROCK_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_BRICKS_STAIRS = block(NdBlocks.ROCK_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_BRICKS_STAIRS = block(NdBlocks.MOSSY_ROCK_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_BRICKS_STAIRS = block(NdBlocks.CRACKED_ROCK_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_TILES_STAIRS = block(NdBlocks.ROCK_TILES_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_TILES_STAIRS = block(NdBlocks.MOSSY_ROCK_TILES_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_TILES_STAIRS = block(NdBlocks.CRACKED_ROCK_TILES_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_ROCK_STAIRS = block(NdBlocks.SMOOTH_ROCK_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_ROCK_STAIRS = block(NdBlocks.POLISHED_ROCK_STAIRS, NdItemGroup.BUILDING);

    public static final BlockItem ROCK_STEP = block(NdBlocks.ROCK_STEP, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_STEP = block(NdBlocks.MOSSY_ROCK_STEP, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_BRICKS_STEP = block(NdBlocks.ROCK_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_BRICKS_STEP = block(NdBlocks.MOSSY_ROCK_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_BRICKS_STEP = block(NdBlocks.CRACKED_ROCK_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_TILES_STEP = block(NdBlocks.ROCK_TILES_STEP, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_TILES_STEP = block(NdBlocks.MOSSY_ROCK_TILES_STEP, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_TILES_STEP = block(NdBlocks.CRACKED_ROCK_TILES_STEP, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_ROCK_STEP = block(NdBlocks.SMOOTH_ROCK_STEP, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_ROCK_STEP = block(NdBlocks.POLISHED_ROCK_STEP, NdItemGroup.BUILDING);

    public static final BlockItem ROCK_WALL = block(NdBlocks.ROCK_WALL, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_WALL = block(NdBlocks.MOSSY_ROCK_WALL, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_BRICKS_WALL = block(NdBlocks.ROCK_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_BRICKS_WALL = block(NdBlocks.MOSSY_ROCK_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_BRICKS_WALL = block(NdBlocks.CRACKED_ROCK_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem ROCK_TILES_WALL = block(NdBlocks.ROCK_TILES_WALL, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_ROCK_TILES_WALL = block(NdBlocks.MOSSY_ROCK_TILES_WALL, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_ROCK_TILES_WALL = block(NdBlocks.CRACKED_ROCK_TILES_WALL, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_ROCK_WALL = block(NdBlocks.SMOOTH_ROCK_WALL, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_ROCK_WALL = block(NdBlocks.POLISHED_ROCK_WALL, NdItemGroup.BUILDING);


    public static final BlockItem MOSSY_DARKROCK = block(NdBlocks.MOSSY_DARKROCK, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_BRICKS = block(NdBlocks.DARKROCK_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_BRICKS = block(NdBlocks.MOSSY_DARKROCK_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_BRICKS = block(NdBlocks.CRACKED_DARKROCK_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_TILES = block(NdBlocks.DARKROCK_TILES, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_TILES = block(NdBlocks.MOSSY_DARKROCK_TILES, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_TILES = block(NdBlocks.CRACKED_DARKROCK_TILES, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_DARKROCK = block(NdBlocks.SMOOTH_DARKROCK, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_DARKROCK = block(NdBlocks.POLISHED_DARKROCK, NdItemGroup.BUILDING);
    public static final BlockItem CHISELED_DARKROCK = block(NdBlocks.CHISELED_DARKROCK, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_PILLAR = block(NdBlocks.DARKROCK_PILLAR, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_LANTERN = block(NdBlocks.DARKROCK_LANTERN, NdItemGroup.BUILDING);

    public static final BlockItem DARKROCK_SLAB = block(NdBlocks.DARKROCK_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_SLAB = block(NdBlocks.MOSSY_DARKROCK_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_BRICKS_SLAB = block(NdBlocks.DARKROCK_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_BRICKS_SLAB = block(NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_BRICKS_SLAB = block(NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_TILES_SLAB = block(NdBlocks.DARKROCK_TILES_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_TILES_SLAB = block(NdBlocks.MOSSY_DARKROCK_TILES_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_TILES_SLAB = block(NdBlocks.CRACKED_DARKROCK_TILES_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_DARKROCK_SLAB = block(NdBlocks.SMOOTH_DARKROCK_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_DARKROCK_SLAB = block(NdBlocks.POLISHED_DARKROCK_SLAB, NdItemGroup.BUILDING);

    public static final BlockItem DARKROCK_STAIRS = block(NdBlocks.DARKROCK_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_STAIRS = block(NdBlocks.MOSSY_DARKROCK_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_BRICKS_STAIRS = block(NdBlocks.DARKROCK_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_BRICKS_STAIRS = block(NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_BRICKS_STAIRS = block(NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_TILES_STAIRS = block(NdBlocks.DARKROCK_TILES_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_TILES_STAIRS = block(NdBlocks.MOSSY_DARKROCK_TILES_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_TILES_STAIRS = block(NdBlocks.CRACKED_DARKROCK_TILES_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_DARKROCK_STAIRS = block(NdBlocks.SMOOTH_DARKROCK_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_DARKROCK_STAIRS = block(NdBlocks.POLISHED_DARKROCK_STAIRS, NdItemGroup.BUILDING);

    public static final BlockItem DARKROCK_STEP = block(NdBlocks.DARKROCK_STEP, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_STEP = block(NdBlocks.MOSSY_DARKROCK_STEP, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_BRICKS_STEP = block(NdBlocks.DARKROCK_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_BRICKS_STEP = block(NdBlocks.MOSSY_DARKROCK_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_BRICKS_STEP = block(NdBlocks.CRACKED_DARKROCK_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_TILES_STEP = block(NdBlocks.DARKROCK_TILES_STEP, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_TILES_STEP = block(NdBlocks.MOSSY_DARKROCK_TILES_STEP, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_TILES_STEP = block(NdBlocks.CRACKED_DARKROCK_TILES_STEP, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_DARKROCK_STEP = block(NdBlocks.SMOOTH_DARKROCK_STEP, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_DARKROCK_STEP = block(NdBlocks.POLISHED_DARKROCK_STEP, NdItemGroup.BUILDING);

    public static final BlockItem DARKROCK_WALL = block(NdBlocks.DARKROCK_WALL, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_WALL = block(NdBlocks.MOSSY_DARKROCK_WALL, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_BRICKS_WALL = block(NdBlocks.DARKROCK_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_BRICKS_WALL = block(NdBlocks.MOSSY_DARKROCK_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_BRICKS_WALL = block(NdBlocks.CRACKED_DARKROCK_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem DARKROCK_TILES_WALL = block(NdBlocks.DARKROCK_TILES_WALL, NdItemGroup.BUILDING);
    public static final BlockItem MOSSY_DARKROCK_TILES_WALL = block(NdBlocks.MOSSY_DARKROCK_TILES_WALL, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARKROCK_TILES_WALL = block(NdBlocks.CRACKED_DARKROCK_TILES_WALL, NdItemGroup.BUILDING);
    public static final BlockItem SMOOTH_DARKROCK_WALL = block(NdBlocks.SMOOTH_DARKROCK_WALL, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_DARKROCK_WALL = block(NdBlocks.POLISHED_DARKROCK_WALL, NdItemGroup.BUILDING);


    public static final BlockItem LIMESTONE_BRICKS = block(NdBlocks.LIMESTONE_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_BRICKS = block(NdBlocks.CRACKED_LIMESTONE_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_TILES = block(NdBlocks.LIMESTONE_TILES, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_TILES = block(NdBlocks.CRACKED_LIMESTONE_TILES, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_LIMESTONE = block(NdBlocks.POLISHED_LIMESTONE, NdItemGroup.BUILDING);
    public static final BlockItem CARVED_LIMESTONE = block(NdBlocks.CARVED_LIMESTONE, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_PILLAR = block(NdBlocks.LIMESTONE_PILLAR, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_LANTERN = block(NdBlocks.LIMESTONE_LANTERN, NdItemGroup.BUILDING);

    public static final BlockItem LIMESTONE_SLAB = block(NdBlocks.LIMESTONE_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_BRICKS_SLAB = block(NdBlocks.LIMESTONE_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_BRICKS_SLAB = block(NdBlocks.CRACKED_LIMESTONE_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_TILES_SLAB = block(NdBlocks.LIMESTONE_TILES_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_TILES_SLAB = block(NdBlocks.CRACKED_LIMESTONE_TILES_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_LIMESTONE_SLAB = block(NdBlocks.POLISHED_LIMESTONE_SLAB, NdItemGroup.BUILDING);

    public static final BlockItem LIMESTONE_STAIRS = block(NdBlocks.LIMESTONE_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_BRICKS_STAIRS = block(NdBlocks.LIMESTONE_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_BRICKS_STAIRS = block(NdBlocks.CRACKED_LIMESTONE_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_TILES_STAIRS = block(NdBlocks.LIMESTONE_TILES_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_TILES_STAIRS = block(NdBlocks.CRACKED_LIMESTONE_TILES_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_LIMESTONE_STAIRS = block(NdBlocks.POLISHED_LIMESTONE_STAIRS, NdItemGroup.BUILDING);

    public static final BlockItem LIMESTONE_STEP = block(NdBlocks.LIMESTONE_STEP, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_BRICKS_STEP = block(NdBlocks.LIMESTONE_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_BRICKS_STEP = block(NdBlocks.CRACKED_LIMESTONE_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_TILES_STEP = block(NdBlocks.LIMESTONE_TILES_STEP, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_TILES_STEP = block(NdBlocks.CRACKED_LIMESTONE_TILES_STEP, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_LIMESTONE_STEP = block(NdBlocks.POLISHED_LIMESTONE_STEP, NdItemGroup.BUILDING);

    public static final BlockItem LIMESTONE_WALL = block(NdBlocks.LIMESTONE_WALL, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_BRICKS_WALL = block(NdBlocks.LIMESTONE_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_BRICKS_WALL = block(NdBlocks.CRACKED_LIMESTONE_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem LIMESTONE_TILES_WALL = block(NdBlocks.LIMESTONE_TILES_WALL, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_LIMESTONE_TILES_WALL = block(NdBlocks.CRACKED_LIMESTONE_TILES_WALL, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_LIMESTONE_WALL = block(NdBlocks.POLISHED_LIMESTONE_WALL, NdItemGroup.BUILDING);


    public static final BlockItem SUMESTONE_BRICKS = block(NdBlocks.SUMESTONE_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_SUMESTONE_BRICKS = block(NdBlocks.CRACKED_SUMESTONE_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_SUMESTONE = block(NdBlocks.POLISHED_SUMESTONE, NdItemGroup.BUILDING);
    public static final BlockItem CHISELED_SUMESTONE = block(NdBlocks.CHISELED_SUMESTONE, NdItemGroup.BUILDING);
    public static final BlockItem SUMESTONE_PILLAR = block(NdBlocks.SUMESTONE_PILLAR, NdItemGroup.BUILDING);
    public static final BlockItem SUMESTONE_LANTERN = block(NdBlocks.SUMESTONE_LANTERN, NdItemGroup.BUILDING);

    public static final BlockItem SUMESTONE_SLAB = block(NdBlocks.SUMESTONE_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem SUMESTONE_BRICKS_SLAB = block(NdBlocks.SUMESTONE_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_SUMESTONE_BRICKS_SLAB = block(NdBlocks.CRACKED_SUMESTONE_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_SUMESTONE_SLAB = block(NdBlocks.POLISHED_SUMESTONE_SLAB, NdItemGroup.BUILDING);

    public static final BlockItem SUMESTONE_STAIRS = block(NdBlocks.SUMESTONE_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem SUMESTONE_BRICKS_STAIRS = block(NdBlocks.SUMESTONE_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_SUMESTONE_BRICKS_STAIRS = block(NdBlocks.CRACKED_SUMESTONE_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_SUMESTONE_STAIRS = block(NdBlocks.POLISHED_SUMESTONE_STAIRS, NdItemGroup.BUILDING);

    public static final BlockItem SUMESTONE_STEP = block(NdBlocks.SUMESTONE_STEP, NdItemGroup.BUILDING);
    public static final BlockItem SUMESTONE_BRICKS_STEP = block(NdBlocks.SUMESTONE_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_SUMESTONE_BRICKS_STEP = block(NdBlocks.CRACKED_SUMESTONE_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_SUMESTONE_STEP = block(NdBlocks.POLISHED_SUMESTONE_STEP, NdItemGroup.BUILDING);

    public static final BlockItem SUMESTONE_WALL = block(NdBlocks.SUMESTONE_WALL, NdItemGroup.BUILDING);
    public static final BlockItem SUMESTONE_BRICKS_WALL = block(NdBlocks.SUMESTONE_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_SUMESTONE_BRICKS_WALL = block(NdBlocks.CRACKED_SUMESTONE_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_SUMESTONE_WALL = block(NdBlocks.POLISHED_SUMESTONE_WALL, NdItemGroup.BUILDING);


    public static final BlockItem DARK_SUMESTONE_BRICKS = block(NdBlocks.DARK_SUMESTONE_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARK_SUMESTONE_BRICKS = block(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_DARK_SUMESTONE = block(NdBlocks.POLISHED_DARK_SUMESTONE, NdItemGroup.BUILDING);
    public static final BlockItem CHISELED_DARK_SUMESTONE = block(NdBlocks.CHISELED_DARK_SUMESTONE, NdItemGroup.BUILDING);
    public static final BlockItem DARK_SUMESTONE_PILLAR = block(NdBlocks.DARK_SUMESTONE_PILLAR, NdItemGroup.BUILDING);
    public static final BlockItem DARK_SUMESTONE_LANTERN = block(NdBlocks.DARK_SUMESTONE_LANTERN, NdItemGroup.BUILDING);

    public static final BlockItem DARK_SUMESTONE_SLAB = block(NdBlocks.DARK_SUMESTONE_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem DARK_SUMESTONE_BRICKS_SLAB = block(NdBlocks.DARK_SUMESTONE_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARK_SUMESTONE_BRICKS_SLAB = block(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_SLAB, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_DARK_SUMESTONE_SLAB = block(NdBlocks.POLISHED_DARK_SUMESTONE_SLAB, NdItemGroup.BUILDING);

    public static final BlockItem DARK_SUMESTONE_STAIRS = block(NdBlocks.DARK_SUMESTONE_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem DARK_SUMESTONE_BRICKS_STAIRS = block(NdBlocks.DARK_SUMESTONE_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARK_SUMESTONE_BRICKS_STAIRS = block(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_STAIRS, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_DARK_SUMESTONE_STAIRS = block(NdBlocks.POLISHED_DARK_SUMESTONE_STAIRS, NdItemGroup.BUILDING);

    public static final BlockItem DARK_SUMESTONE_STEP = block(NdBlocks.DARK_SUMESTONE_STEP, NdItemGroup.BUILDING);
    public static final BlockItem DARK_SUMESTONE_BRICKS_STEP = block(NdBlocks.DARK_SUMESTONE_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARK_SUMESTONE_BRICKS_STEP = block(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_STEP, NdItemGroup.BUILDING);
    public static final BlockItem POLISHED_DARK_SUMESTONE_STEP = block(NdBlocks.POLISHED_DARK_SUMESTONE_STEP, NdItemGroup.BUILDING);

    public static final BlockItem DARK_SUMESTONE_WALL = block(NdBlocks.DARK_SUMESTONE_WALL, NdItemGroup.BUILDING);
    public static final BlockItem DARK_SUMESTONE_BRICKS_WALL = block(NdBlocks.DARK_SUMESTONE_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem CRACKED_DARK_SUMESTONE_BRICKS_WALL = block(NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_WALL, NdItemGroup.BUILDING);
    public static final BlockItem POLISHEDDARK__SUMESTONE_WALL = block(NdBlocks.POLISHED_DARK_SUMESTONE_WALL, NdItemGroup.BUILDING);


    //
    // ITEMS
    //

    public static final Item MUSIC_DISC_DARK = musicDisc("music_disc_dark", NdSoundEvents.MUSIC_DISC_DARK, 0);
    public static final Item MUSIC_DISC_M1 = musicDisc("music_disc_m1", NdSoundEvents.MUSIC_DISC_M1, 0);


    //
    // FACTORY METHODS
    //

    private static <I extends Item> I register(String id, I item) {
        Registry.register(Registry.ITEM, NaturesDebris.id(id), item);
        return item;
    }

    private static BlockItem block(Block block, Item.Settings settings) {
        return register(
            Registry.BLOCK.getId(block) + "",
            new BlockItem(block, settings)
        );
    }

    private static BlockItem block(Block block, ItemGroup group) {
        return block(block, inGroup(group));
    }

    private static Item musicDisc(String id, SoundEvent sound, int comparator) {
        return register(id, new NdMusicDiscItem(comparator, sound, inGroup(ItemGroup.MISC).maxCount(1)));
    }

    private static FabricItemSettings inGroup(ItemGroup group) {
        return new FabricItemSettings().group(group);
    }


    //
    // SUPPLIERS
    //

    public static Supplier<Item> item(String id) {
        return () -> Registry.ITEM.get(NaturesDebris.id(id));
    }
}
