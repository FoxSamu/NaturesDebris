package modernity.common.area;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.storage.RegionFileCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AreaReferenceManager extends RegionFileCache {
    private static final Logger LOGGER = LogManager.getLogger();

    private final HashMap<ChunkPos, AreaReferenceChunk> loadedReferenceChunks = new HashMap<>();

    protected AreaReferenceManager( File dir ) {
        super( dir );
    }

    public AreaReferenceChunk getLoadedChunk( int x, int z ) {
        return loadedReferenceChunks.get( new ChunkPos( x, z ) );
    }

    private AreaReferenceChunk loadChunk( int x, int z ) {
        try {
            CompoundNBT nbt = readChunk( new ChunkPos( x, z ) );
            AreaReferenceChunk chunk = new AreaReferenceChunk( x, z );
            if( nbt != null ) {
                chunk.read( nbt );
            }
            loadedReferenceChunks.put( new ChunkPos( x, z ), chunk );
            return chunk;
        } catch( IOException exc ) {
            CrashReport report = CrashReport.makeCrashReport( exc, "Loading area reference chunk" );
            report.makeCategory( "Area reference chunk" )
                  .addDetail( "Chunk pos", new ChunkPos( x, z ) );
            throw new ReportedException( report );
        }
    }

    public AreaReferenceChunk getChunk( int x, int z ) {
        AreaReferenceChunk chunk = getLoadedChunk( x, z );
        if( chunk == null ) {
            chunk = loadChunk( x, z );
        }
        return chunk;
    }

    private void saveChunk( int x, int z ) {
        AreaReferenceChunk saving = getLoadedChunk( x, z );
        if( saving == null ) {
            LOGGER.warn( "Attempted to save unloaded chunk" );
            return;
        }
        CompoundNBT nbt = new CompoundNBT();
        saving.write( nbt );
        try {
            writeChunk( new ChunkPos( x, z ), nbt );
        } catch( IOException exc ) {
            CrashReport report = CrashReport.makeCrashReport( exc, "Saving area reference chunk" );
            report.makeCategory( "Area reference chunk" )
                  .addDetail( "Chunk pos", new ChunkPos( x, z ) );
            throw new ReportedException( report );
        }
    }

    public boolean isLoaded( int x, int z ) {
        return loadedReferenceChunks.containsKey( new ChunkPos( x, z ) );
    }

    public void load( int x, int z ) {
        if( isLoaded( x, z ) ) return;
        loadChunk( x, z );
    }

    public void unload( int x, int z ) {
        if( ! isLoaded( x, z ) ) return;
        saveChunk( x, z );
        loadedReferenceChunks.remove( new ChunkPos( x, z ) );
    }

    public void saveAll() {
        for( ChunkPos pos : loadedReferenceChunks.keySet() ) {
            saveChunk( pos.x, pos.z );
        }
        LOGGER.info( "Saved all reference chunks" );
    }

    public void unloadAll() {
        saveAll();
        loadedReferenceChunks.clear();
    }
}
