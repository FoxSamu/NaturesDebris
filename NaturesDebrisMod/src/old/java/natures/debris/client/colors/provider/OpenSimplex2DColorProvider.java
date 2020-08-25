/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.colors.provider;

// TODO Re-evaluate
//public class OpenSimplex2DColorProvider extends InterpolateNoiseColorProvider {
//    private final int octaves;
//    private final double scaleX;
//    private final double scaleZ;
//
//    public OpenSimplex2DColorProvider( IColorProvider providerA, IColorProvider providerB, int octaves, double scaleX, double scaleZ ) {
//        super( providerA, providerB );
//        this.octaves = octaves;
//        this.scaleX = scaleX;
//        this.scaleZ = scaleZ;
//    }
//
//    @Override
//    protected INoise3D createNoise( long seed ) {
//        int iSeed = (int) seed;
//        if( octaves <= 1 ) {
//            return INoise3D.from2DY( new OpenSimplex2D( iSeed, scaleX, scaleZ ) );
//        } else {
//            return INoise3D.from2DY( new FractalOpenSimplex2D( iSeed, scaleX, scaleZ, octaves ) );
//        }
//    }
//}
