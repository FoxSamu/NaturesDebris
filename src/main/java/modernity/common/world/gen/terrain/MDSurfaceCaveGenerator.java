package modernity.common.world.gen.terrain;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.MDSurfaceGenSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.MathUtil;
import net.rgsw.noise.FractalOpenSimplex3D;
import net.rgsw.noise.OpenSimplex3D;

import java.util.Random;

/**
 * Generates the caves in the Modernity's surface dimension.
 */
public class MDSurfaceCaveGenerator {
    private static final double XZ_CAVE_SCALE = 0.08;
    private static final double Y_CAVE_SCALE = 0.15;
    private static final double XZ_FORM_SCALE = 0.5;
    private static final double Y_FORM_SCALE = 0.3;
    private static final double FORM_SCALE = 0.4;
    private static final double BASE_LIMIT = - 0.3;

    private final IWorld world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;

    private final MDSurfaceGenSettings settings;

    private final ThreadLocal<double[]> noiseBufferLocal = ThreadLocal.withInitial( () -> new double[ 9 * 9 * 129 ] );


    private OpenSimplex3D cave;
    private FractalOpenSimplex3D form;

    public MDSurfaceCaveGenerator( IWorld world, BiomeProvider provider, MDSurfaceGenSettings settings ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );

        this.settings = settings;

        cave = new OpenSimplex3D( rand.nextInt() );
        form = new FractalOpenSimplex3D( rand.nextInt(), 10, 4 );
    }

    /**
     * Generates caves in a chunk, limiting to the specified height map.
     */
    public void generateCaves( IChunk region, int[] caveHeightmap ) {
        int cx = region.getPos().x; //region.getMainChunkX();
        int cz = region.getPos().z; //region.getMainChunkZ();
        double[] buffer = fillNoiseBuffer( cx * 16, cz * 16 );
        MovingBlockPos rpos = new MovingBlockPos();
        for( int x = 0; x < 8; x++ ) {
            int i0 = x * 9;
            int i1 = ( x + 1 ) * 9;

            for( int z = 0; z < 8; z++ ) {
                int i00 = ( i0 + z ) * 129;
                int i01 = ( i0 + z + 1 ) * 129;
                int i10 = ( i1 + z ) * 129;
                int i11 = ( i1 + z + 1 ) * 129;

                for( int y = 0; y < 128; y++ ) {
                    //Values
                    double noise000 = buffer[ i00 + y ];
                    double noise001 = buffer[ i01 + y ];
                    double noise100 = buffer[ i10 + y ];
                    double noise101 = buffer[ i11 + y ];
                    double noise010 = buffer[ i00 + y + 1 ];
                    double noise011 = buffer[ i01 + y + 1 ];
                    double noise110 = buffer[ i10 + y + 1 ];
                    double noise111 = buffer[ i11 + y + 1 ];

                    //Step along X axis
                    double delta00 = ( noise100 - noise000 ) * 0.5D;
                    double delta01 = ( noise101 - noise001 ) * 0.5D;
                    double delta10 = ( noise110 - noise010 ) * 0.5D;
                    double delta11 = ( noise111 - noise011 ) * 0.5D;

                    double current00 = noise000;
                    double current01 = noise001;
                    double current10 = noise010;
                    double current11 = noise011;

                    //Step X axis
                    for( int xo = 0; xo < 2; xo++ ) {
                        double current0 = current00;
                        double current1 = current10;

                        //Step along Z axis
                        double delta0 = ( current01 - current00 ) * 0.5D;
                        double delta1 = ( current11 - current10 ) * 0.5D;

                        int bx = x * 2 + xo;// + cx * 16;

                        //Step Z axis
                        for( int zo = 0; zo < 2; zo++ ) {
                            //Step along Y axis
                            double delta = ( current1 - current0 ) * 0.5D;

                            double current = current1 - delta;

                            int bz = z * 2 + zo;// + cz * 16;

                            //Step Y axis
                            for( int yo = 0; yo < 1; yo++ ) {
                                double noise = current += delta;

                                int by = y + yo;

                                if( by < 10 ) {
                                    double add = MathUtil.unlerp( 10, 0, by );
                                    noise += add * add * add * 3;
                                }
                                rpos.setPos( bx, by, bz );
                                int upperbnd = caveHeightmap[ bx + bz * 16 ] - 4;
                                if( by > upperbnd - 10 ) {
                                    double add = MathUtil.unlerp( upperbnd - 10, upperbnd, by );
                                    noise += add * add * add * 3;
                                }

                                BlockState state = region.getBlockState( rpos );
                                if( noise < BASE_LIMIT && state.getBlock() != MDBlocks.MODERN_BEDROCK ) {
                                    region.setBlockState( rpos, by > 16
                                                                ? Blocks.CAVE_AIR.getDefaultState()
                                                                : MDBlocks.MODERNIZED_WATER.getDefaultState(), false );
                                }
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

    private double[] fillNoiseBuffer( int cx, int cz ) {
        double[] buffer = noiseBufferLocal.get();
        for( int x = 0; x < 9; x++ ) {
            for( int z = 0; z < 9; z++ ) {
                for( int y = 0; y < 129; y++ ) {
                    int index = ( x * 9 + z ) * 129 + y;
                    int bx = cx + x * 2;
                    int bz = cz + z * 2;
                    buffer[ index ] = generateNoise( bx, y, bz );
                }
            }
        }
        return buffer;
    }


    private double generateNoise( int x, int y, int z ) {
        return cave.generate( x * XZ_CAVE_SCALE, y * Y_CAVE_SCALE, z * XZ_CAVE_SCALE ) + form.generate( x * XZ_FORM_SCALE, y * Y_FORM_SCALE, z * XZ_FORM_SCALE ) * FORM_SCALE;
    }
}
