/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponentTranslation;

import modernity.common.command.argument.SettingArgumentType;
import modernity.common.settings.core.AbstractSetting;
import modernity.common.util.ProxyCommon;

import java.util.ArrayList;

public class SettingsCommand {
    private static final String TK_GET = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:settings.get" ) );
    private static final String TK_SET = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:settings.set" ) );
    private static final String TK_FLUSHED = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:settings.flushed" ) );
    private static final String TK_SAVED = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:settings.saved" ) );
    private static final String TK_RESET = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:settings.reset" ) );

    public static void createCommand( ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        createCommand( "settings", list );
        createCommand( "config", list );
    }

    private static void createCommand( String name, ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        LiteralArgumentBuilder<CommandSource> setBuilder = Commands.literal( "set" ).requires( SettingsCommand::permitted );
        LiteralArgumentBuilder<CommandSource> getBuilder = Commands.literal( "get" );
        LiteralArgumentBuilder<CommandSource> resetBuilder = Commands.literal( "reset" ).requires( SettingsCommand::permitted );
        for( AbstractSetting<?> setting : ProxyCommon.serverSettings() ) {
            setBuilder.then( Commands.literal( setting.getKey() )
                                     .then( Commands.argument( "value", new SettingArgumentType( setting ) )
                                                    .executes( ctx -> set( ctx, setting ) )
                                     )
            );
            getBuilder.then( Commands.literal( setting.getKey() )
                                     .executes( ctx -> get( ctx, setting ) )
            );
            resetBuilder.then( Commands.literal( setting.getKey() )
                                       .executes( ctx -> reset( ctx, setting ) )
            );
        }
        list.add( Commands.literal( name )
                          .then( setBuilder )
                          .then( getBuilder )
                          .then( resetBuilder )
                          .then( Commands.literal( "flush" )
                                         .requires( SettingsCommand::permitted )
                                         .executes( SettingsCommand::flush )
                          )
                          .then( Commands.literal( "save" )
                                         .requires( SettingsCommand::permitted )
                                         .executes( SettingsCommand::save )
                          )
        );
    }

    private static int set( CommandContext<CommandSource> ctx, AbstractSetting<?> setting ) {
        setting.deserialize( ctx.getArgument( "value", String.class ) );
        ctx.getSource().sendFeedback( new TextComponentTranslation( TK_SET, setting, setting.serialize() ), true );
        return 1;
    }

    private static int get( CommandContext<CommandSource> ctx, AbstractSetting<?> setting ) {
        ctx.getSource().sendFeedback( new TextComponentTranslation( TK_GET, setting, setting.serialize() ), true );
        return 1;
    }

    private static int reset( CommandContext<CommandSource> ctx, AbstractSetting<?> setting ) {
        setting.setDefault();
        ctx.getSource().sendFeedback( new TextComponentTranslation( TK_RESET, setting, setting.serialize() ), true );
        return 1;
    }

    private static int flush( CommandContext<CommandSource> ctx ) {
        ProxyCommon.get().getServerSettings().flush();
        ctx.getSource().sendFeedback( new TextComponentTranslation( TK_FLUSHED ), true );
        return 1;
    }

    private static int save( CommandContext<CommandSource> ctx ) {
        ProxyCommon.get().getServerSettings().save( true );
        ctx.getSource().sendFeedback( new TextComponentTranslation( TK_SAVED ), true );
        return 1;
    }

    private static boolean permitted( CommandSource src ) {
        return src.hasPermissionLevel( ProxyCommon.serverSettings().settingsCmdPermission.get() );
    }
}
