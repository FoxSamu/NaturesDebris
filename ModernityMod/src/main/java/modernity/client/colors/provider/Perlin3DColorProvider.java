/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors.provider;

// TODO Re-evaluate
//public class Perlin3DColorProvider extends InterpolateNoiseColorProvider {
//    private final int octaves;
//    private final double scaleX;
//    private final double scaleY;
//    private final double scaleZ;
//
//    public Perlin3DColorProvider( IColorProvider providerA, IColorProvider providerB, int octaves, double scaleX, double scaleY, double scaleZ ) {
//        super( providerA, providerB );
//        this.octaves = octaves;
//        this.scaleX = scaleX;
//        this.scaleY = scaleY;
//        this.scaleZ = scaleZ;
//    }
//
//    @Override
//    protected INoise3D createNoise( long seed ) {
//        int iSeed = (int) seed;
//        if( octaves <= 1 ) {
//            return new Perlin3D( iSeed, scaleX, scaleY, scaleZ );
//        } else {
//            return new FractalPerlin3D( iSeed, scaleX, scaleY, scaleZ, octaves );
//        }
//    }
//}
