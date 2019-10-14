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
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

/**
 * Manages the Modernity commands ({@code /modernity} and {@code /mddebug}).
 */
public final class MDCommands {
    private static final String TK_MAIN_RESULT_TITLE = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:main.title" ) );
    private static final String TK_MAIN_RESULT_VERSION = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:main.version" ) );

    private MDCommands() {
    }

    /**
     * Registers the commands to the {@link CommandDispatcher}.
     */
    public static void register( CommandDispatcher<CommandSource> dispatcher ) {

        ArrayList<LiteralArgumentBuilder<CommandSource>> commandList = new ArrayList<>();

        TPDimCommand.createCommand( commandList );
        MinecraftForge.EVENT_BUS.post( new ModernityCommandSetupEvent( commandList ) );
        register( "modernity", dispatcher, commandList );
        register( "md", dispatcher, commandList );
        register( "m", dispatcher, commandList );


        ArrayList<LiteralArgumentBuilder<CommandSource>> debugCommandList = new ArrayList<>();

        MinecraftForge.EVENT_BUS.post( new ModernityDebugCommandSetupEvent( debugCommandList ) );
        register( "mddebug", dispatcher, debugCommandList );
        register( "mdd", dispatcher, debugCommandList );
        register( "mdebug", dispatcher, debugCommandList );
    }

    /**
     * Registers an alias to the command dispatcher.
     */
    private static void register( String alias, CommandDispatcher<CommandSource> dispatcher, ArrayList<LiteralArgumentBuilder<CommandSource>> commandList ) {
        LiteralArgumentBuilder<CommandSource> root = Commands.literal( alias );
        root.executes( MDCommands::invokeMain );

        for( LiteralArgumentBuilder<CommandSource> comm : commandList ) {
            root.then( comm );
        }

        dispatcher.register( root );
    }

    /**
     * Called when any of the commands is executed without arguments.
     */
    private static int invokeMain( CommandContext<CommandSource> ctx ) {
        CommandSource src = ctx.getSource();
        src.sendFeedback( new TranslationTextComponent( TK_MAIN_RESULT_TITLE ), true );
        src.sendFeedback( new TranslationTextComponent( TK_MAIN_RESULT_VERSION, MDInfo.VERSION, MDInfo.VERSION_NAME ), true );
        return 1;
    }

    static {
        ArgumentTypes.register( "modernity:dimension_type", DimensionArgumentType.class, new ArgumentSerializer<>( DimensionArgumentType::new ) );
    }
}
