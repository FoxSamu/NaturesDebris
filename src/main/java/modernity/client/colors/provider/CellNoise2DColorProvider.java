/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.client.colors.IColorProvider;
import net.rgsw.noise.INoise2D;
import net.rgsw.noise.INoise3D;

public class CellNoise2DColorProvider extends InterpolateNoiseColorProvider {
    private final int sizeX;
    private final int sizeZ;

    public CellNoise2DColorProvider( IColorProvider providerA, IColorProvider providerB, int sizeX, int sizeZ ) {
        super( providerA, providerB );
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
    }

    @Override
    protected INoise3D createNoise( long seed ) {
        int iSeed = (int) seed;
        return INoise3D.from2DY(
            INoise2D.random( iSeed ).scale( 1D / sizeX, 1D / sizeZ )
        );
    }
}
