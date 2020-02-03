/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.client.colors.IColorProvider;
import net.rgsw.noise.FractalPerlin2D;
import net.rgsw.noise.INoise3D;
import net.rgsw.noise.Perlin2D;

public class Perlin2DColorProvider extends InterpolateNoiseColorProvider {
    private final int octaves;
    private final double scaleX;
    private final double scaleZ;

    public Perlin2DColorProvider( IColorProvider providerA, IColorProvider providerB, int octaves, double scaleX, double scaleZ ) {
        super( providerA, providerB );
        this.octaves = octaves;
        this.scaleX = scaleX;
        this.scaleZ = scaleZ;
    }

    @Override
    protected INoise3D createNoise( long seed ) {
        int iSeed = (int) seed;
        if( octaves <= 1 ) {
            return INoise3D.from2DY( new Perlin2D( iSeed, scaleX, scaleZ ) );
        } else {
            return INoise3D.from2DY( new FractalPerlin2D( iSeed, scaleX, scaleZ, octaves ) );
        }
    }
}
