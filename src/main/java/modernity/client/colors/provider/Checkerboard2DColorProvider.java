/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 03 - 2020
 * Author: rgsw
 */

package modernity.client.colors.provider;

// TODO Re-evaluate
//public class Checkerboard2DColorProvider implements IColorProvider {
//    private final IColorProvider providerA;
//    private final IColorProvider providerB;
//    private final int sizeX;
//    private final int sizeZ;
//
//    public Checkerboard2DColorProvider( IColorProvider providerA, IColorProvider providerB, int sizeX, int sizeZ ) {
//        this.providerA = providerA;
//        this.providerB = providerB;
//        this.sizeX = sizeX;
//        this.sizeZ = sizeZ;
//    }
//
//    @Override
//    public int getColor( IEnviromentBlockReader world, BlockPos pos ) {
//        int x = pos.getX() / sizeX;
//        int z = pos.getZ() / sizeZ;
//        return ( ( x + z & 1 ) == 0 ? providerA : providerB ).getColor( world, pos );
//    }
//
//    @Override
//    public void initForSeed( long seed ) {
//        providerA.initForSeed( seed );
//        providerB.initForSeed( seed );
//    }
//}
