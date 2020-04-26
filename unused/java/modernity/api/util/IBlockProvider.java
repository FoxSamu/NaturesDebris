/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 01 - 2020
 * Author: rgsw
 */

package modernity.generic.util;

import modernity.common.generator.blocks.IBlockGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

/**
 * Used to generate a block (usually a plant) in the world.
 *
 * @deprecated Use {@link IBlockGenerator}
 */
@FunctionalInterface
@Deprecated
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