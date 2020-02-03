/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IColorProvider {
    default void initForSeed( long seed ) {
    }

    int getColor( @Nullable IEnviromentBlockReader world, BlockPos pos );
}
