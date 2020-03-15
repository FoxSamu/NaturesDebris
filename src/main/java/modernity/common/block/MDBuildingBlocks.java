/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import modernity.common.block.base.*;
import modernity.common.block.loot.BlockLoot;
import modernity.common.block.misc.*;
import modernity.common.block.portal.HorizontalPortalFrameBlock;
import modernity.common.block.portal.PortalCornerBlock;
import modernity.common.block.portal.VerticalPortalFrameBlock;
import modernity.common.block.utils.*;
import modernity.common.item.MDItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public final class MDBuildingBlocks {

    private MDBuildingBlocks() {
    }

    public static void init() {
    }

    // Rock Blocks
    public static final Block ROCK_BRICKS
        = MDBlocks.simple( "rock_bricks" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block SMOOTH_ROCK
        = MDBlocks.simple( "smooth_rock" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block ROCK_TILES
        = MDBlocks.simple( "rock_tiles" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block MOSSY_ROCK
        = MDBlocks.simple( "mossy_rock" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block MOSSY_ROCK_BRICKS
        = MDBlocks.simple( "mossy_rock_bricks" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block MOSSY_ROCK_TILES
        = MDBlocks.simple( "mossy_rock_tiles" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block CRACKED_ROCK_BRICKS
        = MDBlocks.simple( "cracked_rock_bricks" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block CRACKED_ROCK_TILES
        = MDBlocks.simple( "cracked_rock_tiles" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block POLISHED_ROCK
        = MDBlocks.simple( "polished_rock" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block CHISELED_ROCK
        = MDBlocks.simple( "chiseled_rock" )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Darkrock Blocks
    public static final Block DARKROCK_BRICKS
        = MDBlocks.simple( "darkrock_bricks" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block SMOOTH_DARKROCK
        = MDBlocks.simple( "smooth_darkrock" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block DARKROCK_TILES
        = MDBlocks.simple( "darkrock_tiles" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block MOSSY_DARKROCK
        = MDBlocks.simple( "mossy_darkrock" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block MOSSY_DARKROCK_BRICKS
        = MDBlocks.simple( "mossy_darkrock_bricks" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block MOSSY_DARKROCK_TILES
        = MDBlocks.simple( "mossy_darkrock_tiles" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block CRACKED_DARKROCK_BRICKS
        = MDBlocks.simple( "cracked_darkrock_bricks" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block CRACKED_DARKROCK_TILES
        = MDBlocks.simple( "cracked_darkrock_tiles" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block POLISHED_DARKROCK
        = MDBlocks.simple( "polished_darkrock" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block CHISELED_DARKROCK
        = MDBlocks.simple( "chiseled_darkrock" )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Murk Bricks Blocks
    public static final Block MURK_BRICKS
        = MDBlocks.simple( "murk_bricks" )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block MURK_TILES
        = MDBlocks.simple( "murk_tiles" )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block CRACKED_MURK_BRICKS
        = MDBlocks.simple( "cracked_murk_bricks" )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block CRACKED_MURK_TILES
        = MDBlocks.simple( "cracked_murk_tiles" )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Wooden Blocks
    public static final Block BLACKWOOD_PLANKS
        = MDBlocks.simple( "blackwood_planks" )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final Block INVER_PLANKS
        = MDBlocks.simple( "inver_planks" )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Rock Slabs
    public static final VerticalSlabBlock ROCK_SLAB
        = MDBlocks.function( "rock_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock ROCK_BRICKS_SLAB
        = MDBlocks.function( "rock_bricks_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock SMOOTH_ROCK_SLAB
        = MDBlocks.function( "smooth_rock_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock ROCK_TILES_SLAB
        = MDBlocks.function( "rock_tiles_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock MOSSY_ROCK_SLAB
        = MDBlocks.function( "mossy_rock_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock MOSSY_ROCK_BRICKS_SLAB
        = MDBlocks.function( "mossy_rock_bricks_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock MOSSY_ROCK_TILES_SLAB
        = MDBlocks.function( "mossy_rock_tiles_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock CRACKED_ROCK_BRICKS_SLAB
        = MDBlocks.function( "cracked_rock_bricks_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock CRACKED_ROCK_TILES_SLAB
        = MDBlocks.function( "cracked_rock_tiles_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    // Darkrock Slabs
    public static final VerticalSlabBlock DARKROCK_SLAB
        = MDBlocks.function( "darkrock_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock DARKROCK_BRICKS_SLAB
        = MDBlocks.function( "darkrock_bricks_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock SMOOTH_DARKROCK_SLAB
        = MDBlocks.function( "smooth_darkrock_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock DARKROCK_TILES_SLAB
        = MDBlocks.function( "darkrock_tiles_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock MOSSY_DARKROCK_SLAB
        = MDBlocks.function( "mossy_darkrock_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock MOSSY_DARKROCK_BRICKS_SLAB
        = MDBlocks.function( "mossy_darkrock_bricks_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock MOSSY_DARKROCK_TILES_SLAB
        = MDBlocks.function( "mossy_darkrock_tiles_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock CRACKED_DARKROCK_BRICKS_SLAB
        = MDBlocks.function( "cracked_darkrock_bricks_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock CRACKED_DARKROCK_TILES_SLAB
        = MDBlocks.function( "cracked_darkrock_tiles_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    // Murk Bricks Slabs
    public static final VerticalSlabBlock MURK_BRICKS_SLAB
        = MDBlocks.function( "murk_bricks_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock MURK_TILES_SLAB
        = MDBlocks.function( "murk_tiles_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock CRACKED_MURK_BRICKS_SLAB
        = MDBlocks.function( "cracked_murk_bricks_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock CRACKED_MURK_TILES_SLAB
        = MDBlocks.function( "cracked_murk_tiles_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    // Misc Slabs
    public static final VerticalSlabBlock ASPHALT_CONCRETE_SLAB
        = MDBlocks.function( "asphalt_concrete_slab", VerticalSlabBlock::new )
                  .asphalt()
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock LIMESTONE_SLAB
        = MDBlocks.function( "limestone_slab", VerticalSlabBlock::new )
                  .rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    // Wooden Slabs
    public static final VerticalSlabBlock BLACKWOOD_SLAB
        = MDBlocks.function( "blackwood_slab", VerticalSlabBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();

    public static final VerticalSlabBlock INVER_SLAB
        = MDBlocks.function( "inver_slab", VerticalSlabBlock::new )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.slab() )
                  .create();


    // Rock Stairs
    public static final StairsBlock ROCK_STAIRS
        = MDBlocks.function( "rock_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock ROCK_BRICKS_STAIRS
        = MDBlocks.function( "rock_bricks_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock SMOOTH_ROCK_STAIRS
        = MDBlocks.function( "smooth_rock_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock ROCK_TILES_STAIRS
        = MDBlocks.function( "rock_tiles_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_ROCK_STAIRS
        = MDBlocks.function( "mossy_rock_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_ROCK_BRICKS_STAIRS
        = MDBlocks.function( "mossy_rock_bricks_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_ROCK_TILES_STAIRS
        = MDBlocks.function( "mossy_rock_tiles_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_ROCK_BRICKS_STAIRS
        = MDBlocks.function( "cracked_rock_bricks_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_ROCK_TILES_STAIRS
        = MDBlocks.function( "cracked_rock_tiles_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Darkrock Stairs
    public static final StairsBlock DARKROCK_STAIRS
        = MDBlocks.function( "darkrock_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock DARKROCK_BRICKS_STAIRS
        = MDBlocks.function( "darkrock_bricks_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock SMOOTH_DARKROCK_STAIRS
        = MDBlocks.function( "smooth_darkrock_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock DARKROCK_TILES_STAIRS
        = MDBlocks.function( "darkrock_tiles_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_DARKROCK_STAIRS
        = MDBlocks.function( "mossy_darkrock_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_DARKROCK_BRICKS_STAIRS
        = MDBlocks.function( "mossy_darkrock_bricks_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_DARKROCK_TILES_STAIRS
        = MDBlocks.function( "mossy_darkrock_tiles_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_DARKROCK_BRICKS_STAIRS
        = MDBlocks.function( "cracked_darkrock_bricks_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_DARKROCK_TILES_STAIRS
        = MDBlocks.function( "cracked_darkrock_tiles_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Murk Bricks Stairs
    public static final StairsBlock MURK_BRICKS_STAIRS
        = MDBlocks.function( "murk_bricks_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MURK_TILES_STAIRS
        = MDBlocks.function( "murk_tiles_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_MURK_BRICKS_STAIRS
        = MDBlocks.function( "cracked_murk_bricks_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_MURK_TILES_STAIRS
        = MDBlocks.function( "cracked_murk_tiles_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Misc Stairs
    public static final StairsBlock ASPHALT_CONCRETE_STAIRS
        = MDBlocks.function( "asphalt_concrete_stairs", props -> new StairsBlock( false, props ) )
                  .asphalt()
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock LIMESTONE_STAIRS
        = MDBlocks.function( "limestone_stairs", props -> new StairsBlock( false, props ) )
                  .rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Wooden Stairs
    public static final StairsBlock BLACKWOOD_STAIRS
        = MDBlocks.function( "blackwood_stairs", props -> new StairsBlock( false, props ) )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock INVER_STAIRS
        = MDBlocks.function( "inver_stairs", props -> new StairsBlock( false, props ) )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();


    // Rock Steps
    public static final StairsBlock ROCK_STEP
        = MDBlocks.function( "rock_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock ROCK_BRICKS_STEP
        = MDBlocks.function( "rock_bricks_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock SMOOTH_ROCK_STEP
        = MDBlocks.function( "smooth_rock_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock ROCK_TILES_STEP
        = MDBlocks.function( "rock_tiles_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_ROCK_STEP
        = MDBlocks.function( "mossy_rock_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_ROCK_BRICKS_STEP
        = MDBlocks.function( "mossy_rock_bricks_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_ROCK_TILES_STEP
        = MDBlocks.function( "mossy_rock_tiles_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_ROCK_BRICKS_STEP
        = MDBlocks.function( "cracked_rock_bricks_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_ROCK_TILES_STEP
        = MDBlocks.function( "cracked_rock_tiles_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Darkrock Steps
    public static final StairsBlock DARKROCK_STEP
        = MDBlocks.function( "darkrock_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock DARKROCK_BRICKS_STEP
        = MDBlocks.function( "darkrock_bricks_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock SMOOTH_DARKROCK_STEP
        = MDBlocks.function( "smooth_darkrock_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock DARKROCK_TILES_STEP
        = MDBlocks.function( "darkrock_tiles_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_DARKROCK_STEP
        = MDBlocks.function( "mossy_darkrock_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_DARKROCK_BRICKS_STEP
        = MDBlocks.function( "mossy_darkrock_bricks_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MOSSY_DARKROCK_TILES_STEP
        = MDBlocks.function( "mossy_darkrock_tiles_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_DARKROCK_BRICKS_STEP
        = MDBlocks.function( "cracked_darkrock_bricks_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_DARKROCK_TILES_STEP
        = MDBlocks.function( "cracked_darkrock_tiles_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Murk Bricks Steps
    public static final StairsBlock MURK_BRICKS_STEP
        = MDBlocks.function( "murk_bricks_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock MURK_TILES_STEP
        = MDBlocks.function( "murk_tiles_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_MURK_BRICKS_STEP
        = MDBlocks.function( "cracked_murk_bricks_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock CRACKED_MURK_TILES_STEP
        = MDBlocks.function( "cracked_murk_tiles_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Misc Steps
    public static final StairsBlock ASPHALT_CONCRETE_STEP
        = MDBlocks.function( "asphalt_concrete_step", props -> new StairsBlock( true, props ) )
                  .asphalt()
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock LIMESTONE_STEP
        = MDBlocks.function( "limestone_step", props -> new StairsBlock( true, props ) )
                  .rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Blackwood Steps
    public static final StairsBlock BLACKWOOD_STEP
        = MDBlocks.function( "blackwood_step", props -> new StairsBlock( true, props ) )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final StairsBlock INVER_STEP
        = MDBlocks.function( "inver_step", props -> new StairsBlock( true, props ) )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Rock Corners
    public static final CornerBlock ROCK_CORNER
        = MDBlocks.function( "rock_corner", CornerBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock ROCK_BRICKS_CORNER
        = MDBlocks.function( "rock_bricks_corner", CornerBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock SMOOTH_ROCK_CORNER
        = MDBlocks.function( "smooth_rock_corner", CornerBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock ROCK_TILES_CORNER
        = MDBlocks.function( "rock_tiles_corner", CornerBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock MOSSY_ROCK_CORNER
        = MDBlocks.function( "mossy_rock_corner", CornerBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock MOSSY_ROCK_BRICKS_CORNER
        = MDBlocks.function( "mossy_rock_bricks_corner", CornerBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock MOSSY_ROCK_TILES_CORNER
        = MDBlocks.function( "mossy_rock_tiles_corner", CornerBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock CRACKED_ROCK_BRICKS_CORNER
        = MDBlocks.function( "cracked_rock_bricks_corner", CornerBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock CRACKED_ROCK_TILES_CORNER
        = MDBlocks.function( "cracked_rock_tiles_corner", CornerBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    // Darkrock Corners
    public static final CornerBlock DARKROCK_CORNER
        = MDBlocks.function( "darkrock_corner", CornerBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock DARKROCK_BRICKS_CORNER
        = MDBlocks.function( "darkrock_bricks_corner", CornerBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock SMOOTH_DARKROCK_CORNER
        = MDBlocks.function( "smooth_darkrock_corner", CornerBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock DARKROCK_TILES_CORNER
        = MDBlocks.function( "darkrock_tiles_corner", CornerBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock MOSSY_DARKROCK_CORNER
        = MDBlocks.function( "mossy_darkrock_corner", CornerBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock MOSSY_DARKROCK_BRICKS_CORNER
        = MDBlocks.function( "mossy_darkrock_bricks_corner", CornerBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock MOSSY_DARKROCK_TILES_CORNER
        = MDBlocks.function( "mossy_darkrock_tiles_corner", CornerBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock CRACKED_DARKROCK_BRICKS_CORNER
        = MDBlocks.function( "cracked_darkrock_bricks_corner", CornerBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock CRACKED_DARKROCK_TILES_CORNER
        = MDBlocks.function( "cracked_darkrock_tiles_corner", CornerBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    // Murk Bricks Corners
    public static final CornerBlock MURK_BRICKS_CORNER
        = MDBlocks.function( "murk_bricks_corner", CornerBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock MURK_TILES_CORNER
        = MDBlocks.function( "murk_tiles_corner", CornerBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock CRACKED_MURK_BRICKS_CORNER
        = MDBlocks.function( "cracked_murk_bricks_corner", CornerBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock CRACKED_MURK_TILES_CORNER
        = MDBlocks.function( "cracked_murk_tiles_corner", CornerBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    // Misc Corners
    public static final CornerBlock ASPHALT_CONCRETE_CORNER
        = MDBlocks.function( "asphalt_concrete_corner", CornerBlock::new )
                  .asphalt()
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock LIMESTONE_CORNER
        = MDBlocks.function( "limestone_corner", CornerBlock::new )
                  .rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    // Wooden Corners
    public static final CornerBlock BLACKWOOD_CORNER
        = MDBlocks.function( "blackwood_corner", CornerBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    public static final CornerBlock INVER_CORNER
        = MDBlocks.function( "inver_corner", CornerBlock::new )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.corner() )
                  .create();

    // Rock Walls
    public static final WallBlock ROCK_WALL
        = MDBlocks.function( "rock_wall", WallBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock ROCK_BRICKS_WALL
        = MDBlocks.function( "rock_bricks_wall", WallBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock SMOOTH_ROCK_WALL
        = MDBlocks.function( "smooth_rock_wall", WallBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock MOSSY_ROCK_WALL
        = MDBlocks.function( "mossy_rock_wall", WallBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock MOSSY_ROCK_BRICKS_WALL
        = MDBlocks.function( "mossy_rock_bricks_wall", WallBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock CRACKED_ROCK_BRICKS_WALL
        = MDBlocks.function( "cracked_rock_bricks_wall", WallBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    // Darkrock Walls
    public static final WallBlock DARKROCK_WALL
        = MDBlocks.function( "darkrock_wall", WallBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock DARKROCK_BRICKS_WALL
        = MDBlocks.function( "darkrock_bricks_wall", WallBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock SMOOTH_DARKROCK_WALL
        = MDBlocks.function( "smooth_darkrock_wall", WallBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock MOSSY_DARKROCK_WALL
        = MDBlocks.function( "mossy_darkrock_wall", WallBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock MOSSY_DARKROCK_BRICKS_WALL
        = MDBlocks.function( "mossy_darkrock_bricks_wall", WallBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock CRACKED_DARKROCK_BRICKS_WALL
        = MDBlocks.function( "cracked_darkrock_bricks_wall", WallBlock::new )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    // Murk Bricks Walls
    public static final WallBlock MURK_BRICKS_WALL
        = MDBlocks.function( "murk_bricks_wall", WallBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock CRACKED_MURK_BRICKS_WALL
        = MDBlocks.function( "cracked_murk_bricks_wall", WallBlock::new )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    // Misc Walls
    public static final WallBlock ASPHALT_CONCRETE_WALL
        = MDBlocks.function( "asphalt_concrete_wall", WallBlock::new )
                  .asphalt()
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WallBlock LIMESTONE_WALL
        = MDBlocks.function( "limestone_wall", WallBlock::new )
                  .rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();


    // Fences
    public static final FenceBlock BLACKWOOD_FENCE
        = MDBlocks.function( "blackwood_fence", FenceBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final FenceBlock INVER_FENCE
        = MDBlocks.function( "inver_fence", FenceBlock::new )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    // Fence Gates
    public static final FenceGateBlock BLACKWOOD_FENCE_GATE
        = MDBlocks.function( "blackwood_fence_gate", FenceGateBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final FenceGateBlock INVER_FENCE_GATE
        = MDBlocks.function( "inver_fence_gate", FenceGateBlock::new )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();


    // Doors
    public static final DoorBlock BLACKWOOD_DOOR
        = MDBlocks.function( "blackwood_door", DoorBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final DoorBlock INVER_DOOR
        = MDBlocks.function( "inver_door", DoorBlock::new )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final DoorBlock ALUMINIUM_DOOR
        = MDBlocks.function( "aluminium_door", DoorBlock::new )
                  .metal( MaterialColor.IRON )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    // Trap Doors
    public static final ExtTrapDoorBlock BLACKWOOD_TRAPDOOR
        = MDBlocks.function( "blackwood_trapdoor", ExtTrapDoorBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final ExtTrapDoorBlock INVER_TRAPDOOR
        = MDBlocks.function( "inver_trapdoor", ExtTrapDoorBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final ExtTrapDoorBlock ALUMINIUM_TRAPDOOR
        = MDBlocks.function( "aluminium_trapdoor", ExtTrapDoorBlock::new )
                  .metal( MaterialColor.IRON )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();


    // Pillars
    public static final PillarBlock ROCK_PILLAR
        = MDBlocks.function( "rock_pillar", props -> new PillarBlock( props, 14 ) )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final PillarBlock DARKROCK_PILLAR
        = MDBlocks.function( "darkrock_pillar", props -> new PillarBlock( props, 14 ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final PillarBlock MURK_PILLAR
        = MDBlocks.function( "murk_pillar", props -> new PillarBlock( props, 14 ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();


    // Glass
    public static final TranslucentBlock MURKY_GLASS
        = MDBlocks.function( "murky_glass", TranslucentBlock::new )
                  .glass()
                  .item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.silkTouch( BlockLoot.self() ) )
                  .alias( "dark_glass" )
                  .create();

    public static final PanelBlock MURKY_GLASS_PANE
        = MDBlocks.function( "murky_glass_pane", TranslucentPanelBlock::new )
                  .glass()
                  .item( MDItemGroup.DECORATIVES )
                  .drops( BlockLoot.silkTouch( BlockLoot.self() ) )
                  .alias( "dark_glass_pane" )
                  .create();

    // Poles
    public static final PillarBlock BLACKWOOD_POLE
        = MDBlocks.function( "blackwood_pole", props -> new PillarBlock( props, 4 ) )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final PillarBlock INVER_POLE
        = MDBlocks.function( "inver_pole", props -> new PillarBlock( props, 4 ) )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();


    // Hedges
    public static final HedgeBlock BLACKWOOD_HEDGE
        = MDBlocks.function( "blackwood_hedge", props -> new ColoredHedgeBlock( props, 0 ) )
                  .leaves( MaterialColor.FOLIAGE, 0.3 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HedgeBlock INVER_HEDGE
        = MDBlocks.function( "inver_hedge", props -> new ColoredHedgeBlock( props, 1 ) )
                  .leaves( MaterialColor.FOLIAGE, 0.3 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HedgeBlock RED_INVER_HEDGE
        = MDBlocks.function( "red_inver_hedge", props -> new ColoredHedgeBlock( props, 2 ) )
                  .leaves( MaterialColor.FOLIAGE, 0.3 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HedgeBlock MUXUS_HEDGE
        = MDBlocks.function( "muxus_hedge", HedgeBlock::new )
                  .leaves( MaterialColor.FOLIAGE, 0.3 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();


    // Torches
    public static final TorchBlock EXTINGUISHED_ANTHRACITE_TORCH
        = MDBlocks.function( "extinguished_anthracite_torch", props -> new TorchBlock( false, props ) )
                  .torch( MaterialColor.SNOW, 15 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final ExtinguishableTorchBlock ANTHRACITE_TORCH
        = MDBlocks.function( "anthracite_torch", props -> new ExtinguishableTorchBlock( true, EXTINGUISHED_ANTHRACITE_TORCH, props ) )
                  .torch( MaterialColor.BLACK, 0 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final TorchBlock LUMINOSITE_TORCH
        = MDBlocks.function( "luminosite_torch", props -> new TorchBlock( false, props ) )
                  .torch( MaterialColor.SNOW, 15 )
                  .sound( SoundType.METAL )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    // Ladders
    public static final ExtLadderBlock BLACKWOOD_LADDER
        = MDBlocks.function( "blackwood_ladder", ExtLadderBlock::new )
                  .props( Material.MISCELLANEOUS )
                  .hardness( 0.4F )
                  .sound( SoundType.LADDER )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final ExtLadderBlock INVER_LADDER
        = MDBlocks.function( "inver_ladder", ExtLadderBlock::new )
                  .props( Material.MISCELLANEOUS )
                  .hardness( 0.4F )
                  .sound( SoundType.LADDER )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    // Utilities
    public static final WorkbenchBlock BLACKWOOD_WORKBENCH
        = MDBlocks.function( "blackwood_workbench", WorkbenchBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final WorkbenchBlock INVER_WORKBENCH
        = MDBlocks.function( "inver_workbench", WorkbenchBlock::new )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final RockFurnaceBlock ROCK_FURNACE
        = MDBlocks.function( "rock_furnace", RockFurnaceBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 )
                  .light( 15 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    // Chests
    public static final ExtChestBlock BLACKWOOD_CHEST
        = MDBlocks.function( "blackwood_chest", BlackwoodChestBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA )
                  .item( MDItemGroup.DECORATIVES )
                  .drops( BlockLoot.chest() )
                  .create();

    public static final ExtChestBlock INVER_CHEST
        = MDBlocks.function( "inver_chest", InverChestBlock::new )
                  .wood( MaterialColor.WOOD )
                  .item( MDItemGroup.DECORATIVES )
                  .drops( BlockLoot.chest() )
                  .create();

    // Misc
    public static final FallBlock ASH_BLOCK
        = MDBlocks.function( "ash_block", props -> new DigableFallBlock( 0x333333, props ) )
                  .ash( MaterialColor.GRAY )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    public static final FallBlock SALT_DUST_BLOCK
        = MDBlocks.function( "salt_dust_block", props -> new DigableFallBlock( 0xffffff, props ) )
                  .dust( MaterialColor.SNOW, false )
                  .item( MDItemGroup.BLOCKS )
                  .dropSelf()
                  .create();

    // Portal

    public static final Block DARK_STONE_BRICKS
        = MDBlocks.simple( "dark_stone_bricks" )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final Block INSCRIBED_DARK_STONE_BRICKS
        = MDBlocks.simple( "inscribed_dark_stone_bricks" )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_NATURE
        = MDBlocks.function( "dark_stone_bricks_nature", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_CURSE
        = MDBlocks.function( "dark_stone_bricks_curse", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_CYEN
        = MDBlocks.function( "dark_stone_bricks_cyen", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_FYREN
        = MDBlocks.function( "dark_stone_bricks_fyren", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_TIMEN
        = MDBlocks.function( "dark_stone_bricks_timen", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_PORTAL
        = MDBlocks.function( "dark_stone_bricks_portal", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_RGSW
        = MDBlocks.function( "dark_stone_bricks_rgsw", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final HorizontalPortalFrameBlock HORIZONTAL_PORTAL_FRAME
        = MDBlocks.function( "horizontal_portal_frame", HorizontalPortalFrameBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item()
                  .create();

    public static final VerticalPortalFrameBlock VERTICAL_PORTAL_FRAME
        = MDBlocks.function( "vertical_portal_frame", VerticalPortalFrameBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item()
                  .create();

    public static final PortalCornerBlock PORTAL_CORNER
        = MDBlocks.function( "portal_corner", PortalCornerBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 )
                  .item()
                  .create();

}
