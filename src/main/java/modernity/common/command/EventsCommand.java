/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import modernity.api.dimension.IEnvEventsDimension;
import modernity.common.environment.event.EnvironmentEvent;
import modernity.common.environment.event.EnvironmentEventType;
import modernity.common.registry.MDRegistries;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import java.util.ArrayList;

/**
 * Manages the {@code /modernity tpdim} or {@code /modernity access} command.
 */
public final class EventsCommand {
    private static final String TK_ERROR_INVALID = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.invalid" ) );
    private static final String TK_ERROR_NO_ENTITY = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.no_entity" ) );
    private static final String TK_ALREADY_HERE = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.already_here" ) );
    private static final String TK_CHANGED_DIMEN = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.changed_dimen" ) );

    private EventsCommand() {
    }

    public static void createCommand( ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        createCommand( "event", list );
    }

    private static void createCommand( String name, ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        LiteralArgumentBuilder<CommandSource> cmd = Commands.literal( name )
                                                            .requires( EventsCommand::checkSource );

        for( EnvironmentEventType type : MDRegistries.ENVIRONMENT_EVENTS.getValues() ) {
            LiteralArgumentBuilder<CommandSource> evcmd
                = Commands.literal( type.getRegistryName() + "" )
                          .requires( src -> EventsCommand.checkSource( src, type ) );

            EnvironmentEvent ev = type.getDummy();
            ArrayList<ArgumentBuilder<CommandSource, ?>> evlist = new ArrayList<>();
            ev.buildCommands( evlist );

            // Don't build command if event does not provide a command implementation...
            if( evlist.isEmpty() ) continue;

            for( ArgumentBuilder<CommandSource, ?> builder : evlist ) {
                evcmd.then( builder );
            }

            cmd.then( evcmd );
        }

        list.add( cmd );
    }

    private static boolean checkSource( CommandSource src ) {
        return src.getWorld().dimension instanceof IEnvEventsDimension;
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
