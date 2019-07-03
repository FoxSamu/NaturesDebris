/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 3 - 2019
 */

package modernity.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class MDLocateCommand {
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType( new TextComponentTranslation( "commands.locate.failed" ) );

    public static void register( CommandDispatcher<CommandSource> dispatcher ) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal( "locate" ).requires( src -> src.hasPermissionLevel( 2 ) );
        builder.then( Commands.literal( "Nether_Altar" ).executes( cmd -> locateStructure( cmd.getSource(), "Nether_Altar" ) ) );
        dispatcher.register( builder );
    }

    private static int locateStructure( CommandSource source, String structureName ) throws CommandSyntaxException {
        BlockPos sourcePos = new BlockPos( source.getPos() );
        BlockPos structure = source.getWorld().findNearestStructure( structureName, sourcePos, 100, false );
        if( structure == null ) {
            throw FAILED_EXCEPTION.create();
        } else {
            int dist = MathHelper.floor( getDistance( sourcePos.getX(), sourcePos.getZ(), structure.getX(), structure.getZ() ) );
            ITextComponent coords = TextComponentUtils.wrapInSquareBrackets(
                    new TextComponentTranslation( "chat.coordinates", structure.getX(), "~", structure.getZ() )
            ).applyTextStyle(
                    style -> style.setColor( TextFormatting.GREEN )
                                  .setClickEvent( new ClickEvent(
                                          ClickEvent.Action.SUGGEST_COMMAND,
                                          "/tp @s " + structure.getX() + " ~ " + structure.getZ()
                                  ) )
                                  .setHoverEvent( new HoverEvent(
                                          HoverEvent.Action.SHOW_TEXT,
                                          new TextComponentTranslation( "chat.coordinates.tooltip" )
                                  ) )
            );
            source.sendFeedback( new TextComponentTranslation( "commands.locate.success", structureName, coords, dist ), false );
            return dist;
        }
    }

    private static float getDistance( int x1, int z1, int x2, int z2 ) {
        int dx = x2 - x1;
        int dz = z2 - z1;
        return MathHelper.sqrt( (float) ( dx * dx + dz * dz ) );
    }
}