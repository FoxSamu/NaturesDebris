/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.event;

import com.google.common.collect.ImmutableMap;
import modernity.common.registry.MDRegistries;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Manages, stores and synchronizes environment events.
 */
// MAYBE: Use JSON to allow datapacks to modify events?
@SuppressWarnings( "unchecked" )
public class EnvironmentEventManager extends WorldSavedData {
    public static final String NAME = "md/env_events";

    private final Map<EnvironmentEventType, EnvironmentEvent> eventMap;
    private final Collection<EnvironmentEvent> events;
    private final int updateInterval;
    private final World world;

    private int updateTicks;
    private boolean firstTick;

    public EnvironmentEventManager( int updateInterval, World world, EnvironmentEventType... activeTypes ) {
        super( NAME );
        this.updateInterval = updateInterval;
        this.world = world;

        eventMap = Stream.of( activeTypes )
                         .collect( ImmutableMap.toImmutableMap(
                             type -> type,
                             type -> type.createEvent( this )
                         ) );
        events = eventMap.values();
    }

    /**
     * Returns the world this environment event manager is linked to.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets an environment event by the specified type.
     *
     * @param type The type of the requested event.
     * @return The requested event instance, or null when no event exists with such a type.
     */
    public <T extends EnvironmentEvent> T getByType( EnvironmentEventType type ) {
        return (T) eventMap.get( type );
    }

    /**
     * Gets an environment event by the specified resource ID.
     *
     * @param id The resource ID of the requested event.
     * @return The requested event instance, or null when no event exists with such an ID.
     */
    public <T extends EnvironmentEvent> T getByID( ResourceLocation id ) {
        return (T) getByType( MDRegistries.ENVIRONMENT_EVENTS.getValue( id ) );
    }

    /**
     * Gets an environment event by the specified name (serialized resource ID).
     *
     * @param name The name of the requested event.
     * @return The requested event instance, or null when no event exists with such a name.
     */
    public <T extends EnvironmentEvent> T getByName( String name ) {
        return (T) getByID( new ResourceLocation( name ) );
    }

    /**
     * Ticks all the environment events, sending data to the clients if necessary and not on the client.
     */
    public void tick() {
        markDirty();
        for( EnvironmentEvent event : events ) {
            if( event.isEnabled() ) {
                event.tick();
            }
        }
        if( updateInterval < 0 ) return;

        updateTicks++;
        if( updateTicks >= updateInterval || firstTick ) {
            firstTick = false;
            sync();
            updateTicks = 0;
        }
    }

    /**
     * Synchronizes the environment events with the client when on the server.
     */
    public void sync() {
        if( world.isRemote ) return; // No packet-sending client side!
        // TODO Sync!
//        Modernity.network().sendToDimen( new SEnvironmentPacket( this ), world.dimension.getType() );
    }


    @Override
    public void read( CompoundNBT nbt ) {
        for( EnvironmentEvent event : events ) {
            EnvironmentEventType type = event.getType();

            String key = type.getRegistryName() + "";
            if( nbt.contains( key ) ) {
                CompoundNBT eventNBT = nbt.getCompound( key );
                event.read( eventNBT );
                event.setActive( eventNBT.getBoolean( "active" ) );
                event.setEnabled( ! eventNBT.contains( "enabled", 1 ) || eventNBT.getBoolean( "enabled" ) );
            }
        }
    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {
        for( EnvironmentEvent event : events ) {
            EnvironmentEventType type = event.getType();

            CompoundNBT eventNBT = new CompoundNBT();
            event.write( eventNBT );
            eventNBT.putBoolean( "active", event.isActive() );
            eventNBT.putBoolean( "enabled", event.isEnabled() );

            nbt.put( type.getRegistryName() + "", eventNBT );
        }
        return nbt;
    }

    @Override
    public void save( File file ) {
        if( ! file.getParentFile().exists() ) {
            file.getParentFile().mkdirs();
        }
        super.save( file );
    }
}
