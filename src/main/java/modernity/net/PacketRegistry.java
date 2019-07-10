/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.net;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class PacketRegistry {
    private int nextID;
    private final HashMap<Integer, Class<? extends IPacket>> decode = new HashMap<>();
    private final HashMap<Integer, Constructor<? extends IPacket>> constructors = new HashMap<>();
    private final HashMap<Class<? extends IPacket>, Integer> encode = new HashMap<>();

    public void register( @Nonnull Class<? extends IPacket> packetClass ) {
        // Check constructor before incrementing ID
        Constructor<? extends IPacket> constructor = constructor( packetClass );

        int id = nextID++;
        decode.put( id, packetClass );
        constructors.put( id, constructor );
        encode.put( packetClass, id );
    }

    public int getID( Class<? extends IPacket> packetClass ) {
        if( ! encode.containsKey( packetClass ) ) return - 1;
        return encode.get( packetClass );
    }

    public int getID( @Nonnull IPacket packet ) {
        return getID( packet.getClass() );
    }

    @Nullable
    public Class<? extends IPacket> getPacketClass( @Nonnegative int id ) {
        return decode.get( id );
    }

    @Nonnull
    public IPacket getPacket( @Nonnegative int id ) {
        if( ! constructors.containsKey( id ) ) {
            throw new RuntimeException( "No packet registered with ID " + id + "." );
        }
        try {
            return constructors.get( id ).newInstance();
        } catch( InstantiationException e ) {
            throw new RuntimeException( "Unexpected InstantiationException while creating packet instance during deserialization.", e );
        } catch( IllegalAccessException e ) {
            throw new RuntimeException( "Unexpected IllegalAccessException while creating packet instance during deserialization.", e );
        } catch( InvocationTargetException e ) {
            throw new RuntimeException( "Packet constructor threw an exception during deserialization.", e.getTargetException() );
        }
    }

    private Constructor<? extends IPacket> constructor( Class<? extends IPacket> pktClass ) {
        if( pktClass == null ) {
            throw new IllegalArgumentException( "Null packet class" );
        }
        if( pktClass.isInterface() ) {
            throw new IllegalArgumentException( "Packet class " + pktClass.getSimpleName() + " is an interface: it cannot be constructed during deserialization" );
        }
        if( pktClass.isEnum() ) {
            throw new IllegalArgumentException( "Packet class " + pktClass.getSimpleName() + " is an enum: it cannot be constructed during deserialization" );
        }
        if( ( pktClass.getModifiers() & Modifier.ABSTRACT ) != 0 ) {
            throw new IllegalArgumentException( "Packet class " + pktClass.getSimpleName() + " is abstract: it cannot be constructed during deserialization" );
        }
        try {
            return pktClass.getConstructor();
        } catch( NoSuchMethodException e ) {
            throw new IllegalArgumentException( "Packet class " + pktClass.getSimpleName() + " hasn't a no-argument constructor: it cannot be constructed during deserialization" );
        }
    }
}
