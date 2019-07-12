/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 12 - 2019
 */

package modernity.client.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Particles;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.api.block.IParticleShapeBlock;

import java.util.Random;

@OnlyIn( Dist.CLIENT )
public class MDParticleManager extends ParticleManager {
    private final Random rand = new Random();


    public MDParticleManager( World world, TextureManager renderer ) {
        super( world, renderer );

        this.registerFactory( Particles.BLOCK, new ParticleDiggingModernity.Factory() );
    }

    @Override
    public void addBlockDestroyEffects( BlockPos pos, IBlockState state ) {
        if( ! state.isAir( this.world, pos ) ) {
            VoxelShape shape;
            if( state.getBlock() instanceof IParticleShapeBlock ) {
                shape = ( (IParticleShapeBlock) state.getBlock() ).getParticleShape( state, world, pos );
            } else {
                shape = state.getShape( world, pos );
            }
            shape.forEachBox( ( minx, miny, minz, maxx, maxy, maxz ) -> {
                double sx = Math.min( 1, maxx - minx );
                double sy = Math.min( 1, maxy - miny );
                double sz = Math.min( 1, maxz - minz );
                int xsize = Math.max( 2, MathHelper.ceil( sx / 0.25 ) );
                int ysize = Math.max( 2, MathHelper.ceil( sy / 0.25 ) );
                int zsize = Math.max( 2, MathHelper.ceil( sz / 0.25 ) );

                for( int x = 0; x < xsize; ++ x ) {
                    for( int y = 0; y < ysize; ++ y ) {
                        for( int z = 0; z < zsize; ++ z ) {
                            double xv = ( x + 0.5 ) / xsize;
                            double yv = ( y + 0.5 ) / ysize;
                            double zv = ( z + 0.5 ) / zsize;
                            double xoff = xv * sx + minx;
                            double yoff = yv * sy + miny;
                            double zoff = zv * sz + minz;
                            addEffect( new ParticleDiggingModernity( world, pos.getX() + xoff, pos.getY() + yoff, pos.getZ() + zoff, xv - 0.5, yv - 0.5, zv - 0.5, state ).setBlockPos( pos ) );
                        }
                    }
                }

            } );
        }
    }

    public void addBlockHitEffects( BlockPos pos, EnumFacing side ) {
        IBlockState state = world.getBlockState( pos );
        if( state.getRenderType() != EnumBlockRenderType.INVISIBLE ) {
            int ix = pos.getX();
            int iy = pos.getY();
            int iz = pos.getZ();
            AxisAlignedBB box = state.getShape( world, pos ).getBoundingBox();
            double x = ix + rand.nextDouble() * ( box.maxX - box.minX - 0.2 ) + 0.1 + box.minX;
            double y = iy + rand.nextDouble() * ( box.maxY - box.minY - 0.2 ) + 0.1 + box.minY;
            double z = iz + rand.nextDouble() * ( box.maxZ - box.minZ - 0.2 ) + 0.1 + box.minZ;
            if( side == EnumFacing.DOWN ) {
                y = iy + box.minY - 0.1;
            }

            if( side == EnumFacing.UP ) {
                y = iy + box.maxY + 0.1;
            }

            if( side == EnumFacing.NORTH ) {
                z = iz + box.minZ - 0.1;
            }

            if( side == EnumFacing.SOUTH ) {
                z = iz + box.maxZ + 0.1;
            }

            if( side == EnumFacing.WEST ) {
                x = ix + box.minX - 0.1;
            }

            if( side == EnumFacing.EAST ) {
                x = ix + box.maxX + 0.1;
            }

            addEffect( new ParticleDiggingModernity( world, x, y, z, 0, 0, 0, state ).setBlockPos( pos ).multiplyVelocity( 0.2F ).multipleParticleScaleBy( 0.6F ) );
        }
    }

    @OnlyIn( Dist.CLIENT )
    public static class Factory implements IParticleFactory<BlockParticleData> {
        public Particle makeParticle( BlockParticleData data, World world, double x, double y, double z, double xv, double yv, double zv ) {
            IBlockState state = data.getBlockState();
            return ! state.isAir() && state.getBlock() != Blocks.MOVING_PISTON ? new ParticleDiggingModernity( world, x, y, z, xv, yv, zv, state ).init() : null;
        }
    }
}
