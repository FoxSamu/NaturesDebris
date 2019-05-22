package modernity.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import modernity.MDInfo;
import modernity.api.event.ModernityCommandSetupEvent;

import java.util.ArrayList;

public class MDCommands {
    private static final String TK_MAIN_RESULT_TITLE = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:main.title" ) );
    private static final String TK_MAIN_RESULT_VERSION = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:main.version" ) );

    public static void register( CommandDispatcher<CommandSource> dispatcher ) {
        register( "modernity", dispatcher );
        register( "md", dispatcher );
        register( "m", dispatcher );
    }

    public static void register( String alias, CommandDispatcher<CommandSource> dispatcher ) {
        LiteralArgumentBuilder<CommandSource> root = Commands.literal( alias );
        root.executes( MDCommands::invokeMain );

        ArrayList<LiteralArgumentBuilder<CommandSource>> commandList = new ArrayList<>();

        TPDimCommand.createCommand( commandList );
        MinecraftForge.EVENT_BUS.post( new ModernityCommandSetupEvent( commandList ) );

        for( LiteralArgumentBuilder<CommandSource> comm : commandList ) {
            root.then( comm );
        }

        dispatcher.register( root );
    }

    private static int invokeMain( CommandContext<CommandSource> ctx ) {
        CommandSource src = ctx.getSource();
        src.sendFeedback( new TextComponentTranslation( TK_MAIN_RESULT_TITLE ), true );
        src.sendFeedback( new TextComponentTranslation( TK_MAIN_RESULT_VERSION, MDInfo.VERSION ), true );
        return 1;
    }
}
