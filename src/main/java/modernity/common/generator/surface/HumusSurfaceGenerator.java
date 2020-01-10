/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.surface;

import modernity.api.util.MovingBlockPos;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.INoise3D;

import java.util.Random;

/**
 * Surface generator that generates a basic humus surface, with mud underwater.
 */
public class HumusSurfaceGenerator implements ISurfaceGenerator {

    private static final BlockState HUMUS = MDBlocks.HUMUS.getDefaultState();
    private static final BlockState DIRT = MDBlocks.MURKY_DIRT.getDefaultState();

    @Override
    public void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos rpos ) {
        int ctrl = 0;
        for( int y = 255; y >= 0; y-- ) {
            rpos.setPos( x, y, z );
            if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = - 1;
            } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = (int) ( 3 + 2 * surfaceNoise.generate( x + cx * 16, y, z + cz * 16 ) );
                chunk.setBlockState( rpos, y < 71 ? DIRT : HUMUS, false );
            } else if( ctrl > 0 ) {
                ctrl--;
                chunk.setBlockState( rpos, DIRT, false );
            }
        }
    }
}
