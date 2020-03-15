/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import modernity.common.block.loot.BlockLoot;
import modernity.common.block.loot.CropBlockDrops;
import modernity.common.block.loot.SaltCrystalBlockDrops;
import modernity.common.block.misc.SoulLightBlock;
import modernity.common.block.plant.*;
import modernity.common.item.MDItemGroup;
import modernity.common.item.MDItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( "modernity" )
public final class MDPlantBlocks {
    public static void init() {
    }

    // Basic
    public static final TallMurkGrassBlock MURK_GRASS
        = MDBlocks.function( "murk_grass", TallMurkGrassBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.MURK_RICE_SEEDS, 0.02F ) ) )
                  .alias( "dark_tall_grass" )
                  .create();

    public static final MurkReedBlock MURK_REED
        = MDBlocks.function( "murk_reed", MurkReedBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();

    public static final SimplePlantBlock MINT_PLANT
        = MDBlocks.function( "mint_plant", props -> new SimplePlantBlock( props, SimplePlantBlock.MINT ) )
                  .strongPlant( MaterialColor.GRASS, 0.2 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final RedwoldBlock REDWOLD
        = MDBlocks.function( "redwold", RedwoldBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final NettlesBlock NETTLES
        = MDBlocks.function( "nettles", NettlesBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SaltCrystalBlock SALT_CRYSTAL
        = MDBlocks.function( "salt_crystal", SaltCrystalBlock::new )
                  .crystal( MaterialColor.SNOW, 0.2 )
                  .item( MDItemGroup.PLANTS )
                  .drops( new SaltCrystalBlockDrops() )
                  .create();

    public static final MurinaBlock MURINA
        = MDBlocks.function( "murina", MurinaBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final MossBlock MOSS
        = MDBlocks.function( "moss", MossBlock::new ).weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final FacingPlantBlock DEAD_MOSS
        = MDBlocks.function( "dead_moss", props -> new FacingPlantBlock( props, 2 ) )
                  .weakPlant( MaterialColor.DIRT, 0 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final LichenBlock LICHEN
        = MDBlocks.function( "lichen", LichenBlock::new )
                  .weakPlant( MaterialColor.WHITE_TERRACOTTA, 0 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final MurkFernBlock MURK_FERN
        = MDBlocks.function( "murk_fern", MurkFernBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final TallMurkFernBlock TALL_MURK_FERN
        = MDBlocks.function( "tall_murk_fern", TallMurkFernBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final WireplantBlock WIREPLANT
        = MDBlocks.function( "wireplant", WireplantBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.PLANT_WIRE ) ) )
                  .create();

    public static final WireplantBlock FLOWERED_WIREPLANT
        = MDBlocks.function( "flowered_wireplant", WireplantBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.PLANT_WIRE ) ) )
                  .create();

    public static final WaterWireBlock WATER_WIRE
        = MDBlocks.function( "water_wire", WaterWireBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.PLANT_WIRE ) ) )
                  .create();

    public static final AlgaeBlock ALGAE
        = MDBlocks.function( "algae", AlgaeBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .create();

    public static final HangingMossBlock HANGING_MOSS
        = MDBlocks.function( "hanging_moss", HangingMossBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock HORSETAIL
        = MDBlocks.function( "horsetail", props -> new SimplePlantBlock( props, SimplePlantBlock.HORSETAIL ) )
                  .strongPlant( MaterialColor.GRASS, 0.2 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final CattailBlock CATTAIL
        = MDBlocks.function( "cattail", CattailBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimpleWaterPlantBlock LAKEWEED
        = MDBlocks.function( "lakeweed", props -> new SimpleWaterPlantBlock( props, SimplePlantBlock.LAKEWEED ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final HeathBlock HEATH
        = MDBlocks.function( "heath", HeathBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0.2 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final HeathBlock FLOWERED_HEATH
        = MDBlocks.function( "flowered_heath", HeathBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0.2 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock DEAD_HEATH
        = MDBlocks.function( "dead_heath", props -> new SimplePlantBlock( props, SimplePlantBlock.DEAD_HEATH ) )
                  .weakPlant( MaterialColor.BROWN_TERRACOTTA, 0 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock SEEPWEED
        = MDBlocks.function( "seepweed", props -> new SimplePlantBlock( props, SimplePlantBlock.SEEPWEED ) )
                  .strongPlant( MaterialColor.GRASS, 0.2 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.SEEPWEED_SEEDS ) ) )
                  .create();

    public static final CavePlantBlock CAVE_GRASS
        = MDBlocks.function( "cave_grass", props -> new CavePlantBlock( props, CavePlantBlock.CAVE_GRASS ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock NUDWART
        = MDBlocks.function( "nudwart", props -> new SimplePlantBlock( props, SimplePlantBlock.NUDWART ) )
                  .strongPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.NUDWART_SEEDS ) ) )
                  .create();

    public static final WatergrassBlock WATERGRASS
        = MDBlocks.function( "watergrass", WatergrassBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final MuxusBushBlock MUXUS_BUSH
        = MDBlocks.function( "muxus_bush", MuxusBushBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.MURK_ROOTS_SEEDS, 0.16F ) ) )
                  .create();

    public static final SimplePlantBlock COTTONSEDGE
        = MDBlocks.function( "cottonsedge", props -> new SimplePlantBlock( props, SimplePlantBlock.COTTONSEDGE ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock RED_GRASS
        = MDBlocks.function( "red_grass", props -> new SimplePlantBlock( props, SimplePlantBlock.RED_GRASS ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final CavePlantBlock DEAD_GRASS
        = MDBlocks.function( "dead_grass", props -> new CavePlantBlock( props, CavePlantBlock.DEAD_GRASS ) )
                  .weakPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();


    // Flowers
    public static final FlyFlowerStalkBlock FLY_FLOWER_STALK
        = MDBlocks.function( "fly_flower_stalk", FlyFlowerStalkBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final FlyFlowerBlock FLY_FLOWER
        = MDBlocks.function( "fly_flower", FlyFlowerBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock MURK_FLOWER
        = MDBlocks.function( "murk_flower", props -> new SimplePlantBlock( props, SimplePlantBlock.MURK_FLOWER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock MILKY_EYE
        = MDBlocks.function( "milky_eye", props -> new SimplePlantBlock( props, SimplePlantBlock.MILK_EYE ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock EGIUM
        = MDBlocks.function( "egium", props -> new SimplePlantBlock( props, SimplePlantBlock.EGIUM ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final ShadeBlueBlock SHADE_BLUE
        = MDBlocks.function( "shade_blue", ShadeBlueBlock::new )
                  .strongPlant( MaterialColor.AIR, 0.1 )
                  .sound( MDSoundTypes.SHADE_BLUE )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.SHADE_BLUE_FLOWER ) ) )
                  .create();

    public static final SimplePlantBlock BLUE_BULBFLOWER
        = MDBlocks.function( "blue_bulbflower", props -> new SimplePlantBlock( props, SimplePlantBlock.BULBFLOWER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock YELLOW_BULBFLOWER
        = MDBlocks.function( "yellow_bulbflower", props -> new SimplePlantBlock( props, SimplePlantBlock.BULBFLOWER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock RED_BULBFLOWER
        = MDBlocks.function( "red_bulbflower", props -> new SimplePlantBlock( props, SimplePlantBlock.BULBFLOWER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final TuruptBlock TURUPT
        = MDBlocks.function( "turupt", TuruptBlock::new )
                  .strongPlant( MaterialColor.AIR, 0.1 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final SimplePlantBlock CREEP_OF_THE_MOOR
        = MDBlocks.function( "creep_of_the_moor", props -> new SimplePlantBlock( props, SimplePlantBlock.MOOR_CREEP ) )
                  .weakPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final DoublePlantBlock MURK_LAVENDER
        = MDBlocks.function( "murk_lavender", props -> new DoublePlantBlock( props, DoublePlantBlock.MURK_LAVENDER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    public static final FoxgloveBlock FOXGLOVE
        = MDBlocks.function( "foxglove", FoxgloveBlock::new )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.FOXGLOVE_PETALS ) ) )
                  .create();

    public static final DoublePlantBlock GLOBE_THISTLE
        = MDBlocks.function( "globe_thistle", props -> new DoublePlantBlock( props, DoublePlantBlock.GLOBE_THISTLE ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();


    // Mushrooms
    public static final CavePlantBlock SEEDLE
        = MDBlocks.function( "seedle", props -> new CavePlantBlock( props, CavePlantBlock.SEEDLE ) )
                  .weakPlant( MaterialColor.BROWN_TERRACOTTA, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();

    public static final CavePlantBlock DOTTED_MUSHROOM
        = MDBlocks.function( "dotted_mushroom", props -> new CavePlantBlock( props, CavePlantBlock.DOTTED_MUSHROOM ) )
                  .weakPlant( MaterialColor.RED_TERRACOTTA, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();

    public static final CavePlantBlock BLACK_MUSHROOM
        = MDBlocks.function( "black_mushroom", props -> new CavePlantBlock( props, CavePlantBlock.BLACK_MUSHROOM ) )
                  .weakPlant( MaterialColor.BLACK_TERRACOTTA, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();


    // Melion
    public static final SimplePlantBlock RED_MELION
        = MDBlocks.function( "red_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/red" )
                  .create();

    public static final SimplePlantBlock ORANGE_MELION
        = MDBlocks.function( "orange_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/orange" )
                  .create();

    public static final SimplePlantBlock YELLOW_MELION
        = MDBlocks.function( "yellow_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/yellow" )
                  .create();

    public static final SimplePlantBlock WHITE_MELION
        = MDBlocks.function( "white_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/white" )
                  .create();

    public static final SimplePlantBlock BLUE_MELION
        = MDBlocks.function( "blue_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/blue" )
                  .create();

    public static final SimplePlantBlock INDIGO_MELION
        = MDBlocks.function( "indigo_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/indigo" )
                  .create();

    public static final SimplePlantBlock MAGENTA_MELION
        = MDBlocks.function( "magenta_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/magenta" )
                  .create();


    // Millium
    public static final SimplePlantBlock RED_MILLIUM
        = MDBlocks.function( "red_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/red" )
                  .create();

    public static final SimplePlantBlock YELLOW_MILLIUM
        = MDBlocks.function( "yellow_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/yellow" )
                  .create();

    public static final SimplePlantBlock WHITE_MILLIUM
        = MDBlocks.function( "white_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/white" )
                  .create();

    public static final SimplePlantBlock CYAN_MILLIUM
        = MDBlocks.function( "cyan_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/cyan" )
                  .create();

    public static final SimplePlantBlock GREEN_MILLIUM
        = MDBlocks.function( "green_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/green" )
                  .create();

    public static final SimplePlantBlock MAGENTA_MILLIUM
        = MDBlocks.function( "magenta_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/magenta" )
                  .create();

    public static final SimplePlantBlock BLUE_MILLIUM
        = MDBlocks.function( "blue_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/blue" )
                  .create();


    // Misc
    public static final SoulLightBlock SOUL_LIGHT
        = MDBlocks.function( "soul_light", SoulLightBlock::new )
                  .props( Material.MISCELLANEOUS, MaterialColor.SNOW )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    public static final PebblesBlock PEBBLES
        = MDBlocks.function( "pebbles", PebblesBlock::new )
                  .weakPlant( MaterialColor.AIR, 0.1 )
                  .sound( MDSoundTypes.ASPHALT )
                  .item( MDItemGroup.DECORATIVES )
                  .drops( BlockLoot.silkTouch( BlockLoot.self() ) )
                  .create();

    public static final GooDripsBlock GOO_DRIPS
        = MDBlocks.function( "goo_drips", GooDripsBlock::new )
                  .weakPlant( MaterialColor.AIR, 0 )
                  .sound( SoundType.SLIME )
                  .item( MDItemGroup.DECORATIVES )
                  .drops( BlockLoot.silkTouchOrShears( BlockLoot.self(), BlockLoot.item( () -> MDItems.GOO_BALL ) ) )
                  .create();

    // Crops
    public static final NudwartCropBlock NUDWART_CROP
        = MDBlocks.function( "nudwart_crop", NudwartCropBlock::new )
                  .weakPlant( MaterialColor.GREEN, 0 )
                  .sound( SoundType.CROP )
                  .drops( new CropBlockDrops<>( () -> MDItems.NUDWART_PETALS, () -> MDItems.NUDWART_SEEDS, MDBlockStateProperties.AGE_1_6, 6 ) )
                  .create();

    public static final SeepweedCropBlock SEEPWEED_CROP
        = MDBlocks.function( "seepweed_crop", SeepweedCropBlock::new )
                  .weakPlant( MaterialColor.GREEN, 0 )
                  .sound( SoundType.CROP )
                  .drops( new CropBlockDrops<>( () -> MDItems.SEEPWEED_LEAVES, () -> MDItems.SEEPWEED_SEEDS, MDBlockStateProperties.AGE_1_6, 6 ) )
                  .create();

    public static final MurkRootsCropBlock MURK_ROOTS
        = MDBlocks.function( "murk_roots", MurkRootsCropBlock::new )
                  .weakPlant( MaterialColor.GREEN, 0 )
                  .sound( SoundType.CROP )
                  .drops( new CropBlockDrops<>( () -> MDItems.MURK_ROOT, () -> MDItems.MURK_ROOTS_SEEDS, MDBlockStateProperties.AGE_1_8, 8 ) )
                  .create();

    public static final MurkRiceCropBlock MURK_RICE
        = MDBlocks.function( "murk_rice", MurkRiceCropBlock::new )
                  .weakPlant( MaterialColor.GREEN, 0 )
                  .sound( SoundType.CROP )
                  .drops( new CropBlockDrops<>( () -> MDItems.MURK_RICE, () -> MDItems.MURK_RICE_SEEDS, MDBlockStateProperties.AGE_1_8, 8 ) )
                  .create();
}
