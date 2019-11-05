package modernity.common.area.core;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import modernity.common.Modernity;
import modernity.common.net.SAreaUnwatchPacket;
import modernity.common.net.SAreaUpdatePacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerWorldAreaManager implements IWorldAreaManager {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ServerWorld world;

    private final AreaReferenceManager referenceManager;
    private final AreaIOManager ioManager;

    private final Long2ObjectLinkedOpenHashMap<AreaHolder> loadedAreas = new Long2ObjectLinkedOpenHashMap<>();

    private final HashSet<ServerPlayerEntity> watchers = new HashSet<>();
    private final HashSet<ServerPlayerEntity> unwatchers = new HashSet<>();
    private final HashSet<ChunkPos> unloading = new HashSet<>();

    private int watcherCheckTimer = 0;
    private int unloadTimer = 0;

    public ServerWorldAreaManager( ServerWorld world ) {
        this.world = world;

        File areaFolder = getAreaFolder( world );
        referenceManager = new AreaReferenceManager( areaFolder );
        ioManager = new AreaIOManager( areaFolder, world );
    }

    @Override
    public ServerWorld getWorld() {
        return world;
    }

    @Override
    public void tick() {
        for( AreaHolder holder : loadedAreas.values() ) {
            holder.tick();
        }

        watcherCheckTimer++;
        // TODO: Setting for this tick count (5)
        if( watcherCheckTimer >= 5 ) {
            referenceManager.loadedChunksStream()
                            .forEach( this::checkWatchers );
            watcherCheckTimer = 0;
        }

        unloadTimer++;
        // TODO: Setting for this tick count (100)
        if( unloadTimer >= 100 ) {
            unloading.clear();
            referenceManager.loadedChunksStream()
                            .filter( TrackableAreaReferenceChunk::isNotTracked )
                            .map( TrackableAreaReferenceChunk::getPos )
                            .collect( Collectors.toCollection( () -> unloading ) )
                            .forEach( this::unloadChunk );
            unloadTimer = 0;
        }
    }

    private void checkWatchers( TrackableAreaReferenceChunk chunk ) {
        ChunkPos pos = chunk.getPos();

        watchers.clear();
        unwatchers.clear();

        world.getServer()
             .getPlayerList()
             .getPlayers()
             .stream()
             .filter(
                 player -> player.world.getDimension().getType()
                               == world.getDimension().getType()
             ) // TODO: Setting for this tracking range (16)
             .filter( player -> isCloseEnough( player, pos, 16 ) )
             .collect( Collectors.toCollection( () -> watchers ) );

        watchers.forEach( player -> {
            if( ! chunk.isTracked( player ) ) {
                chunkWatch( pos, player );
            }
        } );

        chunk.trackerStream()
             .collect( Collectors.toCollection( () -> unwatchers ) )
             .forEach( player -> {
                 if( ! watchers.contains( player ) ) {
                     chunkUnwatch( pos, player );
                 }
             } );
    }

    private boolean isCloseEnough( ServerPlayerEntity entity, ChunkPos pos, int dist ) {
        int x = MathHelper.floor( entity.posX ) >> 16;
        int z = MathHelper.floor( entity.posZ ) >> 16;
        int xDist = Math.abs( pos.x - x );
        int zDist = Math.abs( pos.z - z );
        return Math.max( xDist, zDist ) <= dist;
    }

    private void putArea( AreaHolder holder ) {
        loadedAreas.putAndMoveToFirst( holder.area.getReferenceID(), holder );
    }



    public synchronized void loadChunk( ChunkPos pos ) {
        if( referenceManager.isLoaded( pos.x, pos.z ) ) return;

        referenceManager.load( pos.x, pos.z );
        TrackableAreaReferenceChunk chunk = referenceManager.getLoadedChunk( pos.x, pos.z );
        chunk.referenceStream().forEach( this::loadRef );
    }

    private void loadRef( long ref ) {
        if( loadedAreas.containsKey( ref ) ) {
            AreaHolder holder = loadedAreas.get( ref );
            holder.reference();
        } else {
            Area area = ioManager.loadArea( ref );
            if( area == null ) return;
            AreaHolder holder = new AreaHolder( area );
            holder.reference();
            putArea( holder );
        }
    }



    public synchronized void unloadChunk( ChunkPos pos ) {
        TrackableAreaReferenceChunk chunk = referenceManager.getLoadedChunk( pos.x, pos.z );
        if( chunk == null ) {
            return;
        }
        chunk.referenceStream().forEach( this::unloadRef );
        referenceManager.unload( chunk.x, chunk.z );
    }

    private void unloadRef( long ref ) {
        if( loadedAreas.containsKey( ref ) ) {
            AreaHolder holder = loadedAreas.get( ref );
            holder.unreference();
            if( holder.refCounter <= 0 ) {
                if( holder.refCounter < 0 ) {
                    LOGGER.error( "Area reference count was negative?! Did someone hack the area system? Unloading anyways..." );
                }

                holder.unload();

                loadedAreas.remove( ref );

                ioManager.saveArea( holder.area.getReferenceID(), holder.area );
            }
        }
    }



    private void chunkWatch( ChunkPos pos, ServerPlayerEntity player ) {
        loadChunk( pos ); // Load chunk if vanilla didn't load it...
        TrackableAreaReferenceChunk chunk = referenceManager.getLoadedChunk( pos.x, pos.z );
        if( chunk.track( player ) ) {
            chunk.referenceStream().forEach( ref -> watchRef( ref, player ) );
        }
    }

    private void watchRef( long ref, ServerPlayerEntity entity ) {
        AreaHolder holder = loadedAreas.get( ref );
        if( holder == null ) {
            LOGGER.error( "Player tries to watch a not-loaded area?! Did someone hack the area system?" );
            return;
        }
        holder.watch( entity );
    }



    private void chunkUnwatch( ChunkPos pos, ServerPlayerEntity player ) {
        TrackableAreaReferenceChunk chunk = referenceManager.getLoadedChunk( pos.x, pos.z );
        if( chunk == null ) {
            return;
        }
        if( chunk.untrack( player ) ) {
            chunk.referenceStream().forEach( ref -> unwatchRef( ref, player ) );
        }
    }

    private void unwatchRef( long ref, ServerPlayerEntity entity ) {
        AreaHolder holder = loadedAreas.get( ref );
        if( holder == null ) {
            LOGGER.error( "Player tries to unwatch a not-loaded area?! Did someone hack the area system?" );
            return;
        }
        holder.unwatch( entity );
    }



    public synchronized void saveAll() {
        for( AreaHolder holder : loadedAreas.values() ) {
            ioManager.saveArea( holder.area.getReferenceID(), holder.area );
        }
        ioManager.saveAll();
        LOGGER.info( "All world area's are saved" );
        referenceManager.saveAll();
    }



    public synchronized void addArea( Area area ) {
        long refID = ioManager.findFreeRefID( area.getBox().computeRegionX(), area.getBox().computeRegionZ() );
        area.setReferenceID( refID );

        AreaHolder holder = new AreaHolder( area );

        AreaBox box = area.getBox();
        int minX = box.getMinChunkX();
        int minZ = box.getMinChunkZ();
        int maxX = box.getMaxChunkX();
        int maxZ = box.getMaxChunkZ();
        for( int x = minX; x < maxX; x++ ) {
            for( int z = minZ; z < maxZ; z++ ) {
                loadChunk( new ChunkPos( x, z ) );
                TrackableAreaReferenceChunk chunk = referenceManager.getLoadedChunk( x, z );
                if( chunk != null ) {
                    chunk.addReference( refID );
                    holder.reference();
                    chunk.trackerStream().forEach( holder::watch ); // Notify clients of new area
                }
            }
        }

        putArea( holder );
    }

    public synchronized void removeArea( Area area ) {
        long refID = area.getReferenceID();
        if( ! loadedAreas.containsKey( refID ) ) return;

        AreaHolder holder = loadedAreas.remove( refID );

        holder.unload(); // Notify clients of removal

        AreaBox box = area.getBox();
        int minX = box.getMinChunkX();
        int minZ = box.getMinChunkZ();
        int maxX = box.getMaxChunkX();
        int maxZ = box.getMaxChunkZ();
        for( int x = minX; x < maxX; x++ ) {
            for( int z = minZ; z < maxZ; z++ ) {
                TrackableAreaReferenceChunk chunk = referenceManager.getLoadedChunk( x, z );
                if( chunk != null ) {
                    chunk.removeReference( refID );
                }
            }
        }

        ioManager.removeArea( refID );
    }

    @Override
    public Area getLoadedArea( long reference ) {
        return loadedAreas.get( reference ).area;
    }

    @Override
    public boolean isAreaLoaded( long reference ) {
        return loadedAreas.containsKey( reference );
    }

    @Override
    public Stream<Area> streamAreas() {
        return loadedAreas.values().stream().map( holder -> holder.area );
    }

    @Override
    public IAreaReferenceChunk getLoadedChunk( int x, int z ) {
        TrackableAreaReferenceChunk chunk = referenceManager.getLoadedChunk( x, z );
        if( chunk != null ) return chunk.unmodifiable;
        return null;
    }

    @Override
    public boolean isChunkLoadedAt( int x, int z ) {
        return referenceManager.isLoaded( x, z );
    }

    @Override
    public IAreaReferenceChunk getChunk( int x, int z ) {
        return referenceManager.getChunk( x, z ).unmodifiable;
    }

    private static File getAreaFolder( ServerWorld world ) {
        return new File( world.getDimension().getType().getDirectory( world.getSaveHandler().getWorldDirectory() ), "md/area" );
    }

    public static Optional<ServerWorldAreaManager> get( World world ) {
        if( ! ( world instanceof ServerWorld ) ) return Optional.empty();
        return Optional.ofNullable( Modernity.get().getWorldAreaManager( (ServerWorld) world ) );
    }

    private static class AreaHolder {
        final Area area;
        final IServerTickableArea tickable;
        int refCounter;
        int updateCounter;


        final Object2IntOpenHashMap<ServerPlayerEntity> watchers = new Object2IntOpenHashMap<>();

        private AreaHolder( Area area ) {
            this.area = area;
            if( area instanceof IServerTickableArea ) {
                tickable = (IServerTickableArea) area;
            } else {
                tickable = null;
            }
        }

        void reference() {
            refCounter++;
        }

        void unreference() {
            refCounter--;
        }

        void unload() {
            Modernity.network().sendToPlayers( new SAreaUnwatchPacket( area.getReferenceID(), area.world ), watchers.keySet() );
        }

        void watch( ServerPlayerEntity player ) {
            if( ! watchers.containsKey( player ) ) {
                watchers.put( player, 0 );
                Modernity.network().sendToPlayer( new SAreaUpdatePacket( area, area.world ), player );
            }
            watchers.addTo( player, 1 );
        }

        void unwatch( ServerPlayerEntity player ) {
            if( watchers.containsKey( player ) ) {
                int counter = watchers.addTo( player, - 1 ) - 1;
                if( counter <= 0 ) {
                    if( counter < 0 ) {
                        LOGGER.error( "Watcher count was negative?! Did someone hack the area system? Unwatching anyways..." );
                    }
                    Modernity.network().sendToPlayer( new SAreaUnwatchPacket( area.getReferenceID(), area.world ), player );
                    watchers.removeInt( player );
                }
            } else {
                LOGGER.error( "A not-watching player wants to unwatch area?! Did someone hack the area system?" );
            }
        }

        void tick() {
            updateCounter++;
            if( updateCounter > area.getType().updateInterval ) {
                updateCounter = 0;
                Modernity.network().sendToPlayers( new SAreaUpdatePacket( area, area.world ), watchers.keySet() );
            }

            if( tickable != null ) {
                tickable.tickServer();
            }
        }
    }
}