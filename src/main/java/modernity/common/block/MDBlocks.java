/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 02 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import modernity.api.block.IColoredBlock;
import modernity.common.block.base.*;
import modernity.common.block.dirt.DirtlikeBlock;
import modernity.common.block.dirt.GrassBlock;
import modernity.common.block.dirt.LeafyDirtlikeBlock;
import modernity.common.block.dirt.SnowyDirtlikeBlock;
import modernity.common.block.dirt.logic.MDDirtLogics;
import modernity.common.block.farmland.FarmlandBlock;
import modernity.common.block.farmland.GrassFarmlandBlock;
import modernity.common.block.fluid.RegularFluidBlock;
import modernity.common.block.misc.*;
import modernity.common.block.plant.*;
import modernity.common.block.portal.HorizontalPortalFrameBlock;
import modernity.common.block.portal.PortalCornerBlock;
import modernity.common.block.portal.VerticalPortalFrameBlock;
import modernity.common.block.tree.*;
import modernity.common.block.utils.RockFurnaceBlock;
import modernity.common.block.utils.WorkbenchBlock;
import modernity.common.fluid.MDFluids;
import modernity.common.generator.tree.MDTrees;
import modernity.common.item.MDItemGroup;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Object holder for modernity blocks.
 */
@ObjectHolder( "modernity" )
public final class MDBlocks {
    private static final ArrayList<Block> ITEM_BLOCKS = new ArrayList<>();
    private static final RegistryHandler<Block> BLOCKS = new RegistryHandler<>( "modernity" );
    private static final RegistryHandler<Item> ITEMS = new RegistryHandler<>( "modernity" );




    /* ==== NATURE BLOCKS ==== */

    // Rocks
    public static final Block ROCK = blockItem( "rock", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block DARKROCK = blockItem( "darkrock", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block UNBREAKABLE_STONE = blockItem( "unbreakable_stone", new Block( rock( MaterialColor.BLACK, - 1, 3600000 ) ), MDItemGroup.BLOCKS, "modern_bedrock" );
    public static final Block HARDENED_ROCK = blockItem( "hardened_rock", new Block( rock( MaterialColor.BLACK, 3, 15 ) ), MDItemGroup.BLOCKS );
    public static final Block LIMESTONE = blockItem( "limestone", new Block( rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroup.BLOCKS );
    public static final Block ASPHALT_CONCRETE = blockItem( "asphalt_concrete", new Block( asphalt() ), MDItemGroup.BLOCKS );
    public static final Block SUMESTONE = blockItem( "sumestone", new Block( rock( MaterialColor.STONE, 1.8, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block DARK_SUMESTONE = blockItem( "dark_sumestone", new Block( rock( MaterialColor.STONE, 1.8, 6 ) ), MDItemGroup.BLOCKS );

    public static final Block MURKY_TERRACOTTA = blockItem( "murky_terracotta", new Block( rock( MaterialColor.CLAY, 1, 5 ) ), MDItemGroup.BLOCKS );

    // Soils
    public static final DirtlikeBlock MURKY_DIRT = blockItem( "murky_dirt", new DirtlikeBlock( MDDirtLogics.DIRT_LOGIC, dirt( MaterialColor.DIRT, false ) ), MDItemGroup.BLOCKS, "dark_dirt" );
    public static final DirtlikeBlock MURKY_GRASS_BLOCK = blockItem( "murky_grass_block", new GrassBlock( MDDirtLogics.GRASS_LOGIC, dirt( MaterialColor.GRASS, true ) ), MDItemGroup.BLOCKS, "dark_grass_block" );
    public static final DigableFallBlock MURKY_SAND = blockItem( "murky_sand", new DigableFallBlock( 0x826f52, dust( MaterialColor.SAND, false ) ), MDItemGroup.BLOCKS, "dark_sand" );
    public static final DigableBlock MURKY_CLAY = blockItem( "murky_clay", new DigableBlock( clay( MaterialColor.GRAY_TERRACOTTA ) ), MDItemGroup.BLOCKS, "dark_clay" );
    public static final DigableBlock MURKY_COARSE_DIRT = blockItem( "murky_coarse_dirt", new DigableBlock( dirt( MaterialColor.DIRT, false ) ), MDItemGroup.BLOCKS, "coarse_dark_dirt" );
    public static final DigableBlock SALTY_DIRT = blockItem( "salty_dirt", new DigableBlock( dirt( MaterialColor.DIRT, false ) ), MDItemGroup.BLOCKS );
    public static final DirtlikeBlock MURKY_HUMUS = blockItem( "murky_humus", new SnowyDirtlikeBlock( MDDirtLogics.HUMUS_LOGIC, dirt( MaterialColor.ORANGE_TERRACOTTA, true ) ), MDItemGroup.BLOCKS );
    public static final DirtlikeBlock LEAFY_HUMUS = blockItem( "leafy_humus", new LeafyDirtlikeBlock( MDDirtLogics.LEAFY_HUMUS_LOGIC, dirt( MaterialColor.ORANGE_TERRACOTTA, true ) ), MDItemGroup.BLOCKS, "dark_podzol" );
    public static final DirtlikeBlock MURKY_PODZOL = blockItem( "murky_podzol", new SnowyDirtlikeBlock( MDDirtLogics.PODZOL_LOGIC, dirt( MaterialColor.DIRT, true ) ), MDItemGroup.BLOCKS );
    public static final DirtlikeBlock HEATH_BLOCK = blockItem( "heath_block", new DirtlikeBlock( MDDirtLogics.HEATH_LOGIC, dirt( MaterialColor.FOLIAGE, true ) ), MDItemGroup.BLOCKS );
    public static final DigableBlock ACID_DIRT = blockItem( "acid_dirt", new DigableBlock( dirt( MaterialColor.DIRT, true ) ), MDItemGroup.BLOCKS );
    public static final StickyBlock MUD = blockItem( "mud", new DigableStickyBlock( dirt( MaterialColor.BROWN_TERRACOTTA, false ).sound( MDSoundTypes.MUD ) ), MDItemGroup.BLOCKS );
    public static final DigableFallBlock REGOLITH = blockItem( "regolith", new DigableFallBlock( 0x737d8c, dust( MaterialColor.STONE, true ) ), MDItemGroup.BLOCKS, "rock_gravel" );

    // Fluids
    public static final RegularFluidBlock MURKY_WATER = blockOnly( "murky_water", new RegularFluidBlock( MDFluids.MURKY_WATER, fluid( Material.WATER, MaterialColor.WATER ) ), "modernized_water" );
    public static final RegularFluidBlock CLEAN_WATER = blockOnly( "clean_water", new RegularFluidBlock( MDFluids.CLEAN_WATER, fluid( Material.WATER, MaterialColor.WATER ) ) );
    public static final RegularFluidBlock MOLTEN_ROCK = blockOnly( "molten_rock", new RegularFluidBlock( MDFluids.MOLTEN_ROCK, fluid( Material.LAVA, MaterialColor.TNT ).lightValue( 15 ) ), "heatrock" );

    public static final PuddleBlock PUDDLE = blockOnly( "puddle", new PuddleBlock( Block.Properties.create( Material.WATER, MaterialColor.WATER ) ) );



    /* ==== TREE BLOCKS ==== */

    // Blackwood
    public static final AxisBlock STRIPPED_BLACKWOOD_LOG = blockItem( "stripped_blackwood_log", new AxisBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.BLOCKS );
    public static final AxisBlock BLACKWOOD_LOG = blockItem( "blackwood_log", new StripableLogBlock( () -> STRIPPED_BLACKWOOD_LOG, wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.BLOCKS );
    public static final AxisBlock STRIPPED_BLACKWOOD = blockItem( "stripped_blackwood", new AxisBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.BLOCKS );
    public static final AxisBlock BLACKWOOD = blockItem( "blackwood", new StripableLogBlock( () -> STRIPPED_BLACKWOOD, wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.BLOCKS );
    public static final BlackwoodSaplingBlock BLACKWOOD_SAPLING = blockItem( "blackwood_sapling", new BlackwoodSaplingBlock( strongPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS );
    public static final HangLeavesBlock BLACKWOOD_LEAVES = blockItem( "blackwood_leaves", new BlackwoodLeavesBlock( MDBlockTags.BLACKWOOD_LOG, leaves( MaterialColor.FOLIAGE, 0.2 ) ), MDItemGroup.PLANTS );

    // Inver
    public static final AxisBlock STRIPPED_INVER_LOG = blockItem( "stripped_inver_log", new AxisBlock( wood( MaterialColor.WOOD ) ), MDItemGroup.BLOCKS );
    public static final AxisBlock INVER_LOG = blockItem( "inver_log", new StripableLogBlock( () -> STRIPPED_INVER_LOG, wood( MaterialColor.WOOD ) ), MDItemGroup.BLOCKS );
    public static final AxisBlock STRIPPED_INVER = blockItem( "stripped_inver_wood", new AxisBlock( wood( MaterialColor.WOOD ) ), MDItemGroup.BLOCKS );
    public static final AxisBlock INVER_WOOD = blockItem( "inver_wood", new StripableLogBlock( () -> STRIPPED_INVER, wood( MaterialColor.WOOD ) ), MDItemGroup.BLOCKS );
    public static final SimpleSaplingBlock INVER_SAPLING = blockItem( "inver_sapling", new SimpleSaplingBlock( () -> MDTrees.INVER, strongPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS );
    public static final SimpleSaplingBlock RED_INVER_SAPLING = blockItem( "red_inver_sapling", new SimpleSaplingBlock( () -> MDTrees.RED_INVER, strongPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS );
    public static final DecayLeavesBlock INVER_LEAVES = blockItem( "inver_leaves", new InverLeavesBlock( MDBlockTags.INVER_LOG, leaves( MaterialColor.FOLIAGE, 0.2 ), false ), MDItemGroup.PLANTS );
    public static final DecayLeavesBlock RED_INVER_LEAVES = blockItem( "red_inver_leaves", new InverLeavesBlock( MDBlockTags.INVER_LOG, leaves( MaterialColor.RED, 0.2 ), true ), MDItemGroup.PLANTS );



    /* ==== FARMING ==== */

    public static final FarmlandBlock MURKY_DIRT_FARMLAND = blockItem( "murky_dirt_farmland", new FarmlandBlock( MDDirtLogics.DIRT_LOGIC_FL, dirt( MaterialColor.DIRT, false ) ), MDItemGroup.BLOCKS );
    public static final FarmlandBlock MURKY_GRASS_BLOCK_FARMLAND = blockItem( "murky_grass_block_farmland", new GrassFarmlandBlock( MDDirtLogics.GRASS_LOGIC_FL, dirt( MaterialColor.DIRT, false ) ), MDItemGroup.BLOCKS );
    public static final FarmlandBlock LEAFY_HUMUS_FARMLAND = blockItem( "leafy_humus_farmland", new FarmlandBlock( MDDirtLogics.LEAFY_HUMUS_LOGIC_FL, dirt( MaterialColor.DIRT, false ) ), MDItemGroup.BLOCKS );
    public static final FarmlandBlock MURKY_HUMUS_FARMLAND = blockItem( "murky_humus_farmland", new FarmlandBlock( MDDirtLogics.HUMUS_LOGIC_FL, dirt( MaterialColor.DIRT, false ) ), MDItemGroup.BLOCKS );
    public static final FarmlandBlock MURKY_PODZOL_FARMLAND = blockItem( "murky_podzol_farmland", new FarmlandBlock( MDDirtLogics.PODZOL_LOGIC_FL, dirt( MaterialColor.DIRT, false ) ), MDItemGroup.BLOCKS );
    public static final FarmlandBlock HEATH_FARMLAND = blockItem( "heath_farmland", new FarmlandBlock( MDDirtLogics.HEATH_LOGIC_FL, dirt( MaterialColor.DIRT, false ) ), MDItemGroup.BLOCKS );



    /* ==== BUILDING BLOCKS ==== */

    // Blocks
    public static final Block ROCK_BRICKS = blockItem( "rock_bricks", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block SMOOTH_ROCK = blockItem( "smooth_rock", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block ROCK_TILES = blockItem( "rock_tiles", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block MOSSY_ROCK = blockItem( "mossy_rock", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block MOSSY_ROCK_BRICKS = blockItem( "mossy_rock_bricks", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block MOSSY_ROCK_TILES = blockItem( "mossy_rock_tiles", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block CRACKED_ROCK_BRICKS = blockItem( "cracked_rock_bricks", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block CRACKED_ROCK_TILES = blockItem( "cracked_rock_tiles", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block POLISHED_ROCK = blockItem( "polished_rock", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block CHISELED_ROCK = blockItem( "chiseled_rock", new Block( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final Block DARKROCK_BRICKS = blockItem( "darkrock_bricks", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block SMOOTH_DARKROCK = blockItem( "smooth_darkrock", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block DARKROCK_TILES = blockItem( "darkrock_tiles", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block MOSSY_DARKROCK = blockItem( "mossy_darkrock", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block MOSSY_DARKROCK_BRICKS = blockItem( "mossy_darkrock_bricks", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block MOSSY_DARKROCK_TILES = blockItem( "mossy_darkrock_tiles", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block CRACKED_DARKROCK_BRICKS = blockItem( "cracked_darkrock_bricks", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block CRACKED_DARKROCK_TILES = blockItem( "cracked_darkrock_tiles", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block POLISHED_DARKROCK = blockItem( "polished_darkrock", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block CHISELED_DARKROCK = blockItem( "chiseled_darkrock", new Block( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final Block MURK_BRICKS = blockItem( "murk_bricks", new Block( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block MURK_TILES = blockItem( "murk_tiles", new Block( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block CRACKED_MURK_BRICKS = blockItem( "cracked_murk_bricks", new Block( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final Block CRACKED_MURK_TILES = blockItem( "cracked_murk_tiles", new Block( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final Block BLACKWOOD_PLANKS = blockItem( "blackwood_planks", new Block( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.BLOCKS );
    public static final Block INVER_PLANKS = blockItem( "inver_planks", new Block( wood( MaterialColor.WOOD ) ), MDItemGroup.BLOCKS );

    // Slabs
    public static final VerticalSlabBlock ROCK_SLAB = blockItem( "rock_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock ROCK_BRICKS_SLAB = blockItem( "rock_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock SMOOTH_ROCK_SLAB = blockItem( "smooth_rock_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock ROCK_TILES_SLAB = blockItem( "rock_tiles_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock MOSSY_ROCK_SLAB = blockItem( "mossy_rock_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock MOSSY_ROCK_BRICKS_SLAB = blockItem( "mossy_rock_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock MOSSY_ROCK_TILES_SLAB = blockItem( "mossy_rock_tiles_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock CRACKED_ROCK_BRICKS_SLAB = blockItem( "cracked_rock_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock CRACKED_ROCK_TILES_SLAB = blockItem( "cracked_rock_tiles_slab", new VerticalSlabBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final VerticalSlabBlock DARKROCK_SLAB = blockItem( "darkrock_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock DARKROCK_BRICKS_SLAB = blockItem( "darkrock_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock SMOOTH_DARKROCK_SLAB = blockItem( "smooth_darkrock_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock DARKROCK_TILES_SLAB = blockItem( "darkrock_tiles_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock MOSSY_DARKROCK_SLAB = blockItem( "mossy_darkrock_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock MOSSY_DARKROCK_BRICKS_SLAB = blockItem( "mossy_darkrock_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock MOSSY_DARKROCK_TILES_SLAB = blockItem( "mossy_darkrock_tiles_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock CRACKED_DARKROCK_BRICKS_SLAB = blockItem( "cracked_darkrock_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock CRACKED_DARKROCK_TILES_SLAB = blockItem( "cracked_darkrock_tiles_slab", new VerticalSlabBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final VerticalSlabBlock MURK_BRICKS_SLAB = blockItem( "murk_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock MURK_TILES_SLAB = blockItem( "murk_tiles_slab", new VerticalSlabBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock CRACKED_MURK_BRICKS_SLAB = blockItem( "cracked_murk_bricks_slab", new VerticalSlabBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock CRACKED_MURK_TILES_SLAB = blockItem( "cracked_murk_tiles_slab", new VerticalSlabBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final VerticalSlabBlock ASPHALT_CONCRETE_SLAB = blockItem( "asphalt_concrete_slab", new VerticalSlabBlock( asphalt() ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock LIMESTONE_SLAB = blockItem( "limestone_slab", new VerticalSlabBlock( rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroup.BLOCKS );

    public static final VerticalSlabBlock BLACKWOOD_SLAB = blockItem( "blackwood_slab", new VerticalSlabBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.BLOCKS );
    public static final VerticalSlabBlock INVER_SLAB = blockItem( "inver_slab", new VerticalSlabBlock( wood( MaterialColor.WOOD ) ), MDItemGroup.BLOCKS );

    // Stairs
    public static final StairsBlock ROCK_STAIRS = blockItem( "rock_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock ROCK_BRICKS_STAIRS = blockItem( "rock_bricks_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock SMOOTH_ROCK_STAIRS = blockItem( "smooth_rock_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock ROCK_TILES_STAIRS = blockItem( "rock_tiles_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_ROCK_STAIRS = blockItem( "mossy_rock_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_ROCK_BRICKS_STAIRS = blockItem( "mossy_rock_bricks_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_ROCK_TILES_STAIRS = blockItem( "mossy_rock_tiles_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_ROCK_BRICKS_STAIRS = blockItem( "cracked_rock_bricks_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_ROCK_TILES_STAIRS = blockItem( "cracked_rock_tiles_stairs", new StairsBlock( false, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final StairsBlock DARKROCK_STAIRS = blockItem( "darkrock_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock DARKROCK_BRICKS_STAIRS = blockItem( "darkrock_bricks_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock SMOOTH_DARKROCK_STAIRS = blockItem( "smooth_darkrock_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock DARKROCK_TILES_STAIRS = blockItem( "darkrock_tiles_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_DARKROCK_STAIRS = blockItem( "mossy_darkrock_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_DARKROCK_BRICKS_STAIRS = blockItem( "mossy_darkrock_bricks_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_DARKROCK_TILES_STAIRS = blockItem( "mossy_darkrock_tiles_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_DARKROCK_BRICKS_STAIRS = blockItem( "cracked_darkrock_bricks_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_DARKROCK_TILES_STAIRS = blockItem( "cracked_darkrock_tiles_stairs", new StairsBlock( false, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final StairsBlock MURK_BRICKS_STAIRS = blockItem( "murk_bricks_stairs", new StairsBlock( false, rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MURK_TILES_STAIRS = blockItem( "murk_tiles_stairs", new StairsBlock( false, rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_MURK_BRICKS_STAIRS = blockItem( "cracked_murk_bricks_stairs", new StairsBlock( false, rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_MURK_TILES_STAIRS = blockItem( "cracked_murk_tiles_stairs", new StairsBlock( false, rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final StairsBlock ASPHALT_CONCRETE_STAIRS = blockItem( "asphalt_concrete_stairs", new StairsBlock( false, asphalt() ), MDItemGroup.BLOCKS );
    public static final StairsBlock LIMESTONE_STAIRS = blockItem( "limestone_stairs", new StairsBlock( false, rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroup.BLOCKS );

    public static final StairsBlock BLACKWOOD_STAIRS = blockItem( "blackwood_stairs", new StairsBlock( false, wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock INVER_STAIRS = blockItem( "inver_stairs", new StairsBlock( false, wood( MaterialColor.WOOD ) ), MDItemGroup.BLOCKS );

    // Steps
    public static final StairsBlock ROCK_STEP = blockItem( "rock_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock ROCK_BRICKS_STEP = blockItem( "rock_bricks_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock SMOOTH_ROCK_STEP = blockItem( "smooth_rock_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock ROCK_TILES_STEP = blockItem( "rock_tiles_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_ROCK_STEP = blockItem( "mossy_rock_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_ROCK_BRICKS_STEP = blockItem( "mossy_rock_bricks_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_ROCK_TILES_STEP = blockItem( "mossy_rock_tiles_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_ROCK_BRICKS_STEP = blockItem( "cracked_rock_bricks_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_ROCK_TILES_STEP = blockItem( "cracked_rock_tiles_step", new StairsBlock( true, rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final StairsBlock DARKROCK_STEP = blockItem( "darkrock_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock DARKROCK_BRICKS_STEP = blockItem( "darkrock_bricks_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock SMOOTH_DARKROCK_STEP = blockItem( "smooth_darkrock_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock DARKROCK_TILES_STEP = blockItem( "darkrock_tiles_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_DARKROCK_STEP = blockItem( "mossy_darkrock_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_DARKROCK_BRICKS_STEP = blockItem( "mossy_darkrock_bricks_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MOSSY_DARKROCK_TILES_STEP = blockItem( "mossy_darkrock_tiles_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_DARKROCK_BRICKS_STEP = blockItem( "cracked_darkrock_bricks_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_DARKROCK_TILES_STEP = blockItem( "cracked_darkrock_tiles_step", new StairsBlock( true, rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final StairsBlock MURK_BRICKS_STEP = blockItem( "murk_bricks_step", new StairsBlock( true, rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock MURK_TILES_STEP = blockItem( "murk_tiles_step", new StairsBlock( true, rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_MURK_BRICKS_STEP = blockItem( "cracked_murk_bricks_step", new StairsBlock( true, rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock CRACKED_MURK_TILES_STEP = blockItem( "cracked_murk_tiles_step", new StairsBlock( true, rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final StairsBlock ASPHALT_CONCRETE_STEP = blockItem( "asphalt_concrete_step", new StairsBlock( true, asphalt() ), MDItemGroup.BLOCKS );
    public static final StairsBlock LIMESTONE_STEP = blockItem( "limestone_step", new StairsBlock( true, rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroup.BLOCKS );

    public static final StairsBlock BLACKWOOD_STEP = blockItem( "blackwood_step", new StairsBlock( true, wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.BLOCKS );
    public static final StairsBlock INVER_STEP = blockItem( "inver_step", new StairsBlock( true, wood( MaterialColor.WOOD ) ), MDItemGroup.BLOCKS );

    // Corners
    public static final CornerBlock ROCK_CORNER = blockItem( "rock_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock ROCK_BRICKS_CORNER = blockItem( "rock_bricks_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock SMOOTH_ROCK_CORNER = blockItem( "smooth_rock_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock ROCK_TILES_CORNER = blockItem( "rock_tiles_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock MOSSY_ROCK_CORNER = blockItem( "mossy_rock_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock MOSSY_ROCK_BRICKS_CORNER = blockItem( "mossy_rock_bricks_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock MOSSY_ROCK_TILES_CORNER = blockItem( "mossy_rock_tiles_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock CRACKED_ROCK_BRICKS_CORNER = blockItem( "cracked_rock_bricks_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock CRACKED_ROCK_TILES_CORNER = blockItem( "cracked_rock_tiles_corner", new CornerBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final CornerBlock DARKROCK_CORNER = blockItem( "darkrock_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock DARKROCK_BRICKS_CORNER = blockItem( "darkrock_bricks_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock SMOOTH_DARKROCK_CORNER = blockItem( "smooth_darkrock_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock DARKROCK_TILES_CORNER = blockItem( "darkrock_tiles_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock MOSSY_DARKROCK_CORNER = blockItem( "mossy_darkrock_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock MOSSY_DARKROCK_BRICKS_CORNER = blockItem( "mossy_darkrock_bricks_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock MOSSY_DARKROCK_TILES_CORNER = blockItem( "mossy_darkrock_tiles_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock CRACKED_DARKROCK_BRICKS_CORNER = blockItem( "cracked_darkrock_bricks_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock CRACKED_DARKROCK_TILES_CORNER = blockItem( "cracked_darkrock_tiles_corner", new CornerBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final CornerBlock MURK_BRICKS_CORNER = blockItem( "murk_bricks_corner", new CornerBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock MURK_TILES_CORNER = blockItem( "murk_tiles_corner", new CornerBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock CRACKED_MURK_BRICKS_CORNER = blockItem( "cracked_murk_bricks_corner", new CornerBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock CRACKED_MURK_TILES_CORNER = blockItem( "cracked_murk_tiles_corner", new CornerBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.BLOCKS );

    public static final CornerBlock ASPHALT_CONCRETE_CORNER = blockItem( "asphalt_concrete_corner", new CornerBlock( asphalt() ), MDItemGroup.BLOCKS );
    public static final CornerBlock LIMESTONE_CORNER = blockItem( "limestone_corner", new CornerBlock( rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroup.BLOCKS );

    public static final CornerBlock BLACKWOOD_CORNER = blockItem( "blackwood_corner", new CornerBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.BLOCKS );
    public static final CornerBlock INVER_CORNER = blockItem( "inver_corner", new CornerBlock( wood( MaterialColor.WOOD ) ), MDItemGroup.BLOCKS );

    // Walls
    public static final WallBlock ROCK_WALL = blockItem( "rock_wall", new WallBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock ROCK_BRICKS_WALL = blockItem( "rock_bricks_wall", new WallBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock SMOOTH_ROCK_WALL = blockItem( "smooth_rock_wall", new WallBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock MOSSY_ROCK_WALL = blockItem( "mossy_rock_wall", new WallBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock MOSSY_ROCK_BRICKS_WALL = blockItem( "mossy_rock_bricks_wall", new WallBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock CRACKED_ROCK_BRICKS_WALL = blockItem( "cracked_rock_bricks_wall", new WallBlock( rock( MaterialColor.STONE, 1.5, 6 ) ), MDItemGroup.DECORATIVES );

    public static final WallBlock DARKROCK_WALL = blockItem( "darkrock_wall", new WallBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock DARKROCK_BRICKS_WALL = blockItem( "darkrock_bricks_wall", new WallBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock SMOOTH_DARKROCK_WALL = blockItem( "smooth_darkrock_wall", new WallBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock MOSSY_DARKROCK_WALL = blockItem( "mossy_darkrock_wall", new WallBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock MOSSY_DARKROCK_BRICKS_WALL = blockItem( "mossy_darkrock_bricks_wall", new WallBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock CRACKED_DARKROCK_BRICKS_WALL = blockItem( "cracked_darkrock_bricks_wall", new WallBlock( rock( MaterialColor.BLACK, 1.5, 6 ) ), MDItemGroup.DECORATIVES );

    public static final WallBlock MURK_BRICKS_WALL = blockItem( "murk_bricks_wall", new WallBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.DECORATIVES );
    public static final WallBlock CRACKED_MURK_BRICKS_WALL = blockItem( "cracked_murk_bricks_wall", new WallBlock( rock( MaterialColor.CLAY, 1.5, 6 ) ), MDItemGroup.DECORATIVES );

    public static final WallBlock ASPHALT_CONCRETE_WALL = blockItem( "asphalt_concrete_wall", new WallBlock( asphalt() ), MDItemGroup.DECORATIVES );
    public static final WallBlock LIMESTONE_WALL = blockItem( "limestone_wall", new WallBlock( rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ) ), MDItemGroup.DECORATIVES );

    // Fences & Gates
    public static final FenceBlock BLACKWOOD_FENCE = blockItem( "blackwood_fence", new FenceBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.DECORATIVES );
    public static final FenceBlock INVER_FENCE = blockItem( "inver_fence", new FenceBlock( wood( MaterialColor.WOOD ) ), MDItemGroup.DECORATIVES );

    public static final FenceGateBlock BLACKWOOD_FENCE_GATE = blockItem( "blackwood_fence_gate", new FenceGateBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.DECORATIVES );
    public static final FenceGateBlock INVER_FENCE_GATE = blockItem( "inver_fence_gate", new FenceGateBlock( wood( MaterialColor.WOOD ) ), MDItemGroup.DECORATIVES );

    // Doors & Trap Doors
    public static final DoorBlock BLACKWOOD_DOOR = blockItem( "blackwood_door", new DoorBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.DECORATIVES );
    public static final DoorBlock INVER_DOOR = blockItem( "inver_door", new DoorBlock( wood( MaterialColor.WOOD ) ), MDItemGroup.DECORATIVES );
    public static final DoorBlock ALUMINIUM_DOOR = blockItem( "aluminium_door", new DoorBlock( metal( MaterialColor.IRON ) ), MDItemGroup.DECORATIVES );

    public static final ExtTrapDoorBlock BLACKWOOD_TRAPDOOR = blockItem( "blackwood_trapdoor", new ExtTrapDoorBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.DECORATIVES );
    public static final ExtTrapDoorBlock INVER_TRAPDOOR = blockItem( "inver_trapdoor", new ExtTrapDoorBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.DECORATIVES );
    public static final ExtTrapDoorBlock ALUMINIUM_TRAPDOOR = blockItem( "aluminium_trapdoor", new ExtTrapDoorBlock( metal( MaterialColor.IRON ) ), MDItemGroup.DECORATIVES );

    // Pillars
    public static final PillarBlock ROCK_PILLAR = blockItem( "rock_pillar", new PillarBlock( rock( MaterialColor.STONE, 1.5, 6 ), 14 ), MDItemGroup.DECORATIVES );
    public static final PillarBlock DARKROCK_PILLAR = blockItem( "darkrock_pillar", new PillarBlock( rock( MaterialColor.BLACK, 1.5, 6 ), 14 ), MDItemGroup.DECORATIVES );
    public static final PillarBlock MURK_PILLAR = blockItem( "murk_pillar", new PillarBlock( rock( MaterialColor.CLAY, 1.5, 6 ), 14 ), MDItemGroup.DECORATIVES );

    // Misc
    public static final TranslucentBlock MURKY_GLASS = blockItem( "murky_glass", new TranslucentBlock( glass() ), MDItemGroup.BLOCKS, "dark_glass" );
    public static final PanelBlock MURKY_GLASS_PANE = blockItem( "murky_glass_pane", new TranslucentPanelBlock( glass() ), MDItemGroup.DECORATIVES, "dark_glass_pane" );

    public static final PillarBlock BLACKWOOD_POLE = blockItem( "blackwood_pole", new PillarBlock( wood( MaterialColor.BLACK_TERRACOTTA ), 4 ), MDItemGroup.DECORATIVES );
    public static final PillarBlock INVER_POLE = blockItem( "inver_pole", new PillarBlock( wood( MaterialColor.WOOD ), 4 ), MDItemGroup.DECORATIVES );

    public static final MurkyGrassPathBlock MURKY_GRASS_PATH = blockItem( "murky_grass_path", new MurkyGrassPathBlock( dirt( MaterialColor.YELLOW, true ) ), MDItemGroup.DECORATIVES );

    public static final HedgeBlock BLACKWOOD_HEDGE = blockItem( "blackwood_hedge", new ColoredHedgeBlock( leaves( MaterialColor.FOLIAGE, 0.3 ).sound( SoundType.SWEET_BERRY_BUSH ), 0 ), MDItemGroup.DECORATIVES );
    public static final HedgeBlock INVER_HEDGE = blockItem( "inver_hedge", new ColoredHedgeBlock( leaves( MaterialColor.FOLIAGE, 0.3 ).sound( SoundType.SWEET_BERRY_BUSH ), 1 ), MDItemGroup.DECORATIVES );
    public static final HedgeBlock RED_INVER_HEDGE = blockItem( "red_inver_hedge", new ColoredHedgeBlock( leaves( MaterialColor.FOLIAGE, 0.3 ).sound( SoundType.SWEET_BERRY_BUSH ), 2 ), MDItemGroup.DECORATIVES );
    public static final HedgeBlock MUXUS_HEDGE = blockItem( "muxus_hedge", new HedgeBlock( leaves( MaterialColor.FOLIAGE, 0.3 ).sound( SoundType.SWEET_BERRY_BUSH ) ), MDItemGroup.DECORATIVES );

    public static final TorchBlock EXTINGUISHED_ANTHRACITE_TORCH = blockItem( "extinguished_anthracite_torch", new TorchBlock( false, Block.Properties.create( Material.MISCELLANEOUS, MaterialColor.SNOW ).hardnessAndResistance( 0 ).doesNotBlockMovement().sound( SoundType.WOOD ) ), MDItemGroup.DECORATIVES );
    public static final ExtinguishableTorchBlock ANTHRACITE_TORCH = blockItem( "anthracite_torch", new ExtinguishableTorchBlock( true, EXTINGUISHED_ANTHRACITE_TORCH, Block.Properties.create( Material.MISCELLANEOUS, MaterialColor.SNOW ).doesNotBlockMovement().hardnessAndResistance( 0 ).lightValue( 15 ).sound( SoundType.WOOD ) ), MDItemGroup.DECORATIVES );
    public static final TorchBlock LUMINOSITE_TORCH = blockItem( "luminosite_torch", new TorchBlock( false, Block.Properties.create( Material.MISCELLANEOUS, MaterialColor.SNOW ).hardnessAndResistance( 0 ).doesNotBlockMovement().lightValue( 15 ).sound( SoundType.METAL ) ), MDItemGroup.DECORATIVES );

    public static final ExtLadderBlock BLACKWOOD_LADDER = blockItem( "blackwood_ladder", new ExtLadderBlock( Block.Properties.create( Material.MISCELLANEOUS ).hardnessAndResistance( 0.4F ).sound( SoundType.LADDER ) ), MDItemGroup.DECORATIVES );
    public static final ExtLadderBlock INVER_LADDER = blockItem( "inver_ladder", new ExtLadderBlock( Block.Properties.create( Material.MISCELLANEOUS ).hardnessAndResistance( 0.4F ).sound( SoundType.LADDER ) ), MDItemGroup.DECORATIVES );

    // Utilities
    public static final WorkbenchBlock BLACKWOOD_WORKBENCH = blockItem( "blackwood_workbench", new WorkbenchBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.DECORATIVES );
    public static final WorkbenchBlock INVER_WORKBENCH = blockItem( "inver_workbench", new WorkbenchBlock( wood( MaterialColor.BLACK_TERRACOTTA ) ), MDItemGroup.DECORATIVES );
    public static final RockFurnaceBlock ROCK_FURNACE = blockItem( "rock_furnace", new RockFurnaceBlock( rock( MaterialColor.STONE, 1.5, 6 ).lightValue( 15 ) ), MDItemGroup.DECORATIVES );


    /* ==== MISCELLANEOUS ==== */

    public static final FallBlock ASH_BLOCK = blockItem( "ash_block", new DigableFallBlock( 0x333333, ash( MaterialColor.GRAY ) ), MDItemGroup.BLOCKS );
    public static final FallBlock SALT_DUST_BLOCK = blockItem( "salt_dust_block", new DigableFallBlock( 0xffffff, dust( MaterialColor.SNOW, false ) ), MDItemGroup.BLOCKS );



    /* ==== PLANTS ==== */

    // Basic
    public static final TallMurkGrassBlock MURK_GRASS = blockItem( "murk_grass", new TallMurkGrassBlock( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS, "dark_tall_grass" );
    public static final MurkReedBlock MURK_REED = blockItem( "murk_reed", new MurkReedBlock( strongPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock MINT_PLANT = blockItem( "mint_plant", new SimplePlantBlock( strongPlant( MaterialColor.GRASS, 0.2 ), SimplePlantBlock.MINT ), MDItemGroup.PLANTS );
    public static final RedwoldBlock REDWOLD = blockItem( "redwold", new RedwoldBlock( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS );
    public static final NettlesBlock NETTLES = blockItem( "nettles", new NettlesBlock( strongPlant( MaterialColor.GRASS, 0.5 ).sound( SoundType.SWEET_BERRY_BUSH ) ), MDItemGroup.PLANTS );
    public static final SaltCrystalBlock SALT_CRYSTAL = blockItem( "salt_crystal", new SaltCrystalBlock( crystal( MaterialColor.SNOW, 0.2 ) ), MDItemGroup.PLANTS );
    public static final MurinaBlock MURINA = blockItem( "murina", new MurinaBlock( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS );
    public static final MossBlock MOSS = blockItem( "moss", new MossBlock( weakPlant( MaterialColor.GRASS, 0 ).sound( SoundType.SWEET_BERRY_BUSH ) ), MDItemGroup.PLANTS );
    public static final FacingPlantBlock DEAD_MOSS = blockItem( "dead_moss", new FacingPlantBlock( weakPlant( MaterialColor.DIRT, 0 ).sound( SoundType.SWEET_BERRY_BUSH ), 2 ), MDItemGroup.PLANTS );
    public static final LichenBlock LICHEN = blockItem( "lichen", new LichenBlock( weakPlant( MaterialColor.WHITE_TERRACOTTA, 0 ).sound( SoundType.SWEET_BERRY_BUSH ) ), MDItemGroup.PLANTS );
    public static final MurkFernBlock MURK_FERN = blockItem( "murk_fern", new MurkFernBlock( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS );
    public static final TallMurkFernBlock TALL_MURK_FERN = blockItem( "tall_murk_fern", new TallMurkFernBlock( weakPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS );
    public static final WireplantBlock WIREPLANT = blockItem( "wireplant", new WireplantBlock( strongPlant( MaterialColor.GRASS, 0.5 ).sound( SoundType.SWEET_BERRY_BUSH ) ), MDItemGroup.PLANTS );
    public static final WireplantBlock FLOWERED_WIREPLANT = blockItem( "flowered_wireplant", new WireplantBlock( strongPlant( MaterialColor.GRASS, 0.5 ).sound( SoundType.SWEET_BERRY_BUSH ) ), MDItemGroup.PLANTS );
    public static final WaterWireBlock WATER_WIRE = blockItem( "water_wire", new WaterWireBlock( strongPlant( MaterialColor.GRASS, 0.5 ).sound( SoundType.WET_GRASS ) ), MDItemGroup.PLANTS );
    public static final AlgaeBlock ALGAE = blockItem( "algae", new AlgaeBlock( weakPlant( MaterialColor.GRASS, 0 ).sound( SoundType.WET_GRASS ) ), MDItemGroup.PLANTS );
    public static final HangingMossBlock HANGING_MOSS = blockItem( "hanging_moss", new HangingMossBlock( weakPlant( MaterialColor.GRASS, 0 ).sound( SoundType.WET_GRASS ) ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock HORSETAIL = blockItem( "horsetail", new SimplePlantBlock( strongPlant( MaterialColor.GRASS, 0.2 ), SimplePlantBlock.HORSETAIL ), MDItemGroup.PLANTS );
    public static final CattailBlock CATTAIL = blockItem( "cattail", new CattailBlock( strongPlant( MaterialColor.GRASS, 0 ) ), MDItemGroup.PLANTS );
    public static final SimpleWaterPlantBlock LAKEWEED = blockItem( "lakeweed", new SimpleWaterPlantBlock( weakPlant( MaterialColor.GRASS, 0 ).sound( SoundType.WET_GRASS ), SimplePlantBlock.LAKEWEED ), MDItemGroup.PLANTS );
    public static final HeathBlock HEATH = blockItem( "heath", new HeathBlock( weakPlant( MaterialColor.GRASS, 0.2 ).sound( SoundType.SWEET_BERRY_BUSH ) ), MDItemGroup.PLANTS );
    public static final HeathBlock FLOWERED_HEATH = blockItem( "flowered_heath", new HeathBlock( weakPlant( MaterialColor.GRASS, 0.2 ).sound( SoundType.SWEET_BERRY_BUSH ) ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock DEAD_HEATH = blockItem( "dead_heath", new SimplePlantBlock( weakPlant( MaterialColor.BROWN_TERRACOTTA, 0 ).sound( SoundType.SWEET_BERRY_BUSH ), SimplePlantBlock.DEAD_HEATH ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock SEEPWEED = blockItem( "seepweed", new SimplePlantBlock( strongPlant( MaterialColor.GRASS, 0.2 ).sound( SoundType.SWEET_BERRY_BUSH ), SimplePlantBlock.SEEPWEED ), MDItemGroup.PLANTS );
    public static final CavePlantBlock CAVE_GRASS = blockItem( "cave_grass", new CavePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), CavePlantBlock.CAVE_GRASS ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock NUDWART = blockItem( "nudwart", new SimplePlantBlock( strongPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.NUDWART ), MDItemGroup.PLANTS );
    public static final WatergrassBlock WATERGRASS = blockItem( "watergrass", new WatergrassBlock( weakPlant( MaterialColor.GRASS, 0 ).sound( SoundType.WET_GRASS ) ), MDItemGroup.PLANTS );
    public static final MuxusBushBlock MUXUS_BUSH = blockItem( "muxus_bush", new MuxusBushBlock( strongPlant( MaterialColor.GRASS, 0.5 ).sound( SoundType.SWEET_BERRY_BUSH ) ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock COTTONSEDGE = blockItem( "cottonsedge", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.COTTONSEDGE ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock RED_GRASS = blockItem( "red_grass", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.RED_GRASS ), MDItemGroup.PLANTS );
    public static final CavePlantBlock DEAD_GRASS = blockItem( "dead_grass", new CavePlantBlock( weakPlant( MaterialColor.AIR, 0 ), CavePlantBlock.DEAD_GRASS ), MDItemGroup.PLANTS );

    // Flowers
    public static final FlyFlowerStalkBlock FLY_FLOWER_STALK = blockItem( "fly_flower_stalk", new FlyFlowerStalkBlock( strongPlant( MaterialColor.GRASS, 0.5 ).sound( SoundType.WET_GRASS ) ), MDItemGroup.PLANTS );
    public static final FlyFlowerBlock FLY_FLOWER = blockItem( "fly_flower", new FlyFlowerBlock( strongPlant( MaterialColor.GRASS, 0.5 ) ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock MURK_FLOWER = blockItem( "murk_flower", new SimplePlantBlock( strongPlant( MaterialColor.AIR, 0 ), SimplePlantBlock.MURK_FLOWER ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock MILKY_EYE = blockItem( "milky_eye", new SimplePlantBlock( strongPlant( MaterialColor.AIR, 0 ), SimplePlantBlock.MILK_EYE ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock EGIUM = blockItem( "egium", new SimplePlantBlock( strongPlant( MaterialColor.AIR, 0 ), SimplePlantBlock.EGIUM ), MDItemGroup.PLANTS );
    public static final ShadeBlueBlock SHADE_BLUE = blockItem( "shade_blue", new ShadeBlueBlock( strongPlant( MaterialColor.AIR, 0.1 ).sound( MDSoundTypes.SHADE_BLUE ) ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock BLUE_BULBFLOWER = blockItem( "blue_bulbflower", new SimplePlantBlock( strongPlant( MaterialColor.AIR, 0 ), SimplePlantBlock.BULBFLOWER ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock YELLOW_BULBFLOWER = blockItem( "yellow_bulbflower", new SimplePlantBlock( strongPlant( MaterialColor.AIR, 0 ), SimplePlantBlock.BULBFLOWER ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock RED_BULBFLOWER = blockItem( "red_bulbflower", new SimplePlantBlock( strongPlant( MaterialColor.AIR, 0 ), SimplePlantBlock.BULBFLOWER ), MDItemGroup.PLANTS );
    public static final TuruptBlock TURUPT = blockItem( "turupt", new TuruptBlock( strongPlant( MaterialColor.AIR, 0.1 ) ), MDItemGroup.PLANTS );
    public static final SimplePlantBlock CREEP_OF_THE_MOOR = blockItem( "creep_of_the_moor", new SimplePlantBlock( weakPlant( MaterialColor.AIR, 0 ), SimplePlantBlock.MOOR_CREEP ), MDItemGroup.PLANTS );
    public static final DoublePlantBlock MURK_LAVENDER = blockItem( "murk_lavender", new DoublePlantBlock( strongPlant( MaterialColor.AIR, 0 ), DoublePlantBlock.MURK_LAVENDER ), MDItemGroup.PLANTS );
    public static final FoxgloveBlock FOXGLOVE = blockItem( "foxglove", new FoxgloveBlock( strongPlant( MaterialColor.AIR, 0 ) ), MDItemGroup.PLANTS );
    public static final DoublePlantBlock GLOBE_THISTLE = blockItem( "globe_thistle", new DoublePlantBlock( strongPlant( MaterialColor.AIR, 0 ), DoublePlantBlock.GLOBE_THISTLE ), MDItemGroup.PLANTS );

    // Mushrooms
    public static final CavePlantBlock SEEDLE = blockItem( "seedle", new CavePlantBlock( weakPlant( MaterialColor.BROWN_TERRACOTTA, 0 ), CavePlantBlock.SEEDLE ), MDItemGroup.PLANTS );
    public static final CavePlantBlock DOTTED_MUSHROOM = blockItem( "dotted_mushroom", new CavePlantBlock( weakPlant( MaterialColor.RED_TERRACOTTA, 0 ), CavePlantBlock.DOTTED_MUSHROOM ), MDItemGroup.PLANTS );
    public static final CavePlantBlock BLACK_MUSHROOM = blockItem( "black_mushroom", new CavePlantBlock( weakPlant( MaterialColor.BLACK_TERRACOTTA, 0 ), CavePlantBlock.BLACK_MUSHROOM ), MDItemGroup.PLANTS );

    // Melion
    public static final SimplePlantBlock RED_MELION = blockItem( "red_melion", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.MELION ), MDItemGroup.PLANTS, "melion/red" );
    public static final SimplePlantBlock ORANGE_MELION = blockItem( "orange_melion", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.MELION ), MDItemGroup.PLANTS, "melion/orange" );
    public static final SimplePlantBlock YELLOW_MELION = blockItem( "yellow_melion", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.MELION ), MDItemGroup.PLANTS, "melion/yellow" );
    public static final SimplePlantBlock WHITE_MELION = blockItem( "white_melion", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.MELION ), MDItemGroup.PLANTS, "melion/white" );
    public static final SimplePlantBlock BLUE_MELION = blockItem( "blue_melion", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.MELION ), MDItemGroup.PLANTS, "melion/blue" );
    public static final SimplePlantBlock INDIGO_MELION = blockItem( "indigo_melion", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.MELION ), MDItemGroup.PLANTS, "melion/indigo" );
    public static final SimplePlantBlock MAGENTA_MELION = blockItem( "magenta_melion", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ), SimplePlantBlock.MELION ), MDItemGroup.PLANTS, "melion/magenta" );

    // Millium
    public static final SimplePlantBlock RED_MILLIUM = blockItem( "red_millium", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ).lightValue( 5 ), SimplePlantBlock.MILLIUM ), MDItemGroup.PLANTS, "millium/red" );
    public static final SimplePlantBlock YELLOW_MILLIUM = blockItem( "yellow_millium", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ).lightValue( 5 ), SimplePlantBlock.MILLIUM ), MDItemGroup.PLANTS, "millium/yellow" );
    public static final SimplePlantBlock WHITE_MILLIUM = blockItem( "white_millium", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ).lightValue( 5 ), SimplePlantBlock.MILLIUM ), MDItemGroup.PLANTS, "millium/white" );
    public static final SimplePlantBlock CYAN_MILLIUM = blockItem( "cyan_millium", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ).lightValue( 5 ), SimplePlantBlock.MILLIUM ), MDItemGroup.PLANTS, "millium/cyan" );
    public static final SimplePlantBlock GREEN_MILLIUM = blockItem( "green_millium", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ).lightValue( 5 ), SimplePlantBlock.MILLIUM ), MDItemGroup.PLANTS, "millium/green" );
    public static final SimplePlantBlock MAGENTA_MILLIUM = blockItem( "magenta_millium", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ).lightValue( 5 ), SimplePlantBlock.MILLIUM ), MDItemGroup.PLANTS, "millium/magenta" );
    public static final SimplePlantBlock BLUE_MILLIUM = blockItem( "blue_millium", new SimplePlantBlock( weakPlant( MaterialColor.GRASS, 0 ).lightValue( 5 ), SimplePlantBlock.MILLIUM ), MDItemGroup.PLANTS, "millium/blue" );

    // Misc
    public static final SoulLightBlock SOUL_LIGHT = blockItem( "soul_light", new SoulLightBlock( Block.Properties.create( Material.MISCELLANEOUS, MaterialColor.SNOW ) ), MDItemGroup.DECORATIVES );
    public static final PebblesBlock PEBBLES = blockItem( "pebbles", new PebblesBlock( weakPlant( MaterialColor.AIR, 0.1 ).sound( MDSoundTypes.ASPHALT ) ), MDItemGroup.DECORATIVES );
    public static final GooDripsBlock GOO_DRIPS = blockItem( "goo_drips", new GooDripsBlock( weakPlant( MaterialColor.AIR, 0 ).sound( SoundType.SLIME ) ), MDItemGroup.DECORATIVES );

    // Crops
    public static final NudwartCropBlock NUDWART_CROP = blockOnly( "nudwart_crop", new NudwartCropBlock( weakPlant( MaterialColor.GREEN, 0 ).sound( SoundType.CROP ) ) );
    public static final SeepweedCropBlock SEEPWEED_CROP = blockOnly( "seepweed_crop", new SeepweedCropBlock( weakPlant( MaterialColor.GREEN, 0 ).sound( SoundType.CROP ) ) );
    public static final MurkRootsCropBlock MURK_ROOTS = blockOnly( "murk_roots", new MurkRootsCropBlock( weakPlant( MaterialColor.GREEN, 0 ).sound( SoundType.CROP ) ) );
    public static final MurkRiceCropBlock MURK_RICE = blockOnly( "murk_rice", new MurkRiceCropBlock( weakPlant( MaterialColor.GREEN, 0 ).sound( SoundType.CROP ) ) );


    /* ==== MINERALS ==== */

    // Ores
    public static final OreBlock SALT_ORE = blockItem( "salt_ore", new OreBlock( 0, 3, Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( SoundType.STONE ) ), MDItemGroup.BLOCKS );
    public static final Block ALUMINIUM_ORE = blockItem( "aluminium_ore", new Block( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( SoundType.STONE ) ), MDItemGroup.BLOCKS );
    public static final OreBlock ANTHRACITE_ORE = blockItem( "anthracite_ore", new OreBlock( 0, 2, Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( SoundType.STONE ) ), MDItemGroup.BLOCKS );
    public static final OreBlock FINNERITE_ORE = blockItem( "finnerite_ore", new OreBlock( 2, 5, Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( SoundType.STONE ) ), MDItemGroup.BLOCKS );
    public static final OreBlock IVERITE_ORE = blockItem( "iverite_ore", new OreBlock( 2, 5, Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( SoundType.STONE ) ), MDItemGroup.BLOCKS );
    public static final OreBlock SAGERITE_ORE = blockItem( "sagerite_ore", new OreBlock( 2, 5, Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( SoundType.STONE ) ), MDItemGroup.BLOCKS );
    public static final OreBlock LUMINOSITE_ORE = blockItem( "luminosite_ore", new OreBlock( 0, 4, Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).lightValue( 3 ).sound( SoundType.STONE ) ), MDItemGroup.BLOCKS );
    public static final OreBlock GOO_ORE = blockItem( "goo_ore", new OreBlock( 0, 2, Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 3F, 3F ).sound( MDSoundTypes.ASPHALT ) ), MDItemGroup.BLOCKS );
    public static final OreBlock FOSSIL = blockItem( "fossil", new OreBlock( 0, 2, Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 1.5F, 5F ).sound( SoundType.STONE ) ), MDItemGroup.BLOCKS );

    // Blocks
    public static final TranslucentBlock SALT_BLOCK = blockItem( "salt_block", new SaltBlock( Block.Properties.create( Material.GLASS, MaterialColor.SNOW ).hardnessAndResistance( 0.3F ).sound( SoundType.GLASS ) ), MDItemGroup.BLOCKS );
    public static final Block ALUMINIUM_BLOCK = blockItem( "aluminium_block", new Block( Block.Properties.create( Material.IRON, MaterialColor.IRON ).hardnessAndResistance( 5F, 6F ).sound( SoundType.METAL ) ), MDItemGroup.BLOCKS );
    public static final Block ANTHRACITE_BLOCK = blockItem( "anthracite_block", new Block( Block.Properties.create( Material.ROCK, MaterialColor.BLACK ).hardnessAndResistance( 3F, 6F ).sound( SoundType.STONE ) ), MDItemGroup.BLOCKS );
    public static final Block FINNERITE_BLOCK = blockItem( "finnerite_block", new Block( Block.Properties.create( Material.IRON, MaterialColor.BLUE ).hardnessAndResistance( 5F, 6F ).sound( SoundType.METAL ) ), MDItemGroup.BLOCKS );
    public static final Block IVERITE_BLOCK = blockItem( "iverite_block", new Block( Block.Properties.create( Material.IRON, MaterialColor.GREEN ).hardnessAndResistance( 5F, 6F ).sound( SoundType.METAL ) ), MDItemGroup.BLOCKS );
    public static final Block SAGERITE_BLOCK = blockItem( "sagerite_block", new Block( Block.Properties.create( Material.IRON, MaterialColor.RED ).hardnessAndResistance( 5F, 6F ).sound( SoundType.METAL ) ), MDItemGroup.BLOCKS );
    public static final Block LUMINOSITE_BLOCK = blockItem( "luminosite_block", new Block( Block.Properties.create( Material.IRON, MaterialColor.SNOW ).hardnessAndResistance( 5F, 6F ).lightValue( 15 ).sound( SoundType.METAL ) ), MDItemGroup.BLOCKS );
    public static final ExtSlimeBlock GOO_BLOCK = blockItem( "goo_block", new ExtSlimeBlock( Block.Properties.create( Material.CLAY, MaterialColor.DIRT ).slipperiness( 0.8F ).sound( SoundType.SLIME ) ), MDItemGroup.BLOCKS );
    public static final AxisBlock BLACKBONE_BLOCK = blockItem( "blackbone_block", new AxisBlock( Block.Properties.create( Material.ROCK, MaterialColor.BLACK ).hardnessAndResistance( 2 ) ), MDItemGroup.BLOCKS );



    /* ==== PORTAL ==== */

    public static final Block DARK_STONE_BRICKS = blockItem( "dark_stone_bricks", new Block( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), MDItemGroup.DECORATIVES );
    public static final Block INSCRIBED_DARK_STONE_BRICKS = blockItem( "inscribed_dark_stone_bricks", new Block( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), MDItemGroup.DECORATIVES, "dark_stone_bricks_inscribed" );
    public static final HorizontalFacingBlock DARK_STONE_BRICKS_NATURE = blockItem( "dark_stone_bricks_nature", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), MDItemGroup.DECORATIVES );
    public static final HorizontalFacingBlock DARK_STONE_BRICKS_CURSE = blockItem( "dark_stone_bricks_curse", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), MDItemGroup.DECORATIVES );
    public static final HorizontalFacingBlock DARK_STONE_BRICKS_CYEN = blockItem( "dark_stone_bricks_cyen", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), MDItemGroup.DECORATIVES );
    public static final HorizontalFacingBlock DARK_STONE_BRICKS_FYREN = blockItem( "dark_stone_bricks_fyren", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), MDItemGroup.DECORATIVES );
    public static final HorizontalFacingBlock DARK_STONE_BRICKS_TIMEN = blockItem( "dark_stone_bricks_timen", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), MDItemGroup.DECORATIVES );
    public static final HorizontalFacingBlock DARK_STONE_BRICKS_PORTAL = blockItem( "dark_stone_bricks_portal", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), MDItemGroup.DECORATIVES );
    public static final HorizontalFacingBlock DARK_STONE_BRICKS_RGSW = blockItem( "dark_stone_bricks_rgsw", new HorizontalFacingBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), MDItemGroup.DECORATIVES );

    public static final HorizontalPortalFrameBlock HORIZONTAL_PORTAL_FRAME = blockItem( "horizontal_portal_frame", new HorizontalPortalFrameBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ) );
    public static final VerticalPortalFrameBlock VERTICAL_PORTAL_FRAME = blockItem( "vertical_portal_frame", new VerticalPortalFrameBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ) );
    public static final PortalCornerBlock PORTAL_CORNER = blockItem( "portal_corner", new PortalCornerBlock( Block.Properties.create( Material.ROCK, MaterialColor.STONE ).hardnessAndResistance( 2, 6 ) ), "portal_corner_block" );







    private static <T extends Block> T blockOnly( String name, T block, String... aliases ) {
        BLOCKS.register( name, block, aliases );
        return block;
    }

    private static <T extends Block> T blockItem( String name, T block, Item.Properties itemProps, String... aliases ) {
        BLOCKS.register( name, block, aliases );
        ITEMS.register( name, createBlockItem( block, itemProps ), aliases );
        ITEM_BLOCKS.add( block );
        return block;
    }

    private static <T extends Block> T blockItem( String name, T block, ItemGroup group, String... aliases ) {
        BLOCKS.register( name, block, aliases );
        ITEMS.register( name, createBlockItem( block, new Item.Properties().group( group ) ), aliases );
        ITEM_BLOCKS.add( block );
        return block;
    }

    private static <T extends Block> T blockItem( String name, T block, String... aliases ) {
        BLOCKS.register( name, block, aliases );
        ITEMS.register( name, createBlockItem( block, new Item.Properties() ), aliases );
        ITEM_BLOCKS.add( block );
        return block;
    }

    /**
     * Registers the block and item registry handlers to the {@link RegistryEventHandler}. Should be called internally
     * only by the {@link RegistryEventHandler}.
     */
    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( Block.class, BLOCKS );
        handler.addHandler( Item.class, ITEMS );
    }

    /**
     * Registers all colored blocks (blocks that implement {@link IColoredBlock}).
     */
    @OnlyIn( Dist.CLIENT )
    public static void initBlockColors() {
        for( Block block : BLOCKS ) {
            if( block instanceof IColoredBlock ) {
                Minecraft.getInstance().getBlockColors().register( ( (IColoredBlock) block )::colorMultiplier, block );
            }
        }
        for( Block block : ITEM_BLOCKS ) {
            if( block instanceof IColoredBlock ) {
                Minecraft.getInstance().getItemColors().register( ( (IColoredBlock) block )::colorMultiplier, block );
            }
        }
    }

    public static Supplier<BlockState> supply( String id ) {
        return new BlockStateSupplier( new ResourceLocation( "modernity", id ) );
    }

    private static Item createBlockItem( Block t, Item.Properties props ) {
        if( t instanceof ICustomBlockItem ) {
            return ( (ICustomBlockItem) t ).createBlockItem( props );
        }
        return new BlockItem( t, props );
    }

    private static Block.Properties rock( MaterialColor color, double hardness, double resistance ) {
        return Block.Properties.create( Material.ROCK, color )
                               .hardnessAndResistance( (float) hardness, (float) resistance )
                               .sound( SoundType.STONE );
    }

    private static Block.Properties asphalt() {
        return Block.Properties.create( Material.ROCK, MaterialColor.BLACK )
                               .hardnessAndResistance( 1, 4 )
                               .sound( MDSoundTypes.ASPHALT );
    }

    private static Block.Properties glass() {
        return Block.Properties.create( Material.GLASS, MaterialColor.GRAY )
                               .hardnessAndResistance( 0.3F )
                               .sound( SoundType.GLASS );
    }

    private static Block.Properties dirt( MaterialColor color, boolean overgrown ) {
        return Block.Properties.create( Material.EARTH, color )
                               .hardnessAndResistance( overgrown ? 0.6F : 0.5F )
                               .sound( overgrown ? SoundType.PLANT : SoundType.GROUND );
    }

    private static Block.Properties dust( MaterialColor color, boolean gravel ) {
        return Block.Properties.create( ! gravel ? Material.SAND : Material.EARTH, color )
                               .hardnessAndResistance( gravel ? 0.6F : 0.5F )
                               .sound( ! gravel ? SoundType.SAND : SoundType.GROUND );
    }

    private static Block.Properties ash( MaterialColor color ) {
        return Block.Properties.create( MDMaterial.ASH, color )
                               .hardnessAndResistance( 0.5F )
                               .sound( SoundType.SAND );
    }

    private static Block.Properties clay( MaterialColor color ) {
        return Block.Properties.create( Material.CLAY, color )
                               .hardnessAndResistance( 0.5F )
                               .sound( SoundType.GROUND );
    }

    private static Block.Properties fluid( Material mat, MaterialColor color ) {
        return Block.Properties.create( mat, color )
                               .hardnessAndResistance( 100F )
                               .doesNotBlockMovement();
    }

    private static Block.Properties wood( MaterialColor color ) {
        return Block.Properties.create( Material.WOOD, color )
                               .hardnessAndResistance( 2, 3 )
                               .sound( SoundType.WOOD );
    }

    private static Block.Properties metal( MaterialColor color ) {
        return Block.Properties.create( Material.IRON, color )
                               .hardnessAndResistance( 5, 6 )
                               .sound( SoundType.METAL );
    }

    private static Block.Properties weakPlant( MaterialColor color, double hardness ) {
        return Block.Properties.create( Material.TALL_PLANTS, color )
                               .hardnessAndResistance( (float) hardness )
                               .doesNotBlockMovement()
                               .sound( SoundType.PLANT );
    }

    private static Block.Properties strongPlant( MaterialColor color, double hardness ) {
        return Block.Properties.create( Material.PLANTS, color )
                               .hardnessAndResistance( (float) hardness )
                               .doesNotBlockMovement()
                               .sound( SoundType.PLANT );
    }

    private static Block.Properties crystal( MaterialColor color, double hardness ) {
        return Block.Properties.create( MDMaterial.CRYSTAL, color )
                               .hardnessAndResistance( (float) hardness )
                               .doesNotBlockMovement()
                               .sound( SoundType.GLASS );
    }

    private static Block.Properties leaves( MaterialColor color, double hardness ) {
        return Block.Properties.create( Material.LEAVES, color )
                               .hardnessAndResistance( (float) hardness )
                               .sound( SoundType.PLANT );
    }

    private MDBlocks() {
    }
}
