package modernity.common.area.core;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.stream.Stream;

public interface IWorldAreaManager {
    World getWorld();

    void tick();

    Area getLoadedArea( long reference );
    boolean isAreaLoaded( long reference );
    Stream<Area> streamAreas();

    IAreaReferenceChunk getLoadedChunk( int x, int z );
    IAreaReferenceChunk getChunk( int x, int z );
    boolean isChunkLoadedAt( int x, int z );

    default Stream<Area> getAreasAt( int x, int y, int z ) {
        int cx = x >> 4;
        int cz = z >> 4;

        IAreaReferenceChunk chunk = getLoadedChunk( cx, cz );
        if( chunk != null ) {
            return chunk.referenceStream()
                        .mapToObj( this::getLoadedArea )
                        .filter( Objects::nonNull )
                        .filter( area -> area.getBox().contains( x, y, z ) );
        }
        return Stream.empty();
    }

    default Stream<Area> getAreasAt( BlockPos pos ) {
        return getAreasAt( pos.getX(), pos.getY(), pos.getZ() );
    }
}
