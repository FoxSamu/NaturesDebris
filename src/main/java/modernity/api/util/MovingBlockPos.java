/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 8 - 2019
 */

package modernity.api.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class MovingBlockPos extends BlockPos.MutableBlockPos {

    public MovingBlockPos() {
    }

    public MovingBlockPos( BlockPos pos ) {
        super( pos );
    }

    public MovingBlockPos( int x, int y, int z ) {
        super( x, y, z );
    }

    public MovingBlockPos moveDown() {
        return move( EnumFacing.DOWN, 1 );
    }

    public MovingBlockPos moveUp() {
        return move( EnumFacing.UP, 1 );
    }

    public MovingBlockPos moveEast() {
        return move( EnumFacing.EAST, 1 );
    }

    public MovingBlockPos moveWest() {
        return move( EnumFacing.WEST, 1 );
    }

    public MovingBlockPos moveNorth() {
        return move( EnumFacing.NORTH, 1 );
    }

    public MovingBlockPos moveSouth() {
        return move( EnumFacing.SOUTH, 1 );
    }

    public MovingBlockPos moveDown( int offset ) {
        return move( EnumFacing.DOWN, offset );
    }

    public MovingBlockPos moveUp( int offset ) {
        return move( EnumFacing.UP, offset );
    }

    public MovingBlockPos moveEast( int offset ) {
        return move( EnumFacing.EAST, offset );
    }

    public MovingBlockPos moveWest( int offset ) {
        return move( EnumFacing.WEST, offset );
    }

    public MovingBlockPos moveNorth( int offset ) {
        return move( EnumFacing.NORTH, offset );
    }

    public MovingBlockPos moveSouth( int offset ) {
        return move( EnumFacing.SOUTH, offset );
    }

    public MovingBlockPos addPos( BlockPos pos ) {
        this.x += pos.getX();
        this.y += pos.getY();
        this.z += pos.getZ();
        return this;
    }

    public MovingBlockPos addPos( int x, int y, int z ) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public MovingBlockPos copy() {
        return new MovingBlockPos( this );
    }

    @Override
    public MovingBlockPos setPos( Vec3i vec ) {
        return (MovingBlockPos) super.setPos( vec );
    }

    @Override
    public MovingBlockPos setPos( Entity entityIn ) {
        return (MovingBlockPos) super.setPos( entityIn );
    }

    @Override
    public MovingBlockPos setPos( int xIn, int yIn, int zIn ) {
        return (MovingBlockPos) super.setPos( xIn, yIn, zIn );
    }

    @Override
    public MovingBlockPos setPos( double xIn, double yIn, double zIn ) {
        return (MovingBlockPos) super.setPos( xIn, yIn, zIn );
    }

    @Override
    public MovingBlockPos move( EnumFacing facing ) {
        return (MovingBlockPos) super.move( facing );
    }

    @Override
    public MovingBlockPos move( EnumFacing facing, int n ) {
        return (MovingBlockPos) super.move( facing, n );
    }

    public MovingBlockPos setYPos( int y ) {
        this.y = y;
        return this;
    }

    public MovingBlockPos setXPos( int x ) {
        this.x = x;
        return this;
    }

    public MovingBlockPos setZPos( int z ) {
        this.z = z;
        return this;
    }
}
