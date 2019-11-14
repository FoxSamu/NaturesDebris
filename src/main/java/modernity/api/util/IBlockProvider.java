/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

/**
 * Used to generate a block (usually a plant) in the world.
 */
@FunctionalInterface
public interface IBlockProvider {
    boolean provide( IWorld world, BlockPos pos, Random rand );

    class ChooseRandom implements IBlockProvider {
        private final IBlockProvider[] providers;

        public ChooseRandom( IBlockProvider... providers ) {
            this.providers = providers;
        }

        @Override
        public boolean provide( IWorld world, BlockPos pos, Random rand ) {
            return providers[ rand.nextInt( providers.length ) ].provide( world, pos, rand );
        }
    }
}