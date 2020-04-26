/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 09 - 2020
 * Author: rgsw
 */

package modernity.common.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import modernity.common.command.argument.DimensionArgumentType;
import modernity.common.world.dimen.MDDimensions;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;

/**
 * Manages the {@code /modernity tpdim} or {@code /modernity access} command.
 */
public final class TPDimCommand {
    private static final String TK_ERROR_INVALID = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.invalid" ) );
    private static final String TK_ERROR_NO_ENTITY = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.no_entity" ) );
    private static final String TK_ALREADY_HERE = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.already_here" ) );
    private static final String TK_CHANGED_DIMEN = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.changed_dimen" ) );

    private TPDimCommand() {
    }

    public static void createCommand( ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        createCommand( "access", list );
        createCommand( "tpdim", list );
    }

    private static void createCommand( String name, ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        list.add(
            Commands.literal( name )
                    .requires( src -> {
                        // Make sure we're an entity
                        if( ! ( src.getEntity() instanceof ServerPlayerEntity ) ) return false;
                        return src.hasPermissionLevel( 2 );
                    } )
                    .executes( TPDimCommand::teleportDefault )
                    .then(
                        Commands.argument( "dimension", new DimensionArgumentType() )
                                .executes( TPDimCommand::teleportTyped )
                    )
        );
    }

    private static int teleport( CommandContext<CommandSource> ctx ) {
        CommandSource src = ctx.getSource();

        int dimen = ctx.getArgument( "dimension", Integer.class );

        return teleport( src, dimen );
    }

    private static int teleportTyped( CommandContext<CommandSource> ctx ) {
        CommandSource src = ctx.getSource();

        DimensionType dimen = ctx.getArgument( "dimension", DimensionType.class );

        return teleport( src, dimen );
    }

    private static int teleportDefault( CommandContext<CommandSource> ctx ) {
        CommandSource src = ctx.getSource();

        DimensionType dimen = ctx.getSource().getWorld().dimension.getType();

        DimensionType goTo = DimensionType.OVERWORLD;
        if( goTo == dimen ) {
            goTo = MDDimensions.MURK_SURFACE.getType();
        }

        return teleport( src, goTo );
    }

//    private static DimensionType getTypeFromID( int id ) {
//        DimensionType type = DimensionType.getById( id );
//        if( type == null ) {
//            ForgeRegistry<ModDimension> registry = (ForgeRegistry<ModDimension>) ForgeRegistries.MOD_DIMENSIONS;
//            ModDimension dim = registry.getValue( id + 1 );
//            DimensionManager
//        }
//
//    }

    private static int teleport( CommandSource source, int dim ) {
        DimensionType type = DimensionType.getById( dim );
        if( type == null ) {
            source.sendErrorMessage( new TranslationTextComponent( TK_ERROR_INVALID, dim ) );
            return 0;
        }
        return teleport( source, type );
    }

    private static int teleport( CommandSource source, DimensionType dim ) {
        Entity e = source.getEntity();
        if( e == null ) {
            source.sendErrorMessage( new TranslationTextComponent( TK_ERROR_NO_ENTITY ) );
            return 0;
        }
        if( e.dimension == dim ) {
            source.sendFeedback( new TranslationTextComponent( TK_ALREADY_HERE ), true );
            return 1;
        }
        ServerPlayerEntity pl = (ServerPlayerEntity) e;
        pl.teleport( pl.server.getWorld( dim ), e.posX, e.posY, e.posZ, e.rotationYaw, e.rotationPitch );
        source.sendFeedback( new TranslationTextComponent( TK_CHANGED_DIMEN ), true );
        return 1;
    }
}
