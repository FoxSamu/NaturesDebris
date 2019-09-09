/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import modernity.MDInfo;
import modernity.api.event.ModernityCommandSetupEvent;
import modernity.api.event.ModernityDebugCommandSetupEvent;
import modernity.common.command.argument.DimensionArgumentType;
import modernity.common.command.argument.SettingArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class MDCommands {
    private static final String TK_MAIN_RESULT_TITLE = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:main.title" ) );
    private static final String TK_MAIN_RESULT_VERSION = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:main.version" ) );

    public static void register( CommandDispatcher<CommandSource> dispatcher ) {

        ArrayList<LiteralArgumentBuilder<CommandSource>> commandList = new ArrayList<>();

        TPDimCommand.createCommand( commandList );
        SettingsCommand.createCommand( commandList );
        MinecraftForge.EVENT_BUS.post( new ModernityCommandSetupEvent( commandList ) );
        register( "modernity", dispatcher, commandList );
        register( "md", dispatcher, commandList );
        register( "m", dispatcher, commandList );


        ArrayList<LiteralArgumentBuilder<CommandSource>> debugCommandList = new ArrayList<>();

        EBPRetCommand.createCommand( debugCommandList );
        MinecraftForge.EVENT_BUS.post( new ModernityDebugCommandSetupEvent( debugCommandList ) );
        register( "mddebug", dispatcher, debugCommandList );
        register( "mdd", dispatcher, debugCommandList );
        register( "mdebug", dispatcher, debugCommandList );

        // Extend locate command with our structures
        MDLocateCommand.register( dispatcher );
    }

    public static void register( String alias, CommandDispatcher<CommandSource> dispatcher, ArrayList<LiteralArgumentBuilder<CommandSource>> commandList ) {
        LiteralArgumentBuilder<CommandSource> root = Commands.literal( alias );
        root.executes( MDCommands::invokeMain );

        for( LiteralArgumentBuilder<CommandSource> comm : commandList ) {
            root.then( comm );
        }

        dispatcher.register( root );
    }

    private static int invokeMain( CommandContext<CommandSource> ctx ) {
        CommandSource src = ctx.getSource();
        src.sendFeedback( new TextComponentTranslation( TK_MAIN_RESULT_TITLE ), true );
        src.sendFeedback( new TextComponentTranslation( TK_MAIN_RESULT_VERSION, MDInfo.VERSION, MDInfo.VERSION_NAME ), true );
        return 1;
    }

    static {
        ArgumentTypes.register( new ResourceLocation( "modernity:dimension_type" ), DimensionArgumentType.class, new ArgumentSerializer<>( DimensionArgumentType::new ) );
        ArgumentTypes.register( new ResourceLocation( "modernity:setting" ), SettingArgumentType.class, new SettingArgumentType.Serializer() );
    }
}
