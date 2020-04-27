/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.command;

import com.google.common.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import modernity.common.command.argument.DimensionArgumentType;
import modernity.common.command.argument.EnumArgumentType;
import modernity.common.command.node.ResourceLiteralArgumentBuilder;
import modernity.generic.MDInfo;
import modernity.generic.event.ModernityCommandSetupEvent;
import modernity.generic.event.ModernityDebugCommandSetupEvent;
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
@SuppressWarnings( "unchecked" )
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
        EventsCommand.createCommand( commandList );
        SatelliteCommand.createCommand( commandList );
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

    public static ResourceLiteralArgumentBuilder<CommandSource> resLiteral( ResourceLocation literal ) {
        return ResourceLiteralArgumentBuilder.literal( literal );
    }

    public static ResourceLiteralArgumentBuilder<CommandSource> resLiteral( String literal ) {
        return ResourceLiteralArgumentBuilder.literal( literal );
    }

    static {
        ArgumentTypes.register( "modernity:dimension_type", DimensionArgumentType.class, new ArgumentSerializer<>( DimensionArgumentType::new ) );
        TypeToken<EnumArgumentType<?>> enumTypeToken = new TypeToken<EnumArgumentType<?>>() {
        };
        ArgumentTypes.register( "modernity:enum", (Class<EnumArgumentType<?>>) enumTypeToken.getRawType(), new EnumArgumentType.Serializer() );
    }
}
