/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.core.network;

import natures.debris.core.util.DimensionRegion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A packet channel sends packets over a {@link SimpleChannel}.
 */
public class PacketChannel {
    public static final PacketDistributor<DimensionRegion> IN_REGION = new PacketDistributor<>(PacketChannel::msgToRegion, NetworkDirection.PLAY_TO_CLIENT);
    public static final PacketDistributor<String> USERNAME = new PacketDistributor<>(PacketChannel::msgToUsername, NetworkDirection.PLAY_TO_CLIENT);

    private final SimpleChannel channel;
    private final PacketProfile profile = new PacketProfile();
    private final int npv;

    private boolean registryLocked;

    /**
     * Constructs a packet channel.
     *
     * @param name     The channel name.
     * @param npv      The Network Protocol Version of this channel.
     * @param minLimit The minimum NPV for this channel to work. This value is automatically clamped to the current NPV
     *                 when it's higher than the current NPV.
     */
    public PacketChannel(ResourceLocation name, int npv, int minLimit) {
        this.npv = npv;
        Predicate<String> predicate = minLimit >= npv ? matchNPV(npv) : rangeNPV(minLimit, npv);
        channel = NetworkRegistry.newSimpleChannel(name, createNPV(npv), predicate, predicate);

        channel.messageBuilder(PacketMessage.class, 0)
               .encoder(this::write)
               .decoder(this::read)
               .consumer(this::process)
               .add();
    }

    /**
     * Constructs a packet channel.
     *
     * @param name The channel name.
     * @param npv  The Network Protocol Version of this channel, which will be used as minimum.
     */
    public PacketChannel(ResourceLocation name, int npv) {
        this(name, npv, npv);
    }

    @SuppressWarnings("unused")
    private static Consumer<net.minecraft.network.IPacket<?>> msgToRegion(PacketDistributor<DimensionRegion> distributor, Supplier<DimensionRegion> regionSupplier) {
        return pkt -> {
            DimensionRegion region = regionSupplier.get();

            getServer().getWorld(region.getDimension())
                       .getEntitiesWithinAABB(ServerPlayerEntity.class, region.getRegion())
                       .forEach(player -> player.connection.sendPacket(pkt));
        };
    }

    @SuppressWarnings("unused")
    private static Consumer<net.minecraft.network.IPacket<?>> msgToUsername(PacketDistributor<String> distributor, Supplier<String> usernameSupplier) {
        return pkt -> {
            String username = usernameSupplier.get();
            ServerPlayerEntity player = getServer().getPlayerList().getPlayerByUsername(username);
            if (player != null) {
                player.connection.sendPacket(pkt);
            }
        };
    }

    private boolean send(IPacket pkt, PacketDistributor.PacketTarget target, NetworkSide fromSide) {
        if (pkt.refuseSending()) {
            return false;
        }
        channel.send(target, new PacketMessage(pkt, fromSide));
        return true;
    }

    public net.minecraft.network.IPacket<?> toPlayClientPacket(IPacket pkt) {
        return channel.toVanillaPacket(new PacketMessage(pkt, NetworkSide.SERVER), NetworkDirection.PLAY_TO_CLIENT);
    }

    public net.minecraft.network.IPacket<?> toPlayServerPacket(IPacket pkt) {
        return channel.toVanillaPacket(new PacketMessage(pkt, NetworkSide.CLIENT), NetworkDirection.PLAY_TO_SERVER);
    }

    public net.minecraft.network.IPacket<?> toLoginClientPacket(IPacket pkt) {
        return channel.toVanillaPacket(new PacketMessage(pkt, NetworkSide.SERVER), NetworkDirection.LOGIN_TO_CLIENT);
    }

    public net.minecraft.network.IPacket<?> toLoginServerPacket(IPacket pkt) {
        return channel.toVanillaPacket(new PacketMessage(pkt, NetworkSide.CLIENT), NetworkDirection.LOGIN_TO_SERVER);
    }

    /**
     * Sends a packet from the client to the server.
     *
     * @param pkt The packet to send.
     */
    public void sendToServer(IPacket pkt) {
        send(pkt, PacketDistributor.SERVER.noArg(), NetworkSide.CLIENT);
    }

    /**
     * Sends a packet from the server to a specific player's client.
     *
     * @param pkt    The packet to send.
     * @param player The player to send to.
     */
    public void sendToPlayer(IPacket pkt, ServerPlayerEntity player) {
        send(pkt, PacketDistributor.PLAYER.with(() -> player), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to the specified players' clients.
     *
     * @param pkt     The packet to send.
     * @param players The list of players to send to.
     * @see #sendToPlayers(IPacket, Collection)
     * @see #sendToPlayer(IPacket, ServerPlayerEntity)
     */
    public void sendToPlayers(IPacket pkt, ServerPlayerEntity... players) {
        for (ServerPlayerEntity e : players) {
            sendToPlayer(pkt, e);
        }
    }

    /**
     * Sends a packet from the server to the specified players' clients.
     *
     * @param pkt     The packet to send.
     * @param players The list of players to send to.
     * @see #sendToPlayers(IPacket, ServerPlayerEntity...)
     * @see #sendToPlayer(IPacket, ServerPlayerEntity)
     */
    public void sendToPlayers(IPacket pkt, Collection<ServerPlayerEntity> players) {
        for (ServerPlayerEntity e : players) {
            sendToPlayer(pkt, e);
        }
    }

    /**
     * Sends a packet from the server to the specified players' clients.
     *
     * @param pkt     The packet to send.
     * @param players The list of players to send to.
     * @see #sendToPlayers(IPacket, ServerPlayerEntity...)
     * @see #sendToPlayer(IPacket, ServerPlayerEntity)
     */
    public void sendToPlayers(IPacket pkt, Stream<ServerPlayerEntity> players) {
        players.forEach(e -> sendToPlayer(pkt, e));
    }

    /**
     * Sends a packet from the server to all clients in a specific dimension.
     *
     * @param pkt   The packet to send.
     * @param dimen The dimension to send to.
     */
    public void sendToDimen(IPacket pkt, DimensionType dimen) {
        send(pkt, PacketDistributor.DIMENSION.with(() -> dimen), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to all connected clients.
     *
     * @param pkt The packet to send.
     */
    public void sendToAll(IPacket pkt) {
        send(pkt, PacketDistributor.ALL.noArg(), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to all players in a specific spherical area in a specific dimension.
     *
     * @param pkt   The packet to send.
     * @param pt    The center point of the spherical area.
     * @param rad   The radius of the spherical area.
     * @param dimen The dimension to send to.
     */
    public void sendToRange(IPacket pkt, Vec3d pt, double rad, DimensionType dimen) {
        send(pkt, PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pt.x, pt.y, pt.z, rad * rad, dimen)), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to all players in a specific spherical area in a specific dimension, excluding a
     * specific player.
     *
     * @param pkt     The packet to send.
     * @param pt      The center point of the spherical area.
     * @param rad     The radius of the spherical area.
     * @param dimen   The dimension to send to.
     * @param exclude The player to exclude.
     */
    public void sendToRange(IPacket pkt, Vec3d pt, double rad, DimensionType dimen, ServerPlayerEntity exclude) {
        send(pkt, PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(exclude, pt.x, pt.y, pt.z, rad * rad, dimen)), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to all players tracking the specific entity.
     *
     * @param pkt       The packet to send.
     * @param trackable The trackable entity.
     */
    public void sendToEntityTrackers(IPacket pkt, Entity trackable) {
        send(pkt, PacketDistributor.TRACKING_ENTITY.with(() -> trackable), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to all players tracking the specific entity, and when the entity is a player, also
     * to that entity itself.
     *
     * @param pkt       The packet to send.
     * @param trackable The trackable entity.
     */
    public void sendToSelfAndTrackers(IPacket pkt, Entity trackable) {
        send(pkt, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> trackable), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to all players tracking the specified chunk.
     *
     * @param pkt       The packet to send.
     * @param trackable The trackable chunk.
     */
    public void sendToChunkTrackers(IPacket pkt, Chunk trackable) {
        send(pkt, PacketDistributor.TRACKING_CHUNK.with(() -> trackable), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to the client over the specified connection.
     *
     * @param pkt     The packet to send.
     * @param manager The connection to send the packet over.
     */
    public void sendToNetManager(IPacket pkt, NetworkManager manager) {
        List<NetworkManager> managerList = Collections.singletonList(manager);
        send(pkt, PacketDistributor.NMLIST.with(() -> managerList), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to the client over the specified list of connections.
     *
     * @param pkt      The packet to send.
     * @param managers The connections to send the packet over.
     */
    public void sendToNetManagers(IPacket pkt, NetworkManager... managers) {
        List<NetworkManager> managerList = Arrays.asList(managers);
        send(pkt, PacketDistributor.NMLIST.with(() -> managerList), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to the client over the specified list of connections.
     *
     * @param pkt      The packet to send.
     * @param managers The connections to send the packet over.
     */
    public void sendToNetManagers(IPacket pkt, Collection<? extends NetworkManager> managers) {
        List<NetworkManager> managerList = new ArrayList<>(managers);
        send(pkt, PacketDistributor.NMLIST.with(() -> managerList), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to all players in a cuboid area.
     *
     * @param pkt   The packet to send.
     * @param box   The area cuboid.
     * @param dimen The dimension to send to.
     */
    public void sendToRegion(IPacket pkt, AxisAlignedBB box, DimensionType dimen) {
        DimensionRegion region = new DimensionRegion(box, dimen);
        send(pkt, IN_REGION.with(() -> region), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to all players in a cuboid area.
     *
     * @param pkt    The packet to send.
     * @param region The dimension and cuboid region to send to.
     */
    public void sendToRegion(IPacket pkt, DimensionRegion region) {
        send(pkt, IN_REGION.with(() -> region), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to the player with the specified username. This sends no packet when the specified
     * username does not exist.
     *
     * @param pkt      The packet to send.
     * @param username The username to send to.
     */
    public void sendToUsername(IPacket pkt, String username) {
        send(pkt, USERNAME.with(() -> username), NetworkSide.SERVER);
    }

    /**
     * Sends a packet from the server to the players with the specified usernames. This skips all usernames that are not
     * on the server.
     *
     * @param pkt       The packet to send.
     * @param usernames The list of usernames to send to.
     */
    public void sendToUsernames(IPacket pkt, String... usernames) {
        for (String name : usernames) {
            sendToUsername(pkt, name);
        }
    }

    /**
     * Sends a packet from the server to the players with the specified usernames. This skips all usernames that are not
     * on the server.
     *
     * @param pkt       The packet to send.
     * @param usernames The list of usernames to send to.
     */
    public void sendToUsernames(IPacket pkt, Collection<String> usernames) {
        for (String name : usernames) {
            sendToUsername(pkt, name);
        }
    }

    /**
     * Locks the packet registry. This prevents new packets from being registered after the network is opened.
     */
    public void lock() {
        registryLocked = true;
    }

    private static Supplier<String> createNPV(int npv) {
        String s = "MDNPV " + npv;
        return () -> s;
    }

    private static Predicate<String> rangeNPV(int min, int max) {
        return s -> {
            if (!s.startsWith("MDNPV ")) return false;
            try {
                int npv = Integer.parseInt(s.substring(6));
                return npv >= min && npv <= max;
            } catch (NumberFormatException exc) {
                return false;
            }
        };
    }

    private static Predicate<String> matchNPV(int npv) {
        return s -> {
            if (!s.startsWith("MDNPV ")) return false;
            try {
                int npv1 = Integer.parseInt(s.substring(6));
                return npv1 == npv;
            } catch (NumberFormatException exc) {
                return false;
            }
        };
    }

    /**
     * Returns the network protocol version of this channel.
     *
     * @return The network protocol version.
     */
    public int getNetProtocolVersion() {
        return npv;
    }

    /**
     * Registers a packet to send over this channel.
     *
     * @param side     The side this packet is <b>SENT FROM!!!</b>
     * @param pktClass The packet class to register.
     * @throws IllegalStateException When the packet registry is locked.
     */
    public void register(NetworkSide side, Class<? extends IPacket> pktClass) {
        if (registryLocked) {
            throw new IllegalStateException("Packet registry time is over");
        }
        if (side == null) {
            profile.registerPacket(NetworkSide.SERVER, pktClass);
            profile.registerPacket(NetworkSide.CLIENT, pktClass);
        } else {
            profile.registerPacket(side, pktClass);
        }
    }

    /**
     * Registers a packet to send over this channel, with a custom packet factory.
     *
     * @param side     The side this packet is <b>SENT FROM!!!</b>
     * @param pktClass The packet class to register.
     * @param factory  The packet factory.
     * @throws IllegalStateException When the packet registry is locked.
     */
    public <T extends IPacket> void register(NetworkSide side, Class<T> pktClass, Supplier<T> factory) {
        if (registryLocked) {
            throw new IllegalStateException("Packet registry time is over");
        }
        if (side == null) {
            profile.registerPacket(NetworkSide.SERVER, pktClass, factory);
            profile.registerPacket(NetworkSide.CLIENT, pktClass, factory);
        } else {
            profile.registerPacket(side, pktClass, factory);
        }
    }

    private void write(PacketMessage msg, PacketBuffer buff) {
        int id = profile.getID(msg.fromSide, msg.pkt);
        if (id < 0) {
            throw new RuntimeException("Unable to serialize packet: Unregistered packet " + msg.pkt.getName());
        }
        buff.writeByte(msg.fromSide == NetworkSide.CLIENT ? 1 : 0);
        buff.writeInt(id);
        msg.pkt.write(buff);
    }

    private PacketMessage read(PacketBuffer buff) {
        byte sideBit = buff.readByte();
        int id = buff.readInt();
        NetworkSide side = sideBit > 0 ? NetworkSide.CLIENT : NetworkSide.SERVER;

        IPacket pkt = profile.newPacket(side, id);

        pkt.read(buff);
        return new PacketMessage(pkt, side);
    }

    private void process(PacketMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        NetworkSide origin = NetworkSide.fromLogical(ctx.getDirection().getOriginationSide());
        ProcessContext pctx = new ProcessContext(origin, ctx.getSender(), this, msg.pkt);
        msg.pkt.process(pctx);
        ctx.setPacketHandled(true);
    }

    private static MinecraftServer getServer() {
        return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
    }

    private static class PacketMessage {
        private final IPacket pkt;
        private final NetworkSide fromSide;

        private PacketMessage(IPacket pkt, NetworkSide fromSide) {
            this.pkt = pkt;
            this.fromSide = fromSide;
        }
    }
}
