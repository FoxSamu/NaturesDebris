package natures.debris.common.block;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

import natures.debris.core.util.IRegistry;
import natures.debris.common.NaturesDebris;
import natures.debris.common.block.plant.PlantBlock;
import natures.debris.common.block.plant.TestPlantBlock;
import natures.debris.common.block.plant.fluid.LevelledFluidLogic;
import natures.debris.common.item.group.ItemSubgroup;
import natures.debris.common.item.group.NdItemGroup;

@ObjectHolder("ndebris")
public abstract class NdBlocks {
    public static final Block MURKY_DIRT = inj();
    public static final Block MURKY_GRASS_BLOCK = inj();
    public static final Block MURKY_COARSE_DIRT = inj();
    public static final Block MURKY_HUMUS = inj();
    public static final Block MURKY_PODZOL = inj();
    public static final Block LEAFY_HUMUS = inj();
    public static final Block MURKY_SAND = inj();
    public static final Block MURKY_CLAY = inj();
    public static final Block MURKY_TERRACOTTA = inj();
    public static final Block MURKY_GRASS_PATH = inj();

    public static final Block BLACKWOOD_LOG = inj();
    public static final Block INVER_LOG = inj();
    public static final Block STRIPPED_BLACKWOOD_LOG = inj();
    public static final Block STRIPPED_INVER_LOG = inj();
    public static final Block BLACKWOOD = inj();
    public static final Block INVER_WOOD = inj();
    public static final Block STRIPPED_BLACKWOOD = inj();
    public static final Block STRIPPED_INVER_WOOD = inj();

    public static final Block BLACKWOOD_PLANKS = inj();
    public static final Block INVER_PLANKS = inj();
    public static final Block BLACKWOOD_SLAB = inj();
    public static final Block INVER_SLAB = inj();
    public static final Block BLACKWOOD_STAIRS = inj();
    public static final Block INVER_STAIRS = inj();
    public static final Block BLACKWOOD_STEP = inj();
    public static final Block INVER_STEP = inj();
    public static final Block BLACKWOOD_FENCE = inj();
    public static final Block INVER_FENCE = inj();

    public static final Block ROCK = inj();
    public static final Block MOSSY_ROCK = inj();
    public static final Block ROCK_BRICKS = inj();
    public static final Block MOSSY_ROCK_BRICKS = inj();
    public static final Block CRACKED_ROCK_BRICKS = inj();
    public static final Block ROCK_TILES = inj();
    public static final Block MOSSY_ROCK_TILES = inj();
    public static final Block CRACKED_ROCK_TILES = inj();
    public static final Block SMOOTH_ROCK = inj();
    public static final Block POLISHED_ROCK = inj();
    public static final Block CHISELED_ROCK = inj();
    public static final Block ROCK_PILLAR = inj();
    public static final Block ROCK_LANTERN = inj();

    public static final Block ROCK_SLAB = inj();
    public static final Block MOSSY_ROCK_SLAB = inj();
    public static final Block ROCK_BRICKS_SLAB = inj();
    public static final Block MOSSY_ROCK_BRICKS_SLAB = inj();
    public static final Block CRACKED_ROCK_BRICKS_SLAB = inj();
    public static final Block ROCK_TILES_SLAB = inj();
    public static final Block MOSSY_ROCK_TILES_SLAB = inj();
    public static final Block CRACKED_ROCK_TILES_SLAB = inj();
    public static final Block SMOOTH_ROCK_SLAB = inj();
    public static final Block POLISHED_ROCK_SLAB = inj();

    public static final Block ROCK_STAIRS = inj();
    public static final Block MOSSY_ROCK_STAIRS = inj();
    public static final Block ROCK_BRICKS_STAIRS = inj();
    public static final Block MOSSY_ROCK_BRICKS_STAIRS = inj();
    public static final Block CRACKED_ROCK_BRICKS_STAIRS = inj();
    public static final Block ROCK_TILES_STAIRS = inj();
    public static final Block MOSSY_ROCK_TILES_STAIRS = inj();
    public static final Block CRACKED_ROCK_TILES_STAIRS = inj();
    public static final Block SMOOTH_ROCK_STAIRS = inj();
    public static final Block POLISHED_ROCK_STAIRS = inj();

    public static final Block ROCK_STEP = inj();
    public static final Block MOSSY_ROCK_STEP = inj();
    public static final Block ROCK_BRICKS_STEP = inj();
    public static final Block MOSSY_ROCK_BRICKS_STEP = inj();
    public static final Block CRACKED_ROCK_BRICKS_STEP = inj();
    public static final Block ROCK_TILES_STEP = inj();
    public static final Block MOSSY_ROCK_TILES_STEP = inj();
    public static final Block CRACKED_ROCK_TILES_STEP = inj();
    public static final Block SMOOTH_ROCK_STEP = inj();
    public static final Block POLISHED_ROCK_STEP = inj();

    public static final Block ROCK_WALL = inj();
    public static final Block MOSSY_ROCK_WALL = inj();
    public static final Block ROCK_BRICKS_WALL = inj();
    public static final Block MOSSY_ROCK_BRICKS_WALL = inj();
    public static final Block CRACKED_ROCK_BRICKS_WALL = inj();
    public static final Block ROCK_TILES_WALL = inj();
    public static final Block MOSSY_ROCK_TILES_WALL = inj();
    public static final Block CRACKED_ROCK_TILES_WALL = inj();
    public static final Block SMOOTH_ROCK_WALL = inj();
    public static final Block POLISHED_ROCK_WALL = inj();

    public static final Block DARKROCK = inj();
    public static final Block MOSSY_DARKROCK = inj();
    public static final Block DARKROCK_BRICKS = inj();
    public static final Block MOSSY_DARKROCK_BRICKS = inj();
    public static final Block CRACKED_DARKROCK_BRICKS = inj();
    public static final Block DARKROCK_TILES = inj();
    public static final Block MOSSY_DARKROCK_TILES = inj();
    public static final Block CRACKED_DARKROCK_TILES = inj();
    public static final Block SMOOTH_DARKROCK = inj();
    public static final Block POLISHED_DARKROCK = inj();
    public static final Block CHISELED_DARKROCK = inj();
    public static final Block DARKROCK_PILLAR = inj();
    public static final Block DARKROCK_LANTERN = inj();

    public static final Block DARKROCK_SLAB = inj();
    public static final Block MOSSY_DARKROCK_SLAB = inj();
    public static final Block DARKROCK_BRICKS_SLAB = inj();
    public static final Block MOSSY_DARKROCK_BRICKS_SLAB = inj();
    public static final Block CRACKED_DARKROCK_BRICKS_SLAB = inj();
    public static final Block DARKROCK_TILES_SLAB = inj();
    public static final Block MOSSY_DARKROCK_TILES_SLAB = inj();
    public static final Block CRACKED_DARKROCK_TILES_SLAB = inj();
    public static final Block SMOOTH_DARKROCK_SLAB = inj();
    public static final Block POLISHED_DARKROCK_SLAB = inj();

    public static final Block DARKROCK_STAIRS = inj();
    public static final Block MOSSY_DARKROCK_STAIRS = inj();
    public static final Block DARKROCK_BRICKS_STAIRS = inj();
    public static final Block MOSSY_DARKROCK_BRICKS_STAIRS = inj();
    public static final Block CRACKED_DARKROCK_BRICKS_STAIRS = inj();
    public static final Block DARKROCK_TILES_STAIRS = inj();
    public static final Block MOSSY_DARKROCK_TILES_STAIRS = inj();
    public static final Block CRACKED_DARKROCK_TILES_STAIRS = inj();
    public static final Block SMOOTH_DARKROCK_STAIRS = inj();
    public static final Block POLISHED_DARKROCK_STAIRS = inj();

    public static final Block DARKROCK_STEP = inj();
    public static final Block MOSSY_DARKROCK_STEP = inj();
    public static final Block DARKROCK_BRICKS_STEP = inj();
    public static final Block MOSSY_DARKROCK_BRICKS_STEP = inj();
    public static final Block CRACKED_DARKROCK_BRICKS_STEP = inj();
    public static final Block DARKROCK_TILES_STEP = inj();
    public static final Block MOSSY_DARKROCK_TILES_STEP = inj();
    public static final Block CRACKED_DARKROCK_TILES_STEP = inj();
    public static final Block SMOOTH_DARKROCK_STEP = inj();
    public static final Block POLISHED_DARKROCK_STEP = inj();

    public static final Block DARKROCK_WALL = inj();
    public static final Block MOSSY_DARKROCK_WALL = inj();
    public static final Block DARKROCK_BRICKS_WALL = inj();
    public static final Block MOSSY_DARKROCK_BRICKS_WALL = inj();
    public static final Block CRACKED_DARKROCK_BRICKS_WALL = inj();
    public static final Block DARKROCK_TILES_WALL = inj();
    public static final Block MOSSY_DARKROCK_TILES_WALL = inj();
    public static final Block CRACKED_DARKROCK_TILES_WALL = inj();
    public static final Block SMOOTH_DARKROCK_WALL = inj();
    public static final Block POLISHED_DARKROCK_WALL = inj();

    public static final Block TEST_PLANT = inj();

    public static void registerBlocks(IRegistry<Block> registry) {
        registry.registerAll(
            rock("rock", 1.5, 6, false),
            rock("darkrock", 1.5, 6, true),
            dirt("murky_dirt", 0.5, MaterialColor.DIRT, SoundType.GROUND),
            grass("murky_grass_block", 0.6, MaterialColor.GRASS, SoundType.PLANT),
            dirt("murky_coarse_dirt", 0.5, MaterialColor.DIRT, SoundType.GROUND),
            humus("murky_humus", 0.5, MaterialColor.BLACK_TERRACOTTA, SoundType.GROUND),
            leafyHumus("leafy_humus", 0.5, MaterialColor.DIRT, SoundType.PLANT),
            dirt("murky_podzol", 0.5, MaterialColor.DIRT, SoundType.GROUND),
            sand("murky_sand", 0.5, MaterialColor.SAND, SoundType.SAND),
            clay("murky_clay"),
            terracotta("murky_terracotta"),
            grassPath("murky_grass_path", 0.5, MaterialColor.GRASS, SoundType.PLANT),


            stripableLog("blackwood_log", MaterialColor.BLACK_TERRACOTTA, () -> STRIPPED_BLACKWOOD_LOG),
            stripableLog("inver_log", MaterialColor.WOOD, () -> STRIPPED_INVER_LOG),
            stripableLog("blackwood", MaterialColor.BLACK_TERRACOTTA, () -> STRIPPED_BLACKWOOD),
            stripableLog("inver_wood", MaterialColor.BLACK_TERRACOTTA, () -> STRIPPED_INVER_WOOD),
            log("stripped_blackwood_log", MaterialColor.BLACK_TERRACOTTA),
            log("stripped_inver_log", MaterialColor.WOOD),
            log("stripped_blackwood", MaterialColor.BLACK_TERRACOTTA),
            log("stripped_inver_wood", MaterialColor.WOOD),

            wood("blackwood_planks", MaterialColor.BLACK_TERRACOTTA),
            wood("inver_planks", MaterialColor.WOOD),
            woodSlab("blackwood_slab", MaterialColor.BLACK_TERRACOTTA),
            woodSlab("inver_slab", MaterialColor.WOOD),
            woodStairs("blackwood_stairs", MaterialColor.BLACK_TERRACOTTA),
            woodStairs("inver_stairs", MaterialColor.WOOD),
            woodStep("blackwood_step", MaterialColor.BLACK_TERRACOTTA),
            woodStep("inver_step", MaterialColor.WOOD),
            fence("blackwood_fence", MaterialColor.BLACK_TERRACOTTA),
            fence("inver_fence", MaterialColor.WOOD),


            rock("mossy_rock", 1.5, 6, false),
            rock("rock_bricks", 2, 6, false),
            rock("mossy_rock_bricks", 2, 6, false),
            rock("cracked_rock_bricks", 2, 6, false),
            rock("rock_tiles", 2, 6, false),
            rock("mossy_rock_tiles", 2, 6, false),
            rock("cracked_rock_tiles", 2, 6, false),
            rock("smooth_rock", 1.5, 6, false),
            rock("polished_rock", 2, 6, false),
            rock("chiseled_rock", 2, 6, false),
            rockPillar("rock_pillar", 2, 6, false),
            rockLantern("rock_lantern", 2, 6, false),

            rockSlab("rock_slab", 1.5, 6, false),
            rockSlab("mossy_rock_slab", 1.5, 6, false),
            rockSlab("rock_bricks_slab", 2, 6, false),
            rockSlab("mossy_rock_bricks_slab", 2, 6, false),
            rockSlab("cracked_rock_bricks_slab", 2, 6, false),
            rockSlab("rock_tiles_slab", 2, 6, false),
            rockSlab("mossy_rock_tiles_slab", 2, 6, false),
            rockSlab("cracked_rock_tiles_slab", 2, 6, false),
            rockSlab("smooth_rock_slab", 1.5, 6, false),
            rockSlab("polished_rock_slab", 2, 6, false),

            rockStairs("rock_stairs", 1.5, 6, false),
            rockStairs("mossy_rock_stairs", 1.5, 6, false),
            rockStairs("rock_bricks_stairs", 2, 6, false),
            rockStairs("mossy_rock_bricks_stairs", 2, 6, false),
            rockStairs("cracked_rock_bricks_stairs", 2, 6, false),
            rockStairs("rock_tiles_stairs", 2, 6, false),
            rockStairs("mossy_rock_tiles_stairs", 2, 6, false),
            rockStairs("cracked_rock_tiles_stairs", 2, 6, false),
            rockStairs("smooth_rock_stairs", 1.5, 6, false),
            rockStairs("polished_rock_stairs", 2, 6, false),

            rockStep("rock_step", 1.5, 6, false),
            rockStep("mossy_rock_step", 1.5, 6, false),
            rockStep("rock_bricks_step", 2, 6, false),
            rockStep("mossy_rock_bricks_step", 2, 6, false),
            rockStep("cracked_rock_bricks_step", 2, 6, false),
            rockStep("rock_tiles_step", 2, 6, false),
            rockStep("mossy_rock_tiles_step", 2, 6, false),
            rockStep("cracked_rock_tiles_step", 2, 6, false),
            rockStep("smooth_rock_step", 1.5, 6, false),
            rockStep("polished_rock_step", 2, 6, false),

            rockWall("rock_wall", 1.5, 6, false),
            rockWall("mossy_rock_wall", 1.5, 6, false),
            rockWall("rock_bricks_wall", 2, 6, false),
            rockWall("mossy_rock_bricks_wall", 2, 6, false),
            rockWall("cracked_rock_bricks_wall", 2, 6, false),
            rockWall("rock_tiles_wall", 2, 6, false),
            rockWall("mossy_rock_tiles_wall", 2, 6, false),
            rockWall("cracked_rock_tiles_wall", 2, 6, false),
            rockWall("smooth_rock_wall", 1.5, 6, false),
            rockWall("polished_rock_wall", 2, 6, false),


            rock("mossy_darkrock", 1.5, 6, true),
            rock("darkrock_bricks", 2, 6, true),
            rock("mossy_darkrock_bricks", 2, 6, true),
            rock("cracked_darkrock_bricks", 2, 6, true),
            rock("darkrock_tiles", 2, 6, true),
            rock("mossy_darkrock_tiles", 2, 6, true),
            rock("cracked_darkrock_tiles", 2, 6, true),
            rock("smooth_darkrock", 1.5, 6, true),
            rock("polished_darkrock", 2, 6, true),
            rock("chiseled_darkrock", 2, 6, true),
            rockPillar("darkrock_pillar", 2, 6, true),
            rockLantern("darkrock_lantern", 2, 6, true),

            rockSlab("darkrock_slab", 1.5, 6, true),
            rockSlab("mossy_darkrock_slab", 1.5, 6, true),
            rockSlab("darkrock_bricks_slab", 2, 6, true),
            rockSlab("mossy_darkrock_bricks_slab", 2, 6, true),
            rockSlab("cracked_darkrock_bricks_slab", 2, 6, true),
            rockSlab("darkrock_tiles_slab", 2, 6, true),
            rockSlab("mossy_darkrock_tiles_slab", 2, 6, true),
            rockSlab("cracked_darkrock_tiles_slab", 2, 6, true),
            rockSlab("smooth_darkrock_slab", 1.5, 6, true),
            rockSlab("polished_darkrock_slab", 2, 6, true),

            rockStairs("darkrock_stairs", 1.5, 6, true),
            rockStairs("mossy_darkrock_stairs", 1.5, 6, true),
            rockStairs("darkrock_bricks_stairs", 2, 6, true),
            rockStairs("mossy_darkrock_bricks_stairs", 2, 6, true),
            rockStairs("cracked_darkrock_bricks_stairs", 2, 6, true),
            rockStairs("darkrock_tiles_stairs", 2, 6, true),
            rockStairs("mossy_darkrock_tiles_stairs", 2, 6, true),
            rockStairs("cracked_darkrock_tiles_stairs", 2, 6, true),
            rockStairs("smooth_darkrock_stairs", 1.5, 6, true),
            rockStairs("polished_darkrock_stairs", 2, 6, true),

            rockStep("darkrock_step", 1.5, 6, true),
            rockStep("mossy_darkrock_step", 1.5, 6, true),
            rockStep("darkrock_bricks_step", 2, 6, true),
            rockStep("mossy_darkrock_bricks_step", 2, 6, true),
            rockStep("cracked_darkrock_bricks_step", 2, 6, true),
            rockStep("darkrock_tiles_step", 2, 6, true),
            rockStep("mossy_darkrock_tiles_step", 2, 6, true),
            rockStep("cracked_darkrock_tiles_step", 2, 6, true),
            rockStep("smooth_darkrock_step", 1.5, 6, true),
            rockStep("polished_darkrock_step", 2, 6, true),

            rockWall("darkrock_wall", 1.5, 6, true),
            rockWall("mossy_darkrock_wall", 1.5, 6, true),
            rockWall("darkrock_bricks_wall", 2, 6, true),
            rockWall("mossy_darkrock_bricks_wall", 2, 6, true),
            rockWall("cracked_darkrock_bricks_wall", 2, 6, true),
            rockWall("darkrock_tiles_wall", 2, 6, true),
            rockWall("mossy_darkrock_tiles_wall", 2, 6, true),
            rockWall("cracked_darkrock_tiles_wall", 2, 6, true),
            rockWall("smooth_darkrock_wall", 1.5, 6, true),
            rockWall("polished_darkrock_wall", 2, 6, true),

            testPlant("test_plant")
        );
    }

    public static void registerItems(IRegistry<Item> registry) {
        registry.registerAll(
            item(ROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(DARKROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(MURKY_DIRT, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(MURKY_GRASS_BLOCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(MURKY_SAND, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(MURKY_HUMUS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(LEAFY_HUMUS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(MURKY_PODZOL, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(MURKY_COARSE_DIRT, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(MURKY_CLAY, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),
            item(MURKY_GRASS_PATH, NdItemGroup.DECORATIONS, ItemSubgroup.DECORATIONS),
            item(MURKY_TERRACOTTA, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.NATURE),

            item(BLACKWOOD_LOG, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.LOGS),
            item(INVER_LOG, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.LOGS),
            item(BLACKWOOD, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.LOGS),
            item(INVER_WOOD, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.LOGS),
            item(STRIPPED_BLACKWOOD_LOG, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.LOGS),
            item(STRIPPED_INVER_LOG, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.LOGS),
            item(STRIPPED_BLACKWOOD, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.LOGS),
            item(STRIPPED_INVER_WOOD, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.LOGS),

            item(BLACKWOOD_PLANKS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.PLANKS),
            item(INVER_PLANKS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.PLANKS),
            item(BLACKWOOD_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.PLANK_SLABS),
            item(INVER_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.PLANK_SLABS),
            item(BLACKWOOD_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.PLANK_STAIRS),
            item(INVER_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.PLANK_STAIRS),
            item(BLACKWOOD_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.PLANK_STEPS),
            item(INVER_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.PLANK_STEPS),
            item(BLACKWOOD_FENCE, NdItemGroup.DECORATIONS, ItemSubgroup.FENCES),
            item(INVER_FENCE, NdItemGroup.DECORATIONS, ItemSubgroup.FENCES),

            item(MOSSY_ROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(ROCK_BRICKS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(MOSSY_ROCK_BRICKS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(CRACKED_ROCK_BRICKS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(ROCK_TILES, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(MOSSY_ROCK_TILES, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(CRACKED_ROCK_TILES, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(SMOOTH_ROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(POLISHED_ROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(CHISELED_ROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(ROCK_PILLAR, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),
            item(ROCK_LANTERN, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK),

            item(ROCK_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),
            item(MOSSY_ROCK_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),
            item(ROCK_BRICKS_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),
            item(MOSSY_ROCK_BRICKS_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),
            item(CRACKED_ROCK_BRICKS_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),
            item(ROCK_TILES_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),
            item(MOSSY_ROCK_TILES_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),
            item(CRACKED_ROCK_TILES_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),
            item(SMOOTH_ROCK_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),
            item(POLISHED_ROCK_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_SLABS),

            item(ROCK_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),
            item(MOSSY_ROCK_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),
            item(ROCK_BRICKS_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),
            item(MOSSY_ROCK_BRICKS_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),
            item(CRACKED_ROCK_BRICKS_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),
            item(ROCK_TILES_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),
            item(MOSSY_ROCK_TILES_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),
            item(CRACKED_ROCK_TILES_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),
            item(SMOOTH_ROCK_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),
            item(POLISHED_ROCK_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STAIRS),

            item(ROCK_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),
            item(MOSSY_ROCK_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),
            item(ROCK_BRICKS_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),
            item(MOSSY_ROCK_BRICKS_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),
            item(CRACKED_ROCK_BRICKS_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),
            item(ROCK_TILES_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),
            item(MOSSY_ROCK_TILES_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),
            item(CRACKED_ROCK_TILES_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),
            item(SMOOTH_ROCK_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),
            item(POLISHED_ROCK_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.ROCK_STEPS),

            item(ROCK_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),
            item(MOSSY_ROCK_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),
            item(ROCK_BRICKS_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),
            item(MOSSY_ROCK_BRICKS_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),
            item(CRACKED_ROCK_BRICKS_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),
            item(ROCK_TILES_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),
            item(MOSSY_ROCK_TILES_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),
            item(CRACKED_ROCK_TILES_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),
            item(SMOOTH_ROCK_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),
            item(POLISHED_ROCK_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.ROCK_WALLS),


            item(MOSSY_DARKROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(DARKROCK_BRICKS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(MOSSY_DARKROCK_BRICKS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(CRACKED_DARKROCK_BRICKS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(DARKROCK_TILES, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(MOSSY_DARKROCK_TILES, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(CRACKED_DARKROCK_TILES, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(SMOOTH_DARKROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(POLISHED_DARKROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(CHISELED_DARKROCK, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(DARKROCK_PILLAR, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),
            item(DARKROCK_LANTERN, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK),

            item(DARKROCK_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),
            item(MOSSY_DARKROCK_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),
            item(DARKROCK_BRICKS_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),
            item(MOSSY_DARKROCK_BRICKS_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),
            item(CRACKED_DARKROCK_BRICKS_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),
            item(DARKROCK_TILES_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),
            item(MOSSY_DARKROCK_TILES_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),
            item(CRACKED_DARKROCK_TILES_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),
            item(SMOOTH_DARKROCK_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),
            item(POLISHED_DARKROCK_SLAB, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_SLABS),

            item(DARKROCK_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),
            item(MOSSY_DARKROCK_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),
            item(DARKROCK_BRICKS_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),
            item(MOSSY_DARKROCK_BRICKS_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),
            item(CRACKED_DARKROCK_BRICKS_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),
            item(DARKROCK_TILES_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),
            item(MOSSY_DARKROCK_TILES_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),
            item(CRACKED_DARKROCK_TILES_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),
            item(SMOOTH_DARKROCK_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),
            item(POLISHED_DARKROCK_STAIRS, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STAIRS),

            item(DARKROCK_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),
            item(MOSSY_DARKROCK_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),
            item(DARKROCK_BRICKS_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),
            item(MOSSY_DARKROCK_BRICKS_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),
            item(CRACKED_DARKROCK_BRICKS_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),
            item(DARKROCK_TILES_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),
            item(MOSSY_DARKROCK_TILES_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),
            item(CRACKED_DARKROCK_TILES_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),
            item(SMOOTH_DARKROCK_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),
            item(POLISHED_DARKROCK_STEP, NdItemGroup.BUILDING_BLOCKS, ItemSubgroup.DARKROCK_STEPS),

            item(DARKROCK_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),
            item(MOSSY_DARKROCK_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),
            item(DARKROCK_BRICKS_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),
            item(MOSSY_DARKROCK_BRICKS_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),
            item(CRACKED_DARKROCK_BRICKS_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),
            item(DARKROCK_TILES_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),
            item(MOSSY_DARKROCK_TILES_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),
            item(CRACKED_DARKROCK_TILES_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),
            item(SMOOTH_DARKROCK_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),
            item(POLISHED_DARKROCK_WALL, NdItemGroup.DECORATIONS, ItemSubgroup.DARKROCK_WALLS),

            item(TEST_PLANT, NdItemGroup.DECORATIONS, ItemSubgroup.DECORATIONS)
        );
    }

    public static void setup() {
    }

    @OnlyIn(Dist.CLIENT)
    public static void setupClient() {
        RenderTypeLookup.setRenderLayer(MURKY_GRASS_BLOCK, RenderType.getCutoutMipped());

        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        ItemColors itemColors = Minecraft.getInstance().getItemColors();

        blockColors.register(
            // TODO Biome colors coming later
            (state, world, pos, index) -> 0x11783F,
            MURKY_GRASS_BLOCK
        );
        itemColors.register(
            (item, index) -> 0x11783F,
            MURKY_GRASS_BLOCK
        );
    }

    private static <B extends Block> B block(String id, B block) {
        block.setRegistryName(NaturesDebris.resLoc(id));
        return block;
    }

    private static PlantBlock testPlant(String id) {
        return block(id, new TestPlantBlock(
            PlantBlock.Properties.create(Material.TALL_PLANTS)
                                 .nonOpaque()
                                 .blockVision((state, world, pos) -> false)
                                 .suffocates((state, world, pos) -> false)
                                 .zeroHardnessAndResistance()
                                 .fluidLogic(LevelledFluidLogic.INSTANCE)
                                 .offset(AbstractBlock.OffsetType.XYZ)
        ));
    }

    private static Block rock(String id, double hardness, double resistance, boolean dark) {
        return block(id, new Block(
            Block.Properties.create(Material.ROCK, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                            .hardnessAndResistance((float) hardness, (float) resistance)
                            .harvestTool(ToolType.PICKAXE)
        ));
    }

    private static Block rockLantern(String id, double hardness, double resistance, boolean dark) {
        return block(id, new Block(
            Block.Properties.create(Material.ROCK, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                            .hardnessAndResistance((float) hardness, (float) resistance)
                            .harvestTool(ToolType.PICKAXE)
                            .sound(SoundType.BONE)
                            .luminance(state -> 15)
                            .emissiveLighting((state, world, pos) -> true)
        ));
    }

    private static Block rockPillar(String id, double hardness, double resistance, boolean dark) {
        return block(id, new RotatedPillarBlock(
            Block.Properties.create(Material.ROCK, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                            .hardnessAndResistance((float) hardness, (float) resistance)
                            .harvestTool(ToolType.PICKAXE)
        ));
    }

    private static Block rockSlab(String id, double hardness, double resistance, boolean dark) {
        return block(id, new SlabBlock(
            Block.Properties.create(Material.ROCK, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                            .hardnessAndResistance((float) hardness, (float) resistance)
                            .harvestTool(ToolType.PICKAXE)
        ));
    }

    private static Block rockStairs(String id, double hardness, double resistance, boolean dark) {
        return block(id, new SimpleStairsBlock(
            Block.Properties.create(Material.ROCK, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                            .hardnessAndResistance((float) hardness, (float) resistance)
                            .harvestTool(ToolType.PICKAXE)
        ));
    }

    private static Block rockStep(String id, double hardness, double resistance, boolean dark) {
        return block(id, new StepBlock(
            Block.Properties.create(Material.ROCK, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                            .hardnessAndResistance((float) hardness, (float) resistance)
                            .harvestTool(ToolType.PICKAXE)
        ));
    }

    private static Block dirt(String id, double strength, MaterialColor color, SoundType sound) {
        return block(id, new MurkyDirtBlock(
            Block.Properties.create(Material.EARTH, color)
                            .hardnessAndResistance((float) strength)
                            .sound(sound)
                            .harvestTool(ToolType.SHOVEL)
        ));
    }

    private static Block grassPath(String id, double strength, MaterialColor color, SoundType sound) {
        return block(id, new MurkyGrassPathBlock(
            Block.Properties.create(Material.EARTH, color)
                            .hardnessAndResistance((float) strength)
                            .sound(sound)
                            .harvestTool(ToolType.SHOVEL)
                            .blockVision((state, world, pos) -> true)
        ));
    }

    private static Block humus(String id, double strength, MaterialColor color, SoundType sound) {
        return block(id, new MurkyHumusBlock(
            Block.Properties.create(Material.EARTH, color)
                            .hardnessAndResistance((float) strength)
                            .sound(sound)
                            .harvestTool(ToolType.SHOVEL)
                            .tickRandomly()
        ));
    }

    private static Block leafyHumus(String id, double strength, MaterialColor color, SoundType sound) {
        return block(id, new LeafyHumusBlock(
            Block.Properties.create(Material.EARTH, color)
                            .hardnessAndResistance((float) strength)
                            .sound(sound)
                            .harvestTool(ToolType.SHOVEL)
                            .tickRandomly()
        ));
    }

    private static Block grass(String id, double strength, MaterialColor color, SoundType sound) {
        return block(id, new MurkyGrassBlock(
            Block.Properties.create(Material.EARTH, color)
                            .hardnessAndResistance((float) strength)
                            .sound(sound)
                            .harvestTool(ToolType.SHOVEL)
                            .tickRandomly()
        ));
    }

    private static Block sand(String id, double strength, MaterialColor color, SoundType sound) {
        return block(id, new MurkySandBlock(
            Block.Properties.create(Material.EARTH, color)
                            .hardnessAndResistance((float) strength)
                            .sound(sound)
                            .harvestTool(ToolType.SHOVEL)
                            .tickRandomly()
        ));
    }

    private static Block clay(String id) {
        return block(id, new Block(
            Block.Properties.create(Material.CLAY, MaterialColor.BLUE_TERRACOTTA)
                            .sound(SoundType.GROUND)
                            .hardnessAndResistance(.5f)
                            .sound(SoundType.GROUND)
                            .harvestTool(ToolType.SHOVEL)
        ));
    }

    private static Block terracotta(String id) {
        return block(id, new Block(
            Block.Properties.create(Material.ROCK, MaterialColor.BLUE_TERRACOTTA)
                            .hardnessAndResistance(1.25f, 4.2f)
                            .sound(SoundType.STONE)
                            .harvestTool(ToolType.PICKAXE)
        ));
    }

    private static Block log(String id, MaterialColor color) {
        return block(id, new RotatedPillarBlock(
            Block.Properties.create(Material.WOOD, color)
                            .sound(SoundType.WOOD)
                            .hardnessAndResistance(2)
                            .harvestTool(ToolType.AXE)
        ));
    }

    private static Block stripableLog(String id, MaterialColor color, Supplier<Block> stripped) {
        return block(id, new StrippableLogBlock(
            Block.Properties.create(Material.WOOD, color)
                            .sound(SoundType.WOOD)
                            .hardnessAndResistance(2)
                            .harvestTool(ToolType.AXE),
            stripped
        ));
    }

    private static Block wood(String id, MaterialColor color) {
        return block(id, new Block(
            Block.Properties.create(Material.WOOD, color)
                            .sound(SoundType.WOOD)
                            .hardnessAndResistance(2)
                            .harvestTool(ToolType.AXE)
        ));
    }

    private static Block woodSlab(String id, MaterialColor color) {
        return block(id, new SlabBlock(
            Block.Properties.create(Material.WOOD, color)
                            .sound(SoundType.WOOD)
                            .hardnessAndResistance(2)
                            .harvestTool(ToolType.AXE)
        ));
    }

    private static Block woodStairs(String id, MaterialColor color) {
        return block(id, new SimpleStairsBlock(
            Block.Properties.create(Material.WOOD, color)
                            .sound(SoundType.WOOD)
                            .hardnessAndResistance(2)
                            .harvestTool(ToolType.AXE)
        ));
    }

    private static Block woodStep(String id, MaterialColor color) {
        return block(id, new StepBlock(
            Block.Properties.create(Material.WOOD, color)
                            .sound(SoundType.WOOD)
                            .hardnessAndResistance(2)
                            .harvestTool(ToolType.AXE)
        ));
    }

    private static Block fence(String id, MaterialColor color) {
        return block(id, new FenceBlock(
            Block.Properties.create(Material.WOOD, color)
                            .sound(SoundType.WOOD)
                            .hardnessAndResistance(2)
                            .harvestTool(ToolType.AXE)
        ));
    }

    private static Block rockWall(String id, double hardness, double resistance, boolean dark) {
        return block(id, new WallBlock(
            Block.Properties.create(Material.ROCK, dark ? MaterialColor.BLACK : MaterialColor.STONE)
                            .hardnessAndResistance((float) hardness, (float) resistance)
                            .harvestTool(ToolType.PICKAXE)
        ));
    }

    private static BlockItem item(Block block, Item.Properties props, ItemSubgroup subgroup, int index) {
        ResourceLocation id = block.getRegistryName();
        assert id != null;
        BlockItem item = new BlockItem(block, props);
        item.setRegistryName(id);
        if (subgroup != null) {
            if (index == -1)
                subgroup.addItem(item);
            else
                subgroup.addItem(item, index);
        }
        return item;
    }

    private static BlockItem item(Block block, Item.Properties props, ItemSubgroup subgroup) {
        return item(block, props, subgroup, -1);
    }

    private static BlockItem item(Block block, ItemGroup group, ItemSubgroup subgroup, int index) {
        return item(block, new Item.Properties().group(group), subgroup, index);
    }

    private static BlockItem item(Block block, ItemGroup group, ItemSubgroup subgroup) {
        return item(block, new Item.Properties().group(group), subgroup);
    }

    private static BlockItem item(Block block, ItemGroup group) {
        return item(block, new Item.Properties().group(group), null);
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static Block inj() {
        return null;
    }
}
