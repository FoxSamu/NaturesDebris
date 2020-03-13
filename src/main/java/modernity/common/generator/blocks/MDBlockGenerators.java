/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 13 - 2020
 * Author: rgsw
 */

package modernity.common.generator.blocks;

import modernity.common.biome.MDBiomes;
import modernity.common.block.MDBlocks;
import modernity.common.block.plant.DoubleDirectionalPlantBlock;
import modernity.common.block.plant.FacingPlantBlock;
import modernity.common.block.plant.SingleDirectionalPlantBlock;
import modernity.common.block.plant.TallDirectionalPlantBlock;
import modernity.common.generator.blocks.condition.IBlockGenCondition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;

import java.util.Random;
import java.util.function.Function;

public final class MDBlockGenerators {
    public static final IBlockGenerator BLUE_MILLIUM = forSinglePlant( MDBlocks.BLUE_MILLIUM );
    public static final IBlockGenerator CYAN_MILLIUM = forSinglePlant( MDBlocks.CYAN_MILLIUM );
    public static final IBlockGenerator GREEN_MILLIUM = forSinglePlant( MDBlocks.GREEN_MILLIUM );
    public static final IBlockGenerator YELLOW_MILLIUM = forSinglePlant( MDBlocks.YELLOW_MILLIUM );
    public static final IBlockGenerator MAGENTA_MILLIUM = forSinglePlant( MDBlocks.MAGENTA_MILLIUM );
    public static final IBlockGenerator RED_MILLIUM = forSinglePlant( MDBlocks.RED_MILLIUM );
    public static final IBlockGenerator WHITE_MILLIUM = forSinglePlant( MDBlocks.WHITE_MILLIUM );

    public static final IBlockGenerator RANDOM_MILLIUM = random(
        BLUE_MILLIUM,
        CYAN_MILLIUM,
        GREEN_MILLIUM,
        YELLOW_MILLIUM,
        MAGENTA_MILLIUM,
        RED_MILLIUM,
        WHITE_MILLIUM
    );

    public static final IBlockGenerator BLUE_MELION = forSinglePlant( MDBlocks.BLUE_MELION );
    public static final IBlockGenerator ORANGE_MELION = forSinglePlant( MDBlocks.ORANGE_MELION );
    public static final IBlockGenerator INDIGO_MELION = forSinglePlant( MDBlocks.INDIGO_MELION );
    public static final IBlockGenerator YELLOW_MELION = forSinglePlant( MDBlocks.YELLOW_MELION );
    public static final IBlockGenerator MAGENTA_MELION = forSinglePlant( MDBlocks.MAGENTA_MELION );
    public static final IBlockGenerator RED_MELION = forSinglePlant( MDBlocks.RED_MELION );
    public static final IBlockGenerator WHITE_MELION = forSinglePlant( MDBlocks.WHITE_MELION );

    public static final IBlockGenerator RANDOM_MELION = random(
        BLUE_MELION,
        ORANGE_MELION,
        INDIGO_MELION,
        YELLOW_MELION,
        MAGENTA_MELION,
        RED_MELION,
        WHITE_MELION
    );

    public static final IBlockGenerator SMALL_MURK_FERN = forSinglePlant( MDBlocks.MURK_FERN );
    public static final IBlockGenerator LARGE_MURK_FERN = forDoublePlant( MDBlocks.TALL_MURK_FERN );

    public static final IBlockGenerator RANDOM_MURK_FERN = weighted( SMALL_MURK_FERN, 6 )
                                                               .add( LARGE_MURK_FERN.orElse( SMALL_MURK_FERN ), 1 )
                                                               .build();

    public static final IBlockGenerator MURK_GRASS_1 = forTallPlant( MDBlocks.MURK_GRASS, rand -> 1 );
    public static final IBlockGenerator MURK_GRASS_2 = forTallPlant( MDBlocks.MURK_GRASS, rand -> 2 );
    public static final IBlockGenerator MURK_GRASS_3 = forTallPlant( MDBlocks.MURK_GRASS, rand -> 3 );
    public static final IBlockGenerator MURK_GRASS_4 = forTallPlant( MDBlocks.MURK_GRASS, rand -> 4 );

    public static final IBlockGenerator MURK_GRASS_BASIC = forTallPlant( MDBlocks.MURK_GRASS, rng -> rng.nextInt( 10 ) == 0
                                                                                                     ? 2
                                                                                                     : 1 );
    public static final IBlockGenerator MURK_GRASS_LUSH = forTallPlant( MDBlocks.MURK_GRASS, rng -> Math.min( 4, rng.nextInt( 4 ) + rng.nextInt( 2 ) + 1 ) );

    public static final IBlockGenerator LUSH_GRASSLAND_GRASS = alternatives(
        conditional( MURK_GRASS_LUSH, ( world, pos, rand ) -> {
            Biome biome = world.getBiome( pos );
            return biome == MDBiomes.LUSH_GRASSLAND || biome == MDBiomes.HIGH_LUSH_GRASSLAND;
        } ),
        conditional( MURK_GRASS_BASIC, ( world, pos, rand ) -> rand.nextBoolean() )
    );

    public static final IBlockGenerator NETTLES = forSinglePlant( MDBlocks.NETTLES );
    public static final IBlockGenerator MINT_PLANT = forSinglePlant( MDBlocks.MINT_PLANT );
    public static final IBlockGenerator REDWOLD = forSinglePlant( MDBlocks.REDWOLD );
    public static final IBlockGenerator SALT_CRYSTAL = forSinglePlant( MDBlocks.SALT_CRYSTAL );
    public static final IBlockGenerator HORSETAIL = forSinglePlant( MDBlocks.HORSETAIL );
    public static final IBlockGenerator LAKEWEED = forSinglePlant( MDBlocks.LAKEWEED );
    public static final IBlockGenerator SEEPWEED = forSinglePlant( MDBlocks.SEEPWEED );
    public static final IBlockGenerator CAVE_GRASS = forSinglePlant( MDBlocks.CAVE_GRASS );
    public static final IBlockGenerator NUDWART = forSinglePlant( MDBlocks.NUDWART );
    public static final IBlockGenerator COTTONSEDGE = forSinglePlant( MDBlocks.COTTONSEDGE );
    public static final IBlockGenerator RED_GRASS = forSinglePlant( MDBlocks.RED_GRASS );
    public static final IBlockGenerator DEAD_GRASS = forSinglePlant( MDBlocks.DEAD_GRASS );
    public static final IBlockGenerator PEBBLES = forSinglePlant( MDBlocks.PEBBLES );

    public static final IBlockGenerator MURK_FLOWERS = forSinglePlant( MDBlocks.MURK_FLOWER );
    public static final IBlockGenerator MILKY_EYE = forSinglePlant( MDBlocks.MILKY_EYE );
    public static final IBlockGenerator EGIUM = forSinglePlant( MDBlocks.EGIUM );
    public static final IBlockGenerator SHADE_BLUE = forSinglePlant( MDBlocks.SHADE_BLUE );
    public static final IBlockGenerator TURUPT = forSinglePlant( MDBlocks.TURUPT );
    public static final IBlockGenerator CREEP_OF_THE_MOOR = forSinglePlant( MDBlocks.CREEP_OF_THE_MOOR );

    public static final IBlockGenerator RED_BULBFLOWER = forSinglePlant( MDBlocks.RED_BULBFLOWER );
    public static final IBlockGenerator YELLOW_BULBFLOWER = forSinglePlant( MDBlocks.YELLOW_BULBFLOWER );
    public static final IBlockGenerator BLUE_BULBFLOWER = forSinglePlant( MDBlocks.BLUE_BULBFLOWER );

    public static final IBlockGenerator RANDOM_BULBFLOWER = random(
        RED_BULBFLOWER,
        YELLOW_BULBFLOWER,
        BLUE_BULBFLOWER
    );

    public static final IBlockGenerator SEEDLE = forSinglePlant( MDBlocks.SEEDLE );
    public static final IBlockGenerator DOTTED_MUSHROOM = forSinglePlant( MDBlocks.DOTTED_MUSHROOM );
    public static final IBlockGenerator BLACK_MUSHROOM = forSinglePlant( MDBlocks.BLACK_MUSHROOM );

    public static final IBlockGenerator HEATH = forSinglePlant( MDBlocks.HEATH );
    public static final IBlockGenerator FLOWERED_HEATH = forSinglePlant( MDBlocks.FLOWERED_HEATH );
    public static final IBlockGenerator DEAD_HEATH = forSinglePlant( MDBlocks.DEAD_HEATH );

    public static final IBlockGenerator RANDOM_HEATH = weighted( HEATH, 6 )
                                                           .add( FLOWERED_HEATH, 1 )
                                                           .add( DEAD_HEATH, 0.1 )
                                                           .build();

    public static final IBlockGenerator RANDOM_LIVING_HEATH = weighted( HEATH, 6 )
                                                                  .add( FLOWERED_HEATH, 1 )
                                                                  .build();

    public static final IBlockGenerator CATTAIL = forDoublePlant( MDBlocks.CATTAIL );
    public static final IBlockGenerator MURK_LAVENDER = forDoublePlant( MDBlocks.MURK_LAVENDER );
    public static final IBlockGenerator FOXGLOVE = forDoublePlant( MDBlocks.FOXGLOVE );
    public static final IBlockGenerator GLOBE_THISTLE = forDoublePlant( MDBlocks.GLOBE_THISTLE );

    public static final IBlockGenerator WIREPLANT = forSinglePlant( MDBlocks.WIREPLANT );
    public static final IBlockGenerator FLOWERED_WIREPLANT = forSinglePlant( MDBlocks.FLOWERED_WIREPLANT );

    public static final IBlockGenerator RANDOM_WIREPLANT = weighted( WIREPLANT, 4 )
                                                               .add( FLOWERED_WIREPLANT, 1 )
                                                               .build();

    public static final IBlockGenerator MURINA = forTallPlant( MDBlocks.MURINA, rng -> rng.nextInt( 15 ) + 1 );
    public static final IBlockGenerator HANGING_MOSS = forTallPlant( MDBlocks.HANGING_MOSS, rng -> rng.nextInt( 7 ) + 1 );

    public static final IBlockGenerator WATERGRASS_SMALL = forTallPlant( MDBlocks.WATERGRASS, rng -> rng.nextInt( 3 ) + 1 );
    public static final IBlockGenerator WATERGRASS_LARGE = forTallPlant( MDBlocks.WATERGRASS, rng -> rng.nextInt( 10 ) + 1 );

    public static final IBlockGenerator MUXUS_BUSH_SINGLE = new SimpleBushBlockGenerator( MDBlocks.MUXUS_BUSH );
    public static final IBlockGenerator MUXUS_BUSH = new BushBlockGenerator( MDBlocks.MUXUS_BUSH );
    public static final IBlockGenerator WATER_WIRE = forBlock( MDBlocks.WATER_WIRE );

    public static final IBlockGenerator MOSS = forFacingPlant( MDBlocks.MOSS );
    public static final IBlockGenerator DEAD_MOSS = forFacingPlant( MDBlocks.DEAD_MOSS );
    public static final IBlockGenerator LICHEN = forFacingPlant( MDBlocks.LICHEN );

    public static final IBlockGenerator GOO_DRIPS = forSinglePlant( MDBlocks.GOO_DRIPS );

    public static final IBlockGenerator MURK_REED = new MurkReedBlockGenerator();
    public static final IBlockGenerator PUDDLE = new PuddleBlockGenerator();
    public static final IBlockGenerator FLY_FLOWER = new FlyFlowerBlockGenerator();
    public static final IBlockGenerator ALGAE = new AlgaeBlockGenerator();
    public static final IBlockGenerator SOUL_LIGHT = new SoulLightBlockGenerator();

    private MDBlockGenerators() {
    }

    public static IBlockGenerator forBlockState( BlockState block ) {
        return new SimpleBlockGenerator( block );
    }

    public static IBlockGenerator forBlock( Block block ) {
        return new SimpleBlockGenerator( block );
    }

    public static IBlockGenerator forSinglePlant( SingleDirectionalPlantBlock block ) {
        return new SinglePlantBlockGenerator( block );
    }

    public static IBlockGenerator forDoublePlant( DoubleDirectionalPlantBlock block ) {
        return new DoublePlantBlockGenerator( block );
    }

    public static IBlockGenerator forTallPlant( TallDirectionalPlantBlock block, Function<Random, Integer> heightFn ) {
        return new TallPlantBlockGenerator( block, heightFn );
    }

    public static IBlockGenerator forFacingPlant( FacingPlantBlock block ) {
        return new FacingPlantBlockGenerator( block );
    }

    public static IBlockGenerator random( IBlockGenerator first, IBlockGenerator... rest ) {
        return new RandomBlockGenerator( first, rest );
    }

    public static IBlockGenerator alternatives( IBlockGenerator first, IBlockGenerator... rest ) {
        return new AlternativeBlockGenerator( first, rest );
    }

    public static IBlockGenerator conditional( IBlockGenerator gen, IBlockGenCondition cond ) {
        return new ConditionalBlockGenerator( cond, gen );
    }

    public static WeightedBlockGenerator.Builder weighted( IBlockGenerator first, double wgt ) {
        return WeightedBlockGenerator.builder( first, wgt );
    }
}
