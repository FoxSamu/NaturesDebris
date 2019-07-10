/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.net;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import modernity.api.util.DimensionRegion;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PacketChannel {
    public static final PacketDistributor<DimensionRegion> IN_REGION = new PacketDistributor<>( PacketChannel::msgToRegion, NetworkDirection.PLAY_TO_CLIENT );
    public static final PacketDistributor<String> USERNAME = new PacketDistributor<>( PacketChannel::msgToUsername, NetworkDirection.PLAY_TO_CLIENT );

    private final SimpleChannel channel;
    private final PacketProfile profile = new PacketProfile();

    public PacketChannel( ResourceLocation name, int npv, int minLimit ) {
        Predicate<String> predicate = minLimit >= npv ? matchNPV( npv ) : rangeNPV( minLimit, npv );
        channel = NetworkRegistry.newSimpleChannel( name, createNPV( npv ), predicate, predicate );

        channel.messageBuilder( PacketMessage.class, 0 )
               .encoder( this::write )
               .decoder( this::read )
               .consumer( this::process )
               .add();
    }

    public PacketChannel( ResourceLocation name, int npv ) {
        this( name, npv, npv );
    }

    private void send( IPacket pkt, PacketDistributor.PacketTarget target, ESide fromSide ) {
        channel.send( target, new PacketMessage( pkt, fromSide ) );
    }

    public void sendToServer( IPacket pkt ) {
        send( pkt, PacketDistributor.SERVER.noArg(), ESide.CLIENT );
    }

    public void sendToPlayer( IPacket pkt, EntityPlayerMP player ) {
        send( pkt, PacketDistributor.PLAYER.with( () -> player ), ESide.SERVER );
    }

    public void sendToPlayers( IPacket pkt, EntityPlayerMP... players ) {
        for( EntityPlayerMP e : players ) {
            sendToPlayer( pkt, e );
        }
    }

    public void sendToPlayers( IPacket pkt, Collection<EntityPlayerMP> players ) {
        for( EntityPlayerMP e : players ) {
            sendToPlayer( pkt, e );
        }
    }

    public void sendToDimen( IPacket pkt, DimensionType dimen ) {
        send( pkt, PacketDistributor.DIMENSION.with( () -> dimen ), ESide.SERVER );
    }

    public void sendToAll( IPacket pkt ) {
        send( pkt, PacketDistributor.ALL.noArg(), ESide.SERVER );
    }

    public void sendToRegion( IPacket pkt, Vec3d pt, double rad, DimensionType dimen ) {
        send( pkt, PacketDistributor.NEAR.with( PacketDistributor.TargetPoint.p( pt.x, pt.y, pt.z, rad * rad, dimen ) ), ESide.SERVER );
    }

    public void sendToRegion( IPacket pkt, Vec3d pt, double rad, DimensionType dimen, EntityPlayerMP exclude ) {
        send( pkt, PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( exclude, pt.x, pt.y, pt.z, rad * rad, dimen ) ), ESide.SERVER );
    }

    public void sendToEntityTrackers( IPacket pkt, Entity trackable ) {
        send( pkt, PacketDistributor.TRACKING_ENTITY.with( () -> trackable ), ESide.SERVER );
    }

    public void sendToSelfAndTrackers( IPacket pkt, Entity trackable ) {
        send( pkt, PacketDistributor.TRACKING_ENTITY_AND_SELF.with( () -> trackable ), ESide.SERVER );
    }

    public void sendToChunkTrackers( IPacket pkt, Chunk trackable ) {
        send( pkt, PacketDistributor.TRACKING_CHUNK.with( () -> trackable ), ESide.SERVER );
    }

    public void sendToNetManager( IPacket pkt, NetworkManager managers ) {
        List<NetworkManager> managerList = Collections.singletonList( managers );
        send( pkt, PacketDistributor.NMLIST.with( () -> managerList ), ESide.SERVER );
    }

    public void sendToNetManagers( IPacket pkt, NetworkManager... managers ) {
        List<NetworkManager> managerList = Arrays.asList( managers );
        send( pkt, PacketDistributor.NMLIST.with( () -> managerList ), ESide.SERVER );
    }

    public void sendToNetManagers( IPacket pkt, Collection<? extends NetworkManager> managers ) {
        List<NetworkManager> managerList = new ArrayList<>( managers );
        send( pkt, PacketDistributor.NMLIST.with( () -> managerList ), ESide.SERVER );
    }

    public void sendToRegion( IPacket pkt, AxisAlignedBB box, DimensionType type ) {
        DimensionRegion region = new DimensionRegion( box, type );
        send( pkt, IN_REGION.with( () -> region ), ESide.SERVER );
    }

    public void sendToRegion( IPacket pkt, DimensionRegion region ) {
        send( pkt, IN_REGION.with( () -> region ), ESide.SERVER );
    }

    public void sendToUsername( IPacket pkt, String username ) {
        send( pkt, USERNAME.with( () -> username ), ESide.SERVER );
    }

    public void sendToUsernames( IPacket pkt, String... usernames ) {
        for( String name : usernames ) {
            sendToUsername( pkt, name );
        }
    }

    public void sendToUsernames( IPacket pkt, Collection<String> usernames ) {
        for( String name : usernames ) {
            sendToUsername( pkt, name );
        }
    }

    public void register( ESide side, Class<? extends IPacket> pktClass ) {
        if( side == null ) {
            profile.registerPacket( ESide.SERVER, pktClass );
            profile.registerPacket( ESide.CLIENT, pktClass );
        } else {
            profile.registerPacket( side, pktClass );
        }
    }

    private static Supplier<String> createNPV( int npv ) {
        String s = "MDNPV " + npv;
        return () -> s;
    }

    private static Predicate<String> rangeNPV( int min, int max ) {
        return s -> {
            if( ! s.startsWith( "MDNPV " ) ) return false;
            try {
                int npv = Integer.parseInt( s.substring( 6 ) );
                return npv >= min && npv <= max;
            } catch( NumberFormatException exc ) {
                return false;
            }
        };
    }

    private static Predicate<String> matchNPV( int npv ) {
        return s -> {
            if( ! s.startsWith( "MDNPV " ) ) return false;
            try {
                int npv1 = Integer.parseInt( s.substring( 6 ) );
                return npv1 == npv;
            } catch( NumberFormatException exc ) {
                return false;
            }
        };
    }

    private void write( PacketMessage msg, PacketBuffer buff ) {
        int id = profile.getID( msg.fromSide, msg.pkt );
        if( id < 0 ) {
            throw new RuntimeException( "Unable to serialize packet: Unregistered packet " + msg.pkt.getName() );
        }
        buff.writeByte( msg.fromSide == ESide.CLIENT ? 1 : 0 );
        buff.writeInt( id );
        msg.pkt.write( buff );
    }

    private PacketMessage read( PacketBuffer buff ) {
        byte sideBit = buff.readByte();
        int id = buff.readInt();
        ESide side = sideBit > 0 ? ESide.CLIENT : ESide.SERVER;

        IPacket pkt = profile.newPacket( side, id );

        pkt.read( buff );
        return new PacketMessage( pkt, side );
    }

    private void process( PacketMessage msg, Supplier<NetworkEvent.Context> ctxSupplier ) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ESide origin = ESide.fromLogical( ctx.getDirection().getOriginationSide() );
        ProcessContext pctx = new ProcessContext( origin, ctx.getSender(), this, msg.pkt );
        msg.pkt.process( pctx );
        ctx.setPacketHandled( true );
    }

    @SuppressWarnings( "unused" )
    private static Consumer<Packet<?>> msgToRegion( PacketDistributor<DimensionRegion> distributor, Supplier<DimensionRegion> regionSupplier ) {
        return pkt -> {
            DimensionRegion region = regionSupplier.get();

            getServer().getWorld( region.getDimension() )
                       .getEntitiesWithinAABB( EntityPlayerMP.class, region.getRegion() )
                       .forEach( player -> player.connection.sendPacket( pkt ) );
        };
    }

    @SuppressWarnings( "unused" )
    private static Consumer<Packet<?>> msgToUsername( PacketDistributor<String> distributor, Supplier<String> usernameSupplier ) {
        return pkt -> {
            String username = usernameSupplier.get();
            EntityPlayerMP player = getServer().getPlayerList().getPlayerByUsername( username );
            if( player != null ) {
                player.connection.sendPacket( pkt );
            }
        };
    }

    private static MinecraftServer getServer() {
        return LogicalSidedProvider.INSTANCE.get( LogicalSide.SERVER );
    }

    private static class PacketMessage {
        private final IPacket pkt;
        private final ESide fromSide;

        private PacketMessage( IPacket pkt, ESide fromSide ) {
            this.pkt = pkt;
            this.fromSide = fromSide;
        }
    }
}
