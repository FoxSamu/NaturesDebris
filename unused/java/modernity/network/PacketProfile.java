/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.network;

import java.util.EnumMap;
import java.util.function.Supplier;

/**
 * A packet profile maps packets to IDs for both client and server separately. This instance holds two registries: one
 * for the packets from the client and one for the packets from the server.
 */
public class PacketProfile {
    private final EnumMap<ESide, PacketRegistry> packetRegistries = new EnumMap<>( ESide.class );

    /**
     * Registers a packet class for a specific side, using a default constructor as factory. This method calls {@link
     * PacketRegistry#register(Class)} on the packet registry of the specified side and fails when the {@link
     * PacketRegistry} fails.
     *
     * @param side   The side this packet is being <b>SENT FROM!!</b>
     * @param packet The packet class to be registred.
     * @return The unique ID this packet is assigned to.
     *
     * @throws NullPointerException     Thrown by the underlying packet registry of the specified side, or when the
     *                                  specified side is {@code null}.
     * @throws IllegalArgumentException Thrown by the underlying packet registry of the specified side.
     * @see PacketRegistry#register(Class)
     */
    public int registerPacket( ESide side, Class<? extends Packet> packet ) {
        if( side == null ) throw new NullPointerException();
        return packetRegistries.computeIfAbsent( side, PacketRegistry::new ).register( packet );
    }

    /**
     * Registers a packet class for a specific side, using a a custom factory. This method calls {@link
     * PacketRegistry#register(Class, Supplier)} on the packet registry of the specified side and fails when the {@link
     * PacketRegistry} fails.
     *
     * @param side    The side this packet is being <b>SENT FROM!!</b>
     * @param packet  The packet class to be registred.
     * @param factory The packet factory used to reconstruct the packet during deserialization.
     * @return The unique ID this packet is assigned to.
     *
     * @throws NullPointerException     Thrown by the underlying packet registry of the specified side, or when the
     *                                  specified side is {@code null}.
     * @throws IllegalArgumentException Thrown by the underlying packet registry of the specified side.
     * @see PacketRegistry#register(Class, Supplier)
     * @see #registerPacket(ESide, Class)
     */
    public <T extends Packet> int registerPacket( ESide side, Class<T> packet, Supplier<T> factory ) {
        if( side == null ) throw new NullPointerException();
        return packetRegistries.computeIfAbsent( side, PacketRegistry::new ).register( packet, factory );
    }

    /**
     * Returns the ID of a packet from a specific side, or -1 when no such packet is registered.
     *
     * @param side The side the packet is being <b>SENT FROM!!</b>
     * @param pkt  The packet to get the ID of.
     * @return The unique ID of this packet.
     *
     * @throws NullPointerException Thrown when the specified side is {@code null}.
     * @see PacketRegistry#getID(Packet)
     * @see PacketRegistry#getID(Class)
     */
    public int getID( ESide side, Packet pkt ) {
        if( side == null ) throw new NullPointerException();
        return packetRegistries.get( side ).getID( pkt );
    }

    /**
     * Creates a new packet instance using the registered packet factory. This method fails when the specified ID is
     * negative, when no packet is registered to the specified ID or when the registered factory returned {@code null}.
     *
     * @param side The side to create a packet from.
     * @param id   The ID of the packet to reconstruct.
     * @throws NullPointerException     Thrown by the underlying {@link PacketRegistry} when the factory returned null,
     *                                  or when the specified side is {@code null}.
     * @throws IllegalArgumentException Thrown by the underlying {@link PacketRegistry} when the ID is invalid.
     */
    public Packet newPacket( ESide side, int id ) {
        if( side == null ) throw new NullPointerException();
        return packetRegistries.get( side ).getPacket( id );
    }
}
