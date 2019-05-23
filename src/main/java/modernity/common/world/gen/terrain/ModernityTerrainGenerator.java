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
import net.rgsw.noise.FractalPerlin3D;

import modernity.api.util.EcoBlockPos;
import modernity.common.biome.BiomeBase;
import modernity.common.block.MDBlocks;

import java.util.Random;

public class ModernityTerrainGenerator {
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

    private final World world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;

    private final FractalPerlin3D noiseA;
    private final FractalPerlin3D noiseB;
    private final FractalPerlin3D mixNoise;

    private final ThreadLocal<double[]> noiseBuffer = new ThreadLocal<>();

    public ModernityTerrainGenerator( World world, BiomeProvider provider ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );

        noiseA = new FractalPerlin3D( rand.nextInt(), MAIN_NOISE_SIZE_H, MAIN_NOISE_SIZE_V, MAIN_NOISE_SIZE_H, 8 );
        noiseB = new FractalPerlin3D( rand.nextInt(), MAIN_NOISE_SIZE_H, MAIN_NOISE_SIZE_V, MAIN_NOISE_SIZE_H, 8 );
        mixNoise = new FractalPerlin3D( rand.nextInt(), MIX_NOISE_SIZE_H, MIX_NOISE_SIZE_V, MIX_NOISE_SIZE_H, 8 );
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

        Biome[] biomes = this.provider.getBiomes( cx * 4 - 3, cz * 4 - 3, 12, 12 );

        for( int x = 0; x < BUFF_SIZE_X; x++ ) {
            for( int z = 0; z < BUFF_SIZE_Z; z++ ) {

                double heightVar = 0;
                double heightBase = 0;

                for( int x1 = - 3; x1 <= 3; x1++ ) {
                    for( int z1 = - 3; z1 <= 3; z1++ ) {
                        int bx = x1 + x + 3;
                        int bz = z1 + z + 3;

                        BiomeBase b = (BiomeBase) biomes[ bz * 12 + bx ];

                        heightVar += b.getHeightVar();
                        heightBase += b.getBaseHeight();
                    }
                }

                heightVar /= 49;
                heightBase /= 49;

                heightVar /= 8;
                heightBase /= 8;

                double minHeight = heightBase - heightVar;
                double maxHeight = heightBase + heightVar;

                for( int y = 0; y < BUFF_SIZE_Y; y++ ) {
                    double weightValue = Y_BUFFER[ y ];

                    double noise;

                    if( y < minHeight ) {
                        noise = 50;
                    } else if( y > maxHeight ) {
                        noise = - 50;
                    } else {
                        double prog = MathUtil.invLerp( minHeight, maxHeight, y );
                        noise = genNoise( x + cx * 4, y, z + cz * 4 );
                        noise += MathUtil.lerp( 40, - 40, prog );
                    }

                    noise -= weightValue;

                    buff[ index( x, y, z ) ] = noise;
                }
            }
        }
    }

    private double genNoise( int x, int y, int z ) {
        double a = noiseA.generate( x, y, z );
        double b = noiseB.generate( x, y, z );
        double mix = mixNoise.generate( x, y, z ) * 2;
        return MathHelper.clampedLerp( a, b, MathUtil.invLerp( - 1, 1, mix ) ) * 40;
    }

    private int index( int x, int y, int z ) {
        return ( x * BUFF_SIZE_Z + z ) * BUFF_SIZE_Y + y;
    }
}
