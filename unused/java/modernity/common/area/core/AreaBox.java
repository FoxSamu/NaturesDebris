/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.area.core;

import com.google.gson.*;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3i;
import net.redgalaxy.exc.UnexpectedCaseException;
import net.redgalaxy.util.MathUtil;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;

/**
 * Represents a three-dimensional {@code int} bounding box. An area box is assigned to an {@link Area} to indicate it's
 * hard limits. The box of an {@link Area} is used to compute which chunks must reference the area and which region it's
 * stored. All chunks that intersect the box of an {@link Area} will get a reference (see {@link #intersectsChunk}).
 * <p/>
 */
public class AreaBox {
    public static final AreaBox NULL = new AreaBox( 0, 0, 0, 0, 0, 0 );
    public static final AreaBox UNIT = new AreaBox( 0, 0, 0, 1, 1, 1 );

    public final int minX;
    public final int minY;
    public final int minZ;
    public final int maxX;
    public final int maxY;
    public final int maxZ;

    private final int hashCode;

    /**
     * Creates an area box with the specified bounds
     *
     * @param minX Lower X limit (inclusive)
     * @param minY Lower Y limit (inclusive)
     * @param minZ Lower Z limit (inclusive)
     * @param maxX Upper X limit (exclusive)
     * @param maxY Upper Y limit (exclusive)
     * @param maxZ Upper Z limit (exclusive)
     */
    public AreaBox( int minX, int minY, int minZ, int maxX, int maxY, int maxZ ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        hashCode = computeHash();
    }

    /**
     * Creates an area box with the specified bounds
     *
     * @param min Lower limit (inclusive)
     * @param max Upper limit (exclusive)
     */
    public AreaBox( Vec3i min, Vec3i max ) {
        this.minX = min.getX();
        this.minY = min.getY();
        this.minZ = min.getZ();
        this.maxX = max.getX();
        this.maxY = max.getY();
        this.maxZ = max.getZ();
        hashCode = computeHash();
    }

    /**
     * Creates an area box that reads it's bounds from the specified array. This array must have at least 6 elements.
     *
     * @param array The bounds array. Must not be null and must have at least 6 elements.
     * @throws NullPointerException     When the specified array is null
     * @throws IllegalArgumentException When the specified array has less than 6 elements.
     */
    public AreaBox( int[] array ) {
        Validate.notNull( array );
        Validate.isTrue( array.length >= 6, "Array size too small, must at least have 6 items" );
        this.minX = array[ 0 ];
        this.minY = array[ 1 ];
        this.minZ = array[ 2 ];
        this.maxX = array[ 3 ];
        this.maxY = array[ 4 ];
        this.maxZ = array[ 5 ];
        hashCode = computeHash();
    }

    /**
     * Serializes this box to an {@linkplain IntArrayNBT integer array tag} NBT and stores it in the specified
     * {@linkplain CompoundNBT compound tag}.
     *
     * @param nbt The {@linkplain CompoundNBT compound tag} to serialize to
     * @param key The key to save the box at
     * @see #deserialize(CompoundNBT, String)
     */
    public void serialize( CompoundNBT nbt, String key ) {
        nbt.putIntArray( key, toIntArray() );
    }

    /**
     * Returns the region X where an {@link Area} with this box should be stored in.
     */
    public int computeRegionX() {
        return minX >> 9;
    }

    /**
     * Returns the region Z where an {@link Area} with this box should be stored in.
     */
    public int computeRegionZ() {
        return minZ >> 9;
    }

    /**
     * Returns true when the boundary of a chunk with the specified {@link ChunkPos} intersects this box.
     *
     * @param pos The position of the chunk
     * @return True when the chunk boundary intersects this box, false otherwise
     *
     * @see #makeChunkBox(ChunkPos)
     */
    public boolean intersectsChunk( ChunkPos pos ) {
        int nx = pos.x * 16;
        int nz = pos.z * 16;
        int px = nx + 16;
        int pz = nz + 16;

        return nx < maxX && px > minX && nz < maxX && pz > minZ;
    }

    /**
     * Checks if the specified coordinates are contained by this box.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @return True if the coordinates are contained by this box.
     */
    public boolean contains( int x, int y, int z ) {
        return x >= minX && x < maxX && y >= minY && y < maxY && z >= minZ && z < maxZ;
    }

    /**
     * Checks if the specified coordinates are contained by this box.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @return True if the coordinates are contained by this box.
     */
    public boolean contains( double x, double y, double z ) {
        return contains( MathHelper.floor( x ), MathHelper.floor( y ), MathHelper.floor( z ) );
    }

    /**
     * Checks if the specified coordinates are contained by this box.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @return True if the coordinates are contained by this box.
     */
    public boolean contains( float x, float y, float z ) {
        return contains( MathHelper.floor( x ), MathHelper.floor( y ), MathHelper.floor( z ) );
    }

    /**
     * Checks if the specified location is contained by this box.
     *
     * @param pos The location
     * @return True if the coordinates are contained by this box.
     */
    public boolean contains( Vec3i pos ) {
        if( pos == null ) return false;
        return contains( pos.getX(), pos.getY(), pos.getZ() );
    }

    /**
     * Checks if the specified location is contained by this box.
     *
     * @param vec The location
     * @return True if the coordinates are contained by this box.
     */
    public boolean contains( Vec3d vec ) {
        if( vec == null ) return false;
        return contains( vec.x, vec.y, vec.z );
    }


    /**
     * Checks if the location of the specified {@link Entity} is contained by this box.
     *
     * @param e The entity
     * @return True if the entity is inside this box.
     */
    public boolean contains( Entity e ) {
        if( e == null ) return false;
        return contains( e.posX, e.getBoundingBox().minY, e.posZ );
    }

    /**
     * Checks whether this box and the specified box hit or intersect each other.
     *
     * @param other The other box
     * @return True if the two boxes intersect or hit each other
     */
    public boolean intersects( AreaBox other ) {
        return other.maxX >= minX && other.minX <= maxX
                   && other.maxY >= minY && other.minX <= maxY
                   && other.maxZ >= minZ && other.minX <= maxZ;
    }

    /**
     * Checks whether this box and the specified box hit or intersect each other.
     *
     * @param other The other box
     * @return True if the two boxes intersect or hit each other
     */
    public boolean intersects( MutableBoundingBox other ) {
        return other.maxX + 1 >= minX && other.minX <= maxX
                   && other.maxY + 1 >= minY && other.minX <= maxY
                   && other.maxZ + 1 >= minZ && other.minX <= maxZ;
    }

    /**
     * Checks whether this box and the specified box hit or intersect each other.
     *
     * @param other The other box
     * @return True if the two boxes intersect or hit each other
     */
    public boolean intersects( AxisAlignedBB other ) {
        return other.maxX >= minX && other.minX <= maxX
                   && other.maxY >= minY && other.minX <= maxY
                   && other.maxZ >= minZ && other.minX <= maxZ;
    }

    /**
     * Checks whether this box and the specified entity hit or intersect each other.
     *
     * @param e The entity
     * @return True if the box and entity intersect or hit each other
     */
    public boolean intersects( Entity e ) {
        return intersects( e.getBoundingBox() );
    }

    /**
     * Returns the size of this box over the X axis.
     */
    public int getXSize() {
        return maxX - minX;
    }

    /**
     * Returns the size of this box over the Y axis.
     */
    public int getYSize() {
        return maxY - minY;
    }

    /**
     * Returns the size of this box over the Z axis.
     */
    public int getZSize() {
        return maxZ - minZ;
    }

    /**
     * Returns the volume of this box.
     */
    public int getVolume() {
        return getXSize() * getYSize() * getZSize();
    }

    /**
     * Computes the lowest X coordinate of all the chunks that intersect this box.
     */
    public int getMinChunkX() {
        return minX >> 4;
    }

    /**
     * Computes the highest X coordinate of all the chunks that intersect this box.
     */
    public int getMaxChunkX() {
        return ( maxX - 1 >> 4 ) + 1;
    }

    /**
     * Computes the lowest Z coordinate of all the chunks that intersect this box.
     */
    public int getMinChunkZ() {
        return minZ >> 4;
    }

    /**
     * Computes the highest Z coordinate of all the chunks that intersect this box.
     */
    public int getMaxChunkZ() {
        return ( maxZ - 1 >> 4 ) + 1;
    }

    /**
     * Shifts this area by the specified offsets
     *
     * @param x The X offset
     * @param y The Y offset
     * @param z The Z offset
     * @return A new, shifted instance
     */
    public AreaBox shift( int x, int y, int z ) {
        return new AreaBox( minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z );
    }

    /**
     * Shifts this area by the specified offset vector
     *
     * @param v The offset vector
     * @return A new, shifted instance
     */
    public AreaBox shift( Vec3i v ) {
        return shift( v.getX(), v.getY(), v.getZ() );
    }

    /**
     * Inflates this area by the specified offsets
     *
     * @param x The X offset
     * @param y The Y offset
     * @param z The Z offset
     * @return A new, inflated instance
     */
    public AreaBox grow( int x, int y, int z ) {
        return new AreaBox( minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z );
    }

    /**
     * Inflates this area by the specified offset
     *
     * @param size The offset
     * @return A new, inflated instance
     */
    public AreaBox grow( int size ) {
        return grow( size, size, size );
    }

    /**
     * Inflates this area by the specified offset vector
     *
     * @param vec The offset vector
     * @return A new, inflated instance
     */
    public AreaBox grow( Vec3i vec ) {
        return grow( vec.getX(), vec.getY(), vec.getZ() );
    }

    /**
     * Deflates this area by the specified offsets
     *
     * @param x The X offset
     * @param y The Y offset
     * @param z The Z offset
     * @return A new, deflated instance
     */
    public AreaBox shrink( int x, int y, int z ) {
        return grow( - x, - y, - z );
    }

    /**
     * Deflates this area by the specified offset
     *
     * @param size The offset
     * @return A new, deflated instance
     */
    public AreaBox shrink( int size ) {
        return grow( - size );
    }

    /**
     * Deflates this area by the specified offset vector
     *
     * @param vec The offset vector
     * @return A new, deflated instance
     */
    public AreaBox shrink( Vec3i vec ) {
        return grow( - vec.getX(), - vec.getY(), - vec.getZ() );
    }

    /**
     * Extends this area by the specified offsets. Positive offsets are added to the upper limit and negative offsets to
     * the lower limit.
     *
     * @param x The X offset
     * @param y The Y offset
     * @param z The Z offset
     * @return A new, extended instance
     */
    public AreaBox extend( int x, int y, int z ) {
        return new AreaBox(
            minX + Math.min( 0, x ),
            minY + Math.min( 0, y ),
            minZ + Math.min( 0, z ),
            maxX + Math.max( 0, x ),
            maxY + Math.max( 0, y ),
            maxZ + Math.max( 0, z )
        );
    }

    /**
     * Extends this area by the specified offset vector. Positive offsets are added to the upper limit and negative
     * offsets to the lower limit.
     *
     * @param vec The offset vector
     * @return A new, extended instance
     */
    public AreaBox extend( Vec3i vec ) {
        return extend( vec.getX(), vec.getY(), vec.getZ() );
    }

    /**
     * Computes the intersecting region of this box and the other.
     *
     * @param other The other box
     * @return The intersecting region of both boxes
     *
     * @see #intersection(AreaBox, AreaBox)
     */
    public AreaBox intersection( AreaBox other ) {
        return intersection( this, other );
    }

    /**
     * Computes the smallest region that contains both this box and the other box (unite region).
     *
     * @param other The other box
     * @return The unite region of both boxes
     *
     * @see #union(AreaBox, AreaBox)
     */
    public AreaBox union( AreaBox other ) {
        return union( this, other );
    }

    /**
     * Returns a copy of this box that is always {@linkplain #isLegal() legal}.
     *
     * @see #makeLegal(int, int, int, int, int, int)
     * @see #makeLegal(int[])
     */
    public AreaBox makeLegal() {
        return makeLegal( minX, minY, minZ, maxX, maxY, maxZ );
    }

    /**
     * Checks whether this box is 'legal'. This means that each lower bound is less than or equal it's respective upper
     * bound.
     *
     * @return True when the specified box is legal.
     */
    public boolean isLegal() {
        return minX <= maxX && minY <= maxY && minZ <= maxZ;
    }

    /**
     * Checks whether this box is not 'legal'. This means that one or more lower bounds are more than their's respective
     * upper bound.
     *
     * @return True when the specified box is legal.
     */
    public boolean isIllegal() {
        return ! isLegal();
    }

    /**
     * Returns the lower bound as a {@link Vec3i} instance.
     */
    public Vec3i getMin() {
        return new Vec3i( minX, minY, minZ );
    }

    /**
     * Returns the upper bound as a {@link Vec3i} instance.
     */
    public Vec3i getMax() {
        return new Vec3i( maxX, maxY, maxZ );
    }

    /**
     * Computes the box-local position from the global position
     *
     * @param global The global block pos
     * @return The local block pos
     */
    public BlockPos getLocal( BlockPos global ) {
        return global.add( - minX, - minY, - minZ );
    }

    /**
     * Computes the global position from the box-local position
     *
     * @param local The local block pos
     * @return The global block pos
     */
    public BlockPos getGlobal( BlockPos local ) {
        return local.add( minX, minY, minZ );
    }

    /**
     * Computes the shortest manhattan distance between the specified point and a point in this box.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The z coordinate
     * @return The shortest manhattan distance
     */
    public double getManhattanDistance( double x, double y, double z ) {
        if( contains( x, y, z ) ) return 0;
        double xDist = Math.max( x < minX ? minX - x : x - maxX, 0 );
        double yDist = Math.max( y < minY ? minY - y : y - minY, 0 );
        double zDist = Math.max( z < minZ ? minZ - z : z - maxZ, 0 );
        return xDist + yDist + zDist;
    }

    /**
     * Computes the shortest euler distance between the specified point and a point in this box.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The z coordinate
     * @return The shortest distance
     */
    public double getDistance( double x, double y, double z ) {
        if( contains( x, y, z ) ) return 0;
        double clampX = MathUtil.clamp( x, minX, maxX );
        double clampY = MathUtil.clamp( y, minY, maxY );
        double clampZ = MathUtil.clamp( z, minZ, maxZ );
        double distX = x - clampX;
        double distY = y - clampY;
        double distZ = z - clampZ;
        return Math.sqrt( distX * distX + distY * distY + distZ * distZ );
    }

    @Override
    public String toString() {
        return String.format( "AreaBox[%d, %d, %d -- %d, %d, %d]", minX, minY, minZ, maxX, maxY, maxZ );
    }

    @Override
    public boolean equals( Object obj ) {
        return obj instanceof AreaBox && equals( (AreaBox) obj );
    }

    /**
     * Checks if this area box is equal to the specified other box.
     *
     * @param other The other box
     * @return True if the two boxes are equal, false otherwise
     *
     * @see #equals(AreaBox, AreaBox)
     */
    public boolean equals( AreaBox other ) {
        return equals( this, other );
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    protected AreaBox clone() {
        return new AreaBox( minX, minY, minZ, maxX, maxY, maxZ );
    }

    private int computeHash() {
        int result = 1;
        result = 31 * result + minX;
        result = 31 * result + minY;
        result = 31 * result + minZ;
        result = 31 * result + maxX;
        result = 31 * result + maxY;
        result = 31 * result + maxZ;
        return result;
    }

    /**
     * Creates a new {@link AxisAlignedBB} with the same bounds as this box.
     *
     * @return The created {@link AxisAlignedBB}
     */
    public AxisAlignedBB toAABB() {
        return new AxisAlignedBB( minX, minY, minZ, maxX, maxY, maxZ );
    }

    /**
     * Creates a new {@link MutableBoundingBox} with the same bounds as this box. Note that the upper limits of a {@link
     * MutableBoundingBox} are inclusive while our's are exclusive. Therefore we subtract one from each upper coordinate
     * to make them inclusive.
     *
     * @return The created {@link MutableBoundingBox}
     */
    public MutableBoundingBox toMutableBB() {
        return new MutableBoundingBox( minX, minY, minZ, maxX - 1, maxY - 1, maxZ - 1 );
    }

    /**
     * Creates an integer array ({@code int[]}) with the bounds of this box.
     *
     * @return The created array
     */
    public int[] toIntArray() {
        return new int[] { minX, minY, minZ, maxX, maxY, maxZ };
    }

    /**
     * Creates a JSON array with 6 integers holding the bounds of this box.
     *
     * @return The created JSON array
     */
    public JsonArray toJSONArray() {
        JsonArray array = new JsonArray();
        array.add( minX );
        array.add( minY );
        array.add( minZ );
        array.add( maxX );
        array.add( maxY );
        array.add( maxZ );
        return array;
    }

    /**
     * Creates a JSON object with holding the bounds of this box, with keys in the format {@code [min/max][X/Y/Z]}.
     *
     * @return The created JSON object.
     */
    public JsonObject toJSONObject() {
        JsonObject object = new JsonObject();
        object.addProperty( "minX", minX );
        object.addProperty( "minY", minY );
        object.addProperty( "minZ", minZ );
        object.addProperty( "maxX", maxX );
        object.addProperty( "maxY", maxY );
        object.addProperty( "maxZ", maxZ );
        return object;
    }


    /**
     * Creates a box from the specified, all inclusive bounds.
     *
     * @param minX Lower X limit (inclusive)
     * @param minY Lower Y limit (inclusive)
     * @param minZ Lower Z limit (inclusive)
     * @param maxX Upper X limit (inclusive)
     * @param maxY Upper Y limit (inclusive)
     * @param maxZ Upper Z limit (inclusive)
     * @return The created box
     */
    public static AreaBox makeInclusive( int minX, int minY, int minZ, int maxX, int maxY, int maxZ ) {
        return new AreaBox(
            minX, minY, minZ,
            maxX + 1, maxY + 1, maxZ + 1
        );
    }

    /**
     * Creates a box from the specified, all inclusive bounds.
     *
     * @param min Lower limit (inclusive)
     * @param max Upper limit (inclusive)
     * @return The created box
     */
    public static AreaBox makeInclusive( Vec3i min, Vec3i max ) {
        return new AreaBox(
            min.getX(), min.getY(), min.getZ(),
            max.getX() + 1, max.getY() + 1, max.getZ() + 1
        );
    }

    /**
     * Creates a box from the specified, all inclusive bounds.
     *
     * @param array The array storing the bounds. Must not be null and must have at least 6 elements.
     * @return The created box
     *
     * @throws NullPointerException     When the specified array is null.
     * @throws IllegalArgumentException When the specified array has less than 6 elements.
     */
    public static AreaBox makeInclusive( int[] array ) {
        Validate.notNull( array );
        Validate.isTrue( array.length >= 6, "Array size too small, must at least have 6 items" );
        return new AreaBox(
            array[ 0 ],
            array[ 1 ],
            array[ 2 ],
            array[ 3 ] + 1,
            array[ 4 ] + 1,
            array[ 5 ] + 1
        );
    }

    /**
     * Creates a box from the specified, all exclusive bounds.
     *
     * @param minX Lower X limit (exclusive)
     * @param minY Lower Y limit (exclusive)
     * @param minZ Lower Z limit (exclusive)
     * @param maxX Upper X limit (exclusive)
     * @param maxY Upper Y limit (exclusive)
     * @param maxZ Upper Z limit (exclusive)
     * @return The created box
     */
    public static AreaBox makeExclusive( int minX, int minY, int minZ, int maxX, int maxY, int maxZ ) {
        return new AreaBox(
            minX + 1, minY + 1, minZ + 1,
            maxX, maxY, maxZ
        );
    }

    /**
     * Creates a box from the specified, all exclusive bounds.
     *
     * @param array The array storing the bounds. Must not be null and must have at least 6 elements.
     * @return The created box
     *
     * @throws NullPointerException     When the specified array is null.
     * @throws IllegalArgumentException When the specified array has less than 6 elements.
     */
    public static AreaBox makeExclusive( int[] array ) {
        Validate.isTrue( array.length >= 6, "Array size too small, must at least have 6 items" );
        return new AreaBox(
            array[ 0 ] + 1,
            array[ 1 ] + 1,
            array[ 2 ] + 1,
            array[ 3 ],
            array[ 4 ],
            array[ 5 ]
        );
    }

    /**
     * Creates a box from the specified, all exclusive bounds.
     *
     * @param min Lower limit (exclusive)
     * @param max Upper limit (exclusive)
     * @return The created box
     */
    public static AreaBox makeExclusive( Vec3i min, Vec3i max ) {
        return new AreaBox(
            min.getX() + 1, min.getY() + 1, min.getZ() + 1,
            max.getX(), max.getY(), max.getZ()
        );
    }

    /**
     * Creates a box from the specified {@link MutableBoundingBox}. Note that the bounds of {@link MutableBoundingBox}
     * are all inclusive.
     *
     * @param box The {@link MutableBoundingBox} to create this box from.
     * @return The created box
     */
    public static AreaBox makeFromMutableBB( MutableBoundingBox box ) {
        return makeInclusive( box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ );
    }

    /**
     * Computes the smallest possible box that fully contains the specified {@link AxisAlignedBB}.
     *
     * @param box The {@link AxisAlignedBB} to create this box around.
     * @return The created box
     */
    public static AreaBox makeAroundAABB( AxisAlignedBB box ) {
        return new AreaBox(
            MathHelper.floor( box.minX ), MathHelper.floor( box.minY ), MathHelper.floor( box.minZ ),
            MathHelper.ceil( box.maxX ), MathHelper.ceil( box.maxY ), MathHelper.ceil( box.maxZ )
        );
    }

    /**
     * Computes the largest possible box that is fully contained by the specified {@link AxisAlignedBB}.
     *
     * @param box The {@link AxisAlignedBB} to create this box in.
     * @return The created box
     */
    public static AreaBox makeInsideAABB( AxisAlignedBB box ) {
        return new AreaBox(
            MathHelper.ceil( box.minX ), MathHelper.ceil( box.minY ), MathHelper.ceil( box.minZ ),
            MathHelper.floor( box.maxX ), MathHelper.floor( box.maxY ), MathHelper.floor( box.maxZ )
        );
    }

    /**
     * Makes a {@linkplain #isLegal() legal} box from the specified bounds.
     *
     * @param minX The lower X
     * @param minY The lower Y
     * @param minZ The lower Z
     * @param maxX The upper X
     * @param maxY The upper Y
     * @param maxZ The upper Z
     * @return A legal box with the specified bounds.
     */
    public static AreaBox makeLegal( int minX, int minY, int minZ, int maxX, int maxY, int maxZ ) {
        return new AreaBox(
            Math.min( minX, maxX ),
            Math.min( minY, maxY ),
            Math.min( minZ, maxZ ),
            Math.max( minX, maxX ),
            Math.max( minY, maxY ),
            Math.max( minZ, maxZ )
        );
    }

    /**
     * Makes a {@linkplain #isLegal() legal} box from the specified bounds.
     *
     * @param array An array containing the bounds. Must have at least 6 elements and must not be null.
     * @return A legal box with the specified bounds.
     *
     * @throws NullPointerException     When the specified array is null.
     * @throws IllegalArgumentException When the specified array has less than 6 elements.
     */
    public static AreaBox makeLegal( int[] array ) {
        Validate.notNull( array );
        Validate.isTrue( array.length >= 6, "Array size too small, must at least have 6 items" );
        return new AreaBox(
            Math.min( array[ 0 ], array[ 3 ] ),
            Math.min( array[ 1 ], array[ 4 ] ),
            Math.min( array[ 2 ], array[ 5 ] ),
            Math.max( array[ 0 ], array[ 3 ] ),
            Math.max( array[ 1 ], array[ 4 ] ),
            Math.max( array[ 2 ], array[ 5 ] )
        );
    }

    /**
     * Makes a box from local coordinates and orients it horizontally around the specified origin.
     *
     * @param originX The origin X
     * @param originY The origin Y
     * @param originZ The origin Z
     * @param x1      The lower X bound
     * @param y1      The lower Y bound
     * @param z1      The lower Z bound
     * @param x2      The upper X bound
     * @param y2      The upper Y bound
     * @param z2      The upper Z bound
     * @param dir     The horizontal orientation of the box, where {@link Direction#EAST} is the root direction.
     * @return A new oriented box
     */
    public static AreaBox makeOrientedBox( int originX, int originY, int originZ, int x1, int y1, int z1, int x2, int y2, int z2, Direction dir ) {
        switch( dir ) {
            default:
            case EAST:
                return new AreaBox( originX + x1, originY + y1, originZ + z1, originX + x2, originY + y2, originZ + z2 );
            case WEST:
                return new AreaBox( originX - x2, originY + y1, originZ - z2, originX - x1, originY + y2, originZ - z1 );
            case NORTH:
                return new AreaBox( originX + z1, originY + y1, originZ - x2, originX + z2, originY + y2, originZ - x1 );
            case SOUTH:
                return new AreaBox( originX - z2, originY + y1, originZ + x1, originX - z1, originY + y2, originZ + x2 );
        }
    }

    /**
     * Makes a box from local coordinates and orients it horizontally around the specified origin.
     *
     * @param originX The origin X
     * @param originY The origin Y
     * @param originZ The origin Z
     * @param x       The lower X bound
     * @param y       The lower Y bound
     * @param z       The lower Z bound
     * @param xSize   The size of the box along the X axis
     * @param ySize   The size of the box along the Y axis
     * @param zSize   The size of the box along the Z axis
     * @param dir     The horizontal orientation of the box, where {@link Direction#EAST} is the root direction.
     * @return A new oriented box
     */
    public static AreaBox makeOrientedWithSize( int originX, int originY, int originZ, int x, int y, int z, int xSize, int ySize, int zSize, Direction dir ) {
        return makeOrientedBox( originX, originY, originZ, x, y, z, x + xSize, y + ySize, z + zSize, dir );
    }

    /**
     * Makes a box from the chunk boundary of a chunk at the specified location.
     *
     * @param pos The location of the chunk.
     * @return The created box
     */
    public static AreaBox makeChunkBox( ChunkPos pos ) {
        return new AreaBox(
            pos.x << 4, 0, pos.z << 4,
            ( pos.x << 4 ) + 16, 256, ( pos.z << 4 ) + 16
        );
    }

    /**
     * Makes a box from the chunk section boundary of a section at the specified location.
     *
     * @param pos The location of the chunk section.
     * @return The created box
     */
    public static AreaBox makeSectionBox( SectionPos pos ) {
        return new AreaBox(
            pos.getX() << 4,
            pos.getY() << 4,
            pos.getZ() << 4,
            ( pos.getX() << 4 ) + 16,
            ( pos.getY() << 4 ) + 16,
            ( pos.getZ() << 4 ) + 16
        );
    }

    /**
     * Makes a box with the specified lower bounds and the specified dimensions. When one or more sizes are negative,
     * the box will be {@linkplain #isIllegal() illegal}.
     *
     * @param x     The lower X bound
     * @param y     The lower Y bound
     * @param z     The lower Z bound
     * @param xSize The size along X axis
     * @param ySize The size along Y axis
     * @param zSize The size along Z axis
     * @return The created box.
     */
    public static AreaBox makeWithSize( int x, int y, int z, int xSize, int ySize, int zSize ) {
        return new AreaBox( x, y, z, x + xSize, y + ySize, z + zSize );
    }

    /**
     * Makes a box with the specified lower bounds and a size of 1 along each axis.
     *
     * @param x The lower X bound
     * @param y The lower Y bound
     * @param z The lower Z bound
     * @return The created unit box
     */
    public static AreaBox makeUnitBox( int x, int y, int z ) {
        return UNIT.shift( x, y, z );
    }

    /**
     * Makes a box with the specified lower bounds and a size of 1 along each axis.
     *
     * @param pos The lower bounds
     * @return The created unit box
     */
    public static AreaBox makeUnitBox( Vec3i pos ) {
        return makeUnitBox( pos.getX(), pos.getY(), pos.getZ() );
    }

    /**
     * Returns the intersecting area of the two specified boxes. This area is {@linkplain #isIllegal() illegal}, when
     * the two boxes don't intersect.
     *
     * @param a Box A
     * @param b Box B
     * @return The intersecting region of both boxes.
     */
    public static AreaBox intersection( AreaBox a, AreaBox b ) {
        return new AreaBox(
            Math.max( a.minX, b.minX ),
            Math.max( a.minY, b.minY ),
            Math.max( a.minZ, b.minZ ),
            Math.min( a.maxX, b.maxX ),
            Math.min( a.maxY, b.maxY ),
            Math.min( a.maxZ, b.maxZ )
        );
    }

    /**
     * Returns the unite area of the two specified boxes. This area is {@linkplain #isIllegal() illegal}, when one of
     * the two boxes is illegal.
     *
     * @param a Box A
     * @param b Box B
     * @return The unite region of both boxes.
     */
    public static AreaBox union( AreaBox a, AreaBox b ) {
        return new AreaBox(
            Math.min( a.minX, b.minX ),
            Math.min( a.minY, b.minY ),
            Math.min( a.minZ, b.minZ ),
            Math.max( a.maxX, b.maxX ),
            Math.max( a.maxY, b.maxY ),
            Math.max( a.maxZ, b.maxZ )
        );
    }

    /**
     * Returns true when the two specified boxes are equal.
     *
     * @param a Box A
     * @param b Box B
     * @return True if the two boxes are equal.
     */
    public static boolean equals( AreaBox a, AreaBox b ) {
        return a.minX == b.minX && a.minY == b.minY && a.minZ == b.minZ
                   && a.maxX == b.maxX && a.maxY == b.maxY && a.maxZ == b.maxZ;
    }

    /**
     * Deserializes from an int array tag at the specified key in the specified {@link CompoundNBT}.
     *
     * @param nbt The {@link CompoundNBT} to deserialize from
     * @param key The key of the tag to serialize
     * @return The deserialized area box
     */
    public static AreaBox deserialize( CompoundNBT nbt, String key ) {
        return new AreaBox( nbt.getIntArray( key ) );
    }

    /**
     * Deserializes from a JSON element. When the element is {@link JsonNull}, the {@linkplain #NULL null box} is
     * returned. When the element is {@link JsonArray}, it will load 6 numbers from it and uses these as bounds. When
     * the element is {@link JsonObject}, it will load the bounds from keys such as {@code minX}, {@code Z2}, etc. When
     * the element is {@link JsonPrimitive}, it throws a {@link JsonSyntaxException}.
     *
     * @param json The JSON element to deserialize.
     * @return The deserialized box.
     *
     * @throws JsonSyntaxException     If the given JSON is not in the right format to deserialize.
     * @throws UnexpectedCaseException When the {@link JsonElement} instance is not one of {@link JsonNull}, {@link
     *                                 JsonArray}, {@link JsonObject} or {@link JsonPrimitive}.
     */
    public static AreaBox deserialize( JsonElement json ) {
        Validate.notNull( json );
        if( json instanceof JsonPrimitive )
            throw new JsonSyntaxException( "Required NULL, ARRAY or OBJECT. Found PRIMITIVE." );
        if( json instanceof JsonNull ) return NULL;
        if( json instanceof JsonArray ) {
            JsonArray array = (JsonArray) json;
            if( array.size() < 6 ) {
                throw new JsonSyntaxException( "Box array must have 6 numbers" );
            }
            int[] box = new int[ 6 ];
            for( int i = 0; i < 6; i++ ) {
                JsonElement element = array.get( i );
                if( ! element.isJsonPrimitive() || ! element.getAsJsonPrimitive().isNumber() ) {
                    throw new JsonSyntaxException( "Box array must have 6 numbers" );
                }
                box[ i ] = array.get( i ).getAsInt();
            }
            return new AreaBox( box );
        }
        if( json instanceof JsonObject ) {
            JsonObject object = (JsonObject) json;
            return new AreaBox(
                getInt( object, "minX", "minx", "x1", "X1" ),
                getInt( object, "minY", "miny", "y1", "Y1" ),
                getInt( object, "minZ", "minz", "z1", "Z1" ),
                getInt( object, "maxX", "maxx", "x2", "X2" ),
                getInt( object, "maxY", "maxy", "y2", "Y2" ),
                getInt( object, "maxZ", "maxz", "z2", "Z2" )
            );
        }
        throw new UnexpectedCaseException( "JSON element was not a primitive, null, array or object?! What's going on..." );
    }

    private static int getInt( JsonObject object, String... keys ) {
        for( String k : keys ) {
            if( object.has( k ) ) {
                JsonElement element = object.get( k );
                if( element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber() ) {
                    return element.getAsInt();
                }
            }
        }
        throw new JsonSyntaxException( "Required number for at least one of " + Arrays.toString( keys ) );
    }
}
