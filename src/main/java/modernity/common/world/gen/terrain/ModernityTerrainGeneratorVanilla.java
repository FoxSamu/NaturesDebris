/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.world.gen.terrain;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;

import modernity.api.noise.NoiseImprovedOctaves;
import modernity.api.util.EcoBlockPos;
import modernity.common.world.gen.ModernityGenSettings;

@Deprecated
public class ModernityTerrainGeneratorVanilla {
    private static final int BUFF_SIZE_X = 5;
    private static final int BUFF_SIZE_Y = 33;
    private static final int BUFF_SIZE_Z = 5;

    private static final double MAIN_NOISE_SIZE_H = 24.2206;
    private static final double MAIN_NOISE_SIZE_V = 11.1103;

    private static final double MIX_NOISE_SIZE_H = 8.22306;
    private static final double MIX_NOISE_SIZE_V = 5.55515;

    private static final int BUFF_SIZE = BUFF_SIZE_X * BUFF_SIZE_Y * BUFF_SIZE_Z;

    private static final double[] Y_BUFFER = new double[ BUFF_SIZE_Y ];

    static {
        for( int y = 0; y < BUFF_SIZE_Y; ++ y ) {
            Y_BUFFER[ y ] = 0;
            double height = (double) y;

            boolean flip = false;
            if( y > BUFF_SIZE_Y / 2 ) {
                flip = true;
                height = BUFF_SIZE_Y - 1 - y;
            } else {
                Y_BUFFER[ y ] = Math.cos( y * Math.PI * 12 / BUFF_SIZE_Y ) * 2;
            }

            if( height < 4 ) {
                height = 4 - height;
                if( flip ) {
                    Y_BUFFER[ y ] += height * height * height * 5;
                } else {
                    Y_BUFFER[ y ] -= height * height * height * 5;
                }
            }
        }
    }

    private final NoiseImprovedOctaves noiseA;
    private final NoiseImprovedOctaves noiseB;
    private final NoiseImprovedOctaves mixNoise;
    private final NoiseImprovedOctaves depthNoise;

    private final World world;
    private final BiomeProvider provider;

    private final ThreadLocal<double[]> noiseBuffer = ThreadLocal.withInitial( () -> new double[ 5 * 33 * 5 ] );
    private final ThreadLocal<double[]> depthBuffer = ThreadLocal.withInitial( () -> new double[ 5 * 5 ] );
    private final ThreadLocal<double[]> mainBuffer = ThreadLocal.withInitial( () -> new double[ 5 * 33 * 5 ] );
    private final ThreadLocal<double[]> noiseABuffer = ThreadLocal.withInitial( () -> new double[ 5 * 33 * 5 ] );
    private final ThreadLocal<double[]> noiseBBuffer = ThreadLocal.withInitial( () -> new double[ 5 * 33 * 5 ] );

    private final ModernityGenSettings settings;

    private final float[] biomeWeights;

    private final int r;
    private final int r2;

    public ModernityTerrainGeneratorVanilla( World world, BiomeProvider provider, ModernityGenSettings settings ) {
        this.world = world;
        long seed = world.getSeed();
        this.provider = provider;
        this.settings = settings;

        SharedSeedRandom rand = new SharedSeedRandom( seed );
        this.noiseA = new NoiseImprovedOctaves( rand, settings.getMainNoiseSizeOctaves() );
        this.noiseB = new NoiseImprovedOctaves( rand, settings.getMainNoiseSizeOctaves() );
        this.mixNoise = new NoiseImprovedOctaves( rand, settings.getMixNoiseSizeOctaves() );
        this.depthNoise = new NoiseImprovedOctaves( rand, settings.getDepthNoiseSizeOctaves() );

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

    public void generateTerrain( IChunk chunk ) {
        ChunkPos pos = chunk.getPos();
        int cx = pos.x;
        int cz = pos.z;
        setBlocksInChunk( cx, cz, chunk );
    }



    private void generateHeightMap( Biome[] biomes, int xpos, int ypos, int zpos, double[] buffer ) {
        double[] depths = depthNoise.populateNoiseArray( depthBuffer.get(), xpos, zpos, 5, 5, 200, 200 );

        double scaleX = settings.getMainNoiseSizeX();
        double scaleY = settings.getMainNoiseSizeY();
        double scaleZ = settings.getMainNoiseSizeZ();

        double[] mainNoise = mixNoise.populateNoiseArray( mainBuffer.get(), xpos, ypos, zpos, 5, 33, 5, settings.getMixNoiseSizeX(), settings.getMixNoiseSizeY(), settings.getMixNoiseSizeZ() );
        double[] minNoise = noiseA.populateNoiseArray( noiseABuffer.get(), xpos, ypos, zpos, 5, 33, 5, scaleX, scaleY, scaleZ );
        double[] maxNoise = noiseB.populateNoiseArray( noiseBBuffer.get(), xpos, ypos, zpos, 5, 33, 5, scaleX, scaleY, scaleZ );

        int itr3 = 0;
        int itr2 = 0;

        for( int x = 0; x < 5; ++ x ) {
            for( int z = 0; z < 5; ++ z ) {

                double scale = 0;
                double depth = 0;
                double weight = 0;

                Biome centerBiome = biomes[ x + r + ( z + r ) * ( 5 + r2 ) ];

                // Blend surrounding biomes
                for( int biomeX = - r; biomeX <= r; ++ biomeX ) {
                    for( int biomeZ = - r; biomeZ <= r; ++ biomeZ ) {
                        Biome biome = biomes[ x + biomeX + r + ( z + biomeZ + r ) * ( 5 + r2 ) ];

                        double dpt = settings.getBaseBiomeDepth() + biome.getDepth() * settings.getBiomeDepthMultiplier();
                        double scl = settings.getBaseBiomeScale() + biome.getScale() * settings.getBiomeScaleMultiplier();

                        double wgt = biomeWeights[ biomeX + r + ( biomeZ + r ) * r2 ];
                        wgt /= dpt + 2;

                        if( biome.getDepth() > centerBiome.getDepth() ) {
                            wgt /= 2.0F;
                        }

                        scale += scl * wgt;
                        depth += dpt * wgt;

                        weight += wgt;
                    }
                }

                // Divide by weight to get average
                scale = scale / weight;
                depth = depth / weight;

                scale = scale * 0.9 + 0.1;


                // Depth noise
                double depthNoise = depths[ itr2 ] / 8000;
                if( depthNoise < 0 ) {
                    depthNoise = - depthNoise * 0.3;
                }

                depthNoise = depthNoise * 3 - 2;
                if( depthNoise < 0 ) {
                    depthNoise = depthNoise / 2;

                    if( depthNoise < - 1 ) { // Clamp
                        depthNoise = - 1;
                    }

                    depthNoise = depthNoise / 2.8;
                } else {
                    if( depthNoise > 1 ) { // Clamp
                        depthNoise = 1;
                    }

                    depthNoise = depthNoise / 8;
                }

                itr2++;

                depth = ( depth * 4 - 1 ) / 8;
                depth = depth + depthNoise * settings.getDepthNoiseInfluence();
                depth = depth * settings.getHeightStretch() / 8;
                depth = settings.getHeightStretch() + depth * 4;

                for( int y = 0; y < 33; ++ y ) {
                    double density = ( y - depth ) * settings.getHeightScale() * 128 / 256 / scale;
                    if( density < 0 ) {
                        density *= 4;
                    }

                    double min = minNoise[ itr3 ] / 512;
                    double max = maxNoise[ itr3 ] / 512;
                    double main = ( mainNoise[ itr3 ] / 10 + 1 ) / 2;

                    double noise = MathHelper.clampedLerp( min, max, main ) - density;

                    if( y > 29 ) {
                        double h = ( y - 29 ) / 3D;
                        noise = noise * ( 1 - h ) - 10 * h;
                    }

                    buffer[ itr3 ] = noise;
                    ++ itr3;
                }
            }
        }

    }

    public void setBlocksInChunk( int cx, int cz, IChunk primer ) {
        Biome[] biomes = provider.getBiomes( cx * 4 - r, cz * 4 - r, 5 + r2, 5 + r2 );


        double[] buffer = noiseBuffer.get();
        generateHeightMap( biomes, cx * 4, 0, cz * 4, buffer );

        EcoBlockPos rpos = EcoBlockPos.retain();

        for( int x4 = 0; x4 < 4; ++ x4 ) {
            int x1 = x4 * 5;
            int x2 = ( x4 + 1 ) * 5;

            for( int z4 = 0; z4 < 4; ++ z4 ) {
                int xz11 = ( x1 + z4 ) * 33;
                int xz12 = ( x1 + z4 + 1 ) * 33;
                int xz21 = ( x2 + z4 ) * 33;
                int xz22 = ( x2 + z4 + 1 ) * 33;

                for( int y8 = 0; y8 < 32; ++ y8 ) {
                    // Corner 1 noise
                    double noise00 = buffer[ xz11 + y8 ];
                    double noise01 = buffer[ xz12 + y8 ];
                    double noise10 = buffer[ xz21 + y8 ];
                    double noise11 = buffer[ xz22 + y8 ];

                    // Delta values to interpolate to corner 2 noise
                    double delta00 = ( buffer[ xz11 + y8 + 1 ] - noise00 ) / 8;
                    double delta01 = ( buffer[ xz12 + y8 + 1 ] - noise01 ) / 8;
                    double delta10 = ( buffer[ xz21 + y8 + 1 ] - noise10 ) / 8;
                    double delta11 = ( buffer[ xz22 + y8 + 1 ] - noise11 ) / 8;

                    for( int y = 0; y < 8; ++ y ) {
                        // Noise
                        double noise0 = noise00;
                        double noise1 = noise01;

                        // Delta
                        double delta0 = ( noise10 - noise00 ) / 4;
                        double delta1 = ( noise11 - noise01 ) / 4;

                        for( int x = 0; x < 4; ++ x ) {
                            // Noise
                            double noise = noise0;

                            // Delta
                            double delta = ( noise1 - noise0 ) / 4;

                            for( int z = 0; z < 4; ++ z ) {
                                rpos.setPos( x4 * 4 + x, y8 * 8 + y, z4 * 4 + z );

                                if( noise > 0.0D ) {
                                    primer.setBlockState( rpos, settings.getDefaultBlock(), false );
                                } else if( y8 * 8 + y < settings.getWaterLevel() ) {
                                    primer.setBlockState( rpos, settings.getDefaultFluid(), false );
                                }

                                // Interpolate
                                noise += delta;
                            }

                            // Interpolate
                            noise0 += delta0;
                            noise1 += delta1;
                        }

                        // Interpolate
                        noise00 += delta00;
                        noise01 += delta01;
                        noise10 += delta10;
                        noise11 += delta11;
                    }
                }
            }
        }

        rpos.release();
    }
}
