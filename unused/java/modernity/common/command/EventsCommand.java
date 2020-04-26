/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import modernity.generic.dimension.IEnvEventsDimension;
import modernity.common.command.node.ResourceLiteralArgumentBuilder;
import modernity.common.environment.event.EnvironmentEvent;
import modernity.common.environment.event.EnvironmentEventType;
import modernity.common.registry.MDRegistries;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.ArrayList;

/**
 * Manages the {@code /modernity event} command.
 */
public final class EventsCommand {
    private EventsCommand() {
    }

    public static void createCommand( ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        createCommand( "event", list );
    }

    private static void createCommand( String name, ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        LiteralArgumentBuilder<CommandSource> cmd = Commands.literal( name )
                                                            .requires( EventsCommand::checkSource );

        for( EnvironmentEventType type : MDRegistries.ENVIRONMENT_EVENTS.getValues() ) {
            ResourceLiteralArgumentBuilder<CommandSource> evcmd
                = MDCommands.resLiteral( type.getRegistryName() )
                          .requires( src -> EventsCommand.checkSource( src, type ) );

            ArrayList<ArgumentBuilder<CommandSource, ?>> evlist = new ArrayList<>();
            type.buildCommand( evlist );
            buildDefaultCommand( evlist, type );

            // Don't build command if event does not provide a command implementation...
            if( evlist.isEmpty() ) continue;

            for( ArgumentBuilder<CommandSource, ?> builder : evlist ) {
                evcmd.then( builder );
            }

            cmd.then( evcmd );
        }

        list.add( cmd );
    }

    private static void buildDefaultCommand( ArrayList<ArgumentBuilder<CommandSource, ?>> list, EnvironmentEventType type ) {
        list.add(
            Commands.literal( "disable" )
                    .executes( ctx -> {
                        EnvironmentEvent ev = EnvironmentEvent.getFromCommand( ctx, type );
                        ev.disable();
                        return 0;
                    } )
        );
        list.add(
            Commands.literal( "enable" )
                    .executes( ctx -> {
                        EnvironmentEvent ev = EnvironmentEvent.getFromCommand( ctx, type );
                        ev.enable();
                        return 0;
                    } )
        );
    }

    private static boolean checkSource( CommandSource src ) {
        return src.getWorld().dimension instanceof IEnvEventsDimension && src.hasPermissionLevel( 2 );
    }

    private static boolean checkSource( CommandSource src, EnvironmentEventType type ) {
        if( src.getWorld().dimension instanceof IEnvEventsDimension ) {
            IEnvEventsDimension dimen = (IEnvEventsDimension) src.getWorld().dimension;
            EnvironmentEvent event = dimen.getEnvEventManager().getByType( type );
            return event != null;
        }
        return false;
    }
}
