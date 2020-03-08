/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 08 - 2020
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
public class MoorlandSurfaceGenerator implements ISurfaceGenerator {

    private static final BlockState GRASS = MDBlocks.MURKY_GRASS_BLOCK.getDefaultState();
    private static final BlockState HEATH = MDBlocks.HEATH_BLOCK.getDefaultState();
    private static final BlockState ACID = MDBlocks.ACID_DIRT.getDefaultState();
    private static final BlockState DIRT = MDBlocks.MURKY_DIRT.getDefaultState();
    private static final BlockState MUD = MDBlocks.MUD.getDefaultState();

    private INoise3D heathNoise;
    private INoise3D grassNoise;
    private INoise3D acidNoise;

    private final double heathFactor;
    private final double grassFactor;
    private final double acidFactor;

    public MoorlandSurfaceGenerator( double heathFactor, double grassFactor, double acidFactor ) {
        this.heathFactor = heathFactor;
        this.grassFactor = grassFactor;
        this.acidFactor = acidFactor;
    }

    @Override
    public void init( Random rand ) {
        heathNoise = new FractalPerlin3D( rand.nextInt(), 42.23, 5 );
        grassNoise = new FractalPerlin3D( rand.nextInt(), 31.69, 5 );
        acidNoise = new FractalPerlin3D( rand.nextInt(), 23.85, 5 );
    }

    @Override
    public void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos rpos ) {
        int ctrl = 0;
        BlockState dirt = null;
        for( int y = 255; y >= 0; y-- ) {
            rpos.setPos( x, y, z );
            if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = - 1;
            } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = (int) ( 3 + 2 * surfaceNoise.generate( x + cx * 16, y, z + cz * 16 ) );
                BlockState state = computeBlockState( x + cx * 16, y, z + cz * 16 );
                if( state == MUD || state == ACID ) dirt = state;
                else dirt = DIRT;
                chunk.setBlockState( rpos, computeBlockState( x + cx * 16, y, z + cz * 16 ), false );
            } else if( ctrl > 0 ) {
                ctrl--;
                chunk.setBlockState( rpos, dirt, false );
            }
        }
    }

    private BlockState computeBlockState( int x, int y, int z ) {
        if( y >= MurkSurfaceGeneration.MAIN_HEIGHT - 1 ) {
            double heath = heathNoise.generateMultiplied( x, y, z, heathFactor ) + heathFactor;
            double acid = acidNoise.generateMultiplied( x, y, z, acidFactor ) + acidFactor;
            double grass = grassNoise.generateMultiplied( x, y, z, grassFactor ) + grassFactor;
            if( heath > acid && heath > grass ) {
                return HEATH;
            }
            if( acid > grass ) {
                return ACID;
            }
            return GRASS;
        } else {
            return MUD;
        }
    }
}
