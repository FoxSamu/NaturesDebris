/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 17 - 2020
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
public interface MDPlantBlocks {
    static void init() {
    }

    // Basic
    TallMurkGrassBlock MURK_GRASS
        = MDBlocks.function( "murk_grass", TallMurkGrassBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.MURK_RICE_SEEDS, 0.02F ) ) )
                  .alias( "dark_tall_grass" )
                  .create();

    MurkReedBlock MURK_REED
        = MDBlocks.function( "murk_reed", MurkReedBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();

    SimplePlantBlock MINT_PLANT
        = MDBlocks.function( "mint_plant", props -> new SimplePlantBlock( props, SimplePlantBlock.MINT ) )
                  .strongPlant( MaterialColor.GRASS, 0.2 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    RedwoldBlock REDWOLD
        = MDBlocks.function( "redwold", RedwoldBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    NettlesBlock NETTLES
        = MDBlocks.function( "nettles", NettlesBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SaltCrystalBlock SALT_CRYSTAL
        = MDBlocks.function( "salt_crystal", SaltCrystalBlock::new )
                  .crystal( MaterialColor.SNOW, 0.2 )
                  .item( MDItemGroup.PLANTS )
                  .drops( new SaltCrystalBlockDrops() )
                  .create();

    MurinaBlock MURINA
        = MDBlocks.function( "murina", MurinaBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    MossBlock MOSS
        = MDBlocks.function( "moss", MossBlock::new ).weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    FacingPlantBlock DEAD_MOSS
        = MDBlocks.function( "dead_moss", props -> new FacingPlantBlock( props, 2 ) )
                  .weakPlant( MaterialColor.DIRT, 0 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    LichenBlock LICHEN
        = MDBlocks.function( "lichen", LichenBlock::new )
                  .weakPlant( MaterialColor.WHITE_TERRACOTTA, 0 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    MurkFernBlock MURK_FERN
        = MDBlocks.function( "murk_fern", MurkFernBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    TallMurkFernBlock TALL_MURK_FERN
        = MDBlocks.function( "tall_murk_fern", TallMurkFernBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    WireplantBlock WIREPLANT
        = MDBlocks.function( "wireplant", WireplantBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.PLANT_WIRE ) ) )
                  .create();

    WireplantBlock FLOWERED_WIREPLANT
        = MDBlocks.function( "flowered_wireplant", WireplantBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.PLANT_WIRE ) ) )
                  .create();

    WaterWireBlock WATER_WIRE
        = MDBlocks.function( "water_wire", WaterWireBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.PLANT_WIRE ) ) )
                  .create();

    AlgaeBlock ALGAE
        = MDBlocks.function( "algae", AlgaeBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .create();

    HangingMossBlock HANGING_MOSS
        = MDBlocks.function( "hanging_moss", HangingMossBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock HORSETAIL
        = MDBlocks.function( "horsetail", props -> new SimplePlantBlock( props, SimplePlantBlock.HORSETAIL ) )
                  .strongPlant( MaterialColor.GRASS, 0.2 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    CattailBlock CATTAIL
        = MDBlocks.function( "cattail", CattailBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimpleWaterPlantBlock LAKEWEED
        = MDBlocks.function( "lakeweed", props -> new SimpleWaterPlantBlock( props, SimplePlantBlock.LAKEWEED ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    HeathBlock HEATH
        = MDBlocks.function( "heath", HeathBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0.2 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    HeathBlock FLOWERED_HEATH
        = MDBlocks.function( "flowered_heath", HeathBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0.2 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock DEAD_HEATH
        = MDBlocks.function( "dead_heath", props -> new SimplePlantBlock( props, SimplePlantBlock.DEAD_HEATH ) )
                  .weakPlant( MaterialColor.BROWN_TERRACOTTA, 0 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock SEEPWEED
        = MDBlocks.function( "seepweed", props -> new SimplePlantBlock( props, SimplePlantBlock.SEEPWEED ) )
                  .strongPlant( MaterialColor.GRASS, 0.2 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.SEEPWEED_SEEDS ) ) )
                  .create();

    CavePlantBlock CAVE_GRASS
        = MDBlocks.function( "cave_grass", props -> new CavePlantBlock( props, CavePlantBlock.CAVE_GRASS ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock NUDWART
        = MDBlocks.function( "nudwart", props -> new SimplePlantBlock( props, SimplePlantBlock.NUDWART ) )
                  .strongPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.NUDWART_SEEDS ) ) )
                  .create();

    WatergrassBlock WATERGRASS
        = MDBlocks.function( "watergrass", WatergrassBlock::new )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    MuxusBushBlock MUXUS_BUSH
        = MDBlocks.function( "muxus_bush", MuxusBushBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.SWEET_BERRY_BUSH )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.MURK_ROOTS_SEEDS, 0.16F ) ) )
                  .create();

    SimplePlantBlock COTTONSEDGE
        = MDBlocks.function( "cottonsedge", props -> new SimplePlantBlock( props, SimplePlantBlock.COTTONSEDGE ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock RED_GRASS
        = MDBlocks.function( "red_grass", props -> new SimplePlantBlock( props, SimplePlantBlock.RED_GRASS ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    CavePlantBlock DEAD_GRASS
        = MDBlocks.function( "dead_grass", props -> new CavePlantBlock( props, CavePlantBlock.DEAD_GRASS ) )
                  .weakPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();


    // Flowers
    FlyFlowerStalkBlock FLY_FLOWER_STALK
        = MDBlocks.function( "fly_flower_stalk", FlyFlowerStalkBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .sound( SoundType.WET_GRASS )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    FlyFlowerBlock FLY_FLOWER
        = MDBlocks.function( "fly_flower", FlyFlowerBlock::new )
                  .strongPlant( MaterialColor.GRASS, 0.5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock MURK_FLOWER
        = MDBlocks.function( "murk_flower", props -> new SimplePlantBlock( props, SimplePlantBlock.MURK_FLOWER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock MILKY_EYE
        = MDBlocks.function( "milky_eye", props -> new SimplePlantBlock( props, SimplePlantBlock.MILK_EYE ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock EGIUM
        = MDBlocks.function( "egium", props -> new SimplePlantBlock( props, SimplePlantBlock.EGIUM ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    ShadeBlueBlock SHADE_BLUE
        = MDBlocks.function( "shade_blue", ShadeBlueBlock::new )
                  .strongPlant( MaterialColor.AIR, 0.1 )
                  .sound( MDSoundTypes.SHADE_BLUE )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.SHADE_BLUE_FLOWER ) ) )
                  .create();

    SimplePlantBlock BLUE_BULBFLOWER
        = MDBlocks.function( "blue_bulbflower", props -> new SimplePlantBlock( props, SimplePlantBlock.BULBFLOWER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock YELLOW_BULBFLOWER
        = MDBlocks.function( "yellow_bulbflower", props -> new SimplePlantBlock( props, SimplePlantBlock.BULBFLOWER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock RED_BULBFLOWER
        = MDBlocks.function( "red_bulbflower", props -> new SimplePlantBlock( props, SimplePlantBlock.BULBFLOWER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    TuruptBlock TURUPT
        = MDBlocks.function( "turupt", TuruptBlock::new )
                  .strongPlant( MaterialColor.AIR, 0.1 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    SimplePlantBlock CREEP_OF_THE_MOOR
        = MDBlocks.function( "creep_of_the_moor", props -> new SimplePlantBlock( props, SimplePlantBlock.MOOR_CREEP ) )
                  .weakPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    DoublePlantBlock MURK_LAVENDER
        = MDBlocks.function( "murk_lavender", props -> new DoublePlantBlock( props, DoublePlantBlock.MURK_LAVENDER ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();

    FoxgloveBlock FOXGLOVE
        = MDBlocks.function( "foxglove", FoxgloveBlock::new )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self(), BlockLoot.item( () -> MDItems.FOXGLOVE_PETALS ) ) )
                  .create();

    DoublePlantBlock GLOBE_THISTLE
        = MDBlocks.function( "globe_thistle", props -> new DoublePlantBlock( props, DoublePlantBlock.GLOBE_THISTLE ) )
                  .strongPlant( MaterialColor.AIR, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .create();


    // Mushrooms
    CavePlantBlock SEEDLE
        = MDBlocks.function( "seedle", props -> new CavePlantBlock( props, CavePlantBlock.SEEDLE ) )
                  .weakPlant( MaterialColor.BROWN_TERRACOTTA, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();

    CavePlantBlock DOTTED_MUSHROOM
        = MDBlocks.function( "dotted_mushroom", props -> new CavePlantBlock( props, CavePlantBlock.DOTTED_MUSHROOM ) )
                  .weakPlant( MaterialColor.RED_TERRACOTTA, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();

    CavePlantBlock BLACK_MUSHROOM
        = MDBlocks.function( "black_mushroom", props -> new CavePlantBlock( props, CavePlantBlock.BLACK_MUSHROOM ) )
                  .weakPlant( MaterialColor.BLACK_TERRACOTTA, 0 )
                  .item( MDItemGroup.PLANTS )
                  .dropSelf()
                  .create();


    // Melion
    SimplePlantBlock RED_MELION
        = MDBlocks.function( "red_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/red" )
                  .create();

    SimplePlantBlock ORANGE_MELION
        = MDBlocks.function( "orange_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/orange" )
                  .create();

    SimplePlantBlock YELLOW_MELION
        = MDBlocks.function( "yellow_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/yellow" )
                  .create();

    SimplePlantBlock WHITE_MELION
        = MDBlocks.function( "white_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/white" )
                  .create();

    SimplePlantBlock BLUE_MELION
        = MDBlocks.function( "blue_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/blue" )
                  .create();

    SimplePlantBlock INDIGO_MELION
        = MDBlocks.function( "indigo_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/indigo" )
                  .create();

    SimplePlantBlock MAGENTA_MELION
        = MDBlocks.function( "magenta_melion", props -> new SimplePlantBlock( props, SimplePlantBlock.MELION ) )
                  .weakPlant( MaterialColor.GRASS, 0 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "melion/magenta" )
                  .create();


    // Millium
    SimplePlantBlock RED_MILLIUM
        = MDBlocks.function( "red_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/red" )
                  .create();

    SimplePlantBlock YELLOW_MILLIUM
        = MDBlocks.function( "yellow_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/yellow" )
                  .create();

    SimplePlantBlock WHITE_MILLIUM
        = MDBlocks.function( "white_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/white" )
                  .create();

    SimplePlantBlock CYAN_MILLIUM
        = MDBlocks.function( "cyan_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/cyan" )
                  .create();

    SimplePlantBlock GREEN_MILLIUM
        = MDBlocks.function( "green_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/green" )
                  .create();

    SimplePlantBlock MAGENTA_MILLIUM
        = MDBlocks.function( "magenta_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/magenta" )
                  .create();

    SimplePlantBlock BLUE_MILLIUM
        = MDBlocks.function( "blue_millium", props -> new SimplePlantBlock( props, SimplePlantBlock.MILLIUM ) )
                  .weakPlant( MaterialColor.GRASS, 0 ).light( 5 )
                  .item( MDItemGroup.PLANTS )
                  .drops( BlockLoot.shears( BlockLoot.self() ) )
                  .alias( "millium/blue" )
                  .create();


    // Misc
    SoulLightBlock SOUL_LIGHT
        = MDBlocks.function( "soul_light", SoulLightBlock::new )
                  .props( Material.MISCELLANEOUS, MaterialColor.SNOW )
                  .item( MDItemGroup.DECORATIVES )
                  .dropSelf()
                  .create();

    PebblesBlock PEBBLES
        = MDBlocks.function( "pebbles", PebblesBlock::new )
                  .weakPlant( MaterialColor.AIR, 0.1 )
                  .sound( MDSoundTypes.ASPHALT )
                  .item( MDItemGroup.DECORATIVES )
                  .drops( BlockLoot.silkTouch( BlockLoot.self() ) )
                  .create();

    GooDripsBlock GOO_DRIPS
        = MDBlocks.function( "goo_drips", GooDripsBlock::new )
                  .weakPlant( MaterialColor.AIR, 0 )
                  .sound( SoundType.SLIME )
                  .item( MDItemGroup.DECORATIVES )
                  .drops( BlockLoot.silkTouchOrShears( BlockLoot.self(), BlockLoot.item( () -> MDItems.GOO_BALL ) ) )
                  .create();

    // Crops
    NudwartCropBlock NUDWART_CROP
        = MDBlocks.function( "nudwart_crop", NudwartCropBlock::new )
                  .weakPlant( MaterialColor.GREEN, 0 )
                  .sound( SoundType.CROP )
                  .drops( new CropBlockDrops<>( () -> MDItems.NUDWART_PETALS, () -> MDItems.NUDWART_SEEDS, MDBlockStateProperties.AGE_1_6, 6 ) )
                  .create();

    SeepweedCropBlock SEEPWEED_CROP
        = MDBlocks.function( "seepweed_crop", SeepweedCropBlock::new )
                  .weakPlant( MaterialColor.GREEN, 0 )
                  .sound( SoundType.CROP )
                  .drops( new CropBlockDrops<>( () -> MDItems.SEEPWEED_LEAVES, () -> MDItems.SEEPWEED_SEEDS, MDBlockStateProperties.AGE_1_6, 6 ) )
                  .create();

    MurkRootsCropBlock MURK_ROOTS
        = MDBlocks.function( "murk_roots", MurkRootsCropBlock::new )
                  .weakPlant( MaterialColor.GREEN, 0 )
                  .sound( SoundType.CROP )
                  .drops( new CropBlockDrops<>( () -> MDItems.MURK_ROOT, () -> MDItems.MURK_ROOTS_SEEDS, MDBlockStateProperties.AGE_1_8, 8 ) )
                  .create();

    MurkRiceCropBlock MURK_RICE
        = MDBlocks.function( "murk_rice", MurkRiceCropBlock::new )
                  .weakPlant( MaterialColor.GREEN, 0 )
                  .sound( SoundType.CROP )
                  .drops( new CropBlockDrops<>( () -> MDItems.MURK_RICE, () -> MDItems.MURK_RICE_SEEDS, MDBlockStateProperties.AGE_1_8, 8 ) )
                  .create();
}
