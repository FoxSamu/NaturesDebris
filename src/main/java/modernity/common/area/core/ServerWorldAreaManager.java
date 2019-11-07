package modernity.common.area.core;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import modernity.common.Modernity;
import modernity.common.net.SAreaUntrackPacket;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerWorldAreaManager implements IWorldAreaManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int TRACK_RANGE = 16; // TODO: Setting for this tracking range (16)

    private final ServerWorld world;

    private final AreaReferenceManager referenceManager;
    private final AreaIOManager ioManager;

    private final Long2ObjectLinkedOpenHashMap<AreaHolder> loadedAreas = new Long2ObjectLinkedOpenHashMap<>();

    private final HashSet<ServerPlayerEntity> trackers = new HashSet<>();
    private final HashSet<ServerPlayerEntity> untrackers = new HashSet<>();
    private final HashSet<ChunkPos> unloading = new HashSet<>();

    private int trackingTimer = 0;
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
    public synchronized void tick() {
        for( AreaHolder holder : loadedAreas.values() ) {
            holder.tick();
        }

        trackingTimer++;
        // TODO: Setting for this tick count (5)
        if( trackingTimer >= 5 ) {
            updateTrackers();
            trackingTimer = 0;
        }

        unloadTimer++;
        // TODO: Setting for this tick count (100)
        if( unloadTimer >= 100 ) {
            updateUnloading();
            unloadTimer = 0;
        }
    }

    public synchronized void init() {
        updateTrackers();
    }

    private void updateTrackers() {
        world.getServer()
             .getPlayerList()
             .getPlayers()
             .forEach( player -> {
                 if( player.world.getDimension().getType() != world.dimension.getType() ) return;
                 int px = MathHelper.floor( player.posX ) >> 4;
                 int pz = MathHelper.floor( player.posZ ) >> 4;
                 for( int x = - TRACK_RANGE; x <= TRACK_RANGE; x++ ) {
                     for( int z = - TRACK_RANGE; z <= TRACK_RANGE; z++ ) {
                         trackChunk( new ChunkPos( x + px, z + pz ), player );
                     }
                 }
             } );

        referenceManager.loadedChunksStream()
                        .forEach( this::checkTrackers );
    }

    private void updateUnloading() {
        unloading.clear();
        referenceManager.loadedChunksStream()
                        .filter( TrackableAreaReferenceChunk::isNotTracked )
                        .map( IAreaReferenceChunk::getPos )
                        .collect( Collectors.toCollection( () -> unloading ) )
                        .forEach( this::unloadChunk );
    }

    private void checkTrackers( TrackableAreaReferenceChunk chunk ) {
        ChunkPos pos = chunk.getPos();

        untrackers.clear();
        trackers.clear();

        Set<ServerPlayerEntity> players
            = world.getServer()
                   .getPlayerList()
                   .getPlayers()
                   .stream()
                   .filter(
                       player -> player.world.getDimension().getType()
                                     == world.dimension.getType()
                   )
                   .collect( Collectors.toCollection( () -> trackers ) );


        chunk.trackerStream()
             .collect( Collectors.toCollection( () -> untrackers ) )
             .forEach( player -> {
                 if( ! players.contains( player ) || ! isCloseEnough( player, pos, TRACK_RANGE ) ) {
                     untrackChunk( pos, player );
                 }
             } );
    }

    private boolean isCloseEnough( ServerPlayerEntity entity, ChunkPos pos, int dist ) {
        int x = MathHelper.floor( entity.posX ) >> 4;
        int z = MathHelper.floor( entity.posZ ) >> 4;
        int xDist = Math.abs( pos.x - x );
        int zDist = Math.abs( pos.z - z );
        return xDist <= dist && zDist <= dist;
    }

    private void putArea( AreaHolder holder ) {
        loadedAreas.putAndMoveToFirst( holder.area.getReferenceID(), holder );
    }



    private synchronized void loadChunk( ChunkPos pos ) {
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



    private synchronized void unloadChunk( ChunkPos pos ) {
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



    private void trackChunk( ChunkPos pos, ServerPlayerEntity player ) {
        loadChunk( pos ); // Load chunk if not loaded...
        TrackableAreaReferenceChunk chunk = referenceManager.getLoadedChunk( pos.x, pos.z );
        if( chunk.track( player ) ) {
            chunk.referenceStream().forEach( ref -> trackRef( ref, player ) );
        }
    }

    private void trackRef( long ref, ServerPlayerEntity entity ) {
        AreaHolder holder = loadedAreas.get( ref );
        if( holder == null ) {
            LOGGER.error( "Player tries to track a not-loaded area?! Did someone hack the area system?" );
            return;
        }
        holder.track( entity );
    }



    private void untrackChunk( ChunkPos pos, ServerPlayerEntity player ) {
        TrackableAreaReferenceChunk chunk = referenceManager.getLoadedChunk( pos.x, pos.z );
        if( chunk == null ) {
            return;
        }
        if( chunk.untrack( player ) ) {
            chunk.referenceStream().forEach( ref -> untrackRef( ref, player ) );
        }
    }

    private void untrackRef( long ref, ServerPlayerEntity entity ) {
        AreaHolder holder = loadedAreas.get( ref );
        if( holder == null ) {
            LOGGER.error( "Player tries to untrack a not-loaded area?! Did someone hack the area system?" );
            return;
        }
        holder.untrack( entity );
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
                    chunk.trackerStream().forEach( holder::track ); // Notify clients of new area
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
        AreaHolder area = loadedAreas.get( reference );
        return area == null ? null : area.area;
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


        final Object2IntOpenHashMap<ServerPlayerEntity> trackers = new Object2IntOpenHashMap<>();

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
            Modernity.network().sendToPlayers( new SAreaUntrackPacket( area.getReferenceID(), area.world ), trackers.keySet() );
        }

        void track( ServerPlayerEntity player ) {
            if( ! trackers.containsKey( player ) ) {
                trackers.put( player, 0 );
                if( area.getType().updateInterval > 0 ) {
                    Modernity.network().sendToPlayer( new SAreaUpdatePacket( area, area.world ), player );
                }
            }
            trackers.addTo( player, 1 );
        }

        void untrack( ServerPlayerEntity player ) {
            if( trackers.containsKey( player ) ) {
                int counter = trackers.addTo( player, - 1 ) - 1;
                if( counter <= 0 ) {
                    if( counter < 0 ) {
                        LOGGER.error( "Trackers count was negative?! Did someone hack the area system? Untracking anyways..." );
                    }
                    Modernity.network().sendToPlayer( new SAreaUntrackPacket( area.getReferenceID(), area.world ), player );
                    trackers.removeInt( player );
                }
            } else {
                LOGGER.error( "A not-tracking player wants to untrack area?! Did someone hack the area system?" );
            }
        }

        void tick() {
            if( area.getType().updateInterval > 0 ) {
                updateCounter++;
                if( updateCounter >= area.getType().updateInterval ) {
                    updateCounter = 0;
                    Modernity.network().sendToPlayers( new SAreaUpdatePacket( area, area.world ), trackers.keySet() );
                }
            }

            if( tickable != null ) {
                tickable.tickServer();
            }
        }
    }
}