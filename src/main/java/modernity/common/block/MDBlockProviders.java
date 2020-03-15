/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import modernity.api.util.IBlockProvider;

import java.util.Random;
import java.util.function.Function;

@Deprecated
public final class MDBlockProviders {
    public static final IBlockProvider RANDOM_MILLIUM = new IBlockProvider.ChooseRandom(
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.BLUE_MILLIUM.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.CYAN_MILLIUM.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.GREEN_MILLIUM.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.YELLOW_MILLIUM.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.MAGENTA_MILLIUM.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.RED_MILLIUM.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.WHITE_MILLIUM.getDefaultState(), 2 )
    );

    public static final IBlockProvider RANDOM_MELION = new IBlockProvider.ChooseRandom(
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.BLUE_MELION.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.ORANGE_MELION.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.INDIGO_MELION.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.YELLOW_MELION.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.MAGENTA_MELION.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.RED_MELION.getDefaultState(), 2 ),
        ( world, pos, rand ) -> world.setBlockState( pos, MDPlantBlocks.WHITE_MELION.getDefaultState(), 2 )
    );

    public static final IBlockProvider MURK_FERN_RAND_HEIGHT = ( world, pos, rand ) -> {
        if( rand.nextInt( 4 ) == 0 ) {
            if( MDPlantBlocks.TALL_MURK_FERN.provide( world, pos, rand ) ) {
                return true;
            }
        }
        return MDPlantBlocks.MURK_FERN.provide( world, pos, rand );
    };

    private static final Function<Random, Integer> GRASS_HEIGHT_PROVIDER = rng -> Math.min( 4, rng.nextInt( 4 ) + rng.nextInt( 2 ) + 1 );

    public static final IBlockProvider HIGH_GRASS = ( world, pos, rand ) -> MDPlantBlocks.MURK_GRASS.provide( world, pos, rand, GRASS_HEIGHT_PROVIDER );

    private MDBlockProviders() {
    }
}
