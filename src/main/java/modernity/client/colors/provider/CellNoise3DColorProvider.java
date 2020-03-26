/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

import modernity.client.colors.IColorProvider;
import net.rgsw.noise.INoise3D;

// TODO Re-evaluate
//public class CellNoise3DColorProvider extends InterpolateNoiseColorProvider {
//    private final int sizeX;
//    private final int sizeY;
//    private final int sizeZ;
//
//    public CellNoise3DColorProvider( IColorProvider providerA, IColorProvider providerB, int sizeX, int sizeY, int sizeZ ) {
//        super( providerA, providerB );
//        this.sizeX = sizeX;
//        this.sizeY = sizeY;
//        this.sizeZ = sizeZ;
//    }
//
//    @Override
//    protected INoise3D createNoise( long seed ) {
//        return INoise3D.random( (int) seed ).scale( 1D / sizeX, 1D / sizeY, 1D / sizeZ );
//    }
//}
