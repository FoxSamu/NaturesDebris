/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.block;

import modernity.api.util.IBlockProvider;

import java.util.Random;
import java.util.function.Function;

public final class MDBlockProviders {
    public static final IBlockProvider RANDOM_MILLIUM = new IBlockProvider.ChooseRandom(
        MDBlocks.BLUE_MILLIUM,
        MDBlocks.CYAN_MILLIUM,
        MDBlocks.GREEN_MILLIUM,
        MDBlocks.YELLOW_MILLIUM,
        MDBlocks.MAGENTA_MILLIUM,
        MDBlocks.RED_MILLIUM,
        MDBlocks.WHITE_MILLIUM
    );

    public static final IBlockProvider RANDOM_MELION = new IBlockProvider.ChooseRandom(
        MDBlocks.BLUE_MELION,
        MDBlocks.ORANGE_MELION,
        MDBlocks.INDIGO_MELION,
        MDBlocks.YELLOW_MELION,
        MDBlocks.MAGENTA_MELION,
        MDBlocks.RED_MELION,
        MDBlocks.WHITE_MELION
    );

    public static final IBlockProvider MURK_FERN_RAND_HEIGHT = ( world, pos, rand ) -> {
        if( rand.nextInt( 4 ) == 0 ) {
            if( MDBlocks.TALL_MURK_FERN.provide( world, pos, rand ) ) {
                return true;
            }
        }
        return MDBlocks.MURK_FERN.provide( world, pos, rand );
    };

    private static final Function<Random, Integer> GRASS_HEIGHT_PROVIDER = rng -> Math.min( 4, rng.nextInt( 4 ) + rng.nextInt( 2 ) + 1 );

    public static final IBlockProvider HIGH_GRASS = ( world, pos, rand ) -> MDBlocks.MURK_GRASS.provide( world, pos, rand, GRASS_HEIGHT_PROVIDER );

    private MDBlockProviders() {
    }
}
