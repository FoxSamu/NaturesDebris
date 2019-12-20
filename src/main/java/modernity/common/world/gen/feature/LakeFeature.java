/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

/**
 * Feature that generates a small pool of a specific fluid, generating any additional blocks around it.
 */
public class LakeFeature extends Feature<LakeFeature.Config> {
    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();

    public LakeFeature() {
        super( dynamic -> new Config( null, null, null, null ) );
    }

    @Override
    public boolean place( IWorld world, ChunkGenerator<?> generator, Random rand, BlockPos pos, Config config ) {
        while( pos.getY() > 5 && world.isAirBlock( pos ) ) {
            pos = pos.down();
        }

        if( pos.getY() <= 4 ) {
            return false;
        } else {
            pos = pos.down( 4 );
            MovingBlockPos rpos = new MovingBlockPos();
            {
                boolean[] lakeData = new boolean[ 2048 ];
                int iterations = rand.nextInt( 4 ) + 4;

                for( int i = 0; i < iterations; ++ i ) {
                    double sizeX = rand.nextDouble() * 6.0D + 3.0D;
                    double sizeY = rand.nextDouble() * 4.0D + 2.0D;
                    double sizeZ = rand.nextDouble() * 6.0D + 3.0D;
                    double sphereX = rand.nextDouble() * ( 16.0D - sizeX - 2.0D ) + 1.0D + sizeX / 2.0D;
                    double sphereY = rand.nextDouble() * ( 8.0D - sizeY - 4.0D ) + 2.0D + sizeY / 2.0D;
                    double sphereZ = rand.nextDouble() * ( 16.0D - sizeZ - 2.0D ) + 1.0D + sizeZ / 2.0D;

                    for( int x = 1; x < 15; ++ x ) {
                        for( int z = 1; z < 15; ++ z ) {
                            for( int y = 1; y < 7; ++ y ) {
                                double lx = ( (double) x - sphereX ) / ( sizeX / 2.0D );
                                double ly = ( (double) y - sphereY ) / ( sizeY / 2.0D );
                                double lz = ( (double) z - sphereZ ) / ( sizeZ / 2.0D );

                                double radius = lx * lx + ly * ly + lz * lz;
                                if( radius < 1.0D ) {
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
                            boolean lake = ! lakeData[ i ] && ( x < 15 && lakeData[ ( ( x + 1 ) * 16 + z ) * 8 + y ] || x > 0 && lakeData[ ( ( x - 1 ) * 16 + z ) * 8 + y ] || z < 15 && lakeData[ ( x * 16 + z + 1 ) * 8 + y ] || z > 0 && lakeData[ ( x * 16 + z - 1 ) * 8 + y ] || y < 7 && lakeData[ ( x * 16 + z ) * 8 + y + 1 ] || y > 0 && lakeData[ i - 1 ] );
                            if( lake ) {
                                rpos.setPos( pos );
                                rpos.addPos( x, y, z );
                                Material mat = world.getBlockState( rpos ).getMaterial();

                                if( y >= 4 && mat.isLiquid() ) {
                                    return false;
                                }

                                if( y < 4 && ! mat.isSolid() && world.getBlockState( rpos ).getBlock() != config.fluid ) {
                                    return false;
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

                                world.setBlockState( rpos, y >= 4 ? CAVE_AIR : config.fluid.getDefaultState(), 2 );
                            }
                        }
                    }
                }

                if( config.replaceGrass != null ) {
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
                                        if( world.getBlockState( tpos ).getBlock() == MDBlocks.MURKY_DIRT && world.getLightFor( LightType.SKY, rpos ) > 0 ) {
                                            world.setBlockState( tpos, config.replaceGrass.getDefaultState(), 2 );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if( config.sides != null ) {
                    for( int x = 0; x < 16; ++ x ) {
                        for( int z = 0; z < 16; ++ z ) {
                            for( int y = 0; y < 8; ++ y ) {
                                int i = ( x * 16 + z ) * 8 + y;
                                boolean lake = ! lakeData[ i ] && ( x < 15 && lakeData[ ( ( x + 1 ) * 16 + z ) * 8 + y ] || x > 0 && lakeData[ ( ( x - 1 ) * 16 + z ) * 8 + y ] || z < 15 && lakeData[ ( x * 16 + z + 1 ) * 8 + y ] || z > 0 && lakeData[ ( x * 16 + z - 1 ) * 8 + y ] || y < 7 && lakeData[ ( x * 16 + z ) * 8 + y + 1 ] || y > 0 && lakeData[ i - 1 ] );
                                rpos.setPos( pos );
                                rpos.addPos( x, y, z );
                                if( lake && ( y < 4 || rand.nextInt( 2 ) != 0 ) && world.getBlockState( rpos ).getMaterial().isSolid() ) {
                                    world.setBlockState( rpos, config.sides.getDefaultState(), 2 );
                                }
                            }
                        }
                    }
                }

                if( config.cover != null ) {
                    for( int x = 0; x < 16; ++ x ) {
                        for( int z = 0; z < 16; ++ z ) {
                            rpos.setPos( pos );
                            rpos.addPos( x, 4, z );
                            world.setBlockState( rpos, config.cover.getDefaultState(), 2 );
                        }
                    }
                }

                return true;
            }
        }
    }

    /**
     * Configuration for the {@link LakeFeature}.
     */
    public static class Config implements IFeatureConfig {
        public final Block fluid;
        public final Block sides;
        public final Block cover;
        public final Block replaceGrass;

        /**
         * Creates a lake configuration.
         * @param fluid        The fluid block to generate in the lake.
         * @param sides        The block to generate at edges. When null, the default block is kept.
         * @param cover        A block to cover the top layer with. When null, no top layer is generated.
         * @param replaceGrass A block to replace top dirt with. When null, dirt is kept dirt.
         */
        public Config( Block fluid, Block sides, Block cover, Block replaceGrass ) {
            this.fluid = fluid;
            this.sides = sides;
            this.cover = cover;
            this.replaceGrass = replaceGrass;
        }

        @Override
        public <T> Dynamic<T> serialize( DynamicOps<T> ops ) {
            return new Dynamic<>( ops );
        }
    }
}
