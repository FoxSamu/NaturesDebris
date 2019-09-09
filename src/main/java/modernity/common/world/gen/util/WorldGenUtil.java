/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 5 - 2019
 */

package modernity.common.world.gen.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.IBlockMatcherReaderAware;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.Random;

public class WorldGenUtil {
    public static BlockPos randomFloorHeight( IBlockReader world, int x, int z, Random rand, int miny, int maxy, IBlockMatcherReaderAware<IBlockState> air ) {
        int r = rand.nextInt( maxy - miny ) + miny;

        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos( x, r, z );

        while( air.test( world.getBlockState( mpos ), world, mpos ) ) {
            mpos.move( EnumFacing.DOWN );
            if( mpos.getY() < miny ) break;
        }

        while( ! air.test( world.getBlockState( mpos ), world, mpos ) ) {
            mpos.move( EnumFacing.UP );
            if( mpos.getY() > maxy ) break;
        }

        if( mpos.getY() < miny || mpos.getY() >= maxy ) return BlockPos.ORIGIN.down();

        return mpos.toImmutable();
    }
}
