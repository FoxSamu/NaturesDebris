/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 09 - 2020
 * Author: rgsw
 */

package modernity.common.generator.surface;

import modernity.api.util.MovingBlockPos;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlocks;
import modernity.common.generator.MurkSurfaceGeneration;
import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.FractalPerlin3D;
import net.rgsw.noise.INoise3D;

import java.util.Random;

/**
 * Surface generator that generates a basic humus surface, with mud underwater.
 */
public class ForestSurfaceGenerator implements ISurfaceGenerator {

    private static final BlockState HUMUS = MDBlocks.MURKY_HUMUS.getDefaultState();
    private static final BlockState LEAFY_HUMUS = MDBlocks.LEAFY_HUMUS.getDefaultState();
    private static final BlockState GRASS = MDBlocks.MURKY_GRASS_BLOCK.getDefaultState();
    private static final BlockState DIRT = MDBlocks.MURKY_DIRT.getDefaultState();
    private static final BlockState MUD = MDBlocks.MUD.getDefaultState();

    private INoise3D humusNoise;
    private INoise3D leafyNoise;
    private INoise3D grassNoise;
    private INoise3D mudNoise;

    @Override
    public void init( Random rand ) {
        humusNoise = new FractalPerlin3D( rand.nextInt(), 13.125792, 5 );
        leafyNoise = new FractalPerlin3D( rand.nextInt(), 13.125792, 5 );
        grassNoise = new FractalPerlin3D( rand.nextInt(), 13.125792, 5 );
        mudNoise = new FractalPerlin3D( rand.nextInt(), 13.125792, 5 );
    }

    @Override
    public void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos rpos ) {
        int ctrl = 0;
        for( int y = 255; y >= 0; y-- ) {
            rpos.setPos( x, y, z );
            if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = - 1;
            } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = (int) ( 3 + 2 * surfaceNoise.generate( x + cx * 16, y, z + cz * 16 ) );
                chunk.setBlockState( rpos, computeBlockState( x + cx * 16, y, z + cz * 16 ), false );
            } else if( ctrl > 0 ) {
                ctrl--;
                chunk.setBlockState( rpos, DIRT, false );
            }
        }
    }

    private BlockState computeBlockState( int x, int y, int z ) {
        if( y >= MurkSurfaceGeneration.MAIN_HEIGHT - 1 ) {
            double humus = humusNoise.generateMultiplied( x, y, z, 10 ) + 10;
            double leafy = leafyNoise.generateMultiplied( x, y, z, 10 ) + 10;
            double grass = grassNoise.generateMultiplied( x, y, z, 9 ) + 9;
            if( humus > leafy && humus > grass ) {
                return HUMUS;
            }
            if( leafy > grass ) {
                return LEAFY_HUMUS;
            }
            return GRASS;
        } else {
            double mud = mudNoise.generate( x, y, z );
            return mud > 0 ? MUD : DIRT;
        }
    }
}
