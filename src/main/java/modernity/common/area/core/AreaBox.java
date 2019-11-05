package modernity.common.area.core;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
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
        nbt.putIntArray( key, new int[] { minX, minY, minZ, maxX, maxY, maxZ } );
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
        return x >= minX && x < maxX && y >= minX && y < maxX && z >= minX && z < maxX;
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
}
