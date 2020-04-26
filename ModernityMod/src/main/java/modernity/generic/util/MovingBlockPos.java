/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.generic.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

/**
 * More user friendly version of {@link Mutable}
 */
public class MovingBlockPos extends BlockPos.Mutable {

    public MovingBlockPos() {
    }

    public MovingBlockPos( BlockPos pos ) {
        super( pos );
    }

    public MovingBlockPos( int x, int y, int z ) {
        super( x, y, z );
    }

    public MovingBlockPos origin() {
        return setPos( 0, 0, 0 );
    }

    /**
     * Move down by 1
     */
    public MovingBlockPos moveDown() {
        return move( Direction.DOWN, 1 );
    }

    /**
     * Move up by 1
     */
    public MovingBlockPos moveUp() {
        return move( Direction.UP, 1 );
    }

    /**
     * Move east by 1
     */
    public MovingBlockPos moveEast() {
        return move( Direction.EAST, 1 );
    }

    /**
     * Move west by 1
     */
    public MovingBlockPos moveWest() {
        return move( Direction.WEST, 1 );
    }

    /**
     * Move north by 1
     */
    public MovingBlockPos moveNorth() {
        return move( Direction.NORTH, 1 );
    }

    /**
     * Move south by 1
     */
    public MovingBlockPos moveSouth() {
        return move( Direction.SOUTH, 1 );
    }

    /**
     * Move down by specified offset
     */
    public MovingBlockPos moveDown( int offset ) {
        return move( Direction.DOWN, offset );
    }

    /**
     * Move up by specified offset
     */
    public MovingBlockPos moveUp( int offset ) {
        return move( Direction.UP, offset );
    }

    /**
     * Move east by specified offset
     */
    public MovingBlockPos moveEast( int offset ) {
        return move( Direction.EAST, offset );
    }

    /**
     * Move west by specified offset
     */
    public MovingBlockPos moveWest( int offset ) {
        return move( Direction.WEST, offset );
    }

    /**
     * Move north by specified offset
     */
    public MovingBlockPos moveNorth( int offset ) {
        return move( Direction.NORTH, offset );
    }

    /**
     * Move south by specified offset
     */
    public MovingBlockPos moveSouth( int offset ) {
        return move( Direction.SOUTH, offset );
    }

    /**
     * Adds a value to this block pos
     */
    public MovingBlockPos addPos( Vec3i pos ) {
        this.x += pos.getX();
        this.y += pos.getY();
        this.z += pos.getZ();
        return this;
    }

    /**
     * Adds a value to this block pos
     */
    public MovingBlockPos addPos( int x, int y, int z ) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Copies this instance
     */
    public MovingBlockPos copy() {
        return new MovingBlockPos( this );
    }

    /**
     * Sets the position of this block pos
     */
    @Override
    public MovingBlockPos setPos( Vec3i vec ) {
        return (MovingBlockPos) super.setPos( vec );
    }

    /**
     * Sets the position of this block pos to the position of an entity
     */
    @Override
    public MovingBlockPos setPos( Entity entity ) {
        return (MovingBlockPos) super.setPos( entity );
    }

    /**
     * Sets the position of this block pos
     */
    @Override
    public MovingBlockPos setPos( int xIn, int yIn, int zIn ) {
        return (MovingBlockPos) super.setPos( xIn, yIn, zIn );
    }

    /**
     * Sets the position of this block pos by rounding the specified position
     */
    @Override
    public MovingBlockPos setPos( double xIn, double yIn, double zIn ) {
        return (MovingBlockPos) super.setPos( xIn, yIn, zIn );
    }

    /**
     * Moves this blockpos by 1 in the specified direction
     */
    @Override
    public MovingBlockPos move( Direction facing ) {
        return (MovingBlockPos) super.move( facing );
    }

    /**
     * Moves this blockpos by the specified amount in the specified direction
     */
    @Override
    public MovingBlockPos move( Direction facing, int n ) {
        return (MovingBlockPos) super.move( facing, n );
    }

    /**
     * Sets the y value of this block pos
     */
    public MovingBlockPos setYPos( int y ) {
        this.y = y;
        return this;
    }

    /**
     * Sets the x value of this block pos
     */
    public MovingBlockPos setXPos( int x ) {
        this.x = x;
        return this;
    }

    /**
     * Sets the z value of this block pos
     */
    public MovingBlockPos setZPos( int z ) {
        this.z = z;
        return this;
    }
}
