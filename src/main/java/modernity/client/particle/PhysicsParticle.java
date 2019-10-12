/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.client.particle;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.stream.Stream;

@OnlyIn( Dist.CLIENT )
public abstract class PhysicsParticle extends SpriteTexturedParticle {
    protected double bounciness;
    protected double weight;
    protected double airFriction = 0.02;
    protected final IAnimatedSprite sprite;

    protected PhysicsParticle( World world, double x, double y, double z, IAnimatedSprite sprite ) {
        super( world, x, y, z );
        this.sprite = sprite;
    }

    public PhysicsParticle( World world, double x, double y, double z, double xv, double yv, double zv, IAnimatedSprite sprite ) {
        super( world, x, y, z, xv, yv, zv );
        this.sprite = sprite;
    }

    protected static double collide( Direction.Axis axis, AxisAlignedBB box, Stream<VoxelShape> shapeStream, double velocity ) {
        return VoxelShapes.getAllowedOffset( axis, box, shapeStream, velocity );
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
        BlockState state = world.getBlockState( pos );
        IFluidState fstate = world.getFluidState( pos );
        boolean floating = false;
        if( ! fstate.isEmpty() ) {
            double height = fstate.func_215679_a( world, pos );
            if( world.getBlockState( pos.up() ).getBlock() == state.getBlock() ) height = 1;
            if( posY % 1 < height ) {
                gravMult -= weight;
                floating = true;
            }
        }

        motionY -= 0.04 * particleGravity * gravMult;
        move( motionX, motionY, motionZ );
        double fr = 1 - airFriction;
        motionX *= fr;
        motionY *= fr;
        motionZ *= fr;
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

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move( double x, double y, double z ) {
        double origX = x;
        double origY = y;
        double origZ = z;
        if( canCollide && ( x != 0 || y != 0 || z != 0 ) ) {
            Vec3d movement = Entity.func_223307_a( null, new Vec3d( x, y, z ), getBoundingBox(), world, ISelectionContext.dummy(), new ReuseableStream<>( Stream.empty() ) );
            x = movement.x;
            y = movement.y;
            z = movement.z;
        }

        if( x != 0 || y != 0 || z != 0 ) {
            setBoundingBox( getBoundingBox().offset( x, y, z ) );
            resetPositionToBB();
        }

        if( origX != x ) {
            motionX = origX * - bounciness;
        }

        if( origZ != z ) {
            motionZ = origZ * - bounciness;
        }

        if( origY != y ) {
            motionY = origY * - bounciness;
        }

        onGround = origY != y && origY < 0;
    }
}
