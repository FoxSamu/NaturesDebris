package modernity.common.world.gen.terrain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.MathUtil;
import net.rgsw.noise.InverseFractalPerlin2D;
import net.rgsw.noise.InverseFractalPerlin3D;

import modernity.api.util.EcoBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.ModernityGenSettings;

import java.util.Random;

@Deprecated
public class ModernityTerrainGeneratorAncient {
    private static final int BUFF_SIZE_X = 5;
    private static final int BUFF_SIZE_Y = 33;
    private static final int BUFF_SIZE_Z = 5;

    private static final double MAIN_NOISE_SIZE_H = 684.412;
    private static final double MAIN_NOISE_SIZE_V = 684.412;

    private static final double MIX_NOISE_SIZE_H = 684.412 / 80;
    private static final double MIX_NOISE_SIZE_V = 684.412 / 160;
    private static final double DEPTH_NOISE_SIZE = 200;

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

    private final World world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;

    private final InverseFractalPerlin3D noiseA;
    private final InverseFractalPerlin3D noiseB;
    private final InverseFractalPerlin3D mixNoise;
    private final InverseFractalPerlin2D depthNoise;

    private final ModernityGenSettings settings;

    private final ThreadLocal<double[]> noiseBuffer = new ThreadLocal<>();

    public ModernityTerrainGeneratorAncient( World world, BiomeProvider provider, ModernityGenSettings settings ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );

        this.settings = settings;
        noiseA = new InverseFractalPerlin3D( rand.nextInt(), 1 / settings.getMainNoiseSizeX(), 1 / settings.getMainNoiseSizeY(), 1 / settings.getMainNoiseSizeZ(), 8 );
        noiseB = new InverseFractalPerlin3D( rand.nextInt(), 1 / settings.getMainNoiseSizeX(), 1 / settings.getMainNoiseSizeY(), 1 / settings.getMainNoiseSizeZ(), 8 );
        mixNoise = new InverseFractalPerlin3D( rand.nextInt(), 1 / settings.getMixNoiseSizeX(), 1 / settings.getMixNoiseSizeY(), 1 / settings.getMixNoiseSizeZ(), 16 );
        depthNoise = new InverseFractalPerlin2D( rand.nextInt(), 1 / settings.getDepthNoiseSizeX(), 1 / settings.getDepthNoiseSizeZ(), 16 );

    }

    public void generateTerrain( IChunk chunk ) {
        ChunkPos pos = chunk.getPos();
        int cx = pos.x;
        int cz = pos.z;
        fillNoiseBuffer( cx, cz );

        double[] buffer = noiseBuffer.get();

        EcoBlockPos rpos = EcoBlockPos.retain();
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

                                IBlockState state = Blocks.AIR.getDefaultState();

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
        rpos.release();
    }

    private void fillNoiseBuffer( int cx, int cz ) {
        double[] buff = noiseBuffer.get();
        if( buff == null ) {
            buff = new double[ BUFF_SIZE ];
            noiseBuffer.set( buff );
        }

        int r = settings.getBiomeBlendRadius();

        Biome[] biomes = this.provider.getBiomes( cx * 4 - r, cz * 4 - r, 6 + r * 2, 6 + r * 2 );

        for( int x = 0; x < BUFF_SIZE_X; x++ ) {
            for( int z = 0; z < BUFF_SIZE_Z; z++ ) {

                double scale = 0;
                double depth = 0;

                // TODO: Chunk gen settings
                for( int x1 = - r; x1 <= r; x1++ ) {
                    for( int z1 = - r; z1 <= r; z1++ ) {
                        int bx = x1 + x + r;
                        int bz = z1 + z + r;

                        Biome b = biomes[ bz * ( 6 + r * 2 ) + bx ];

                        scale += b.getScale();
                        depth += b.getDepth();
                    }
                }

                int max = ( 1 + r * 2 ) * ( 1 + r * 2 );
                scale /= max;
                depth /= max;

                double depthNoise = this.depthNoise.generateMultiplied( x + cx * 4, z + cz * 4, 1/*65536*/ ) / 8000;

                if( depthNoise < 0 ) {
                    depthNoise = - depthNoise * 0.3;
                }

                depthNoise = depthNoise * 3 - 2;
                if( depthNoise < 0 ) {
                    depthNoise /= 2;

                    if( depthNoise < - 1 ) { // Clamp
                        depthNoise = - 1;
                    }

                    depthNoise /= 2.8;
                } else {
                    if( depthNoise > 1 ) { // Clamp
                        depthNoise = 1;
                    }

                    depthNoise /= 8;
                }


                // Apply height stretch
                depth = ( depth * 4 - 1 ) / 8;
                depth = depth + depthNoise * 0.2;
                depth = depth * 8.5 / 8;
                depth = 8.5 + depth * 4;

//                double minHeight = baseHeight - heightVar;
//                double maxHeight = baseHeight + heightVar;

                for( int y = 0; y < BUFF_SIZE_Y; y++ ) {
                    // Apply height difference and height scale
                    double density = ( y - depth ) * 12 * 128 / 256 / scale;
                    if( density < 0 ) {
                        density *= 4;
                    }

//                    double weightValue = Y_BUFFER[ y ];

                    double a = noiseA.generateMultiplied( x + cx * 4, y, z + cz * 4, 1/*256*/ ) / 512;
                    double b = noiseB.generateMultiplied( x + cx * 4, y, z + cz * 4, 1/*256*/ ) / 512;
                    double mix = ( mixNoise.generateMultiplied( x + cx * 4, y, z + cz * 4, 1/*65536*/ ) / 10 + 1 ) / 2;
                    double noise = MathHelper.clampedLerp( a, b, mix ) - density;

//                    if( y < minHeight ) {
//                        noise = 50;
//                    } else if( y > maxHeight ) {
//                        noise = - 50;
//                    } else {
//                        double prog = MathUtil.invLerp( minHeight, maxHeight, y );
//                        noise = genNoise( x + cx * 4, y, z + cz * 4 );
//                        noise += MathUtil.lerp( 40, - 40, prog );
//                    }

                    // Apply extreme negative values at height limit, so hills never exceed height limit
                    if( y > 29 ) {
                        double h = ( y - 29 ) / 3.0;
                        noise = noise * ( 1 - h ) - 10 * h;
                    }

//                    noise -= weightValue;

                    buff[ index( x, y, z ) ] = noise;
                }
            }
        }
    }

    private double genNoise( int x, int y, int z ) {
        double a = noiseA.generate( x, y, z ) * 256;
        double b = noiseB.generate( x, y, z ) * 256;
        double mix = mixNoise.generate( x, y, z ) * 65536;
        return MathHelper.clampedLerp( a, b, MathUtil.invLerp( - 1, 1, mix ) );
    }

    private int index( int x, int y, int z ) {
        return ( x * BUFF_SIZE_Z + z ) * BUFF_SIZE_Y + y;
    }
}
