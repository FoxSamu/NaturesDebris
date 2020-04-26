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
import modernity.common.generator.MurkSurfaceGeneration;
import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.FractalOpenSimplex2D;
import net.rgsw.noise.FractalPerlin3D;
import net.rgsw.noise.INoise3D;

import java.util.Random;

/**
 * Surface generator that generates the swamp surface, with mud underwater and marshes in shallow water.
 */
public class SwampSurfaceGenerator implements ISurfaceGenerator {

    private static final BlockState GRASS = MDNatureBlocks.MURKY_GRASS_BLOCK.getDefaultState();
    private static final BlockState DIRT = MDNatureBlocks.MURKY_DIRT.getDefaultState();
    private static final BlockState MUD = MDNatureBlocks.MUD.getDefaultState();
    private static final BlockState PODZOL = MDNatureBlocks.MURKY_PODZOL.getDefaultState();

    private FractalOpenSimplex2D marshNoise;
    private FractalOpenSimplex2D marshGroupNoise;

    private INoise3D podzolNoise;
    private INoise3D grassNoise;
    private INoise3D mudNoise;

    private final boolean marshes;
    private final double podzolFactor;
    private final double grassFactor;
    private final double mudFactor;

    public SwampSurfaceGenerator( boolean marshes, double podzolFactor, double grassFactor, double mudFactor ) {
        this.marshes = marshes;
        this.podzolFactor = podzolFactor;
        this.grassFactor = grassFactor;
        this.mudFactor = mudFactor;
    }

    @Override
    public void init( Random rand ) {
        marshNoise = new FractalOpenSimplex2D( rand.nextInt(), 3.26224, 3 );
        marshGroupNoise = new FractalOpenSimplex2D( rand.nextInt(), 31.46233, 3 );

        grassNoise = new FractalPerlin3D( rand.nextInt(), 21.73, 5 );
        podzolNoise = new FractalPerlin3D( rand.nextInt(), 17.81, 5 );
        mudNoise = new FractalPerlin3D( rand.nextInt(), 26.81, 5 );
    }

    @Override
    public void buildSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, ModernityBiome biome, INoise3D surfaceNoise, MovingBlockPos rpos ) {
        int ctrl = 0;
        BlockState secondLayers = null;
        for( int y = 255; y >= 0; y-- ) {
            rpos.setPos( x, y, z );
            if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = - 1;
            } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = (int) ( 5 + 2 * surfaceNoise.generate( x + cx * 16, y, z + cz * 16 ) );

                BlockState top = computeBlockState( cx * 16 + x, y, cz * 16 + z );
                boolean marsh = false;
                if( y == 70 && marshes ) {
                    double groupNoise = marshGroupNoise.generateMultiplied( cx * 16 + x, cz * 16 + z, 8 ) + 1;
                    if( groupNoise > 0 ) {
                        double noise = marshNoise.generateMultiplied( cx * 16 + x, cz * 16 + z, 8 );
                        if( noise > 0 ) {
                            top = computeBlockState( x, y + 1, z );
                            rpos.moveUp();
                            chunk.setBlockState( rpos, top, false );
                            rpos.moveDown();
                            marsh = true;
                        }
                    }
                }
                secondLayers = top == MUD ? MUD : DIRT;

                if( marsh ) top = secondLayers;
                chunk.setBlockState( rpos, top, false );
            } else if( ctrl > 0 ) {
                ctrl--;
                chunk.setBlockState( rpos, secondLayers, false );
            }
        }
    }

    private BlockState computeBlockState( int x, int y, int z ) {
        if( y >= MurkSurfaceGeneration.MAIN_HEIGHT - 1 ) {
            double podzol = podzolNoise.generateMultiplied( x, y, z, podzolFactor ) + podzolFactor;
            double grass = grassNoise.generateMultiplied( x, y, z, grassFactor ) + grassFactor;
            double mud = mudNoise.generateMultiplied( x, y, z, mudFactor ) + mudFactor;
            if( mud > podzol && mud > grass ) {
                return MUD;
            }
            if( podzol > grass ) {
                return PODZOL;
            }
            return GRASS;
        } else {
            return MUD;
        }
    }
}
