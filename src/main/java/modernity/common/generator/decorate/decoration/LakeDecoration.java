/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDNatureBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.gen.ChunkGenerator;

import java.util.Random;

public class LakeDecoration implements IDecoration {
    private static final BlockState CAVE_AIR = MDNatureBlocks.CAVE_AIR.getDefaultState();

    private static final ThreadLocal<boolean[]> LAKE_DATA = ThreadLocal.withInitial( () -> new boolean[ 2048 ] );

    private final Block fluid;
    private final Block sides;
    private final Block cover;
    private final Block replaceDirt;

    public LakeDecoration( Block fluid, Block sides, Block cover, Block replaceDirt ) {
        this.fluid = fluid;
        this.sides = sides;
        this.cover = cover;
        this.replaceDirt = replaceDirt;
    }

    public Block getFluid() {
        return fluid;
    }

    public Block getSides() {
        return sides;
    }

    public Block getCover() {
        return cover;
    }

    public Block getReplaceDirt() {
        return replaceDirt;
    }

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        MovingBlockPos rpos = new MovingBlockPos( pos );
        while( rpos.getY() > 5 && world.isAirBlock( rpos ) ) {
            rpos.moveDown();
        }
        pos = rpos.toImmutable();

        if( pos.getY() > 4 ) {
            pos = pos.down( 4 );

            boolean[] lakeData = LAKE_DATA.get();

            for( int i = 0; i < 2048; i++ ) lakeData[ i ] = false;

            int iterations = rand.nextInt( 4 ) + 4;

            for( int i = 0; i < iterations; ++ i ) {
                double sizeX = rand.nextDouble() * 6 + 3;
                double sizeY = rand.nextDouble() * 4 + 2;
                double sizeZ = rand.nextDouble() * 6 + 3;
                double sphereX = rand.nextDouble() * ( 16 - sizeX - 2 ) + 1 + sizeX / 2;
                double sphereY = rand.nextDouble() * ( 8 - sizeY - 4 ) + 2 + sizeY / 2;
                double sphereZ = rand.nextDouble() * ( 16 - sizeZ - 2 ) + 1 + sizeZ / 2;

                for( int x = 1; x < 15; ++ x ) {
                    for( int z = 1; z < 15; ++ z ) {
                        for( int y = 1; y < 7; ++ y ) {
                            double lx = ( x - sphereX ) / ( sizeX / 2 );
                            double ly = ( y - sphereY ) / ( sizeY / 2 );
                            double lz = ( z - sphereZ ) / ( sizeZ / 2 );

                            double radius = lx * lx + ly * ly + lz * lz;
                            if( radius < 1 ) {
                                lakeData[ ( x * 16 + z ) * 8 + y ] = true;
                            }
                        }
                    }
                }
            }

            for( int x = 0; x < 16; ++ x ) {
                for( int z = 0; z < 16; ++ z ) {
                    for( int y = 0; y < 8; ++ y ) {
                        int i = ( x * 16 + z ) * 8 + y;
                        boolean lake = ! lakeData[ i ] && (
                            x < 15 && lakeData[ ( ( x + 1 ) * 16 + z ) * 8 + y ]
                                || x > 0 && lakeData[ ( ( x - 1 ) * 16 + z ) * 8 + y ]
                                || z < 15 && lakeData[ ( x * 16 + z + 1 ) * 8 + y ]
                                || z > 0 && lakeData[ ( x * 16 + z - 1 ) * 8 + y ]
                                || y < 7 && lakeData[ ( x * 16 + z ) * 8 + y + 1 ]
                                || y > 0 && lakeData[ i - 1 ]
                        );
                        if( lake ) {
                            rpos.setPos( pos );
                            rpos.addPos( x, y, z );
                            Material mat = world.getBlockState( rpos ).getMaterial();

                            if( y >= 4 && mat.isLiquid() ) {
                                return;
                            }

                            if( y < 4 && ! mat.isSolid() && mat != Material.LEAVES && world.getBlockState( rpos ).getBlock() != fluid ) {
                                return;
                            }
                        }
                    }
                }
            }

            for( int x = 0; x < 16; ++ x ) {
                for( int z = 0; z < 16; ++ z ) {
                    for( int y = 0; y < 8; ++ y ) {
                        if( lakeData[ ( x * 16 + z ) * 8 + y ] ) {
                            rpos.setPos( pos );
                            rpos.addPos( x, y, z );

                            world.setBlockState( rpos, y >= 4 ? CAVE_AIR : fluid.getDefaultState(), 2 );
                        }
                    }
                }
            }

            if( replaceDirt != null ) {
                MovingBlockPos tpos = new MovingBlockPos();
                {
                    for( int x = 0; x < 16; ++ x ) {
                        for( int z = 0; z < 16; ++ z ) {
                            for( int y = 4; y < 8; ++ y ) {
                                if( lakeData[ ( x * 16 + z ) * 8 + y ] ) {
                                    rpos.setPos( pos );
                                    rpos.addPos( x, y, z );
                                    tpos.setPos( rpos );
                                    tpos.moveDown();
                                    if( world.getBlockState( tpos ).getBlock() == MDNatureBlocks.MURKY_DIRT && world.getLightFor( LightType.SKY, rpos ) > 0 ) {
                                        world.setBlockState( tpos, replaceDirt.getDefaultState(), 2 );
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if( sides != null ) {
                for( int x = 0; x < 16; ++ x ) {
                    for( int z = 0; z < 16; ++ z ) {
                        for( int y = 0; y < 8; ++ y ) {
                            int i = ( x * 16 + z ) * 8 + y;
                            boolean lake = ! lakeData[ i ] && (
                                x < 15 && lakeData[ ( ( x + 1 ) * 16 + z ) * 8 + y ]
                                    || x > 0 && lakeData[ ( ( x - 1 ) * 16 + z ) * 8 + y ]
                                    || z < 15 && lakeData[ ( x * 16 + z + 1 ) * 8 + y ]
                                    || z > 0 && lakeData[ ( x * 16 + z - 1 ) * 8 + y ]
                                    || y < 7 && lakeData[ ( x * 16 + z ) * 8 + y + 1 ]
                                    || y > 0 && lakeData[ i - 1 ]
                            );
                            rpos.setPos( pos );
                            rpos.addPos( x, y, z );
                            if( lake && ( y < 4 || rand.nextInt( 2 ) != 0 ) && world.getBlockState( rpos ).getMaterial().isSolid() ) {
                                world.setBlockState( rpos, sides.getDefaultState(), 2 );
                            }
                        }
                    }
                }
            }

            if( cover != null ) {
                for( int x = 0; x < 16; ++ x ) {
                    for( int z = 0; z < 16; ++ z ) {
                        rpos.setPos( pos );
                        rpos.addPos( x, 4, z );
                        world.setBlockState( rpos, cover.getDefaultState(), 2 );
                    }
                }
            }
        }
    }
}
