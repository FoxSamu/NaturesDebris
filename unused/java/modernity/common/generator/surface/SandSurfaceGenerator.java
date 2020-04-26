/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.generator.surface;

import modernity.generic.util.MovingBlockPos;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDNatureBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.INoise3D;

import java.util.Random;

/**
 * Surface generator that generates a basic grass surface, with mud underwater.
 */
public class SandSurfaceGenerator implements ISurfaceGenerator {

    private static final BlockState SAND = MDNatureBlocks.MURKY_SAND.getDefaultState();

    @Override
    public void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos rpos ) {
        int ctrl = 0;
        for( int y = 255; y >= 0; y-- ) {
            rpos.setPos( x, y, z );
            if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = - 1;
            } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = (int) ( 3 + 2 * surfaceNoise.generate( x + cx * 16, y, z + cz * 16 ) );
                chunk.setBlockState( rpos, SAND, false );
            } else if( ctrl > 0 ) {
                ctrl--;
                chunk.setBlockState( rpos, SAND, false );
            }
        }
    }
}
