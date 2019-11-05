package modernity.common.area.core;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.*;
import org.apache.commons.lang3.Validate;

public class AreaBox {
    public final int minX;
    public final int minY;
    public final int minZ;
    public final int maxX;
    public final int maxY;
    public final int maxZ;

    public AreaBox( int minX, int minY, int minZ, int maxX, int maxY, int maxZ ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public AreaBox( int[] array ) {
        Validate.isTrue( array.length >= 6, "Array size too small, must at least have 6 items" );
        this.minX = array[ 0 ];
        this.minY = array[ 1 ];
        this.minZ = array[ 2 ];
        this.maxX = array[ 3 ];
        this.maxY = array[ 4 ];
        this.maxZ = array[ 5 ];
    }

    public AreaBox( CompoundNBT nbt, String key ) {
        this( nbt.getIntArray( key ) );
    }

    public void serialize( CompoundNBT nbt, String key ) {
        nbt.putIntArray( key, toIntArray() );
    }

    public int[] toIntArray() {
        return new int[] { minX, minY, minZ, maxX, maxY, maxZ };
    }

    public int computeRegionX() {
        return minX >> 9;
    }

    public int computeRegionZ() {
        return minZ >> 9;
    }

    public boolean intersectsChunk( ChunkPos pos ) {
        int nx = pos.x * 16;
        int nz = pos.z * 16;
        int px = nx + 16;
        int pz = nz + 16;

        return nx < maxX && px > minX && nz < maxX && pz > minZ;
    }

    public boolean contains( int x, int y, int z ) {
        return x >= minX && x < maxX && y >= minY && y < maxY && z >= minZ && z < maxZ;
    }

    public boolean contains( double x, double y, double z ) {
        return contains( MathHelper.floor( x ), MathHelper.floor( y ), MathHelper.floor( z ) );
    }

    public boolean contains( float x, float y, float z ) {
        return contains( MathHelper.floor( x ), MathHelper.floor( y ), MathHelper.floor( z ) );
    }

    public boolean contains( Vec3i pos ) {
        if( pos == null ) return false;
        return contains( pos.getX(), pos.getY(), pos.getZ() );
    }

    public boolean contains( Vec3d vec ) {
        if( vec == null ) return false;
        return contains( vec.x, vec.y, vec.z );
    }

    public boolean contains( Entity e ) {
        if( e == null ) return false;
        return contains( e.posX, e.getBoundingBox().minY, e.posZ );
    }

    public boolean intersects( AreaBox other ) {
        return other.maxX >= minX && other.minX <= maxX
                   && other.maxY >= minY && other.minX <= maxY
                   && other.maxZ >= minZ && other.minX <= maxZ;
    }

    public boolean intersects( MutableBoundingBox other ) {
        return other.maxX >= minX && other.minX <= maxX
                   && other.maxY >= minY && other.minX <= maxY
                   && other.maxZ >= minZ && other.minX <= maxZ;
    }

    public boolean intersects( AxisAlignedBB other ) {
        return other.maxX >= minX && other.minX <= maxX
                   && other.maxY >= minY && other.minX <= maxY
                   && other.maxZ >= minZ && other.minX <= maxZ;
    }

    public int getXSize() {
        return maxX - minX;
    }

    public int getYSize() {
        return maxY - minY;
    }

    public int getZSize() {
        return maxZ - minZ;
    }

    public int getVolume() {
        return getXSize() * getYSize() * getZSize();
    }

    public int getMinChunkX() {
        return minX >> 4;
    }

    public int getMaxChunkX() {
        return ( maxX - 1 >> 4 ) + 1;
    }

    public int getMinChunkZ() {
        return minZ >> 4;
    }

    public int getMaxChunkZ() {
        return ( maxZ - 1 >> 4 ) + 1;
    }

    public AxisAlignedBB toAABB() {
        return new AxisAlignedBB( minX, minY, minZ, maxX, maxY, maxZ );
    }

    public MutableBoundingBox toMutableBB() { // Add 1 as the upper limit is inclusive in MBB (ours is exclusive)
        return new MutableBoundingBox( minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1 );
    }

    public static AreaBox fromMutableBB( MutableBoundingBox box ) {
        return new AreaBox( box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ );
    }

    public static AreaBox wrapAABB( AxisAlignedBB box ) {
        return new AreaBox(
            MathHelper.floor( box.minX ), MathHelper.floor( box.minY ), MathHelper.floor( box.minZ ),
            MathHelper.ceil( box.maxX ), MathHelper.ceil( box.maxY ), MathHelper.ceil( box.maxZ )
        );
    }
}
