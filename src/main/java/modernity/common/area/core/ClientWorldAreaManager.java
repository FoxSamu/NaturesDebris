package modernity.common.area.core;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import modernity.client.ModernityClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.rgsw.exc.UnexpectedCaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@OnlyIn( Dist.CLIENT )
public class ClientWorldAreaManager implements IWorldAreaManager {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ClientWorld world;
    private final Long2ObjectLinkedOpenHashMap<Area> loadedAreas = new Long2ObjectLinkedOpenHashMap<>();
    private final Set<IClientTickableArea> tickableAreas = new HashSet<>();

    private final Long2ObjectOpenHashMap<SimpleAreaReferenceChunk> loadedChunks = new Long2ObjectOpenHashMap<>();

    public ClientWorldAreaManager( ClientWorld world ) {
        this.world = world;
    }

    @Override
    public ClientWorld getWorld() {
        return world;
    }

    @Override
    public void tick() {
        for( IClientTickableArea tickable : tickableAreas ) {
            tickable.tickClient();
        }
    }

    @Override
    public Area getLoadedArea( long reference ) {
        return loadedAreas.get( reference );
    }

    @Override
    public boolean isAreaLoaded( long reference ) {
        return loadedAreas.containsKey( reference );
    }

    @Override
    public Stream<Area> streamAreas() {
        return loadedAreas.values().stream();
    }

    @Override
    public IAreaReferenceChunk getLoadedChunk( int x, int z ) {
        SimpleAreaReferenceChunk chunk = loadedChunks.get( ChunkPos.asLong( x, z ) );
        if( chunk != null ) return chunk.unmodifiable;
        return null;
    }

    @Override
    public IAreaReferenceChunk getChunk( int x, int z ) {
        IAreaReferenceChunk chunk = getLoadedChunk( x, z );
        if( chunk == null ) {
            return new EmptyAreaReferenceChunk( x, z );
        }
        return chunk;
    }

    @Override
    public boolean isChunkLoadedAt( int x, int z ) {
        return loadedChunks.containsKey( ChunkPos.asLong( x, z ) );
    }

    public void receiveAreaUpdate( long refID, CompoundNBT nbt, DimensionType dimen ) {
        if( dimen != world.dimension.getType() ) return;

        Area area = loadedAreas.get( refID );
        if( area == null ) {
            area = Area.deserialize( nbt, refID, world, Area.SerializeType.NETWORK );
            if( area == null ) {
                throw new UnexpectedCaseException( "Unable to deserialize area from packet buffer because of unknown ID..." );
            }
            loadedAreas.put( refID, area );
            if( area instanceof IClientTickableArea ) {
                tickableAreas.add( (IClientTickableArea) area );
            }

            int minX = area.getBox().getMinChunkX();
            int minZ = area.getBox().getMinChunkZ();
            int maxX = area.getBox().getMaxChunkX();
            int maxZ = area.getBox().getMaxChunkZ();
            for( int x = minX; x < maxX; x++ ) {
                for( int z = minZ; z < maxZ; z++ ) {
                    long pos = ChunkPos.asLong( x, z );
                    SimpleAreaReferenceChunk chunk = loadedChunks.get( pos );
                    if( chunk == null ) {
                        chunk = new SimpleAreaReferenceChunk( x, z );
                        loadedChunks.put( pos, chunk );
                    }
                    chunk.addReference( refID );
                }
            }
        } else {
            area.read( nbt, Area.SerializeType.NETWORK );
        }
    }

    public void receiveAreaUnwatch( long refID, DimensionType dimen ) {
        if( dimen != world.dimension.getType() ) return;

        Area area = loadedAreas.remove( refID );
        if( area instanceof IClientTickableArea ) {
            tickableAreas.remove( area );
        }

        int minX = area.getBox().getMinChunkX();
        int minZ = area.getBox().getMinChunkZ();
        int maxX = area.getBox().getMaxChunkX();
        int maxZ = area.getBox().getMaxChunkZ();
        for( int x = minX; x < maxX; x++ ) {
            for( int z = minZ; z < maxZ; z++ ) {
                long pos = ChunkPos.asLong( x, z );
                SimpleAreaReferenceChunk chunk = loadedChunks.get( pos );
                if( chunk == null ) {
                    continue;
                }
                chunk.removeReference( refID );
                if( ! chunk.hasReferences() ) {
                    loadedChunks.remove( pos );
                }
            }
        }
    }

    public static Optional<ClientWorldAreaManager> get() {
        return Optional.ofNullable( ModernityClient.get().getWorldAreaManager() );
    }
}
