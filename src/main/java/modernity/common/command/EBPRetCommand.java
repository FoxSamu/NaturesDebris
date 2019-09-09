/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import modernity.api.util.EcoBlockPos;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;

public class EBPRetCommand {
    private static final String TK_CHECK_LOG = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:debug.checklog" ) );

    public static void createCommand( ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        createCommand( "dumpEBPRetainers", list );
        createCommand( "logEBPRetainers", list );
    }

    private static void createCommand( String name, ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        list.add(
                Commands.literal( name )
                        .executes( EBPRetCommand::teleport )
        );
    }

    private static int teleport( CommandContext<CommandSource> ctx ) {
        CommandSource src = ctx.getSource();

        src.sendFeedback( new TextComponentTranslation( TK_CHECK_LOG ), true );

        EcoBlockPos.dumpRetainersInLog();
        return 1;
    }
}
