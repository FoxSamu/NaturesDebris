/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.map.surface;

import modernity.api.util.BlockUpdates;
import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlockTags;
import modernity.common.block.MDBlocks;
import modernity.common.generator.map.NoiseDepositGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;
import net.rgsw.noise.FractalOpenSimplex3D;
import net.rgsw.noise.INoise3D;

public class DarkrockGenerator extends NoiseDepositGenerator<SurfaceGenData> {
    private final INoise3D noise;

    public DarkrockGenerator( IWorld world ) {
        super( world );
        noise = new FractalOpenSimplex3D( rand.nextInt(), 43.51234, 4 ).subtract( 0.3 );
    }

    @Override
    protected double generateNoise( int x, int y, int z, SurfaceGenData data ) {
        return noise.generate( x, y, z );
    }

    @Override
    protected void place( WorldGenRegion region, MovingBlockPos pos, double noise, SurfaceGenData data ) {
        if( canPlace( region, pos, noise ) ) {
            region.setBlockState( pos, MDBlocks.DARKROCK.getDefaultState(), BlockUpdates.NOTIFY_CLIENTS );
        }
    }

    private boolean canPlace( WorldGenRegion region, BlockPos pos, double noise ) {
        return noise > 0 && region.getBlockState( pos ).isIn( MDBlockTags.ROCK );
    }
}
