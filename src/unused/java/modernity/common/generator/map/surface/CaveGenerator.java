/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.common.generator.map.surface;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDNatureBlocks;
import modernity.common.generator.MurkSurfaceGeneration;
import modernity.common.generator.map.MapGenerator;
import modernity.common.generator.util.NoiseBuffer;
import net.minecraft.block.BlockState;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;
import net.redgalaxy.util.MathUtil;
import net.rgsw.noise.FractalOpenSimplex3D;
import net.rgsw.noise.OpenSimplex3D;

/**
 * Generates the caves in the Modernity's surface dimension.
 */
public class CaveGenerator extends MapGenerator<SurfaceGenData> {
    private static final BlockState AIR = MDNatureBlocks.CAVE_AIR.getDefaultState();
    private static final BlockState WATER = MDNatureBlocks.MURKY_WATER.getDefaultState();

    private static final int SEG_SIZE_X = 2;                                    // The size of a segment along x-axis
    private static final int SEG_SIZE_Y = 2;                                    // The size of a segment along y-axis
    private static final int SEG_SIZE_Z = 2;                                    // The size of a semgent along z-axis

    private static final int SEGMENTS_X = 16 / SEG_SIZE_X;                      // Amount of segments along x-axis
    private static final int SEGMENTS_Y = 256 / SEG_SIZE_Y;                     // Amount of segments along y-axis
    private static final int SEGMENTS_Z = 16 / SEG_SIZE_Z;                      // Amount of semgents along z-axis

    private static final int BUFF_SIZE_X = SEGMENTS_X + 1;                      // The x-size of the noise buffer
    private static final int BUFF_SIZE_Y = SEGMENTS_Y + 1;                      // The y-size of the noise buffer
    private static final int BUFF_SIZE_Z = SEGMENTS_Z + 1;                      // The z-size of the noise buffer

    private static final double XZ_CAVE_SCALE = 0.08;                           // Cave noise scale along x and z
    private static final double Y_CAVE_SCALE = 0.15;                            // Cave noise scale along y
    private static final double XZ_FORM_SCALE = 0.5;                            // Form noise scale along x and z
    private static final double Y_FORM_SCALE = 0.3;                             // Form noise scale along y
    private static final double FORM_MULTIPLIER = 0.4;                          // Multiplier of form noise
    private static final double NOISE_ADDEND = 0.3;                             // Value to be added to the full noise


    /**
     * Noise generator that determines the local density of caves
     */
    private OpenSimplex3D cave;

    /**
     * Noise generator that determines the actual cave shapes
     */
    private FractalOpenSimplex3D form;

    public CaveGenerator( IWorld world ) {
        super( world );

        cave = new OpenSimplex3D( rand.nextInt() );
        form = new FractalOpenSimplex3D( rand.nextInt(), 10, 4 );
    }

    /**
     * Generates caves in a chunk, limiting to the height map in the specified data.
     *
     * @param region The region to generate caves in
     * @param data   A generator data instance
     */
    @Override
    public void generate( WorldGenRegion region, SurfaceGenData data ) {
        int[] heightmap = data.getHeightmap();

        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();

        NoiseBuffer buffer = fillNoiseBuffer( cx * 16, cz * 16, data );

        MovingBlockPos rpos = new MovingBlockPos();

        for( int x0 = 0; x0 < SEGMENTS_X; x0++ ) {
            int x1 = x0 + 1;

            for( int z0 = 0; z0 < SEGMENTS_Z; z0++ ) {
                int z1 = z0 + 1;

                for( int y0 = 0; y0 < SEGMENTS_Y; y0++ ) {
                    int y1 = y0 + 1;

                    // Values
                    double noise000 = buffer.get( x0, y0, z0 );
                    double noise001 = buffer.get( x0, y0, z1 );
                    double noise100 = buffer.get( x1, y0, z0 );
                    double noise101 = buffer.get( x1, y0, z1 );
                    double noise010 = buffer.get( x0, y1, z0 );
                    double noise011 = buffer.get( x0, y1, z1 );
                    double noise110 = buffer.get( x1, y1, z0 );
                    double noise111 = buffer.get( x1, y1, z1 );

                    double delta00 = ( noise100 - noise000 ) / SEG_SIZE_X;
                    double delta01 = ( noise101 - noise001 ) / SEG_SIZE_X;
                    double delta10 = ( noise110 - noise010 ) / SEG_SIZE_X;
                    double delta11 = ( noise111 - noise011 ) / SEG_SIZE_X;

                    double current00 = noise000;
                    double current01 = noise001;
                    double current10 = noise010;
                    double current11 = noise011;

                    // Step along X axis
                    for( int xo = 0; xo < SEG_SIZE_X; xo++ ) {
                        int bx = x0 * SEG_SIZE_X + xo;

                        double delta0 = ( current01 - current00 ) / SEG_SIZE_Z;
                        double delta1 = ( current11 - current10 ) / SEG_SIZE_Z;

                        double current0 = current00;
                        double current1 = current10;

                        // Step along Z axis
                        for( int zo = 0; zo < SEG_SIZE_Z; zo++ ) {
                            int bz = z0 * SEG_SIZE_Z + zo;

                            double delta = ( current1 - current0 ) / SEG_SIZE_Y;

                            double current = current0;

                            // Step along Y axis
                            for( int yo = 0; yo < SEG_SIZE_Y; yo++ ) {
                                int by = y0 * SEG_SIZE_Y + yo;

                                rpos.setPos( bx + cx * 16, by, bz + cz * 16 );

                                double noise = extrapolateNoise( by, current, heightmap[ bx + bz * 16 ] );
                                placeBlock( region, rpos, noise );

                                current += delta;
                            }

                            current0 += delta0;
                            current1 += delta1;
                        }

                        current00 += delta00;
                        current01 += delta01;
                        current10 += delta10;
                        current11 += delta11;
                    }
                }
            }
        }
    }

    /**
     * Extrapolates the given noise value at low or high heights.
     * @param y      The Y coordinate
     * @param noise  The noise value
     * @param height The height map value
     * @return The extrapolated noise value
     */
    private double extrapolateNoise( int y, double noise, int height ) {
        if( y < 10 ) {
            double add = MathUtil.unlerp( 10, 0, y );
            noise += add * add * add * 3;
        }

        int upperbnd = height - 12;
        if( y > upperbnd - 10 ) {
            double add = MathUtil.unlerp( upperbnd - 10, upperbnd, y );
            noise += add * add * add * 3;
        }

        return noise;
    }

    /**
     * Places a block in the world region, based on the specified noise value.
     * @param region The region to place the block in
     * @param rpos   The position to place the block at
     * @param noise  The noise value
     */
    private void placeBlock( WorldGenRegion region, MovingBlockPos rpos, double noise ) {
        BlockState state = region.getBlockState( rpos );
        if( noise < 0 && state.getBlock() != MDNatureBlocks.UNBREAKABLE_STONE ) {
            state = AIR;
            if( rpos.getY() <= MurkSurfaceGeneration.CAVE_WATER_LEVEL ) {
                state = WATER;
            }

            region.setBlockState( rpos, state, 2 );
        }
    }

    /**
     * Fills the noise buffer for the specified chunk.
     * @param rx   Chunk start X in block coordinates
     * @param rz   Chunk start Z in block coordinates
     * @param data A generator data instance
     * @return The filled noise buffer
     */
    private NoiseBuffer fillNoiseBuffer( int rx, int rz, SurfaceGenData data ) {
        NoiseBuffer buffer = data.initCaveBuffer( BUFF_SIZE_X, BUFF_SIZE_Y, BUFF_SIZE_Z );

        for( int x = 0; x < BUFF_SIZE_X; x++ ) {
            for( int z = 0; z < BUFF_SIZE_Z; z++ ) {
                for( int y = 0; y < BUFF_SIZE_Y; y++ ) {
                    int bx = rx + x * SEG_SIZE_X;
                    int by = y * SEG_SIZE_Y;
                    int bz = rz + z * SEG_SIZE_Z;
                    buffer.set( x, y, z, generateNoise( bx, by, bz ) );
                }
            }
        }
        return buffer;
    }


    /**
     * Generates and mixes noise for the specified coordinates
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return The combined noise value
     */
    private double generateNoise( int x, int y, int z ) {
        return cave.generate( x * XZ_CAVE_SCALE, y * Y_CAVE_SCALE, z * XZ_CAVE_SCALE )
                   + form.generate( x * XZ_FORM_SCALE, y * Y_FORM_SCALE, z * XZ_FORM_SCALE ) * FORM_MULTIPLIER
                   + NOISE_ADDEND;
    }
}
