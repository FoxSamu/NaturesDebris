package modernity.common.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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
                        .then(
                                Commands.argument( "dimension", IntegerArgumentType.integer() )
                                        .executes( TPDimCommand::teleport )
                        )
        );
    }

    private static int teleport( CommandContext<CommandSource> ctx ) {
        CommandSource src = ctx.getSource();

        int dimen = ctx.getArgument( "dimension", Integer.class );

        return teleport( src, dimen );
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
            System.out.println( dim );
            source.sendErrorMessage( new TextComponentTranslation( TK_ERROR_INVALID, dim ) );
            return 0;
        }
        return teleport( source, type );
    }

    private static int teleport( CommandSource source, DimensionType dim ) {
        Entity e = source.getEntity();
        if( e == null ) return 0;
        e.changeDimension( dim, new TP( source.getWorld(), e.posX, e.posY, e.posZ ) );
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
