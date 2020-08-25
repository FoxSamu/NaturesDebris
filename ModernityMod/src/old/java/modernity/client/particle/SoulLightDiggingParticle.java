/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.particle;

// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class SoulLightDiggingParticle extends SoulLightParticle {
//
//    public SoulLightDiggingParticle( World world, double x, double y, double z, double xv, double yv, double zv, SoulLightColor color ) {
//        super(
//            world,
//            x,
//            y,
//            z,
//            ( xv + ( world.rand.nextDouble() - world.rand.nextDouble() ) ) * 0.05F,
//            ( yv + ( world.rand.nextDouble() - world.rand.nextDouble() ) ) * 0.05F,
//            ( zv + ( world.rand.nextDouble() - world.rand.nextDouble() ) ) * 0.05F,
//            color.red,
//            color.green,
//            color.blue,
//            0.2F,
//            0.2F,
//            50 + world.rand.nextInt( 20 ),
//            false
//        );
//    }
//
//    public static void addBlockDestroyEffects( ParticleManager manager, World world, BlockPos pos, BlockState state, SoulLightColor color ) {
//        VoxelShape shape = state.getShape( world, pos );
//
//        shape.forEachBox( ( minX, minY, minZ, maxX, maxY, maxZ ) -> {
//            double xRange = Math.min( 1, maxX - minX );
//            double yRange = Math.min( 1, maxY - minY );
//            double zRange = Math.min( 1, maxZ - minZ );
//            int xCount = Math.max( 2, MathHelper.ceil( xRange * 4 ) );
//            int yCount = Math.max( 2, MathHelper.ceil( yRange * 4 ) );
//            int zCount = Math.max( 2, MathHelper.ceil( zRange * 4 ) );
//
//            for( int xi = 0; xi < xCount; ++ xi ) {
//                for( int yi = 0; yi < yCount; ++ yi ) {
//                    for( int zi = 0; zi < zCount; ++ zi ) {
//                        double lx = ( xi + 0.5 ) / xCount;
//                        double ly = ( yi + 0.5 ) / yCount;
//                        double lz = ( zi + 0.5 ) / zCount;
//
//                        double gx = lx * xRange + minX;
//                        double gy = ly * yRange + minY;
//                        double gz = lz * zRange + minZ;
//
//                        manager.addEffect(
//                            new SoulLightDiggingParticle(
//                                world,
//                                pos.getX() + gx,
//                                pos.getY() + gy,
//                                pos.getZ() + gz,
//                                lx - 0.5,
//                                ly - 0.5,
//                                lz - 0.5,
//                                color
//                            )
//                        );
//
//                        manager.addEffect(
//                            new SoulLightDiggingParticle(
//                                world,
//                                pos.getX() + gx,
//                                pos.getY() + gy,
//                                pos.getZ() + gz,
//                                lx - 0.5,
//                                ly - 0.5,
//                                lz - 0.5,
//                                color
//                            )
//                        );
//                    }
//                }
//            }
//
//        } );
//    }
//
//    public static void addBlockHitEffects( ParticleManager manager, World world, BlockPos pos, Direction side, SoulLightColor color ) {
//        BlockState state = world.getBlockState( pos );
//        if( state.getRenderType() != BlockRenderType.INVISIBLE ) {
//            int x = pos.getX();
//            int y = pos.getY();
//            int z = pos.getZ();
//
//            AxisAlignedBB box = state.getShape( world, pos ).getBoundingBox();
//
//            double px = x + world.rand.nextDouble() * ( box.maxX - box.minX - 0.2 ) + 0.1 + box.minX;
//            double py = y + world.rand.nextDouble() * ( box.maxY - box.minY - 0.2 ) + 0.1 + box.minY;
//            double pz = z + world.rand.nextDouble() * ( box.maxZ - box.minZ - 0.2 ) + 0.1 + box.minZ;
//
//            if( side == Direction.DOWN ) {
//                py = y + box.minY - 0.1;
//            }
//
//            if( side == Direction.UP ) {
//                py = y + box.maxY + 0.1;
//            }
//
//            if( side == Direction.NORTH ) {
//                pz = z + box.minZ - 0.1;
//            }
//
//            if( side == Direction.SOUTH ) {
//                pz = z + box.maxZ + 0.1;
//            }
//
//            if( side == Direction.WEST ) {
//                px = x + box.minX - 0.1;
//            }
//
//            if( side == Direction.EAST ) {
//                px = x + box.maxX + 0.1;
//            }
//
//            manager.addEffect(
//                new SoulLightDiggingParticle(
//                    world,
//                    px,
//                    py,
//                    pz,
//                    0,
//                    0,
//                    0,
//                    color
//                )
//            );
//        }
//    }
//}