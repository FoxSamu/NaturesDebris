package net.shadew.ndebris.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;
import java.util.function.Supplier;

import net.shadew.ndebris.common.NaturesDebris;

public abstract class NdBlocks {
    public static final Block ROCK = rock("rock", 1.5, 6, false);
    public static final Block DARKROCK = rock("darkrock", 1.5, 6, true);
    public static final Block LIMESTONE = limestone("limestone", 1, 4);
    public static final Block SUMESTONE = sumestone("sumestone", 2, 6, false);
    public static final Block DARK_SUMESTONE = sumestone("dark_sumestone", 2, 6, true);
    public static final Block MURKY_DIRT = dirt("murky_dirt", 0.5, MaterialColor.DIRT, BlockSoundGroup.GRAVEL);
    public static final Block MURKY_GRASS_BLOCK = grass("murky_grass_block", 0.6, MaterialColor.GRASS, BlockSoundGroup.GRASS);
    public static final Block MURKY_COARSE_DIRT = dirt("murky_coarse_dirt", 0.5, MaterialColor.DIRT, BlockSoundGroup.GRAVEL);
    public static final Block MURKY_HUMUS = humus("murky_humus", 0.5, MaterialColor.BLACK_TERRACOTTA, BlockSoundGroup.GRAVEL);
    public static final Block LEAFY_HUMUS = leafyHumus("leafy_humus", 0.5, MaterialColor.DIRT, BlockSoundGroup.GRASS);
    public static final Block MURKY_PODZOL = dirt("murky_podzol", 0.5, MaterialColor.DIRT, BlockSoundGroup.GRAVEL);
    public static final Block MURKY_SAND = sand("murky_sand", 0.5, MaterialColor.SAND, BlockSoundGroup.SAND);
    public static final Block MURKY_CLAY = clay("murky_clay");
    public static final Block MURKY_TERRACOTTA = terracotta("murky_terracotta");
    public static final Block MURKY_GRASS_PATH = grassPath("murky_grass_path", 0.5, MaterialColor.GRASS, BlockSoundGroup.GRASS);


    public static final Block BLACKWOOD_LOG = strippableLog("blackwood_log", MaterialColor.BLACK_TERRACOTTA, block("stripped_blackwood_log"));
    public static final Block INVER_LOG = strippableLog("inver_log", MaterialColor.WOOD, block("stripped_inver_log"));
    public static final Block BLACKWOOD = strippableLog("blackwood", MaterialColor.BLACK_TERRACOTTA, block("stripped_blackwood"));
    public static final Block INVER_WOOD = strippableLog("inver_wood", MaterialColor.BLACK_TERRACOTTA, block("stripped_inver_wood"));
    public static final Block STRIPPED_BLACKWOOD_LOG = log("stripped_blackwood_log", MaterialColor.BLACK_TERRACOTTA);
    public static final Block STRIPPED_INVER_LOG = log("stripped_inver_log", MaterialColor.WOOD);
    public static final Block STRIPPED_BLACKWOOD = log("stripped_blackwood", MaterialColor.BLACK_TERRACOTTA);
    public static final Block STRIPPED_INVER_WOOD = log("stripped_inver_wood", MaterialColor.WOOD);

    public static final Block BLACKWOOD_PLANKS = wood("blackwood_planks", MaterialColor.BLACK_TERRACOTTA);
    public static final Block INVER_PLANKS = wood("inver_planks", MaterialColor.WOOD);
    public static final Block BLACKWOOD_SLAB = woodSlab("blackwood_slab", MaterialColor.BLACK_TERRACOTTA);
    public static final Block INVER_SLAB = woodSlab("inver_slab", MaterialColor.WOOD);
    public static final Block BLACKWOOD_STAIRS = woodStairs("blackwood_stairs", MaterialColor.BLACK_TERRACOTTA);
    public static final Block INVER_STAIRS = woodStairs("inver_stairs", MaterialColor.WOOD);
    public static final Block BLACKWOOD_STEP = woodStep("blackwood_step", MaterialColor.BLACK_TERRACOTTA);
    public static final Block INVER_STEP = woodStep("inver_step", MaterialColor.WOOD);
    public static final Block BLACKWOOD_FENCE = fence("blackwood_fence", MaterialColor.BLACK_TERRACOTTA);
    public static final Block INVER_FENCE = fence("inver_fence", MaterialColor.WOOD);


    public static final Block MOSSY_ROCK = rock("mossy_rock", 1.5, 6, false);
    public static final Block ROCK_BRICKS = rock("rock_bricks", 2, 6, false);
    public static final Block MOSSY_ROCK_BRICKS = rock("mossy_rock_bricks", 2, 6, false);
    public static final Block CRACKED_ROCK_BRICKS = rock("cracked_rock_bricks", 2, 6, false);
    public static final Block ROCK_TILES = rock("rock_tiles", 2, 6, false);
    public static final Block MOSSY_ROCK_TILES = rock("mossy_rock_tiles", 2, 6, false);
    public static final Block CRACKED_ROCK_TILES = rock("cracked_rock_tiles", 2, 6, false);
    public static final Block SMOOTH_ROCK = rock("smooth_rock", 1.5, 6, false);
    public static final Block POLISHED_ROCK = rock("polished_rock", 2, 6, false);
    public static final Block CHISELED_ROCK = rock("chiseled_rock", 2, 6, false);
    public static final Block ROCK_PILLAR = rockPillar("rock_pillar", 2, 6, false);
    public static final Block ROCK_LANTERN = rockLantern("rock_lantern", 2, 6, false);

    public static final Block ROCK_SLAB = rockSlab("rock_slab", 1.5, 6, false);
    public static final Block MOSSY_ROCK_SLAB = rockSlab("mossy_rock_slab", 1.5, 6, false);
    public static final Block ROCK_BRICKS_SLAB = rockSlab("rock_bricks_slab", 2, 6, false);
    public static final Block MOSSY_ROCK_BRICKS_SLAB = rockSlab("mossy_rock_bricks_slab", 2, 6, false);
    public static final Block CRACKED_ROCK_BRICKS_SLAB = rockSlab("cracked_rock_bricks_slab", 2, 6, false);
    public static final Block ROCK_TILES_SLAB = rockSlab("rock_tiles_slab", 2, 6, false);
    public static final Block MOSSY_ROCK_TILES_SLAB = rockSlab("mossy_rock_tiles_slab", 2, 6, false);
    public static final Block CRACKED_ROCK_TILES_SLAB = rockSlab("cracked_rock_tiles_slab", 2, 6, false);
    public static final Block SMOOTH_ROCK_SLAB = rockSlab("smooth_rock_slab", 1.5, 6, false);
    public static final Block POLISHED_ROCK_SLAB = rockSlab("polished_rock_slab", 2, 6, false);

    public static final Block ROCK_STAIRS = rockStairs("rock_stairs", 1.5, 6, false);
    public static final Block MOSSY_ROCK_STAIRS = rockStairs("mossy_rock_stairs", 1.5, 6, false);
    public static final Block ROCK_BRICKS_STAIRS = rockStairs("rock_bricks_stairs", 2, 6, false);
    public static final Block MOSSY_ROCK_BRICKS_STAIRS = rockStairs("mossy_rock_bricks_stairs", 2, 6, false);
    public static final Block CRACKED_ROCK_BRICKS_STAIRS = rockStairs("cracked_rock_bricks_stairs", 2, 6, false);
    public static final Block ROCK_TILES_STAIRS = rockStairs("rock_tiles_stairs", 2, 6, false);
    public static final Block MOSSY_ROCK_TILES_STAIRS = rockStairs("mossy_rock_tiles_stairs", 2, 6, false);
    public static final Block CRACKED_ROCK_TILES_STAIRS = rockStairs("cracked_rock_tiles_stairs", 2, 6, false);
    public static final Block SMOOTH_ROCK_STAIRS = rockStairs("smooth_rock_stairs", 1.5, 6, false);
    public static final Block POLISHED_ROCK_STAIRS = rockStairs("polished_rock_stairs", 2, 6, false);

    public static final Block ROCK_STEP = rockStep("rock_step", 1.5, 6, false);
    public static final Block MOSSY_ROCK_STEP = rockStep("mossy_rock_step", 1.5, 6, false);
    public static final Block ROCK_BRICKS_STEP = rockStep("rock_bricks_step", 2, 6, false);
    public static final Block MOSSY_ROCK_BRICKS_STEP = rockStep("mossy_rock_bricks_step", 2, 6, false);
    public static final Block CRACKED_ROCK_BRICKS_STEP = rockStep("cracked_rock_bricks_step", 2, 6, false);
    public static final Block ROCK_TILES_STEP = rockStep("rock_tiles_step", 2, 6, false);
    public static final Block MOSSY_ROCK_TILES_STEP = rockStep("mossy_rock_tiles_step", 2, 6, false);
    public static final Block CRACKED_ROCK_TILES_STEP = rockStep("cracked_rock_tiles_step", 2, 6, false);
    public static final Block SMOOTH_ROCK_STEP = rockStep("smooth_rock_step", 1.5, 6, false);
    public static final Block POLISHED_ROCK_STEP = rockStep("polished_rock_step", 2, 6, false);

    public static final Block ROCK_WALL = rockWall("rock_wall", 1.5, 6, false);
    public static final Block MOSSY_ROCK_WALL = rockWall("mossy_rock_wall", 1.5, 6, false);
    public static final Block ROCK_BRICKS_WALL = rockWall("rock_bricks_wall", 2, 6, false);
    public static final Block MOSSY_ROCK_BRICKS_WALL = rockWall("mossy_rock_bricks_wall", 2, 6, false);
    public static final Block CRACKED_ROCK_BRICKS_WALL = rockWall("cracked_rock_bricks_wall", 2, 6, false);
    public static final Block ROCK_TILES_WALL = rockWall("rock_tiles_wall", 2, 6, false);
    public static final Block MOSSY_ROCK_TILES_WALL = rockWall("mossy_rock_tiles_wall", 2, 6, false);
    public static final Block CRACKED_ROCK_TILES_WALL = rockWall("cracked_rock_tiles_wall", 2, 6, false);
    public static final Block SMOOTH_ROCK_WALL = rockWall("smooth_rock_wall", 1.5, 6, false);
    public static final Block POLISHED_ROCK_WALL = rockWall("polished_rock_wall", 2, 6, false);



    public static final Block MOSSY_DARKROCK = rock("mossy_darkrock", 1.5, 6, true);
    public static final Block DARKROCK_BRICKS = rock("darkrock_bricks", 2, 6, true);
    public static final Block MOSSY_DARKROCK_BRICKS = rock("mossy_darkrock_bricks", 2, 6, true);
    public static final Block CRACKED_DARKROCK_BRICKS = rock("cracked_darkrock_bricks", 2, 6, true);
    public static final Block DARKROCK_TILES = rock("darkrock_tiles", 2, 6, true);
    public static final Block MOSSY_DARKROCK_TILES = rock("mossy_darkrock_tiles", 2, 6, true);
    public static final Block CRACKED_DARKROCK_TILES = rock("cracked_darkrock_tiles", 2, 6, true);
    public static final Block SMOOTH_DARKROCK = rock("smooth_darkrock", 1.5, 6, true);
    public static final Block POLISHED_DARKROCK = rock("polished_darkrock", 2, 6, true);
    public static final Block CHISELED_DARKROCK = rock("chiseled_darkrock", 2, 6, true);
    public static final Block DARKROCK_PILLAR = rockPillar("darkrock_pillar", 2, 6, true);
    public static final Block DARKROCK_LANTERN = rockLantern("darkrock_lantern", 2, 6, true);

    public static final Block DARKROCK_SLAB = rockSlab("darkrock_slab", 1.5, 6, true);
    public static final Block MOSSY_DARKROCK_SLAB = rockSlab("mossy_darkrock_slab", 1.5, 6, true);
    public static final Block DARKROCK_BRICKS_SLAB = rockSlab("darkrock_bricks_slab", 2, 6, true);
    public static final Block MOSSY_DARKROCK_BRICKS_SLAB = rockSlab("mossy_darkrock_bricks_slab", 2, 6, true);
    public static final Block CRACKED_DARKROCK_BRICKS_SLAB = rockSlab("cracked_darkrock_bricks_slab", 2, 6, true);
    public static final Block DARKROCK_TILES_SLAB = rockSlab("darkrock_tiles_slab", 2, 6, true);
    public static final Block MOSSY_DARKROCK_TILES_SLAB = rockSlab("mossy_darkrock_tiles_slab", 2, 6, true);
    public static final Block CRACKED_DARKROCK_TILES_SLAB = rockSlab("cracked_darkrock_tiles_slab", 2, 6, true);
    public static final Block SMOOTH_DARKROCK_SLAB = rockSlab("smooth_darkrock_slab", 1.5, 6, true);
    public static final Block POLISHED_DARKROCK_SLAB = rockSlab("polished_darkrock_slab", 2, 6, true);

    public static final Block DARKROCK_STAIRS = rockStairs("darkrock_stairs", 1.5, 6, true);
    public static final Block MOSSY_DARKROCK_STAIRS = rockStairs("mossy_darkrock_stairs", 1.5, 6, true);
    public static final Block DARKROCK_BRICKS_STAIRS = rockStairs("darkrock_bricks_stairs", 2, 6, true);
    public static final Block MOSSY_DARKROCK_BRICKS_STAIRS = rockStairs("mossy_darkrock_bricks_stairs", 2, 6, true);
    public static final Block CRACKED_DARKROCK_BRICKS_STAIRS = rockStairs("cracked_darkrock_bricks_stairs", 2, 6, true);
    public static final Block DARKROCK_TILES_STAIRS = rockStairs("darkrock_tiles_stairs", 2, 6, true);
    public static final Block MOSSY_DARKROCK_TILES_STAIRS = rockStairs("mossy_darkrock_tiles_stairs", 2, 6, true);
    public static final Block CRACKED_DARKROCK_TILES_STAIRS = rockStairs("cracked_darkrock_tiles_stairs", 2, 6, true);
    public static final Block SMOOTH_DARKROCK_STAIRS = rockStairs("smooth_darkrock_stairs", 1.5, 6, true);
    public static final Block POLISHED_DARKROCK_STAIRS = rockStairs("polished_darkrock_stairs", 2, 6, true);

    public static final Block DARKROCK_STEP = rockStep("darkrock_step", 1.5, 6, true);
    public static final Block MOSSY_DARKROCK_STEP = rockStep("mossy_darkrock_step", 1.5, 6, true);
    public static final Block DARKROCK_BRICKS_STEP = rockStep("darkrock_bricks_step", 2, 6, true);
    public static final Block MOSSY_DARKROCK_BRICKS_STEP = rockStep("mossy_darkrock_bricks_step", 2, 6, true);
    public static final Block CRACKED_DARKROCK_BRICKS_STEP = rockStep("cracked_darkrock_bricks_step", 2, 6, true);
    public static final Block DARKROCK_TILES_STEP = rockStep("darkrock_tiles_step", 2, 6, true);
    public static final Block MOSSY_DARKROCK_TILES_STEP = rockStep("mossy_darkrock_tiles_step", 2, 6, true);
    public static final Block CRACKED_DARKROCK_TILES_STEP = rockStep("cracked_darkrock_tiles_step", 2, 6, true);
    public static final Block SMOOTH_DARKROCK_STEP = rockStep("smooth_darkrock_step", 1.5, 6, true);
    public static final Block POLISHED_DARKROCK_STEP = rockStep("polished_darkrock_step", 2, 6, true);

    public static final Block DARKROCK_WALL = rockWall("darkrock_wall", 1.5, 6, true);
    public static final Block MOSSY_DARKROCK_WALL = rockWall("mossy_darkrock_wall", 1.5, 6, true);
    public static final Block DARKROCK_BRICKS_WALL = rockWall("darkrock_bricks_wall", 2, 6, true);
    public static final Block MOSSY_DARKROCK_BRICKS_WALL = rockWall("mossy_darkrock_bricks_wall", 2, 6, true);
    public static final Block CRACKED_DARKROCK_BRICKS_WALL = rockWall("cracked_darkrock_bricks_wall", 2, 6, true);
    public static final Block DARKROCK_TILES_WALL = rockWall("darkrock_tiles_wall", 2, 6, true);
    public static final Block MOSSY_DARKROCK_TILES_WALL = rockWall("mossy_darkrock_tiles_wall", 2, 6, true);
    public static final Block CRACKED_DARKROCK_TILES_WALL = rockWall("cracked_darkrock_tiles_wall", 2, 6, true);
    public static final Block SMOOTH_DARKROCK_WALL = rockWall("smooth_darkrock_wall", 1.5, 6, true);
    public static final Block POLISHED_DARKROCK_WALL = rockWall("polished_darkrock_wall", 2, 6, true);


    public static final Block LIMESTONE_BRICKS = limestone("limestone_bricks", 1, 4);
    public static final Block CRACKED_LIMESTONE_BRICKS = limestone("cracked_limestone_bricks", 1, 4);
    public static final Block LIMESTONE_TILES = limestone("limestone_tiles", 1, 4);
    public static final Block CRACKED_LIMESTONE_TILES = limestone("cracked_limestone_tiles", 1, 4);
    public static final Block POLISHED_LIMESTONE = limestone("polished_limestone", 1, 4);
    public static final Block CARVED_LIMESTONE = limestone("carved_limestone", 1, 4);
    public static final Block LIMESTONE_PILLAR = limestonePillar("limestone_pillar", 1, 4);
    public static final Block LIMESTONE_LANTERN = limestoneLantern("limestone_lantern", 1, 4);

    public static final Block LIMESTONE_SLAB = limestoneSlab("limestone_slab", 1, 4);
    public static final Block LIMESTONE_BRICKS_SLAB = limestoneSlab("limestone_bricks_slab", 1, 4);
    public static final Block CRACKED_LIMESTONE_BRICKS_SLAB = limestoneSlab("cracked_limestone_bricks_slab", 1, 4);
    public static final Block LIMESTONE_TILES_SLAB = limestoneSlab("limestone_tiles_slab", 1, 4);
    public static final Block CRACKED_LIMESTONE_TILES_SLAB = limestoneSlab("cracked_limestone_tiles_slab", 1, 4);
    public static final Block POLISHED_LIMESTONE_SLAB = limestoneSlab("polished_limestone_slab", 1, 4);

    public static final Block LIMESTONE_STAIRS = limestoneStairs("limestone_stairs", 1, 4);
    public static final Block LIMESTONE_BRICKS_STAIRS = limestoneStairs("limestone_bricks_stairs", 1, 4);
    public static final Block CRACKED_LIMESTONE_BRICKS_STAIRS = limestoneStairs("cracked_limestone_bricks_stairs", 1, 4);
    public static final Block LIMESTONE_TILES_STAIRS = limestoneStairs("limestone_tiles_stairs", 1, 4);
    public static final Block CRACKED_LIMESTONE_TILES_STAIRS = limestoneStairs("cracked_limestone_tiles_stairs", 1, 4);
    public static final Block POLISHED_LIMESTONE_STAIRS = limestoneStairs("polished_limestone_stairs", 1, 4);

    public static final Block LIMESTONE_STEP = limestoneStep("limestone_step", 1, 4);
    public static final Block LIMESTONE_BRICKS_STEP = limestoneStep("limestone_bricks_step", 1, 4);
    public static final Block CRACKED_LIMESTONE_BRICKS_STEP = limestoneStep("cracked_limestone_bricks_step", 1, 4);
    public static final Block LIMESTONE_TILES_STEP = limestoneStep("limestone_tiles_step", 1, 4);
    public static final Block CRACKED_LIMESTONE_TILES_STEP = limestoneStep("cracked_limestone_tiles_step", 1, 4);
    public static final Block POLISHED_LIMESTONE_STEP = limestoneStep("polished_limestone_step", 1, 4);

    public static final Block LIMESTONE_WALL = limestoneWall("limestone_wall", 1, 4);
    public static final Block LIMESTONE_BRICKS_WALL = limestoneWall("limestone_bricks_wall", 1, 4);
    public static final Block CRACKED_LIMESTONE_BRICKS_WALL = limestoneWall("cracked_limestone_bricks_wall", 1, 4);
    public static final Block LIMESTONE_TILES_WALL = limestoneWall("limestone_tiles_wall", 1, 4);
    public static final Block CRACKED_LIMESTONE_TILES_WALL = limestoneWall("cracked_limestone_tiles_wall", 1, 4);
    public static final Block POLISHED_LIMESTONE_WALL = limestoneWall("polished_limestone_wall", 1, 4);


    public static final Block SUMESTONE_BRICKS = sumestone("sumestone_bricks", 2, 6, false);
    public static final Block CRACKED_SUMESTONE_BRICKS = sumestone("cracked_sumestone_bricks", 2, 6, false);
    public static final Block POLISHED_SUMESTONE = sumestone("polished_sumestone", 2, 6, false);
    public static final Block CHISELED_SUMESTONE = sumestone("chiseled_sumestone", 2, 6, false);
    public static final Block SUMESTONE_PILLAR = sumestonePillar("sumestone_pillar", 2, 6, false);
    public static final Block SUMESTONE_LANTERN = sumestoneLantern("sumestone_lantern", 2, 6, false);

    public static final Block SUMESTONE_SLAB = sumestoneSlab("sumestone_slab", 2, 6, false);
    public static final Block SUMESTONE_BRICKS_SLAB = sumestoneSlab("sumestone_bricks_slab", 2, 6, false);
    public static final Block CRACKED_SUMESTONE_BRICKS_SLAB = sumestoneSlab("cracked_sumestone_bricks_slab", 2, 6, false);
    public static final Block POLISHED_SUMESTONE_SLAB = sumestoneSlab("polished_sumestone_slab", 2, 6, false);

    public static final Block SUMESTONE_STAIRS = sumestoneStairs("sumestone_stairs", 2, 6, false);
    public static final Block SUMESTONE_BRICKS_STAIRS = sumestoneStairs("sumestone_bricks_stairs", 2, 6, false);
    public static final Block CRACKED_SUMESTONE_BRICKS_STAIRS = sumestoneStairs("cracked_sumestone_bricks_stairs", 2, 6, false);
    public static final Block POLISHED_SUMESTONE_STAIRS = sumestoneStairs("polished_sumestone_stairs", 2, 6, false);

    public static final Block SUMESTONE_STEP = sumestoneStep("sumestone_step", 2, 6, false);
    public static final Block SUMESTONE_BRICKS_STEP = sumestoneStep("sumestone_bricks_step", 2, 6, false);
    public static final Block CRACKED_SUMESTONE_BRICKS_STEP = sumestoneStep("cracked_sumestone_bricks_step", 2, 6, false);
    public static final Block POLISHED_SUMESTONE_STEP = sumestoneStep("polished_sumestone_step", 2, 6, false);

    public static final Block SUMESTONE_WALL = sumestoneWall("sumestone_wall", 2, 6, false);
    public static final Block SUMESTONE_BRICKS_WALL = sumestoneWall("sumestone_bricks_wall", 2, 6, false);
    public static final Block CRACKED_SUMESTONE_BRICKS_WALL = sumestoneWall("cracked_sumestone_bricks_wall", 2, 6, false);
    public static final Block POLISHED_SUMESTONE_WALL = sumestoneWall("polished_sumestone_wall", 2, 6, false);


    public static final Block DARK_SUMESTONE_BRICKS = sumestone("dark_sumestone_bricks", 2, 6, true);
    public static final Block CRACKED_DARK_SUMESTONE_BRICKS = sumestone("cracked_dark_sumestone_bricks", 2, 6, true);
    public static final Block POLISHED_DARK_SUMESTONE = sumestone("polished_dark_sumestone", 2, 6, true);
    public static final Block CHISELED_DARK_SUMESTONE = sumestone("chiseled_dark_sumestone", 2, 6, true);
    public static final Block DARK_SUMESTONE_PILLAR = sumestonePillar("dark_sumestone_pillar", 2, 6, true);
    public static final Block DARK_SUMESTONE_LANTERN = sumestoneLantern("dark_sumestone_lantern", 2, 6, true);

    public static final Block DARK_SUMESTONE_SLAB = sumestoneSlab("dark_sumestone_slab", 2, 6, true);
    public static final Block DARK_SUMESTONE_BRICKS_SLAB = sumestoneSlab("dark_sumestone_bricks_slab", 2, 6, true);
    public static final Block CRACKED_DARK_SUMESTONE_BRICKS_SLAB = sumestoneSlab("cracked_dark_sumestone_bricks_slab", 2, 6, true);
    public static final Block POLISHED_DARK_SUMESTONE_SLAB = sumestoneSlab("polished_dark_sumestone_slab", 2, 6, true);

    public static final Block DARK_SUMESTONE_STAIRS = sumestoneStairs("dark_sumestone_stairs", 2, 6, true);
    public static final Block DARK_SUMESTONE_BRICKS_STAIRS = sumestoneStairs("dark_sumestone_bricks_stairs", 2, 6, true);
    public static final Block CRACKED_DARK_SUMESTONE_BRICKS_STAIRS = sumestoneStairs("cracked_dark_sumestone_bricks_stairs", 2, 6, true);
    public static final Block POLISHED_DARK_SUMESTONE_STAIRS = sumestoneStairs("polished_dark_sumestone_stairs", 2, 6, true);

    public static final Block DARK_SUMESTONE_STEP = sumestoneStep("dark_sumestone_step", 2, 6, true);
    public static final Block DARK_SUMESTONE_BRICKS_STEP = sumestoneStep("dark_sumestone_bricks_step", 2, 6, true);
    public static final Block CRACKED_DARK_SUMESTONE_BRICKS_STEP = sumestoneStep("cracked_dark_sumestone_bricks_step", 2, 6, true);
    public static final Block POLISHED_DARK_SUMESTONE_STEP = sumestoneStep("polished_dark_sumestone_step", 2, 6, true);

    public static final Block DARK_SUMESTONE_WALL = sumestoneWall("dark_sumestone_wall", 2, 6, true);
    public static final Block DARK_SUMESTONE_BRICKS_WALL = sumestoneWall("dark_sumestone_bricks_wall", 2, 6, true);
    public static final Block CRACKED_DARK_SUMESTONE_BRICKS_WALL = sumestoneWall("cracked_dark_sumestone_bricks_wall", 2, 6, true);
    public static final Block POLISHED_DARK_SUMESTONE_WALL = sumestoneWall("polished_dark_sumestone_wall", 2, 6, true);


    //
    // FACTORY METHODS
    //

    private static <T extends Block> T register(String id, T block) {
        Registry.register(Registry.BLOCK, NaturesDebris.id(id), block);
        return block;
    }

    private static Block rock(String id, double hardness, double resistance, boolean dark) {
        return register(id, new Block(NdBlockTypes.rock(dark, hardness, resistance)));
    }

    private static Block rockLantern(String id, double hardness, double resistance, boolean dark) {
        return register(id, new Block(
            NdBlockTypes.rock(dark, hardness, resistance, BlockSoundGroup.BONE)
                        .luminance(state -> 15)
                        .emissiveLighting((state, world, pos) -> true)
        ));
    }

    private static Block rockPillar(String id, double hardness, double resistance, boolean dark) {
        return register(id, new PillarBlock(NdBlockTypes.rock(dark, hardness, resistance)));
    }

    private static Block rockSlab(String id, double hardness, double resistance, boolean dark) {
        return register(id, new SlabBlock(NdBlockTypes.rock(dark, hardness, resistance)));
    }

    private static Block rockStairs(String id, double hardness, double resistance, boolean dark) {
        return register(id, new SimpleStairsBlock(NdBlockTypes.rock(dark, hardness, resistance)));
    }

    private static Block rockStep(String id, double hardness, double resistance, boolean dark) {
        return register(id, new StepBlock(NdBlockTypes.rock(dark, hardness, resistance)));
    }

    private static Block rockWall(String id, double hardness, double resistance, boolean dark) {
        return register(id, new WallBlock(NdBlockTypes.rock(dark, hardness, resistance)));
    }

    private static Block limestone(String id, double hardness, double resistance) {
        return register(id, new Block(NdBlockTypes.limestone(hardness, resistance)));
    }

    private static Block limestoneLantern(String id, double hardness, double resistance) {
        return register(id, new Block(
            NdBlockTypes.limestone(hardness, resistance, BlockSoundGroup.BONE)
                        .luminance(state -> 15)
                        .emissiveLighting((state, world, pos) -> true)
        ));
    }

    private static Block limestonePillar(String id, double hardness, double resistance) {
        return register(id, new PillarBlock(NdBlockTypes.limestone(hardness, resistance)));
    }

    private static Block limestoneSlab(String id, double hardness, double resistance) {
        return register(id, new SlabBlock(NdBlockTypes.limestone(hardness, resistance)));
    }

    private static Block limestoneStairs(String id, double hardness, double resistance) {
        return register(id, new SimpleStairsBlock(NdBlockTypes.limestone(hardness, resistance)));
    }

    private static Block limestoneStep(String id, double hardness, double resistance) {
        return register(id, new StepBlock(NdBlockTypes.limestone(hardness, resistance)));
    }

    private static Block limestoneWall(String id, double hardness, double resistance) {
        return register(id, new WallBlock(NdBlockTypes.limestone(hardness, resistance)));
    }

    private static Block sumestone(String id, double hardness, double resistance, boolean dark) {
        return register(id, new Block(NdBlockTypes.sumestone(dark, hardness, resistance)));
    }

    private static Block sumestoneLantern(String id, double hardness, double resistance, boolean dark) {
        return register(id, new Block(
            NdBlockTypes.sumestone(dark, hardness, resistance, BlockSoundGroup.BONE)
                        .luminance(state -> 15)
                        .emissiveLighting((state, world, pos) -> true)
        ));
    }

    private static Block sumestonePillar(String id, double hardness, double resistance, boolean dark) {
        return register(id, new PillarBlock(NdBlockTypes.sumestone(dark, hardness, resistance)));
    }

    private static Block sumestoneSlab(String id, double hardness, double resistance, boolean dark) {
        return register(id, new SlabBlock(NdBlockTypes.sumestone(dark, hardness, resistance)));
    }

    private static Block sumestoneStairs(String id, double hardness, double resistance, boolean dark) {
        return register(id, new SimpleStairsBlock(NdBlockTypes.sumestone(dark, hardness, resistance)));
    }

    private static Block sumestoneStep(String id, double hardness, double resistance, boolean dark) {
        return register(id, new StepBlock(NdBlockTypes.sumestone(dark, hardness, resistance)));
    }

    private static Block sumestoneWall(String id, double hardness, double resistance, boolean dark) {
        return register(id, new WallBlock(NdBlockTypes.sumestone(dark, hardness, resistance)));
    }

    private static Block dirt(String id, double strength, MaterialColor color, BlockSoundGroup sound) {
        return register(id, new MurkyDirtBlock(NdBlockTypes.soil(strength, color, sound)));
    }

    private static Block grassPath(String id, double strength, MaterialColor color, BlockSoundGroup sound) {
        return register(id, new MurkyGrassPathBlock(
            NdBlockTypes.soil(strength, color, sound)
                        .blockVision((state, world, pos) -> true)
        ));
    }

    private static Block humus(String id, double strength, MaterialColor color, BlockSoundGroup sound) {
        return register(id, new MurkyHumusBlock(
            NdBlockTypes.soil(strength, color, sound)
                        .ticksRandomly()
        ));
    }

    private static Block leafyHumus(String id, double strength, MaterialColor color, BlockSoundGroup sound) {
        return register(id, new LeafyHumusBlock(
            NdBlockTypes.soil(strength, color, sound)
                        .ticksRandomly()
        ));
    }

    private static Block grass(String id, double strength, MaterialColor color, BlockSoundGroup sound) {
        return register(id, new MurkyGrassBlock(
            NdBlockTypes.soil(strength, color, sound)
                        .ticksRandomly()
        ));
    }

    private static Block sand(String id, double strength, MaterialColor color, BlockSoundGroup sound) {
        return register(id, new MurkySandBlock(
            NdBlockTypes.soil(strength, color, sound)
                        .ticksRandomly()
        ));
    }

    private static Block clay(String id) {
        return register(id, new Block(
            FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.BLUE_TERRACOTTA)
                               .strength(.5f)
                               .sounds(BlockSoundGroup.GRAVEL)
                               .breakByTool(FabricToolTags.SHOVELS)
        ));
    }

    private static Block terracotta(String id) {
        return register(id, new Block(
            FabricBlockSettings.of(Material.STONE, MaterialColor.BLUE_TERRACOTTA)
                               .strength(1.25f, 4.2f)
                               .sounds(BlockSoundGroup.STONE)
                               .requiresTool()
                               .breakByTool(FabricToolTags.PICKAXES)
        ));
    }

    private static Block log(String id, MaterialColor color) {
        return register(id, new PillarBlock(NdBlockTypes.wood(color, 2)));
    }

    private static Block strippableLog(String id, MaterialColor color, Supplier<Block> stripped) {
        return register(id, new StrippableLogBlock(NdBlockTypes.wood(color, 2), stripped));
    }

    private static Block wood(String id, MaterialColor color) {
        return register(id, new Block(NdBlockTypes.wood(color, 2)));
    }

    private static Block woodSlab(String id, MaterialColor color) {
        return register(id, new SlabBlock(NdBlockTypes.wood(color, 2)));
    }

    private static Block woodStairs(String id, MaterialColor color) {
        return register(id, new SimpleStairsBlock(NdBlockTypes.wood(color, 2)));
    }

    private static Block woodStep(String id, MaterialColor color) {
        return register(id, new StepBlock(NdBlockTypes.wood(color, 2)));
    }

    private static Block fence(String id, MaterialColor color) {
        return register(id, new FenceBlock(NdBlockTypes.wood(color, 2)));
    }


    //
    // SUPPLIERS
    //

    public static Supplier<Block> block(String id) {
        return () -> Registry.BLOCK.get(NaturesDebris.id(id));
    }

    public static Supplier<BlockState> blockState(String id, Function<BlockState, BlockState> state) {
        return () -> state.apply(Registry.BLOCK.get(NaturesDebris.id(id)).getDefaultState());
    }

    public static Supplier<BlockState> blockState(String id) {
        return () -> Registry.BLOCK.get(NaturesDebris.id(id)).getDefaultState();
    }
}
