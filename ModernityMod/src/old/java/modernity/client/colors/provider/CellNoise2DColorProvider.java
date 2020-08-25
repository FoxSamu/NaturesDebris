/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors.provider;

// TODO Re-evaluate
//public class CellNoise2DColorProvider extends InterpolateNoiseColorProvider {
//    private final int sizeX;
//    private final int sizeZ;
//
//    public CellNoise2DColorProvider( IColorProvider providerA, IColorProvider providerB, int sizeX, int sizeZ ) {
//        super( providerA, providerB );
//        this.sizeX = sizeX;
//        this.sizeZ = sizeZ;
//    }
//
//    @Override
//    protected INoise3D createNoise( long seed ) {
//        int iSeed = (int) seed;
//        return INoise3D.from2DY(
//            INoise2D.random( iSeed ).scale( 1D / sizeX, 1D / sizeZ )
//        );
//    }
//}
