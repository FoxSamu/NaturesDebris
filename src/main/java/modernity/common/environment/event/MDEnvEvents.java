/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.event;

import com.mojang.brigadier.builder.ArgumentBuilder;
import modernity.common.environment.event.impl.CloudlessEnvEvent;
import modernity.common.environment.event.impl.CloudsEnvEvent;
import modernity.common.environment.event.impl.FogEnvEvent;
import modernity.common.environment.event.impl.PrecipitationEnvEvent;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.command.CommandSource;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Holder class for the Modernity's environment event types.
 */
@ObjectHolder( "modernity" )
public final class MDEnvEvents {
    private static final RegistryHandler<EnvironmentEventType> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final EnvironmentEventType FOG = register( "fog", FogEnvEvent::new, ScheduledEnvEvent::buildCommand );
    public static final EnvironmentEventType CLOUDS = register( "clouds", CloudsEnvEvent::new, ScheduledEnvEvent::buildCommand );
    public static final EnvironmentEventType CLOUDLESS = register( "cloudless", CloudlessEnvEvent::new, ScheduledEnvEvent::buildCommand );
    public static final EnvironmentEventType PRECIPITATION = register( "precipitation", PrecipitationEnvEvent::new, PrecipitationEnvEvent::buildCommand );

    private static EnvironmentEventType register( String id, Function<EnvironmentEventManager, EnvironmentEvent> factory, BiConsumer<ArrayList<ArgumentBuilder<CommandSource, ?>>, EnvironmentEventType> commandFactory ) {
        return ENTRIES.register( id, new EnvironmentEventType( factory, commandFactory ) );
    }

    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( EnvironmentEventType.class, ENTRIES );
    }

    private MDEnvEvents() {
    }
}
