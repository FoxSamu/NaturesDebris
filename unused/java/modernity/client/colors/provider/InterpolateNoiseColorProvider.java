/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.generic.util.ColorUtil;
import modernity.client.colors.IColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.redgalaxy.util.MathUtil;

public abstract class InterpolateNoiseColorProvider extends NoiseColorProvider {
    private final IColorProvider providerA;
    private final IColorProvider providerB;

    public InterpolateNoiseColorProvider( IColorProvider providerA, IColorProvider providerB ) {
        this.providerA = providerA;
        this.providerB = providerB;
    }

    @Override
    protected int computeColor( IEnviromentBlockReader world, BlockPos pos, double noise ) {
        return ColorUtil.interpolate(
            providerA.getColor( world, pos ),
            providerB.getColor( world, pos ),
            MathUtil.unlerp( - 1, 1, noise )
        );
    }

    @Override
    public void initForSeed( long seed ) {
        super.initForSeed( seed );
        providerA.initForSeed( seed );
        providerB.initForSeed( seed );
    }
}
