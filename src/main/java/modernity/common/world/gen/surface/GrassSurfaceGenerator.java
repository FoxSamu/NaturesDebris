/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 2 - 2019
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

public class GrassSurfaceGenerator implements ISurfaceGenerator<MDSurfaceGenSettings> {

    private static final BlockState GRASS = MDBlocks.DARK_GRASS_BLOCK.getDefaultState();
    private static final BlockState DIRT = MDBlocks.DARK_DIRT.getDefaultState();
    private static final BlockState MUD = MDBlocks.MUD.getDefaultState();

    @Override
    public void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos rpos, MDSurfaceGenSettings settings ) {
        int ctrl = 0;
        BlockState secondLayers = null;
        for( int y = 255; y >= 0; y-- ) {
            rpos.setPos( x, y, z );
            if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = - 1;
            } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = (int) ( 3 + 2 * surfaceNoise.generate( x + cx * 16, y, z + cz * 16 ) );
                boolean underwater = y < settings.getWaterLevel() - 1;
                chunk.setBlockState( rpos, underwater ? MUD : GRASS, false );
                secondLayers = underwater ? MUD : DIRT;
            } else if( ctrl > 0 ) {
                ctrl--;
                chunk.setBlockState( rpos, secondLayers, false );
            }
        }
    }
}
