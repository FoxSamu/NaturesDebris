/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.core.network;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * A packet registry maps packets to unique IDs and unique IDs to packets. Usually, two packet registries are used
 * together: one for packets from the server, and one for packets from the client (see {@link PacketProfile}). An
 * instance of this class maps IDs to packet classes, IDs to packet constructors (for convenience in constructing) and
 * packet classes to IDs, and can be used to reinstantiate packets from IDs.
 *
 * @see PacketProfile
 */
public class PacketRegistry {
    private int nextID;
    private final HashMap<Integer, Class<? extends IPacket>> decode = new HashMap<>();
    private final HashMap<Integer, Supplier<? extends IPacket>> constructors = new HashMap<>();
    private final HashMap<Class<? extends IPacket>, Integer> encode = new HashMap<>();

    public PacketRegistry() {

    }

    // Helper constructor used in PacketProfile
    @SuppressWarnings("unused")
    PacketRegistry(NetworkSide side) {
    }

    private static <T extends IPacket> Supplier<T> wrapConstructor(Constructor<T> constructor) {
        return () -> {
            try {
                return constructor.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("Unexpected InstantiationException while creating packet instance during deserialization.", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unexpected IllegalAccessException while creating packet instance during deserialization.", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Packet constructor threw an exception during deserialization.", e.getTargetException());
            }
        };
    }

    /**
     * Registers a packet class to this registry, automatically assigning an ID to this packet class. This method
     * returns the ID assigned to this packet. This method fails when at least one of the following situations applies:
     * <ul>
     * <li>The packet class is {@code null}.</li>
     * <li>The packet class is already registered to this registry.</li>
     * <li>The packet class is an interface.</li>
     * <li>The packet class is an enum.</li>
     * <li>The packet class is an abstract class.</li>
     * <li>The packet class has no visible constructor with no arguments.</li>
     * </ul>
     * In either case it will throw an {@link IllegalArgumentException}, except when the packet class is {@code null}.
     * In the {@code null}-case, it will throw an {@link NullPointerException} instead.
     *
     * @param packetClass The packet class to be registered.
     * @return The unique ID assigned to the registered packet.
     *
     * @throws NullPointerException     Thrown when the specified packet is {@code null}.
     * @throws IllegalArgumentException Thrown in any other case of failure.
     */
    public int register(Class<? extends IPacket> packetClass) {
        if (packetClass == null) {
            throw new NullPointerException("Packet to be registered was null");
        }
        if (encode.containsKey(packetClass)) {
            throw new IllegalArgumentException("A packet of " + packetClass + " is already registered to ID " + encode.get(packetClass));
        }
        // Check constructor before incrementing ID
        Constructor<? extends IPacket> constructor = constructor(packetClass);

        int id = nextID++;
        decode.put(id, packetClass);
        constructors.put(id, wrapConstructor(constructor));
        encode.put(packetClass, id);
        return id;
    }

    /**
     * Registers a packet class to this registry, by calling {@link IPacket#getClass()} on the specified packet. This
     * method throws a {@link NullPointerException} when the specified packet is {@code null}. This method redirects the
     * packet class to the {@link #register(Class)} method. That means that calling <code>registerClass( packet )</code>
     * is equivalent to calling <code>register( packet.getClass() )</code>.
     *
     * @param pkt The packet to be registered.
     * @return The unique ID assigned to the registered packet.
     *
     * @throws NullPointerException     Thrown when the specified packet is {@code null}.
     * @throws IllegalArgumentException Thrown in any other case of failure caused by the {@link #register} method.
     */
    public int register(IPacket pkt) {
        if (pkt == null) {
            throw new NullPointerException("Packet to be registered was null");
        }
        return register(pkt.getClass());
    }

    /**
     * Registers a packet class, using a custom factory for construction. Packets registered via this method can be
     * abstract, an interface or an enum (not recommended), and should not have a no-arg constructor, as long as the
     * supplier can still provide a packet instance during serialization. The factory does not necessarily have to
     * create new instances (e.g. enum packets can return enum constants), though it's recommended that it does. The
     * factory may never return {@code null}.
     *
     * @param packetClass The packet class to register.
     * @param factory     The factory that creates new instances during deserialization.
     * @throws NullPointerException     Thrown when either the packet class or the factory is {@code null}.
     * @throws IllegalArgumentException When the packet class to be registered is already registered.
     */
    public <T extends IPacket> int register(Class<T> packetClass, Supplier<T> factory) {
        if (packetClass == null) {
            throw new NullPointerException("Packet to be registered was null");
        }
        if (factory == null) {
            throw new NullPointerException("Packet factory was null");
        }
        if (encode.containsKey(packetClass)) {
            throw new IllegalArgumentException("A packet of " + packetClass + " is already registered to ID " + encode.get(packetClass));
        }

        int id = nextID++;
        decode.put(id, packetClass);
        constructors.put(id, factory);
        encode.put(packetClass, id);
        return id;
    }

    /**
     * Returns the unique ID linked to a registered packet class. This method returns -1 when the packet class is not
     * registered in this registry or when it is {@code null}.
     *
     * @param packetClass The packet class to get the unique ID of.
     * @return The unique ID of the specified packet class, or -1 when the packet is {@code null} or not registered.
     */
    public int getID(Class<? extends IPacket> packetClass) {
        if (!encode.containsKey(packetClass)) return -1;
        return encode.get(packetClass);
    }

    /**
     * Returns the unique ID linked to a registered packet. This method returns -1 when the packet is not registered in
     * this registry, or when the packet is {@code null}. This method redirects directly to {@link #getID(Class)}:
     * calling {@code getID(pkt)} is equivalent to calling {@code getID(pkt.getClass())}.
     *
     * @param packet The packet to get the unique ID of.
     * @return The unique ID of the specified packet, or -1 when the packet is {@code null} or not registered.
     */
    public int getID(IPacket packet) {
        if (packet == null) return -1;
        return getID(packet.getClass());
    }

    /**
     * Returns the packet class linked to the specified ID. This method returns {@code null} when either the specified
     * ID is negative or when no such packet is registered to the specified ID.
     *
     * @param id The ID to get the packet class of.
     * @return The registered packet class, or {@code null} when no such packet was registered or the specified ID was
     *     negative.
     */
    @Nullable
    public Class<? extends IPacket> getPacketClass(@Nonnegative int id) {
        return decode.get(id);
    }

    /**
     * Constructs a new packet from the specified ID. This method fails when no such packet is registered to the
     * specified ID, or when the registered packet factory returned {@code null}. This method never returns {@code
     * null}.
     *
     * @param id The ID of the packet to construct. Must not be negative.
     * @return The constructed packet.
     *
     * @throws IllegalArgumentException Thrown when no such packet was registered to the specified ID, or when the
     *                                  specified ID was negative.
     * @throws NullPointerException     When the registered factory returned {@code null}.
     */
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public IPacket getPacket(@Nonnegative int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Packet ID negative: " + id);
        }
        if (!constructors.containsKey(id)) {
            throw new IllegalArgumentException("No packet registered with ID " + id + ".");
        }
        IPacket constructed = constructors.get(id).get();
        if (constructed == null) {
            throw new NullPointerException("Packet factory returned null");
        }
        return constructed;
    }

    private Constructor<? extends IPacket> constructor(Class<? extends IPacket> pktClass) {
        if (pktClass.isInterface()) {
            throw new IllegalArgumentException("Packet class " + pktClass.getSimpleName() + " is an interface: it cannot be constructed during deserialization");
        }
        if (pktClass.isEnum()) {
            throw new IllegalArgumentException("Packet class " + pktClass.getSimpleName() + " is an enum: it cannot be constructed during deserialization");
        }
        if ((pktClass.getModifiers() & Modifier.ABSTRACT) != 0) {
            throw new IllegalArgumentException("Packet class " + pktClass.getSimpleName() + " is abstract: it cannot be constructed during deserialization");
        }
        try {
            return pktClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Packet class " + pktClass.getSimpleName() + " hasn't a public, no-argument constructor: it cannot be constructed during deserialization");
        }
    }
}
