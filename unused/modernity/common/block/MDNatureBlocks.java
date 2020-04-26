/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import modernity.common.block.base.*;
import modernity.common.block.dirt.DirtlikeBlock;
import modernity.common.block.dirt.GrassBlock;
import modernity.common.block.dirt.LeafyDirtlikeBlock;
import modernity.common.block.dirt.SnowyDirtlikeBlock;
import modernity.common.block.dirt.logic.MDDirtLogics;
import modernity.common.block.farmland.FarmlandBlock;
import modernity.common.block.farmland.GrassFarmlandBlock;
import modernity.common.block.fluid.RegularFluidBlock;
import modernity.common.block.loot.BlockLoot;
import modernity.common.block.loot.MDBlockDrops;
import modernity.common.block.misc.PuddleBlock;
import modernity.common.fluid.MDFluids;
import modernity.common.item.MDItemGroup;
import modernity.common.item.MDItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public final class MDNatureBlocks {
    public static void init() {
    }

    public static final ExtAirBlock CAVE_AIR
        = MDBlocks.function( "cave_air", ExtAirBlock::new )
                  .props( Material.AIR, MaterialColor.AIR )
                  .create();

    // Rocks
    public static final Block ROCK
        = MDBlocks.simple( "rock" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE ).create();

    public static final Block DARKROCK
        = MDBlocks.simple( "darkrock" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE ).create();

    public static final Block UNBREAKABLE_STONE
        = MDBlocks.simple( "unbreakable_stone" )
                  .bedrock( MaterialColor.BLACK ).item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .alias( "modern_bedrock" ).create();

    public static final Block HARDENED_ROCK
        = MDBlocks.simple( "hardened_rock" )
                  .rock( MaterialColor.BLACK, 3, 15 ).item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE ).create();

    public static final Block LIMESTONE
        = MDBlocks.simple( "limestone" )
                  .rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ).item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE ).create();

    public static final Block ASPHALT_CONCRETE
        = MDBlocks.simple( "asphalt_concrete" )
                  .asphalt().item( MDItemGroup.BLOCKS )
                  .recipeAsphalt( () -> MDItems.GOO_BALL, () -> MDItems.ANTHRACITE, () -> MDNatureBlocks.REGOLITH, 1, null )
                  .drops( MDBlockDrops.SIMPLE ).create();

    public static final Block SUMESTONE
        = MDBlocks.simple( "sumestone" )
                  .rock( MaterialColor.STONE, 1.8, 6 ).item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE ).create();

    public static final Block DARK_SUMESTONE
        = MDBlocks.simple( "dark_sumestone" )
                  .rock( MaterialColor.STONE, 1.8, 6 ).item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE ).create();


    public static final Block MURKY_TERRACOTTA
        = MDBlocks.simple( "murky_terracotta" )
                  .rock( MaterialColor.CLAY, 1, 5 ).item( MDItemGroup.BLOCKS )
                  .recipeSmelting( () -> MDNatureBlocks.MURKY_CLAY, 0.1, 200, null )
                  .drops( MDBlockDrops.SIMPLE ).create();


    // Soils
    public static final DirtlikeBlock MURKY_DIRT
        = MDBlocks.function( "murky_dirt", props -> new DirtlikeBlock( MDDirtLogics.DIRT_LOGIC, props ) )
                  .dirt( MaterialColor.DIRT, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .alias( "dark_dirt" )
                  .create();

    public static final DirtlikeBlock MURKY_GRASS_BLOCK
        = MDBlocks.function( "murky_grass_block", props -> new GrassBlock( MDDirtLogics.GRASS_LOGIC, props ) )
                  .dirt( MaterialColor.GRASS, true )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.DIRT_LIKE )
                  .alias( "dark_grass_block" )
                  .create();

    public static final DigableFallBlock MURKY_SAND
        = MDBlocks.function( "murky_sand", props -> new DigableFallBlock( 0x826f52, props ) )
                  .dust( MaterialColor.SAND, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .alias( "dark_sand" )
                  .create();

    public static final DigableBlock MURKY_CLAY
        = MDBlocks.function( "murky_clay", DigableBlock::new )
                  .clay( MaterialColor.GRAY_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .alias( "dark_clay" )
                  .create();

    public static final DigableBlock MURKY_COARSE_DIRT
        = MDBlocks.function( "murky_coarse_dirt", DigableBlock::new )
                  .dirt( MaterialColor.DIRT, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .recipeAdd( () -> MDNatureBlocks.REGOLITH, () -> MURKY_DIRT, 2, null )
                  .alias( "coarse_dark_dirt" )
                  .create();

    public static final DigableBlock SALTY_DIRT =
        MDBlocks.function( "salty_dirt", DigableBlock::new )
                .dirt( MaterialColor.DIRT, false )
                .item( MDItemGroup.BLOCKS )
                .drops( MDBlockDrops.SIMPLE )
                .recipeAdd( () -> MDItems.SALT_DUST, () -> MURKY_DIRT, 1, null )
                .create();

    public static final DirtlikeBlock MURKY_HUMUS =
        MDBlocks.function( "murky_humus", props -> new SnowyDirtlikeBlock( MDDirtLogics.HUMUS_LOGIC, props ) )
                .dirt( MaterialColor.ORANGE_TERRACOTTA, true )
                .item( MDItemGroup.BLOCKS )
                .drops( MDBlockDrops.DIRT_LIKE )
                .recipeOne( () -> MDNatureBlocks.LEAFY_HUMUS, 1, null )
                .create();

    public static final DirtlikeBlock LEAFY_HUMUS
        = MDBlocks.function( "leafy_humus", props -> new LeafyDirtlikeBlock( MDDirtLogics.LEAFY_HUMUS_LOGIC, props ) )
                  .dirt( MaterialColor.ORANGE_TERRACOTTA, true )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.DIRT_LIKE )
                  .alias( "dark_podzol" )
                  .create();

    public static final DirtlikeBlock MURKY_PODZOL
        = MDBlocks.function( "murky_podzol", props -> new SnowyDirtlikeBlock( MDDirtLogics.PODZOL_LOGIC, props ) )
                  .dirt( MaterialColor.DIRT, true )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.DIRT_LIKE )
                  .create();

    public static final DirtlikeBlock HEATH_BLOCK
        = MDBlocks.function( "heath_block", props -> new DirtlikeBlock( MDDirtLogics.HEATH_LOGIC, props ) )
                  .dirt( MaterialColor.FOLIAGE, true )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.DIRT_LIKE )
                  .create();

    public static final DigableBlock ACID_DIRT
        = MDBlocks.function( "acid_dirt", DigableBlock::new )
                  .dirt( MaterialColor.DIRT, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .create();

    public static final StickyBlock MUD
        = MDBlocks.function( "mud", DigableStickyBlock::new )
                  .dirt( MaterialColor.BROWN_TERRACOTTA, false )
                  .sound( MDSoundTypes.MUD )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .create();

    public static final DigableFallBlock REGOLITH
        = MDBlocks.function( "regolith", props -> new DigableFallBlock( 0x737d8c, props ) )
                  .dust( MaterialColor.STONE, true )
                  .item( MDItemGroup.BLOCKS )
                  .drops( MDBlockDrops.SIMPLE )
                  .create();


    // Fluids
    public static final RegularFluidBlock MURKY_WATER
        = MDBlocks.function( "murky_water", props -> new RegularFluidBlock( MDFluids.MURKY_WATER, props ) )
                  .fluid( Material.WATER, MaterialColor.WATER )
                  .alias( "modernized_water" )
                  .create();

    public static final RegularFluidBlock CLEAN_WATER
        = MDBlocks.function( "clean_water", props -> new RegularFluidBlock( MDFluids.CLEAN_WATER, props ) )
                  .fluid( Material.WATER, MaterialColor.WATER )
                  .create();

    public static final RegularFluidBlock MOLTEN_ROCK
        = MDBlocks.function( "molten_rock", props -> new RegularFluidBlock( MDFluids.MOLTEN_ROCK, props ) )
                  .fluid( Material.LAVA, MaterialColor.TNT )
                  .light( 15 )
                  .alias( "heatrock" )
                  .create();


    public static final PuddleBlock PUDDLE
        = MDBlocks.function( "puddle", PuddleBlock::new )
                  .props( Material.WATER, MaterialColor.WATER )
                  .create();


    // Misc
    public static final MurkyGrassPathBlock MURKY_GRASS_PATH
        = MDBlocks.function( "murky_grass_path", MurkyGrassPathBlock::new )
                  .dirt( MaterialColor.YELLOW, true )
                  .item( MDItemGroup.DECORATIVES )
                  .drops( MDBlockDrops.DIRT_LIKE )
                  .create();

    // Farmland
    public static final FarmlandBlock MURKY_DIRT_FARMLAND
        = MDBlocks.function( "murky_dirt_farmland", props -> new FarmlandBlock( MDDirtLogics.DIRT_LOGIC_FL, props ) )
                  .dirt( MaterialColor.DIRT, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.item( MURKY_DIRT ) )
                  .create();

    public static final FarmlandBlock MURKY_GRASS_BLOCK_FARMLAND
        = MDBlocks.function( "murky_grass_block_farmland", props -> new GrassFarmlandBlock( MDDirtLogics.GRASS_LOGIC_FL, props ) )
                  .dirt( MaterialColor.DIRT, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.silkTouch( BlockLoot.item( MURKY_GRASS_BLOCK ), BlockLoot.item( MURKY_DIRT ) ) )
                  .create();

    public static final FarmlandBlock MURKY_HUMUS_FARMLAND
        = MDBlocks.function( "murky_humus_farmland", props -> new FarmlandBlock( MDDirtLogics.HUMUS_LOGIC_FL, props ) )
                  .dirt( MaterialColor.DIRT, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.silkTouch( BlockLoot.item( MURKY_HUMUS ), BlockLoot.item( MURKY_DIRT ) ) )
                  .create();

    public static final FarmlandBlock LEAFY_HUMUS_FARMLAND
        = MDBlocks.function( "leafy_humus_farmland", props -> new FarmlandBlock( MDDirtLogics.LEAFY_HUMUS_LOGIC_FL, props ) )
                  .dirt( MaterialColor.DIRT, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.silkTouch( BlockLoot.item( LEAFY_HUMUS ), BlockLoot.item( MURKY_DIRT ) ) )
                  .create();

    public static final FarmlandBlock MURKY_PODZOL_FARMLAND
        = MDBlocks.function( "murky_podzol_farmland", props -> new FarmlandBlock( MDDirtLogics.PODZOL_LOGIC_FL, props ) )
                  .dirt( MaterialColor.DIRT, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.silkTouch( BlockLoot.item( MURKY_PODZOL ), BlockLoot.item( MURKY_DIRT ) ) )
                  .create();

    public static final FarmlandBlock HEATH_FARMLAND
        = MDBlocks.function( "heath_farmland", props -> new FarmlandBlock( MDDirtLogics.HEATH_LOGIC_FL, props ) )
                  .dirt( MaterialColor.DIRT, false )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.silkTouch( BlockLoot.item( HEATH_BLOCK ), BlockLoot.item( MURKY_DIRT ) ) )
                  .create();
}
