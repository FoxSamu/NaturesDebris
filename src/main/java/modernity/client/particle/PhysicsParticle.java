/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 17 - 2019
 */

package modernity.client.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class PhysicsParticle extends Particle {
    protected double bounciness;
    protected double weight;

    protected PhysicsParticle( World world, double x, double y, double z ) {
        super( world, x, y, z );
    }

    public PhysicsParticle( World world, double x, double y, double z, double xv, double yv, double zv ) {
        super( world, x, y, z, xv, yv, zv );
    }

    protected static double collide( EnumFacing.Axis axis, AxisAlignedBB box, Stream<VoxelShape> shapeStream, double velocity ) {
        // MCP, please...
        return VoxelShapes.func_212437_a( axis, box, shapeStream, velocity );
    }

    @Override
    public void tick() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if( age++ >= maxAge ) {
            setExpired();
        }

        double weight = 2 - Math.max( Math.min( this.weight, 2 ), 0 );
        double gravMult = 1;
        BlockPos pos = new BlockPos( posX, posY, posZ );
        IBlockState state = world.getBlockState( pos );
        boolean floating = false;
        if( state.getMaterial().isLiquid() ) {
            double height = state.getFluidState().getHeight();
            if( world.getBlockState( pos.up() ).getBlock() == state.getBlock() ) height = 1;
            if( posY % 1 < state.getFluidState().getHeight() ) {
                gravMult -= weight;
                floating = true;
            }
        }

        motionY -= 0.04 * particleGravity * gravMult;
        move( motionX, motionY, motionZ );
        motionX *= 0.98;
        motionY *= 0.98;
        motionZ *= 0.98;
        if( floating ) {
            Vec3d flow = state.getFluidState().getFlow( world, pos ).normalize();
            motionX += flow.x * 0.04;
            motionY += flow.y * 0.04;
            motionZ += flow.z * 0.04;

            onGround = false;
            motionX *= 0.5;
            motionY *= 0.5;
            motionZ *= 0.5;
        }
        if( onGround ) {
            motionX *= 0.7;
            motionZ *= 0.7;
        }
    }

    public void move( double x, double y, double z ) {
        double origX = x;
        double origY = y;
        double origZ = z;

        if( canCollide && ( x != 0 || y != 0 || z != 0 ) ) {
            ReuseableStream<VoxelShape> stream = new ReuseableStream<>( world.getCollisionBoxes( null, getBoundingBox(), x, y, z ) );

            y = collide( EnumFacing.Axis.Y, getBoundingBox(), stream.createStream(), y );
            if( y != 0 ) {
                setBoundingBox( getBoundingBox().offset( 0, y, 0 ) );
            }

            x = collide( EnumFacing.Axis.X, getBoundingBox(), stream.createStream(), x );
            if( x != 0 ) {
                setBoundingBox( getBoundingBox().offset( x, 0, 0 ) );
            }

            z = collide( EnumFacing.Axis.Z, getBoundingBox(), stream.createStream(), z );
            if( z != 0 ) {
                setBoundingBox( getBoundingBox().offset( 0, 0, z ) );
            }
        } else {
            setBoundingBox( getBoundingBox().offset( x, y, z ) );
        }

        this.resetPositionToBB();


        if( origX != x ) {
            this.motionX = origX * - this.bounciness;
        }

        if( origZ != z ) {
            this.motionZ = origZ * - this.bounciness;
        }

        if( origY != y ) {
            this.motionY = origY * - this.bounciness;
        }

        this.onGround = origY != y && origY < 0;
    }
}
