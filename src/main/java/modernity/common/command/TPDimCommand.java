/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 9 - 2019
 */

package modernity.common.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import modernity.common.command.argument.DimensionArgumentType;
import modernity.common.world.dim.MDDimensions;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;

public class TPDimCommand {
    private static final String TK_ERROR_INVALID = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.invalid" ) );
    private static final String TK_ERROR_NO_ENTITY = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.no_entity" ) );
    private static final String TK_ALREADY_HERE = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.already_here" ) );
    private static final String TK_CHANGED_DIMEN = Util.makeTranslationKey( "command", new ResourceLocation( "modernity:access.changed_dimen" ) );

    public static void createCommand( ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        createCommand( "access", list );
        createCommand( "tpdim", list );
    }

    private static void createCommand( String name, ArrayList<LiteralArgumentBuilder<CommandSource>> list ) {
        list.add(
                Commands.literal( name )
                        .requires( src -> {
                            // Make sure we're an entity
                            if( src.getEntity() == null ) return false;
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
            goTo = MDDimensions.MODERNITY.getType();
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
            source.sendErrorMessage( new TextComponentTranslation( TK_ERROR_INVALID, dim ) );
            return 0;
        }
        return teleport( source, type );
    }

    private static int teleport( CommandSource source, DimensionType dim ) {
        Entity e = source.getEntity();
        if( e == null ) {
            source.sendErrorMessage( new TextComponentTranslation( TK_ERROR_NO_ENTITY ) );
            return 0;
        }
        if( e.dimension == dim ) {
            source.sendFeedback( new TextComponentTranslation( TK_ALREADY_HERE ), true );
            return 1;
        }
        e.changeDimension( dim, new TP( source.getWorld(), e.posX, e.posY, e.posZ ) );
        source.sendFeedback( new TextComponentTranslation( TK_CHANGED_DIMEN ), true );
        return 1;
    }

    // Custom teleporter type for command
    private static class TP extends Teleporter {
        public final double x;
        public final double y;
        public final double z;

        public TP( WorldServer world, double x, double y, double z ) {
            super( world );
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void placeInPortal( Entity entityIn, float rotationYaw ) {
        }

        @Override
        public boolean placeInExistingPortal( Entity entityIn, float rotationYaw ) {
            return true;
        }

        @Override
        public boolean makePortal( Entity entityIn ) {
            return true;
        }

        @Override
        public void placeEntity( World world, Entity entity, float yaw ) {
            entity.setPosition( this.x, this.y, this.z );
        }
    }
}
