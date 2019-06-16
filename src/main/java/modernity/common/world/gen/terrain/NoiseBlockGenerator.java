/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 16 - 2019
 */

package modernity.common.world.gen.terrain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.WorldGenRegion;
import net.rgsw.noise.INoise3D;

import modernity.api.util.EcoBlockPos;

import java.util.function.Predicate;

public class NoiseBlockGenerator {
    private final ThreadLocal<double[]> noiseBufferLocal = ThreadLocal.withInitial( () -> new double[ 9 * 9 * 129 ] );

    private final INoise3D noise;
    private final Predicate<IBlockState> matcher;
    private final IBlockState state;

    public NoiseBlockGenerator( INoise3D noise, Predicate<IBlockState> matcher, IBlockState state ) {
        this.noise = noise;
        this.matcher = matcher;
        this.state = state;
    }

    public void generate( WorldGenRegion region ) {
        generate( region.getChunk( region.getMainChunkX(), region.getMainChunkZ() ) );
    }

    public void generate( IChunk region ) {
        int cx = region.getPos().x;
        int cz = region.getPos().z;
        double[] buffer = fillNoiseBuffer( cx * 16, cz * 16 );
        try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
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

                                    rpos.setPos( bx, by, bz );

                                    if( canPlace( region, rpos, noise ) ) {
                                        region.setBlockState( rpos, getState( region, rpos, noise ), false );
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


    protected double generateNoise( int x, int y, int z ) {
        return noise.generate( x, y, z );
    }

    protected boolean canPlace( IChunk chunk, BlockPos pos, double noise ) {
        return noise > 0 && matcher.test( chunk.getBlockState( pos ) );
    }

    protected IBlockState getState( IChunk chunk, BlockPos pos, double noise ) {
        return state;
    }
}
