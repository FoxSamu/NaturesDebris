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
import net.rgsw.noise.FractalPerlin3D;
import net.rgsw.noise.INoise3D;

import java.util.Random;

/**
 * Surface generator that generates a basic humus surface, with mud underwater.
 */
public class ForestSurfaceGenerator implements ISurfaceGenerator {

    private static final BlockState HUMUS = MDNatureBlocks.MURKY_HUMUS.getDefaultState();
    private static final BlockState LEAFY_HUMUS = MDNatureBlocks.LEAFY_HUMUS.getDefaultState();
    private static final BlockState PODZOL = MDNatureBlocks.MURKY_PODZOL.getDefaultState();
    private static final BlockState GRASS = MDNatureBlocks.MURKY_GRASS_BLOCK.getDefaultState();
    private static final BlockState DIRT = MDNatureBlocks.MURKY_DIRT.getDefaultState();
    private static final BlockState MUD = MDNatureBlocks.MUD.getDefaultState();

    private INoise3D humusNoise;
    private INoise3D leafyNoise;
    private INoise3D grassNoise;
    private INoise3D podzolNoise;
    private INoise3D mudNoise;

    private final double humusFactor;
    private final double grassFactor;
    private final double podzolFactor;

    public ForestSurfaceGenerator( double humusFactor, double grassFactor, double podzolFactor ) {
        this.humusFactor = humusFactor;
        this.grassFactor = grassFactor;
        this.podzolFactor = podzolFactor;
    }

    @Override
    public void init( Random rand ) {
        humusNoise = new FractalPerlin3D( rand.nextInt(), 31.15, 5 );
        grassNoise = new FractalPerlin3D( rand.nextInt(), 21.73, 5 );
        podzolNoise = new FractalPerlin3D( rand.nextInt(), 17.81, 5 );
        mudNoise = new FractalPerlin3D( rand.nextInt(), 28.24, 5 );

        leafyNoise = new FractalPerlin3D( rand.nextInt(), 25.88, 5 );
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
            double humus = humusNoise.generateMultiplied( x, y, z, humusFactor ) + humusFactor;
            double podzol = podzolNoise.generateMultiplied( x, y, z, podzolFactor ) + podzolFactor;
            double grass = grassNoise.generateMultiplied( x, y, z, grassFactor ) + grassFactor;
            if( humus > podzol && humus > grass ) {
                double leafy = leafyNoise.generate( x, y, z ) + 0.15;
                return leafy > 0 ? LEAFY_HUMUS : HUMUS;
            }
            if( podzol > grass ) {
                return PODZOL;
            }
            return GRASS;
        } else {
            double mud = mudNoise.generate( x, y, z );
            return mud > 0 ? MUD : DIRT;
        }
    }
}
