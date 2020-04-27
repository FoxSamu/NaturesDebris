/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.map.surface;

import modernity.common.block.MDNatureBlocks;
import modernity.common.fluid.MDFluids;
import modernity.common.generator.MurkSurfaceGeneration;
import modernity.common.generator.map.RangeMapGenerator;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.FractalPerlin3D;
import net.rgsw.noise.INoise3D;

import java.util.Random;

public class CanyonGenerator extends RangeMapGenerator<SurfaceGenData> {
    private static final double RADIUS_NOISE_SCALE = 7.521521;
    private static final double RADIUS_DETAIL_THRESHOLD = 1.4;

    public CanyonGenerator( IWorld world ) {
        super( world, 6 );
    }

    @Override
    protected void generateRecursively( IWorld world, IChunk chunk, int cx, int cz, int ox, int oz, SurfaceGenData data ) {
        long seed = rand.nextLong();
        boolean hasCanyon = hasCanyon( ox, oz );

        if( hasCanyon ) {
            Random rand = new Random( seed );
            addCanyon(
                world, rand, cx, cz, ox, oz,
                rand.nextDouble() * 16 + ox * 16,
                rand.nextDouble() * 35 + 5,
                rand.nextDouble() * 16 + oz * 16,
                rand.nextDouble() * Math.PI * 2,
                rand.nextDouble() * 20 + 40,
                0,
                rand.nextDouble() * 0.2 - 0.1,
                rand.nextDouble() + 4,
                rand.nextInt( 10 ) + 30,
                0,
                null,
                data.getHeightmap()
            );
        }
    }

    /**
     * Creates a canyon
     *
     * @param world     The world
     * @param rand      A random number generator with the seed for this canyon
     * @param cx        The chunk x we're generating
     * @param cz        The chunk z we're generating
     * @param ox        The chunk x the canyon is at
     * @param oz        The chunk z the canyon is at
     * @param x         The canyon's root x
     * @param y         The canyon's root y
     * @param z         The canyon's root z
     * @param direction The canyon's direction
     * @param length    The canyon's length (amount of columns)
     * @param progress  The canyon's progress (usually 0 except in recursion cases)
     * @param bending   The canyon's bending factor
     * @param width     The canyon's width
     * @param height    The canyon's column height
     * @param recursion The current split recursion
     * @param radiuses  The canyon's radius generator. Creates a new one when null.
     */
    protected void addCanyon( IWorld world, Random rand, int cx, int cz, int ox, int oz, double x, double y, double z, double direction, double length, double progress, double bending, double width, int height, int recursion, INoise3D radiuses, int[] heightmap ) {
        if( radiuses == null ) {
            radiuses = new FractalPerlin3D( rand.nextInt(), RADIUS_NOISE_SCALE, 4 );
        }

        MovingBlockPos mpos = new MovingBlockPos();

        int lenI = (int) length;
        int start = (int) ( length * progress );

        int nx = cx * 16;
        int nz = cz * 16;
        int px = cx * 16 + 16;
        int pz = cz * 16 + 16;

        int minX = nx - 16;
        int maxX = px + 16;
        int minZ = nz - 16;
        int maxZ = pz + 16;

        double split = recursion == 0 && rand.nextInt( 4 ) == 0 ? rand.nextDouble() * 0.5 + 0.5 : - 1;

        // Generate columns
        for( int i = start; i < lenI; i++ ) {
            progress = i / length;

            double prg = progress * 0.7 + 0.15;
            double w = ( Math.sin( prg * Math.PI ) * 0.65 + 0.35 ) * width;

            // Estimate if we're going to place any block in this column to skip a lot of useless calculations
            if( x - w >= minX && x + w <= maxX && z - w >= minZ && z + w <= maxZ ) {

                // Carve spheres
                for( int h = 0; h < height; h++ ) {
                    double by = y + h;

                    double r = ( radiuses.generate( x, by, z ) + 1 ) / 2 * 0.5 + 0.5;
                    double radius = r * w;
                    int radI = (int) ( radius + 1 );

                    // Too detailed to generate, don't generate
                    if( radius < RADIUS_DETAIL_THRESHOLD ) continue;

                    // Check if we can generate a sphere
                    boolean canGenerate = true;
                    checkLoop:
                    for( int lx = - radI; lx <= radI; lx++ ) {
                        for( int ly = - radI; ly <= radI; ly++ ) {
                            for( int lz = - radI; lz <= radI; lz++ ) {
                                if( lx * lx + ly * ly + lz * lz < radius * radius ) {
                                    mpos.setPos( x, by, z );
                                    mpos.addPos( lx, ly, lz );

                                    int vx = mpos.getX() - nx;
                                    int vy = mpos.getY();
                                    int vz = mpos.getZ() - nz;

                                    if( vx >= 0 && vx < 16 && vz >= 0 && vz < 16 ) {
                                        if( vy > heightmap[ vx + vz * 16 ] - 16 ) {
                                            canGenerate = false;
                                            break checkLoop;
                                        }
                                    }

                                    canGenerate = checkPosition( mpos, world, cx, cz );
                                    if( ! canGenerate ) break checkLoop;
                                }
                            }
                        }
                    }

                    if( canGenerate ) {

                        // Generate the sphere
                        for( int lx = - radI; lx <= radI; lx++ ) {
                            for( int ly = - radI; ly <= radI; ly++ ) {
                                for( int lz = - radI; lz <= radI; lz++ ) {
                                    if( lx * lx + ly * ly + lz * lz < radius * radius ) {
                                        mpos.setPos( x, by, z );
                                        mpos.addPos( lx, ly, lz );

                                        if( mpos.getX() >= nx && mpos.getX() < px && mpos.getZ() >= nz && mpos.getZ() < pz && mpos.getY() >= 0 && mpos.getY() < 256 ) {
                                            replaceBlock( mpos, world );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Advance X and Z coords
            double sin = Math.sin( direction ) * 1.5;
            double cos = Math.cos( direction ) * 1.5;

            x += sin;
            z += cos;

            direction += bending;

            if( split >= 0 && progress > split ) {
                addCanyon( world, rand, cx, cz, ox, oz, x, y, z, direction, length, progress, bending * ( 1 + rand.nextDouble() ), width, height, recursion + 1, radiuses, heightmap );
                addCanyon( world, rand, cx, cz, ox, oz, x, y, z, direction, length, progress, - bending * ( 1 + rand.nextDouble() ), width, height, recursion + 1, radiuses, heightmap );
                break;
            }
        }
    }

    protected boolean hasCanyon( int cx, int cz ) {
        int rx = cx >> 3;
        int rz = cz >> 3;

        long seed = rx * 512735912384L + rz * 63249818L ^ world.getSeed();
        Random rand = new Random( seed );

        int count = 1;
        if( rand.nextInt( 3 ) == 0 ) count++;
        if( rand.nextInt( 15 ) == 0 ) count++;

        for( int i = 0; i < count; i++ ) {
            int randX = rand.nextInt( 8 );
            int randZ = rand.nextInt( 8 );

            int chX = ( rx << 3 ) + randX;
            int chZ = ( rz << 3 ) + randZ;

            if( chX == cx && chZ == cz ) {
                return true;
            }
        }

        return false;
    }

    protected boolean checkPosition( MovingBlockPos pos, IWorld world, int cx, int cz ) {
        if( ! world.isAreaLoaded( pos, 2 ) ) {
            return false;
        }
        int y = pos.getY();
        for( Direction dir : Direction.values() ) {
            pos.move( dir, 1 );

            if( pos.getX() < cx * 16 || pos.getX() > cx * 16 + 15 ) {
                pos.move( dir, - 1 );
                continue;
            }

            if( pos.getZ() < cz * 16 || pos.getZ() > cz * 16 + 15 ) {
                pos.move( dir, - 1 );
                continue;
            }

            BlockState bstate = world.getBlockState( pos );
            IFluidState fstate = bstate.getFluidState();

            if( y > MurkSurfaceGeneration.CAVE_WATER_LEVEL && ! ( dir == Direction.DOWN || fstate.isEmpty() ) ) {
                return false;
            } else if( y <= MurkSurfaceGeneration.CAVE_WATER_LEVEL && ! ( dir == Direction.UP || fstate.getFluid() == MDFluids.MURKY_WATER || bstate.getMaterial().blocksMovement() ) ) {
                return false;
            }

            pos.move( dir, - 1 );
        }
        return true;
    }

    protected void replaceBlock( MovingBlockPos pos, IWorld world ) {
        if( world.getBlockState( pos ).getBlockHardness( world, pos ) > 1000 ) {
            return;
        }
        if( pos.getY() <= MurkSurfaceGeneration.CAVE_WATER_LEVEL ) {
            world.setBlockState( pos, MDNatureBlocks.MURKY_WATER.getDefaultState(), 2 );
        } else {
            world.setBlockState( pos, MDNatureBlocks.CAVE_AIR.getDefaultState(), 2 );
        }
    }

    protected double[] generateRadiuses( Random rand, int size ) {
        double[] radiuses = new double[ size ];
        for( int i = 0; i < size; i++ ) {
            radiuses[ i ] = rand.nextDouble() * 0.5 + 0.5;
        }
        return radiuses;
    }
}
