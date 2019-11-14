/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.terrain;

import modernity.api.util.MovingBlockPos;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.MDSurfaceGenSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.MathUtil;
import net.rgsw.noise.FractalPerlin2D;
import net.rgsw.noise.INoise2D;
import net.rgsw.noise.INoise3D;
import net.rgsw.noise.InverseFractalPerlin3D;

import java.util.Random;

/**
 * Generates the main terrain shapes of the Modernity's surface dimension using only rock.
 */
public class MDSurfaceTerrainGenerator {
    private static final int BUFF_SIZE_X = 5;
    private static final int BUFF_SIZE_Y = 33;
    private static final int BUFF_SIZE_Z = 5;

    private final IWorld world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider biomeGen;

    private final INoise3D noiseA;
    private final INoise3D noiseB;
    private final INoise3D mixNoise;
    private final INoise2D depthNoise;

    private final MDSurfaceGenSettings settings;

    private final ThreadLocal<double[]> noiseBuffer = new ThreadLocal<>();

    private final float[] biomeWeights;

    private final int r;
    private final int r2;

    public MDSurfaceTerrainGenerator( IWorld world, BiomeProvider biomeGen, MDSurfaceGenSettings settings ) {
        this.world = world;
        this.seed = world.getSeed();
        this.biomeGen = biomeGen;
        this.rand = new Random( seed );

        this.settings = settings;
        noiseA = new InverseFractalPerlin3D( rand.nextInt(), 1 / ( 4 * 684.412 ), 1 / ( 4 * 684.412 ), 1 / ( 4 * 684.412 ), 16 );
        noiseB = new InverseFractalPerlin3D( rand.nextInt(), 1 / ( 4 * 684.412 ), 1 / ( 8 * 684.412 ), 1 / ( 4 * 684.412 ), 16 );
        mixNoise = new InverseFractalPerlin3D( rand.nextInt(), 1 / ( 4 * 4.277574920654297 ), 1 / ( 8 * 8.555149841308594 ), 1 / ( 4 * 4.277574920654297 ), 8 );
        depthNoise = new FractalPerlin2D( rand.nextInt(), 16, 16 );

        r = settings.getBiomeBlendRadius();
        r2 = r * 2 + 1;

        this.biomeWeights = new float[ r2 * r2 ];
        for( int x = - r; x <= r; ++ x ) {
            for( int z = - r; z <= r; ++ z ) {
                double dx = x / (double) r * 2;
                double dz = z / (double) r * 2;
                float weight = 10.0F / MathHelper.sqrt( dx * dx + dz * dz + 0.2F );
                this.biomeWeights[ x + r + ( z + r ) * r2 ] = weight;
            }
        }
    }

    /**
     * Generates the terrain in the specified chunk.
     */
    public void generateTerrain( IChunk chunk ) {
        ChunkPos pos = chunk.getPos();
        int cx = pos.x;
        int cz = pos.z;
        fillNoiseBuffer( cx, cz );

        double[] buffer = noiseBuffer.get();

        MovingBlockPos rpos = new MovingBlockPos();
        for( int x4 = 0; x4 < 4; x4++ ) {
            for( int z4 = 0; z4 < 4; z4++ ) {
                for( int y8 = 0; y8 < 32; y8++ ) {
                    double noise00 = buffer[ index( x4, y8, z4 ) ];
                    double noise01 = buffer[ index( x4, y8, z4 + 1 ) ];
                    double noise10 = buffer[ index( x4 + 1, y8, z4 ) ];
                    double noise11 = buffer[ index( x4 + 1, y8, z4 + 1 ) ];

                    double delta00 = ( buffer[ index( x4, y8 + 1, z4 ) ] - noise00 ) / 8;
                    double delta01 = ( buffer[ index( x4, y8 + 1, z4 + 1 ) ] - noise01 ) / 8;
                    double delta10 = ( buffer[ index( x4 + 1, y8 + 1, z4 ) ] - noise10 ) / 8;
                    double delta11 = ( buffer[ index( x4 + 1, y8 + 1, z4 + 1 ) ] - noise11 ) / 8;

                    for( int y = 0; y < 8; y++ ) {

                        double noise0 = noise00;
                        double noise1 = noise01;

                        double delta0 = ( noise10 - noise00 ) / 4;
                        double delta1 = ( noise11 - noise01 ) / 4;

                        for( int x = 0; x < 4; x++ ) {
                            double noise = noise0;
                            double delta = ( noise1 - noise0 ) / 4;

                            for( int z = 0; z < 4; z++ ) {

                                int posx = x + x4 * 4;
                                int posy = y + y8 * 8;
                                int posz = z + z4 * 4;

                                BlockState state = Blocks.AIR.getDefaultState();

                                if( posy < settings.getWaterLevel() ) {
                                    state = MDBlocks.MODERNIZED_WATER.getDefaultState();
                                }

                                if( noise > 0 ) {
                                    state = MDBlocks.ROCK.getDefaultState();
                                }

                                rpos.setPos( posx, posy, posz );
                                chunk.setBlockState( rpos, state, false );

                                // Change noise value by delta for interpolation
                                noise += delta;
                            }

                            // Change noise values by deltas for interpolation
                            noise0 += delta0;
                            noise1 += delta1;
                        }

                        // Change noise values by deltas for interpolation
                        noise00 += delta00;
                        noise01 += delta01;
                        noise10 += delta10;
                        noise11 += delta11;
                    }
                }
            }
        }
    }

    /**
     * Get a list of biomes in the specified region.
     */
    private Biome[] getBiomes( int x, int z, int width, int length ) {
        Biome[] biomes = new Biome[ width * length ];
        for( int xi = 0; xi < width; xi++ ) {
            int xp = x + xi;
            for( int zi = 0; zi < length; zi++ ) {
                int zp = z + zi;
                biomes[ zi * width + xi ] = biomeGen.func_222366_b( xp, zp );
            }
        }
        return biomes;
    }

    /**
     * Fills the noise buffer for a specific chunk, reusing the buffer stored in a thread-local field.
     */
    private void fillNoiseBuffer( int cx, int cz ) {
        double[] buff = noiseBuffer.get();
        if( buff == null ) {
            buff = new double[ BUFF_SIZE_X * BUFF_SIZE_Y * BUFF_SIZE_Z ];
            noiseBuffer.set( buff );
        }

        Biome[] biomes = getBiomes( cx * 4 - r, cz * 4 - r, 6 + r * 2, 6 + r * 2 );

        int mainHeight = 72;

        for( int x = 0; x < BUFF_SIZE_X; x++ ) {
            for( int z = 0; z < BUFF_SIZE_Z; z++ ) {

                double scale = 0;
                double depth = 0;
                double variation = 0;
                double total = 0;

                ModernityBiome centerBiome = (ModernityBiome) biomes[ x + r + ( z + r ) * ( 5 + r2 ) ];

                for( int x1 = - r; x1 <= r; x1++ ) {
                    for( int z1 = - r; z1 <= r; z1++ ) {
                        int bx = x1 + x + r;
                        int bz = z1 + z + r;

                        ModernityBiome biome = (ModernityBiome) biomes[ bz * ( 6 + r * 2 ) + bx ];
                        double dpt = biome.getBaseHeight();

                        double wgt = biomeWeights[ x1 + r + ( z1 + r ) * r2 ];
                        wgt /= dpt + 66;

                        if( dpt > centerBiome.getBaseHeight() ) {
                            wgt /= 2;
                        }

                        double weight = biome.getBlendWeight() * wgt;

                        variation += biome.getHeightVariation() * weight;
                        scale += biome.getHeightDifference() * weight;
                        depth += dpt * weight;
                        total += weight;
                    }
                }

                double depthNoise = this.depthNoise.generateMultiplied( x + cx * 4, z + cz * 4, 4 );
                if( depthNoise < 0 ) {
                    depthNoise *= - 1;
                    depthNoise /= 4;
                } else {
                    depthNoise *= 15;
                    if( depthNoise > 4 ) {
                        depthNoise = 4;
                    }
                }

                depthNoise -= 3;
                depthNoise *= - 0.25;

                scale /= total * 8;
                depth /= total;
                variation /= total * 8;

                depth += mainHeight;
                depth /= 8;

                depth += depthNoise * variation;

                double minh = depth - scale;
                double maxh = depth + scale;


                for( int y = 0; y < BUFF_SIZE_Y; y++ ) {
                    // Apply height difference and height scale
                    double density = MathUtil.lerp( - 1, 1, MathUtil.unlerp( minh, maxh, y ) );

                    double a = noiseA.generateMultiplied( x + cx * 4, y, z + cz * 4, 6 );
                    double b = noiseB.generateMultiplied( x + cx * 4, y, z + cz * 4, 6 );
                    double mix = ( mixNoise.generateMultiplied( x + cx * 4, y, z + cz * 4, 4 ) + 1 ) / 2;
                    double noise = MathUtil.lerp( a, b, MathUtil.clamp( mix, 0, 1 ) ) - density;

                    buff[ index( x, y, z ) ] = noise;
                }
            }
        }
    }

    private int index( int x, int y, int z ) {
        return ( x * BUFF_SIZE_Z + z ) * BUFF_SIZE_Y + y;
    }
}
