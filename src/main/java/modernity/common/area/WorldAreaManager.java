package modernity.common.area;

import net.minecraft.util.math.ChunkPos;

import java.io.File;
import java.util.HashMap;

public class WorldAreaManager {
    private final File rootFile;

    private final HashMap<ChunkPos, AreaReferenceChunk> references = new HashMap<>();
    private final HashMap<ChunkPos, AreaMappingRegion> mappings = new HashMap<>();

    public WorldAreaManager( File rootFile ) {
        this.rootFile = rootFile;
    }

    public File getRootFile() {
        return rootFile;
    }

    public File getFileAt( String dir ) {
        return new File( rootFile, dir );
    }

    public AreaReferenceChunk getLoadedReferenceRegion( int x, int z ) {
        return references.get( new ChunkPos( x, z ) );
    }

    public AreaMappingRegion getLoadedMappingRegion( int x, int z ) {
        return mappings.get( new ChunkPos( x, z ) );
    }

}
