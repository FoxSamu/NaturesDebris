/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 12 - 2019
 */

package modernity.common.world.gen.surface;

import modernity.api.util.MovingBlockPos;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.MDSurfaceGenSettings;
import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.INoise3D;

import java.util.Random;

public class HumusSurfaceGenerator implements ISurfaceGenerator<MDSurfaceGenSettings> {

    private static final BlockState HUMUS = MDBlocks.HUMUS.getDefaultState();
    private static final BlockState DIRT = MDBlocks.DARK_DIRT.getDefaultState();

    @Override
    public void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos rpos, MDSurfaceGenSettings settings ) {
        int ctrl = 0;
        for( int y = 255; y >= 0; y-- ) {
            rpos.setPos( x, y, z );
            if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = - 1;
            } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = (int) ( 3 + 2 * surfaceNoise.generate( x + cx * 16, y, z + cz * 16 ) );
                chunk.setBlockState( rpos, y < settings.getWaterLevel() - 1 ? DIRT : HUMUS, false );
            } else if( ctrl > 0 ) {
                ctrl--;
                chunk.setBlockState( rpos, DIRT, false );
            }
        }
    }
}
