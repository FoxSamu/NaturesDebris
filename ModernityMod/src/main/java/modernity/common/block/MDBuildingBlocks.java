/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
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
import modernity.common.item.MDItemTags;
import modernity.common.item.MDItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

@ObjectHolder( "modernity" )
public final class MDBuildingBlocks {

    private MDBuildingBlocks() {
    }

    public static void init() {
    }

    // Rock Blocks
    public static final Block ROCK = MDNatureBlocks.ROCK;

    public static final Block ROCK_BRICKS
        = MDBlocks.simple( "rock_bricks" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeBlock4( () -> MDNatureBlocks.ROCK, 4, null )
                  .recipeStonecutting( () -> ROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final Block SMOOTH_ROCK
        = MDBlocks.simple( "smooth_rock" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeSmelting( () -> MDNatureBlocks.ROCK, 0.1, 200, null )
                  .dropSelf().create();

    public static final Block ROCK_TILES
        = MDBlocks.simple( "rock_tiles" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeBlock4( () -> MDBuildingBlocks.SMOOTH_ROCK, 4, null )
                  .recipeStonecutting( () -> SMOOTH_ROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final Block MOSSY_ROCK
        = MDBlocks.simple( "mossy_rock" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeJoin( () -> MDNatureBlocks.ROCK, () -> MDPlantBlocks.MOSS, 1, null )
                  .dropSelf().create();

    public static final Block MOSSY_ROCK_BRICKS
        = MDBlocks.simple( "mossy_rock_bricks" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeJoin( () -> MDBuildingBlocks.ROCK_BRICKS, () -> MDPlantBlocks.MOSS, 1, "%s_from_moss" )
                  .recipeBlock4( () -> MOSSY_ROCK, 4, null )
                  .recipeStonecutting( () -> MOSSY_ROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final Block MOSSY_ROCK_TILES
        = MDBlocks.simple( "mossy_rock_tiles" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeJoin( () -> MDBuildingBlocks.ROCK_TILES, () -> MDPlantBlocks.MOSS, 1, null )
                  .dropSelf().create();

    public static final Block CRACKED_ROCK_BRICKS
        = MDBlocks.simple( "cracked_rock_bricks" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeSmelting( () -> MDBuildingBlocks.ROCK_BRICKS, 0.1, 200, null )
                  .dropSelf().create();

    public static final Block CRACKED_ROCK_TILES
        = MDBlocks.simple( "cracked_rock_tiles" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeSmelting( () -> MDBuildingBlocks.ROCK_TILES, 0.1, 200, null )
                  .dropSelf().create();

    public static final Block POLISHED_ROCK
        = MDBlocks.simple( "polished_rock" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeOne( () -> MDBuildingBlocks.SMOOTH_ROCK, 1, null )
                  .recipeStonecutting( () -> SMOOTH_ROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final Block CHISELED_ROCK
        = MDBlocks.simple( "chiseled_rock" )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeVert( () -> MDBuildingBlocks.SMOOTH_ROCK_SLAB, 1, null )
                  .recipeStonecutting( () -> SMOOTH_ROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    // Darkrock Blocks
    public static final Block DARKROCK = MDNatureBlocks.DARKROCK;

    public static final Block DARKROCK_BRICKS
        = MDBlocks.simple( "darkrock_bricks" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeBlock4( () -> MDNatureBlocks.DARKROCK, 4, null )
                  .recipeStonecutting( () -> DARKROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final Block SMOOTH_DARKROCK
        = MDBlocks.simple( "smooth_darkrock" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeSmelting( () -> MDNatureBlocks.DARKROCK, 0.1, 200, null )
                  .dropSelf().create();

    public static final Block DARKROCK_TILES
        = MDBlocks.simple( "darkrock_tiles" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeBlock4( () -> MDBuildingBlocks.SMOOTH_DARKROCK, 4, null )
                  .recipeStonecutting( () -> SMOOTH_DARKROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final Block MOSSY_DARKROCK
        = MDBlocks.simple( "mossy_darkrock" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeJoin( () -> MDNatureBlocks.DARKROCK, () -> MDPlantBlocks.MOSS, 1, null )
                  .dropSelf().create();

    public static final Block MOSSY_DARKROCK_BRICKS
        = MDBlocks.simple( "mossy_darkrock_bricks" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeJoin( () -> MDBuildingBlocks.DARKROCK_BRICKS, () -> MDPlantBlocks.MOSS, 1, "%s_from_moss" )
                  .recipeBlock4( () -> MDBuildingBlocks.MOSSY_DARKROCK, 4, null )
                  .recipeStonecutting( () -> MOSSY_DARKROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final Block MOSSY_DARKROCK_TILES
        = MDBlocks.simple( "mossy_darkrock_tiles" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeJoin( () -> MDBuildingBlocks.DARKROCK_TILES, () -> MDPlantBlocks.MOSS, 1, null )
                  .dropSelf().create();

    public static final Block CRACKED_DARKROCK_BRICKS
        = MDBlocks.simple( "cracked_darkrock_bricks" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeSmelting( () -> MDBuildingBlocks.DARKROCK_BRICKS, 0.1, 200, null )
                  .dropSelf().create();

    public static final Block CRACKED_DARKROCK_TILES
        = MDBlocks.simple( "cracked_darkrock_tiles" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeSmelting( () -> MDBuildingBlocks.DARKROCK_TILES, 0.1, 200, null )
                  .dropSelf().create();

    public static final Block POLISHED_DARKROCK
        = MDBlocks.simple( "polished_darkrock" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeOne( () -> MDBuildingBlocks.SMOOTH_DARKROCK, 1, null )
                  .recipeStonecutting( () -> SMOOTH_DARKROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final Block CHISELED_DARKROCK
        = MDBlocks.simple( "chiseled_darkrock" )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeVert( () -> MDBuildingBlocks.SMOOTH_DARKROCK_SLAB, 1, null )
                  .recipeStonecutting( () -> SMOOTH_DARKROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    // Murk Bricks Blocks
    public static final Block MURK_BRICKS
        = MDBlocks.simple( "murk_bricks" )
                  .rock( MaterialColor.CLAY, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeBlock4( () -> MDNatureBlocks.MURKY_TERRACOTTA, 4, null )
                  .recipeStonecutting( () -> MDNatureBlocks.MURKY_TERRACOTTA, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final Block MURK_TILES
        = MDBlocks.simple( "murk_tiles" )
                  .rock( MaterialColor.CLAY, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeBlock4( () -> MDBuildingBlocks.MURK_BRICKS, 4, null )
                  .recipeStonecutting( () -> MDNatureBlocks.MURKY_TERRACOTTA, 1, "%s_stonecutting" )
                  .recipeStonecutting( () -> MURK_BRICKS, 1, "%s_stonecutting_bricks" )
                  .dropSelf().create();

    public static final Block CRACKED_MURK_BRICKS
        = MDBlocks.simple( "cracked_murk_bricks" )
                  .rock( MaterialColor.CLAY, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeSmelting( () -> MDBuildingBlocks.MURK_BRICKS, 0.1, 200, null )
                  .dropSelf().create();

    public static final Block CRACKED_MURK_TILES
        = MDBlocks.simple( "cracked_murk_tiles" )
                  .rock( MaterialColor.CLAY, 1.5, 6 ).item( MDItemGroup.BLOCKS )
                  .recipeSmelting( () -> MDBuildingBlocks.MURK_TILES, 0.1, 200, null )
                  .dropSelf().create();

    // Misc Blocks
    public static final Block ASPHALT_CONCRETE = MDNatureBlocks.ASPHALT_CONCRETE;
    public static final Block LIMESTONE = MDNatureBlocks.LIMESTONE;

    // Wooden Blocks
    public static final Block BLACKWOOD_PLANKS
        = MDBlocks.simple( "blackwood_planks" )
                  .wood( MaterialColor.BLACK_TERRACOTTA ).item( MDItemGroup.BLOCKS )
                  .recipeOne( MDItemTags.BLACKWOOD_LOGS, 4, null )
                  .dropSelf().create();

    public static final Block INVER_PLANKS
        = MDBlocks.simple( "inver_planks" )
                  .wood( MaterialColor.WOOD ).item( MDItemGroup.BLOCKS )
                  .recipeOne( MDItemTags.INVER_LOGS, 4, null )
                  .dropSelf().create();




    //// SLABS ////

    private static MDBlocks.BlockConfig<VerticalSlabBlock> slab( String id, Supplier<IItemProvider> madeFrom ) {
        return MDBlocks.function( id, VerticalSlabBlock::new )
                       .item( MDItemGroup.BLOCKS )
                       .drops( BlockLoot.slab() )
                       .recipeSlab( madeFrom, - 1, null );
    }

    private static MDBlocks.BlockConfig<VerticalSlabBlock> stoneSlab( String id, Supplier<IItemProvider> madeFrom ) {
        return slab( id, madeFrom ).recipeStonecutting( madeFrom, 2, "%s_stonecutting" );
    }

    // Rock Slabs
    private static MDBlocks.BlockConfig<VerticalSlabBlock> rockSlab( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneSlab( id, madeFrom ).rock( MaterialColor.STONE, 1.5, 6 );
    }

    public static final VerticalSlabBlock ROCK_SLAB = rockSlab( "rock_slab", () -> ROCK ).create();
    public static final VerticalSlabBlock ROCK_BRICKS_SLAB = rockSlab( "rock_bricks_slab", () -> ROCK_BRICKS ).create();
    public static final VerticalSlabBlock SMOOTH_ROCK_SLAB = rockSlab( "smooth_rock_slab", () -> SMOOTH_ROCK ).create();
    public static final VerticalSlabBlock ROCK_TILES_SLAB = rockSlab( "rock_tiles_slab", () -> ROCK_TILES ).create();
    public static final VerticalSlabBlock MOSSY_ROCK_SLAB = rockSlab( "mossy_rock_slab", () -> MOSSY_ROCK ).create();
    public static final VerticalSlabBlock MOSSY_ROCK_BRICKS_SLAB = rockSlab( "mossy_rock_bricks_slab", () -> MOSSY_ROCK_BRICKS ).create();
    public static final VerticalSlabBlock MOSSY_ROCK_TILES_SLAB = rockSlab( "mossy_rock_tiles_slab", () -> MOSSY_ROCK_TILES ).create();
    public static final VerticalSlabBlock CRACKED_ROCK_BRICKS_SLAB = rockSlab( "cracked_rock_bricks_slab", () -> CRACKED_ROCK_BRICKS ).create();
    public static final VerticalSlabBlock CRACKED_ROCK_TILES_SLAB = rockSlab( "cracked_rock_tiles_slab", () -> CRACKED_ROCK_TILES ).create();

    // Darkrock Slabs
    private static MDBlocks.BlockConfig<VerticalSlabBlock> darkrockSlab( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneSlab( id, madeFrom ).rock( MaterialColor.BLACK, 1.5, 6 );
    }

    public static final VerticalSlabBlock DARKROCK_SLAB = darkrockSlab( "darkrock_slab", () -> DARKROCK ).create();
    public static final VerticalSlabBlock DARKROCK_BRICKS_SLAB = darkrockSlab( "darkrock_bricks_slab", () -> DARKROCK_BRICKS ).create();
    public static final VerticalSlabBlock SMOOTH_DARKROCK_SLAB = darkrockSlab( "smooth_darkrock_slab", () -> SMOOTH_DARKROCK ).create();
    public static final VerticalSlabBlock DARKROCK_TILES_SLAB = darkrockSlab( "darkrock_tiles_slab", () -> DARKROCK_TILES ).create();
    public static final VerticalSlabBlock MOSSY_DARKROCK_SLAB = darkrockSlab( "mossy_darkrock_slab", () -> MOSSY_DARKROCK ).create();
    public static final VerticalSlabBlock MOSSY_DARKROCK_BRICKS_SLAB = darkrockSlab( "mossy_darkrock_bricks_slab", () -> MOSSY_DARKROCK_BRICKS ).create();
    public static final VerticalSlabBlock MOSSY_DARKROCK_TILES_SLAB = darkrockSlab( "mossy_darkrock_tiles_slab", () -> MOSSY_DARKROCK_TILES ).create();
    public static final VerticalSlabBlock CRACKED_DARKROCK_BRICKS_SLAB = darkrockSlab( "cracked_darkrock_bricks_slab", () -> CRACKED_DARKROCK_BRICKS ).create();
    public static final VerticalSlabBlock CRACKED_DARKROCK_TILES_SLAB = darkrockSlab( "cracked_darkrock_tiles_slab", () -> CRACKED_DARKROCK_TILES ).create();

    // Murk Bricks Slabs
    private static MDBlocks.BlockConfig<VerticalSlabBlock> murkSlab( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneSlab( id, madeFrom ).rock( MaterialColor.CLAY, 1.5, 6 );
    }

    public static final VerticalSlabBlock MURK_BRICKS_SLAB = murkSlab( "murk_bricks_slab", () -> MURK_BRICKS ).create();
    public static final VerticalSlabBlock MURK_TILES_SLAB = murkSlab( "murk_tiles_slab", () -> MURK_TILES ).create();
    public static final VerticalSlabBlock CRACKED_MURK_BRICKS_SLAB = murkSlab( "cracked_murk_bricks_slab", () -> CRACKED_MURK_BRICKS ).create();
    public static final VerticalSlabBlock CRACKED_MURK_TILES_SLAB = murkSlab( "cracked_murk_tiles_slab", () -> CRACKED_MURK_TILES ).create();

    // Misc Slabs
    public static final VerticalSlabBlock ASPHALT_CONCRETE_SLAB = stoneSlab( "asphalt_concrete_slab", () -> ASPHALT_CONCRETE ).asphalt().create();
    public static final VerticalSlabBlock LIMESTONE_SLAB = stoneSlab( "limestone_slab", () -> LIMESTONE ).rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ).create();

    // Wooden Slabs
    public static final VerticalSlabBlock BLACKWOOD_SLAB = slab( "blackwood_slab", () -> BLACKWOOD_PLANKS ).wood( MaterialColor.BLACK_TERRACOTTA ).create();
    public static final VerticalSlabBlock INVER_SLAB = slab( "inver_slab", () -> INVER_PLANKS ).wood( MaterialColor.WOOD ).create();




    //// STAIRS ////

    private static MDBlocks.BlockConfig<StairsBlock> stairs( String id, Supplier<IItemProvider> madeFrom ) {
        return MDBlocks.function( id, props -> new StairsBlock( true, props ) )
                       .item( MDItemGroup.BLOCKS )
                       .dropSelf()
                       .recipeStairs( madeFrom, - 1, null );
    }

    private static MDBlocks.BlockConfig<StairsBlock> stoneStairs( String id, Supplier<IItemProvider> madeFrom ) {
        return stairs( id, madeFrom ).recipeStonecutting( madeFrom, 1, "%s_stonecutting" );
    }

    // Rock Stairs
    private static MDBlocks.BlockConfig<StairsBlock> rockStairs( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneStairs( id, madeFrom ).rock( MaterialColor.STONE, 1.5, 6 );
    }

    public static final StairsBlock ROCK_STAIRS = rockStairs( "rock_stairs", () -> ROCK ).create();
    public static final StairsBlock ROCK_BRICKS_STAIRS = rockStairs( "rock_bricks_stairs", () -> ROCK_BRICKS ).create();
    public static final StairsBlock SMOOTH_ROCK_STAIRS = rockStairs( "smooth_rock_stairs", () -> SMOOTH_ROCK ).create();
    public static final StairsBlock ROCK_TILES_STAIRS = rockStairs( "rock_tiles_stairs", () -> ROCK_TILES ).create();
    public static final StairsBlock MOSSY_ROCK_STAIRS = rockStairs( "mossy_rock_stairs", () -> MOSSY_ROCK ).create();
    public static final StairsBlock MOSSY_ROCK_BRICKS_STAIRS = rockStairs( "mossy_rock_bricks_stairs", () -> MOSSY_ROCK_BRICKS ).create();
    public static final StairsBlock MOSSY_ROCK_TILES_STAIRS = rockStairs( "mossy_rock_tiles_stairs", () -> MOSSY_ROCK_TILES ).create();
    public static final StairsBlock CRACKED_ROCK_BRICKS_STAIRS = rockStairs( "cracked_rock_bricks_stairs", () -> CRACKED_ROCK_BRICKS ).create();
    public static final StairsBlock CRACKED_ROCK_TILES_STAIRS = rockStairs( "cracked_rock_tiles_stairs", () -> CRACKED_ROCK_TILES ).create();

    // Darkrock Stairs
    private static MDBlocks.BlockConfig<StairsBlock> darkrockStairs( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneStairs( id, madeFrom ).rock( MaterialColor.BLACK, 1.5, 6 );
    }

    public static final StairsBlock DARKROCK_STAIRS = darkrockStairs( "darkrock_stairs", () -> DARKROCK ).create();
    public static final StairsBlock DARKROCK_BRICKS_STAIRS = darkrockStairs( "darkrock_bricks_stairs", () -> DARKROCK_BRICKS ).create();
    public static final StairsBlock SMOOTH_DARKROCK_STAIRS = darkrockStairs( "smooth_darkrock_stairs", () -> SMOOTH_DARKROCK ).create();
    public static final StairsBlock DARKROCK_TILES_STAIRS = darkrockStairs( "darkrock_tiles_stairs", () -> DARKROCK_TILES ).create();
    public static final StairsBlock MOSSY_DARKROCK_STAIRS = darkrockStairs( "mossy_darkrock_stairs", () -> MOSSY_DARKROCK ).create();
    public static final StairsBlock MOSSY_DARKROCK_BRICKS_STAIRS = darkrockStairs( "mossy_darkrock_bricks_stairs", () -> MOSSY_DARKROCK_BRICKS ).create();
    public static final StairsBlock MOSSY_DARKROCK_TILES_STAIRS = darkrockStairs( "mossy_darkrock_tiles_stairs", () -> MOSSY_DARKROCK_TILES ).create();
    public static final StairsBlock CRACKED_DARKROCK_BRICKS_STAIRS = darkrockStairs( "cracked_darkrock_bricks_stairs", () -> CRACKED_DARKROCK_BRICKS ).create();
    public static final StairsBlock CRACKED_DARKROCK_TILES_STAIRS = darkrockStairs( "cracked_darkrock_tiles_stairs", () -> CRACKED_DARKROCK_TILES ).create();

    // Murk Bricks Stairs
    private static MDBlocks.BlockConfig<StairsBlock> murkStairs( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneStairs( id, madeFrom ).rock( MaterialColor.CLAY, 1.5, 6 );
    }

    public static final StairsBlock MURK_BRICKS_STAIRS = murkStairs( "murk_bricks_stairs", () -> MURK_BRICKS ).create();
    public static final StairsBlock MURK_TILES_STAIRS = murkStairs( "murk_tiles_stairs", () -> MURK_TILES ).create();
    public static final StairsBlock CRACKED_MURK_BRICKS_STAIRS = murkStairs( "cracked_murk_bricks_stairs", () -> CRACKED_MURK_BRICKS ).create();
    public static final StairsBlock CRACKED_MURK_TILES_STAIRS = murkStairs( "cracked_murk_tiles_stairs", () -> CRACKED_MURK_TILES ).create();

    // Misc Stairs
    public static final StairsBlock ASPHALT_CONCRETE_STAIRS = stoneStairs( "asphalt_concrete_stairs", () -> ASPHALT_CONCRETE ).asphalt().create();
    public static final StairsBlock LIMESTONE_STAIRS = stoneStairs( "limestone_stairs", () -> LIMESTONE ).rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ).create();

    // Wooden Stairs
    public static final StairsBlock BLACKWOOD_STAIRS = stairs( "blackwood_stairs", () -> BLACKWOOD_PLANKS ).wood( MaterialColor.BLACK_TERRACOTTA ).create();
    public static final StairsBlock INVER_STAIRS = stairs( "inver_stairs", () -> INVER_PLANKS ).wood( MaterialColor.WOOD ).create();




    //// STEPS ////

    private static MDBlocks.BlockConfig<StairsBlock> step( String id, Supplier<IItemProvider> madeFrom ) {
        return MDBlocks.function( id, props -> new StairsBlock( true, props ) )
                       .item( MDItemGroup.BLOCKS )
                       .dropSelf()
                       .recipeStep( madeFrom, - 1, null );
    }

    private static MDBlocks.BlockConfig<StairsBlock> stoneStep( String id, Supplier<IItemProvider> madeFrom ) {
        return step( id, madeFrom ).recipeStonecutting( madeFrom, 4, "%s_stonecutting" );
    }

    // Rock Steps
    private static MDBlocks.BlockConfig<StairsBlock> rockStep( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneStep( id, madeFrom ).rock( MaterialColor.STONE, 1.5, 6 );
    }

    public static final StairsBlock ROCK_STEP = rockStep( "rock_step", () -> ROCK ).create();
    public static final StairsBlock ROCK_BRICKS_STEP = rockStep( "rock_bricks_step", () -> ROCK_BRICKS ).create();
    public static final StairsBlock SMOOTH_ROCK_STEP = rockStep( "smooth_rock_step", () -> SMOOTH_ROCK ).create();
    public static final StairsBlock ROCK_TILES_STEP = rockStep( "rock_tiles_step", () -> ROCK_TILES ).create();
    public static final StairsBlock MOSSY_ROCK_STEP = rockStep( "mossy_rock_step", () -> MOSSY_ROCK ).create();
    public static final StairsBlock MOSSY_ROCK_BRICKS_STEP = rockStep( "mossy_rock_bricks_step", () -> MOSSY_ROCK_BRICKS ).create();
    public static final StairsBlock MOSSY_ROCK_TILES_STEP = rockStep( "mossy_rock_tiles_step", () -> MOSSY_ROCK_TILES ).create();
    public static final StairsBlock CRACKED_ROCK_BRICKS_STEP = rockStep( "cracked_rock_bricks_step", () -> CRACKED_ROCK_BRICKS ).create();
    public static final StairsBlock CRACKED_ROCK_TILES_STEP = rockStep( "cracked_rock_tiles_step", () -> CRACKED_ROCK_TILES ).create();

    // Darkrock Steps
    private static MDBlocks.BlockConfig<StairsBlock> darkrockStep( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneStep( id, madeFrom ).rock( MaterialColor.BLACK, 1.5, 6 );
    }

    public static final StairsBlock DARKROCK_STEP = darkrockStep( "darkrock_step", () -> DARKROCK ).create();
    public static final StairsBlock DARKROCK_BRICKS_STEP = darkrockStep( "darkrock_bricks_step", () -> DARKROCK_BRICKS ).create();
    public static final StairsBlock SMOOTH_DARKROCK_STEP = darkrockStep( "smooth_darkrock_step", () -> SMOOTH_DARKROCK ).create();
    public static final StairsBlock DARKROCK_TILES_STEP = darkrockStep( "darkrock_tiles_step", () -> DARKROCK_TILES ).create();
    public static final StairsBlock MOSSY_DARKROCK_STEP = darkrockStep( "mossy_darkrock_step", () -> MOSSY_DARKROCK ).create();
    public static final StairsBlock MOSSY_DARKROCK_BRICKS_STEP = darkrockStep( "mossy_darkrock_bricks_step", () -> MOSSY_DARKROCK_BRICKS ).create();
    public static final StairsBlock MOSSY_DARKROCK_TILES_STEP = darkrockStep( "mossy_darkrock_tiles_step", () -> MOSSY_DARKROCK_TILES ).create();
    public static final StairsBlock CRACKED_DARKROCK_BRICKS_STEP = darkrockStep( "cracked_darkrock_bricks_step", () -> CRACKED_DARKROCK_BRICKS ).create();
    public static final StairsBlock CRACKED_DARKROCK_TILES_STEP = darkrockStep( "cracked_darkrock_tiles_step", () -> CRACKED_DARKROCK_TILES ).create();

    // Murk Bricks Steps
    private static MDBlocks.BlockConfig<StairsBlock> murkStep( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneStep( id, madeFrom ).rock( MaterialColor.CLAY, 1.5, 6 );
    }

    public static final StairsBlock MURK_BRICKS_STEP = murkStep( "murk_bricks_step", () -> MURK_BRICKS ).create();
    public static final StairsBlock MURK_TILES_STEP = murkStep( "murk_tiles_step", () -> MURK_TILES ).create();
    public static final StairsBlock CRACKED_MURK_BRICKS_STEP = murkStep( "cracked_murk_bricks_step", () -> CRACKED_MURK_BRICKS ).create();
    public static final StairsBlock CRACKED_MURK_TILES_STEP = murkStep( "cracked_murk_tiles_step", () -> CRACKED_MURK_TILES ).create();

    // Misc Steps
    public static final StairsBlock ASPHALT_CONCRETE_STEP = stoneStep( "asphalt_concrete_step", () -> ASPHALT_CONCRETE ).asphalt().create();
    public static final StairsBlock LIMESTONE_STEP = stoneStep( "limestone_step", () -> LIMESTONE ).rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ).create();

    // Blackwood Steps
    public static final StairsBlock BLACKWOOD_STEP = step( "blackwood_step", () -> BLACKWOOD_PLANKS ).wood( MaterialColor.BLACK_TERRACOTTA ).create();
    public static final StairsBlock INVER_STEP = step( "inver_step", () -> INVER_PLANKS ).wood( MaterialColor.WOOD ).create();




    //// CORNERS ////

    private static MDBlocks.BlockConfig<CornerBlock> corner( String id, Supplier<IItemProvider> madeFrom ) {
        return MDBlocks.function( id, CornerBlock::new )
                       .item( MDItemGroup.BLOCKS )
                       .drops( BlockLoot.corner() )
                       .recipeCorner( madeFrom, - 1, null );
    }

    private static MDBlocks.BlockConfig<CornerBlock> stoneCorner( String id, Supplier<IItemProvider> madeFrom ) {
        return corner( id, madeFrom ).recipeStonecutting( madeFrom, 8, "%s_stonecutting" );
    }

    // Rock Corners
    private static MDBlocks.BlockConfig<CornerBlock> rockCorner( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneCorner( id, madeFrom ).rock( MaterialColor.STONE, 1.5, 6 );
    }

    public static final CornerBlock ROCK_CORNER = rockCorner( "rock_corner", () -> ROCK ).create();
    public static final CornerBlock ROCK_BRICKS_CORNER = rockCorner( "rock_bricks_corner", () -> ROCK_BRICKS ).create();
    public static final CornerBlock SMOOTH_ROCK_CORNER = rockCorner( "smooth_rock_corner", () -> SMOOTH_ROCK ).create();
    public static final CornerBlock ROCK_TILES_CORNER = rockCorner( "rock_tiles_corner", () -> ROCK_TILES ).create();
    public static final CornerBlock MOSSY_ROCK_CORNER = rockCorner( "mossy_rock_corner", () -> MOSSY_ROCK ).create();
    public static final CornerBlock MOSSY_ROCK_BRICKS_CORNER = rockCorner( "mossy_rock_bricks_corner", () -> MOSSY_ROCK_BRICKS ).create();
    public static final CornerBlock MOSSY_ROCK_TILES_CORNER = rockCorner( "mossy_rock_tiles_corner", () -> MOSSY_ROCK_TILES ).create();
    public static final CornerBlock CRACKED_ROCK_BRICKS_CORNER = rockCorner( "cracked_rock_bricks_corner", () -> CRACKED_ROCK_BRICKS ).create();
    public static final CornerBlock CRACKED_ROCK_TILES_CORNER = rockCorner( "cracked_rock_tiles_corner", () -> CRACKED_ROCK_TILES ).create();

    // Darkrock Corners
    private static MDBlocks.BlockConfig<CornerBlock> darkrockCorner( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneCorner( id, madeFrom ).rock( MaterialColor.BLACK, 1.5, 6 );
    }

    public static final CornerBlock DARKROCK_CORNER = darkrockCorner( "darkrock_corner", () -> DARKROCK ).create();
    public static final CornerBlock DARKROCK_BRICKS_CORNER = darkrockCorner( "darkrock_bricks_corner", () -> DARKROCK_BRICKS ).create();
    public static final CornerBlock SMOOTH_DARKROCK_CORNER = darkrockCorner( "smooth_darkrock_corner", () -> SMOOTH_DARKROCK ).create();
    public static final CornerBlock DARKROCK_TILES_CORNER = darkrockCorner( "darkrock_tiles_corner", () -> DARKROCK_TILES ).create();
    public static final CornerBlock MOSSY_DARKROCK_CORNER = darkrockCorner( "mossy_darkrock_corner", () -> MOSSY_DARKROCK ).create();
    public static final CornerBlock MOSSY_DARKROCK_BRICKS_CORNER = darkrockCorner( "mossy_darkrock_bricks_corner", () -> MOSSY_DARKROCK_BRICKS ).create();
    public static final CornerBlock MOSSY_DARKROCK_TILES_CORNER = darkrockCorner( "mossy_darkrock_tiles_corner", () -> MOSSY_DARKROCK_TILES ).create();
    public static final CornerBlock CRACKED_DARKROCK_BRICKS_CORNER = darkrockCorner( "cracked_darkrock_bricks_corner", () -> CRACKED_DARKROCK_BRICKS ).create();
    public static final CornerBlock CRACKED_DARKROCK_TILES_CORNER = darkrockCorner( "cracked_darkrock_tiles_corner", () -> CRACKED_DARKROCK_TILES ).create();

    // Murk Bricks Corners
    private static MDBlocks.BlockConfig<CornerBlock> murkCorner( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneCorner( id, madeFrom ).rock( MaterialColor.CLAY, 1.5, 6 );
    }

    public static final CornerBlock MURK_BRICKS_CORNER = murkCorner( "murk_bricks_corner", () -> MURK_BRICKS ).create();
    public static final CornerBlock MURK_TILES_CORNER = murkCorner( "murk_tiles_corner", () -> MURK_TILES ).create();
    public static final CornerBlock CRACKED_MURK_BRICKS_CORNER = murkCorner( "cracked_murk_bricks_corner", () -> CRACKED_MURK_BRICKS ).create();
    public static final CornerBlock CRACKED_MURK_TILES_CORNER = murkCorner( "cracked_murk_tiles_corner", () -> CRACKED_MURK_TILES ).create();

    // Misc Corners
    public static final CornerBlock ASPHALT_CONCRETE_CORNER = stoneCorner( "asphalt_concrete_corner", () -> ASPHALT_CONCRETE ).asphalt().create();
    public static final CornerBlock LIMESTONE_CORNER = stoneCorner( "limestone_corner", () -> LIMESTONE ).rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ).create();

    // Wooden Corners
    public static final CornerBlock BLACKWOOD_CORNER = corner( "blackwood_corner", () -> BLACKWOOD_PLANKS ).wood( MaterialColor.BLACK_TERRACOTTA ).create();
    public static final CornerBlock INVER_CORNER = corner( "inver_corner", () -> INVER_PLANKS ).wood( MaterialColor.WOOD ).create();




    //// WALLS ////

    private static MDBlocks.BlockConfig<WallBlock> wall( String id, Supplier<IItemProvider> madeFrom ) {
        return MDBlocks.function( id, WallBlock::new )
                       .item( MDItemGroup.DECORATIVES )
                       .dropSelf()
                       .recipeWall( madeFrom, 6, null );
    }

    private static MDBlocks.BlockConfig<WallBlock> stoneWall( String id, Supplier<IItemProvider> madeFrom ) {
        return wall( id, madeFrom ).recipeStonecutting( madeFrom, 1, "%s_stonecutting" );
    }

    // Rock Walls
    private static MDBlocks.BlockConfig<WallBlock> rockWall( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneWall( id, madeFrom ).rock( MaterialColor.STONE, 1.5, 6 );
    }

    public static final WallBlock ROCK_WALL = rockWall( "rock_wall", () -> ROCK ).create();
    public static final WallBlock ROCK_BRICKS_WALL = rockWall( "rock_bricks_wall", () -> ROCK_BRICKS ).create();
    public static final WallBlock SMOOTH_ROCK_WALL = rockWall( "smooth_rock_wall", () -> SMOOTH_ROCK ).create();
    public static final WallBlock MOSSY_ROCK_WALL = rockWall( "mossy_rock_wall", () -> MOSSY_ROCK ).create();
    public static final WallBlock MOSSY_ROCK_BRICKS_WALL = rockWall( "mossy_rock_bricks_wall", () -> MOSSY_ROCK_BRICKS ).create();
    public static final WallBlock CRACKED_ROCK_BRICKS_WALL = rockWall( "cracked_rock_bricks_wall", () -> CRACKED_ROCK_BRICKS ).create();

    // Darkrock Walls
    private static MDBlocks.BlockConfig<WallBlock> darkrockWall( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneWall( id, madeFrom ).rock( MaterialColor.BLACK, 1.5, 6 );
    }

    public static final WallBlock DARKROCK_WALL = darkrockWall( "darkrock_wall", () -> DARKROCK ).create();
    public static final WallBlock DARKROCK_BRICKS_WALL = darkrockWall( "darkrock_bricks_wall", () -> DARKROCK_BRICKS ).create();
    public static final WallBlock SMOOTH_DARKROCK_WALL = darkrockWall( "smooth_darkrock_wall", () -> SMOOTH_DARKROCK ).create();
    public static final WallBlock MOSSY_DARKROCK_WALL = darkrockWall( "mossy_darkrock_wall", () -> MOSSY_DARKROCK ).create();
    public static final WallBlock MOSSY_DARKROCK_BRICKS_WALL = darkrockWall( "mossy_darkrock_bricks_wall", () -> MOSSY_DARKROCK_BRICKS ).create();
    public static final WallBlock CRACKED_DARKROCK_BRICKS_WALL = darkrockWall( "cracked_darkrock_bricks_wall", () -> CRACKED_DARKROCK_BRICKS ).create();

    // Murk Bricks Walls
    private static MDBlocks.BlockConfig<WallBlock> murkWall( String id, Supplier<IItemProvider> madeFrom ) {
        return stoneWall( id, madeFrom ).rock( MaterialColor.CLAY, 1.5, 6 );
    }

    public static final WallBlock MURK_BRICKS_WALL = murkWall( "murk_bricks_wall", () -> MURK_BRICKS ).create();
    public static final WallBlock CRACKED_MURK_BRICKS_WALL = murkWall( "cracked_murk_bricks_wall", () -> CRACKED_MURK_BRICKS ).create();

    // Misc Walls
    public static final WallBlock ASPHALT_CONCRETE_WALL = stoneWall( "asphalt_concrete_wall", () -> ASPHALT_CONCRETE ).asphalt().create();
    public static final WallBlock LIMESTONE_WALL = stoneWall( "limestone_wall", () -> LIMESTONE ).rock( MaterialColor.WHITE_TERRACOTTA, 1, 4 ).create();




    //// MISC ////

    // Fences
    public static final FenceBlock BLACKWOOD_FENCE
        = MDBlocks.function( "blackwood_fence", FenceBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA ).item( MDItemGroup.DECORATIVES )
                  .recipeFence( () -> MDItems.BLACKWOOD_STICK, () -> BLACKWOOD_PLANKS, 3, null )
                  .dropSelf().create();

    public static final FenceBlock INVER_FENCE
        = MDBlocks.function( "inver_fence", FenceBlock::new )
                  .wood( MaterialColor.WOOD ).item( MDItemGroup.DECORATIVES )
                  .recipeFence( () -> MDItems.INVER_STICK, () -> INVER_PLANKS, 3, null )
                  .dropSelf().create();

    // Fence Gates
    public static final FenceGateBlock BLACKWOOD_FENCE_GATE
        = MDBlocks.function( "blackwood_fence_gate", FenceGateBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA ).item( MDItemGroup.DECORATIVES )
                  .recipeFence( () -> BLACKWOOD_PLANKS, () -> MDItems.BLACKWOOD_STICK, 1, null )
                  .dropSelf().create();

    public static final FenceGateBlock INVER_FENCE_GATE
        = MDBlocks.function( "inver_fence_gate", FenceGateBlock::new )
                  .wood( MaterialColor.WOOD ).item( MDItemGroup.DECORATIVES )
                  .recipeFence( () -> INVER_PLANKS, () -> MDItems.INVER_STICK, 1, null )
                  .dropSelf().create();


    // Doors
    public static final DoorBlock BLACKWOOD_DOOR
        = MDBlocks.function( "blackwood_door", DoorBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA ).item( MDItemGroup.DECORATIVES )
                  .recipeDoor( () -> BLACKWOOD_PLANKS, 3, null )
                  .dropSelf().create();

    public static final DoorBlock INVER_DOOR
        = MDBlocks.function( "inver_door", DoorBlock::new )
                  .wood( MaterialColor.WOOD ).item( MDItemGroup.DECORATIVES )
                  .recipeDoor( () -> INVER_PLANKS, 3, null )
                  .dropSelf().create();

    public static final DoorBlock ALUMINIUM_DOOR
        = MDBlocks.function( "aluminium_door", DoorBlock::new )
                  .metal( MaterialColor.IRON ).item( MDItemGroup.DECORATIVES )
                  .recipeDoor( () -> MDItems.ALUMINIUM_INGOT, 3, null )
                  .dropSelf().create();

    // Trap Doors
    public static final ExtTrapDoorBlock BLACKWOOD_TRAPDOOR
        = MDBlocks.function( "blackwood_trapdoor", ExtTrapDoorBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA ).item( MDItemGroup.DECORATIVES )
                  .recipeWall( () -> BLACKWOOD_PLANKS, 2, null )
                  .dropSelf().create();

    public static final ExtTrapDoorBlock INVER_TRAPDOOR
        = MDBlocks.function( "inver_trapdoor", ExtTrapDoorBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA ).item( MDItemGroup.DECORATIVES )
                  .recipeWall( () -> INVER_PLANKS, 2, null )
                  .dropSelf().create();

    public static final ExtTrapDoorBlock ALUMINIUM_TRAPDOOR
        = MDBlocks.function( "aluminium_trapdoor", ExtTrapDoorBlock::new )
                  .metal( MaterialColor.IRON ).item( MDItemGroup.DECORATIVES )
                  .recipeBlock4( () -> MDItems.ALUMINIUM_INGOT, 1, null )
                  .dropSelf().create();


    // Pillars
    public static final PillarBlock ROCK_PILLAR
        = MDBlocks.function( "rock_pillar", props -> new PillarBlock( props, 14 ) )
                  .rock( MaterialColor.STONE, 1.5, 6 ).item( MDItemGroup.DECORATIVES )
                  .recipeVert3( () -> SMOOTH_ROCK, 3, null )
                  .recipeStonecutting( () -> SMOOTH_ROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final PillarBlock DARKROCK_PILLAR
        = MDBlocks.function( "darkrock_pillar", props -> new PillarBlock( props, 14 ) )
                  .rock( MaterialColor.BLACK, 1.5, 6 ).item( MDItemGroup.DECORATIVES )
                  .recipeVert3( () -> SMOOTH_DARKROCK, 3, null )
                  .recipeStonecutting( () -> SMOOTH_DARKROCK, 1, "%s_stonecutting" )
                  .dropSelf().create();

    public static final PillarBlock MURK_PILLAR
        = MDBlocks.function( "murk_pillar", props -> new PillarBlock( props, 14 ) )
                  .rock( MaterialColor.CLAY, 1.5, 6 ).item( MDItemGroup.DECORATIVES )
                  .recipeVert3( () -> MDNatureBlocks.MURKY_TERRACOTTA, 3, null )
                  .recipeStonecutting( () -> MDNatureBlocks.MURKY_TERRACOTTA, 1, "%s_stonecutting" )
                  .dropSelf().create();


    // Glass
    public static final TranslucentBlock MURKY_GLASS
        = MDBlocks.function( "murky_glass", TranslucentBlock::new )
                  .glass().item( MDItemGroup.BLOCKS )
                  .drops( BlockLoot.silkTouch( BlockLoot.self() ) )
                  .recipeSmelting( () -> MDNatureBlocks.MURKY_SAND, 0.1, 200, null )
                  .alias( "dark_glass" ).create();

    public static final PanelBlock MURKY_GLASS_PANE
        = MDBlocks.function( "murky_glass_pane", TranslucentPanelBlock::new )
                  .glass().item( MDItemGroup.DECORATIVES )
                  .drops( BlockLoot.silkTouch( BlockLoot.self() ) )
                  .recipeWall( () -> MURKY_GLASS, 16, null )
                  .alias( "dark_glass_pane" ).create();

    // Poles
    public static final PillarBlock BLACKWOOD_POLE
        = MDBlocks.function( "blackwood_pole", props -> new PillarBlock( props, 4 ) )
                  .wood( MaterialColor.BLACK_TERRACOTTA ).item( MDItemGroup.DECORATIVES )
                  .recipeVert3( () -> BLACKWOOD_PLANKS, 6, null )
                  .dropSelf().create();

    public static final PillarBlock INVER_POLE
        = MDBlocks.function( "inver_pole", props -> new PillarBlock( props, 4 ) )
                  .wood( MaterialColor.WOOD ).item( MDItemGroup.DECORATIVES )
                  .recipeVert3( () -> INVER_PLANKS, 6, null )
                  .dropSelf().create();


    // Hedges
    private static HedgeBlock hedge( String id, int color, Supplier<IItemProvider> madeFrom ) {
        return ( color == - 1
                 ? MDBlocks.function( id, HedgeBlock::new )
                 : MDBlocks.function( id, props -> new ColoredHedgeBlock( props, color ) ) )
                   .leaves( MaterialColor.FOLIAGE, 0.3 )
                   .sound( SoundType.SWEET_BERRY_BUSH )
                   .item( MDItemGroup.DECORATIVES )
                   .dropSelf()
                   .recipeWall( madeFrom, 6, null )
                   .create();
    }

    public static final HedgeBlock BLACKWOOD_HEDGE = hedge( "blackwood_hedge", 0, () -> MDTreeBlocks.BLACKWOOD_LEAVES );
    public static final HedgeBlock INVER_HEDGE = hedge( "inver_hedge", 1, () -> MDTreeBlocks.INVER_LEAVES );
    public static final HedgeBlock RED_INVER_HEDGE = hedge( "red_inver_hedge", 2, () -> MDTreeBlocks.RED_INVER_LEAVES );
    public static final HedgeBlock MUXUS_HEDGE = hedge( "muxus_hedge", - 1, () -> MDPlantBlocks.MUXUS_BUSH );

    // Torches
    public static final TorchBlock EXTINGUISHED_ANTHRACITE_TORCH
        = MDBlocks.function( "extinguished_anthracite_torch", props -> new TorchBlock( false, props ) )
                  .torch( MaterialColor.SNOW, 15 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final ExtinguishableTorchBlock ANTHRACITE_TORCH
        = MDBlocks.function( "anthracite_torch", props -> new ExtinguishableTorchBlock( true, EXTINGUISHED_ANTHRACITE_TORCH, props ) )
                  .torch( MaterialColor.BLACK, 0 ).item( MDItemGroup.DECORATIVES )
                  .recipeTorch( () -> MDItems.ANTHRACITE, MDItemTags.STICKS, 4, null )
                  .dropSelf().create();

    public static final TorchBlock LUMINOSITE_TORCH
        = MDBlocks.function( "luminosite_torch", props -> new TorchBlock( false, props ) )
                  .torch( MaterialColor.SNOW, 15 ).sound( SoundType.METAL ).item( MDItemGroup.DECORATIVES )
                  .recipeTorch( () -> MDItems.LUMINOSITE_SHARDS, () -> MDItems.ALUMINIUM_INGOT, 6, null )
                  .dropSelf().create();

    // Ladders
    public static final ExtLadderBlock BLACKWOOD_LADDER
        = MDBlocks.function( "blackwood_ladder", ExtLadderBlock::new )
                  .props( Material.MISCELLANEOUS ).hardness( 0.4F ).sound( SoundType.LADDER ).item( MDItemGroup.DECORATIVES )
                  .recipeH( () -> MDItems.BLACKWOOD_STICK, 3, null )
                  .dropSelf().create();

    public static final ExtLadderBlock INVER_LADDER
        = MDBlocks.function( "inver_ladder", ExtLadderBlock::new )
                  .props( Material.MISCELLANEOUS ).hardness( 0.4F ).sound( SoundType.LADDER ).item( MDItemGroup.DECORATIVES )
                  .recipeH( () -> MDItems.INVER_STICK, 3, null )
                  .dropSelf().create();

    // Utilities
    public static final WorkbenchBlock BLACKWOOD_WORKBENCH
        = MDBlocks.function( "blackwood_workbench", WorkbenchBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA ).item( MDItemGroup.DECORATIVES )
                  .recipeBlock4( () -> BLACKWOOD_PLANKS, 1, null )
                  .dropSelf().create();

    public static final WorkbenchBlock INVER_WORKBENCH
        = MDBlocks.function( "inver_workbench", WorkbenchBlock::new )
                  .wood( MaterialColor.WOOD ).item( MDItemGroup.DECORATIVES )
                  .recipeBlock4( () -> INVER_PLANKS, 1, null )
                  .dropSelf().create();

    public static final RockFurnaceBlock ROCK_FURNACE
        = MDBlocks.function( "rock_furnace", RockFurnaceBlock::new )
                  .rock( MaterialColor.STONE, 1.5, 6 ).light( 15 ).item( MDItemGroup.DECORATIVES )
                  .recipeO8( MDItemTags.ROCK, 1, null )
                  .dropSelf().create();

    // Chests
    public static final ExtChestBlock BLACKWOOD_CHEST
        = MDBlocks.function( "blackwood_chest", BlackwoodChestBlock::new )
                  .wood( MaterialColor.BLACK_TERRACOTTA ).item( MDItemGroup.DECORATIVES )
                  .recipeO8( () -> BLACKWOOD_PLANKS, 1, null )
                  .drops( BlockLoot.chest() ).create();

    public static final ExtChestBlock INVER_CHEST
        = MDBlocks.function( "inver_chest", InverChestBlock::new )
                  .wood( MaterialColor.WOOD ).item( MDItemGroup.DECORATIVES )
                  .recipeO8( () -> INVER_PLANKS, 1, null )
                  .drops( BlockLoot.chest() ).create();

    // Misc
    public static final FallBlock ASH_BLOCK
        = MDBlocks.function( "ash_block", props -> new DigableFallBlock( 0x333333, props ) )
                  .ash( MaterialColor.GRAY ).item( MDItemGroup.BLOCKS )
                  .recipeBlock9( () -> MDItems.ASH, 1, null )
                  .dropSelf().create();

    public static final FallBlock SALT_DUST_BLOCK
        = MDBlocks.function( "salt_dust_block", props -> new DigableFallBlock( 0xffffff, props ) )
                  .dust( MaterialColor.SNOW, false ).item( MDItemGroup.BLOCKS )
                  .recipeBlock9( () -> MDItems.SALT_DUST, 1, null )
                  .dropSelf().create();




    //// PORTAL ////

    public static final Block DARK_STONE_BRICKS
        = MDBlocks.simple( "dark_stone_bricks" )
                  .rock( MaterialColor.STONE, 2, 6 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final Block INSCRIBED_DARK_STONE_BRICKS
        = MDBlocks.simple( "inscribed_dark_stone_bricks" )
                  .rock( MaterialColor.STONE, 2, 6 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_NATURE
        = MDBlocks.function( "dark_stone_bricks_nature", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_CURSE
        = MDBlocks.function( "dark_stone_bricks_curse", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_CYEN
        = MDBlocks.function( "dark_stone_bricks_cyen", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_FYREN
        = MDBlocks.function( "dark_stone_bricks_fyren", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_TIMEN
        = MDBlocks.function( "dark_stone_bricks_timen", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_PORTAL
        = MDBlocks.function( "dark_stone_bricks_portal", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final HorizontalFacingBlock DARK_STONE_BRICKS_RGSW
        = MDBlocks.function( "dark_stone_bricks_rgsw", HorizontalFacingBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item( MDItemGroup.DECORATIVES )
                  .dropSelf().create();

    public static final HorizontalPortalFrameBlock HORIZONTAL_PORTAL_FRAME
        = MDBlocks.function( "horizontal_portal_frame", HorizontalPortalFrameBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item().create();

    public static final VerticalPortalFrameBlock VERTICAL_PORTAL_FRAME
        = MDBlocks.function( "vertical_portal_frame", VerticalPortalFrameBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item().create();

    public static final PortalCornerBlock PORTAL_CORNER
        = MDBlocks.function( "portal_corner", PortalCornerBlock::new )
                  .rock( MaterialColor.STONE, 2, 6 ).item().create();

}
