/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.client.colors.IColorProvider;
import net.rgsw.noise.FractalOpenSimplex3D;
import net.rgsw.noise.INoise3D;
import net.rgsw.noise.OpenSimplex3D;

public class OpenSimplex3DColorProvider extends InterpolateNoiseColorProvider {
    private final int octaves;
    private final double scaleX;
    private final double scaleY;
    private final double scaleZ;

    public OpenSimplex3DColorProvider( IColorProvider providerA, IColorProvider providerB, int octaves, double scaleX, double scaleY, double scaleZ ) {
        super( providerA, providerB );
        this.octaves = octaves;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    @Override
    protected INoise3D createNoise( long seed ) {
        int iSeed = (int) seed;
        if( octaves <= 1 ) {
            return new OpenSimplex3D( iSeed, scaleX, scaleY, scaleZ );
        } else {
            return new FractalOpenSimplex3D( iSeed, scaleX, scaleY, scaleZ, octaves );
        }
    }
}
