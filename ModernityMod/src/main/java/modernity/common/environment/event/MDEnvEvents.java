/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.environment.event;

import com.mojang.brigadier.builder.ArgumentBuilder;
import modernity.common.environment.event.impl.*;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Holder class for the Modernity's environment event types.
 */
public final class MDEnvEvents {
    private static final RegistryHandler<EnvironmentEventType> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final EnvironmentEventType FOG = register( "fog", FogEnvEvent::new, ScheduledEnvEvent::buildCommand );
    public static final EnvironmentEventType CLOUDS = register( "clouds", CloudsEnvEvent::new, ScheduledEnvEvent::buildCommand );
    public static final EnvironmentEventType CLOUDLESS = register( "cloudless", CloudlessEnvEvent::new, ScheduledEnvEvent::buildCommand );
    public static final EnvironmentEventType PRECIPITATION = register( "precipitation", PrecipitationEnvEvent::new, PrecipitationEnvEvent::buildCommand );
    public static final EnvironmentEventType SKYLIGHT = register( "skylight", SkyLightEnvEvent::new, SkyLightEnvEvent::buildCommand );

    private static EnvironmentEventType register( String id, Function<EnvironmentEventManager, EnvironmentEvent> factory, BiConsumer<ArrayList<ArgumentBuilder<CommandSource, ?>>, EnvironmentEventType> commandFactory ) {
        return ENTRIES.register( id, new EnvironmentEventType( factory, commandFactory ) );
    }

    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( EnvironmentEventType.class, ENTRIES );
    }

    private MDEnvEvents() {
    }
}
