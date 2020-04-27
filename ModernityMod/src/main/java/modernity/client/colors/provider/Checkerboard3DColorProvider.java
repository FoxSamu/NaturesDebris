/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors.provider;

// TODO Re-evaluate
//public class Checkerboard3DColorProvider implements IColorProvider {
//    private final IColorProvider providerA;
//    private final IColorProvider providerB;
//    private final int sizeX;
//    private final int sizeY;
//    private final int sizeZ;
//
//    public Checkerboard3DColorProvider( IColorProvider providerA, IColorProvider providerB, int sizeX, int sizeY, int sizeZ ) {
//        this.providerA = providerA;
//        this.providerB = providerB;
//        this.sizeX = sizeX;
//        this.sizeY = sizeY;
//        this.sizeZ = sizeZ;
//    }
//
//    @Override
//    public int getColor( IEnviromentBlockReader world, BlockPos pos ) {
//        int x = pos.getX() / sizeX;
//        int y = pos.getY() / sizeY;
//        int z = pos.getZ() / sizeZ;
//        return ( ( x + y + z & 1 ) == 0 ? providerA : providerB ).getColor( world, pos );
//    }
//
//    @Override
//    public void initForSeed( long seed ) {
//        providerA.initForSeed( seed );
//        providerB.initForSeed( seed );
//    }
//}
