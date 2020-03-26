/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.client.colors.IColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.rgsw.noise.INoise3D;

public abstract class NoiseColorProvider implements IColorProvider {
    private INoise3D noise;

    protected abstract INoise3D createNoise( long seed );

    protected abstract int computeColor( IEnviromentBlockReader world, BlockPos pos, double noise );

    @Override
    public void initForSeed( long seed ) {
        noise = createNoise( seed );
    }

    @Override
    public final int getColor( IEnviromentBlockReader world, BlockPos pos ) {
        return computeColor( world, pos, noise.generate( pos.getX(), pos.getY(), pos.getZ() ) );
    }
}
