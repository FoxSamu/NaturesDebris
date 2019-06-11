package modernity.common.world.gen.decorate.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import modernity.api.util.EcoBlockPos;
import modernity.common.block.MDBlocks;

import java.util.Random;

public class LakeFeature extends Feature<LakeFeature.Config> {
    private static final IBlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();

    @Override
    public boolean place( IWorld world, IChunkGenerator generator, Random rand, BlockPos pos, Config config ) {
        while( pos.getY() > 5 && world.isAirBlock( pos ) ) {
            pos = pos.down();
        }

        if( pos.getY() <= 4 ) {
            return false;
        } else {
            pos = pos.down( 4 );
            try( EcoBlockPos rpos = EcoBlockPos.retain() ) {
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
                            boolean lake = ! lakeData[ ( x * 16 + z ) * 8 + y ] && ( x < 15 && lakeData[ ( ( x + 1 ) * 16 + z ) * 8 + y ] || x > 0 && lakeData[ ( ( x - 1 ) * 16 + z ) * 8 + y ] || z < 15 && lakeData[ ( x * 16 + z + 1 ) * 8 + y ] || z > 0 && lakeData[ ( x * 16 + z - 1 ) * 8 + y ] || y < 7 && lakeData[ ( x * 16 + z ) * 8 + y + 1 ] || y > 0 && lakeData[ ( x * 16 + z ) * 8 + y - 1 ] );
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
                    try( EcoBlockPos tpos = EcoBlockPos.retain() ) {
                        for( int x = 0; x < 16; ++ x ) {
                            for( int z = 0; z < 16; ++ z ) {
                                for( int y = 4; y < 8; ++ y ) {
                                    if( lakeData[ ( x * 16 + z ) * 8 + y ] ) {
                                        rpos.setPos( pos );
                                        rpos.addPos( x, y, z );
                                        tpos.setPos( rpos );
                                        tpos.moveDown();
                                        if( world.getBlockState( tpos ).getBlock() == MDBlocks.DARK_DIRT && world.getLightFor( EnumLightType.SKY, rpos ) > 0 ) {
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
                                boolean lake = ! lakeData[ ( x * 16 + z ) * 8 + y ] && ( x < 15 && lakeData[ ( ( x + 1 ) * 16 + z ) * 8 + y ] || x > 0 && lakeData[ ( ( x - 1 ) * 16 + z ) * 8 + y ] || z < 15 && lakeData[ ( x * 16 + z + 1 ) * 8 + y ] || z > 0 && lakeData[ ( x * 16 + z - 1 ) * 8 + y ] || y < 7 && lakeData[ ( x * 16 + z ) * 8 + y + 1 ] || y > 0 && lakeData[ ( x * 16 + z ) * 8 + y - 1 ] );
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
                            if( world.getBiome( rpos ).doesWaterFreeze( world, rpos, false ) ) {
                                world.setBlockState( rpos, config.cover.getDefaultState(), 2 );
                            }
                        }
                    }
                }

                return true;
            }
        }
    }

    public static class Config implements IFeatureConfig {
        public final Block fluid;
        public final Block sides;
        public final Block cover;
        public final Block replaceGrass;

        public Config( Block fluid, Block sides, Block cover, Block replaceGrass ) {
            this.fluid = fluid;
            this.sides = sides;
            this.cover = cover;
            this.replaceGrass = replaceGrass;
        }
    }
}
