/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.common.net;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

public class MDPackets {
    private static final EnumPacketDirection CLIENTBOUND = EnumPacketDirection.CLIENTBOUND;
    private static final EnumPacketDirection SERVERBOUND = EnumPacketDirection.SERVERBOUND;

    public static void register() {
        registerPlay( SERVERBOUND, SPacketStructure.class );
        registerPlay( SERVERBOUND, SPacketOpenContainer.class );
    }

    private static final String SRG_EnumConnectionState_registerPacket = "func_179245_a";
    private static Method registerPacketMethod;

    private static void init() {
        if( registerPacketMethod == null ) {
            registerPacketMethod = ObfuscationReflectionHelper.findMethod( EnumConnectionState.class, SRG_EnumConnectionState_registerPacket, EnumPacketDirection.class, Class.class );
        }
    }

    public static void registerPacket( EnumConnectionState state, EnumPacketDirection direction, Class<? extends Packet<?>> packetClass ) {
        try {
            init();
            registerPacketMethod.invoke( state, direction, packetClass );
        } catch( Throwable problem ) {
            throw new RuntimeException( "Could not register packet", problem );
        }
    }

    public static void registerPlay( EnumPacketDirection direction, Class<? extends Packet<?>> packetClass ) {
        registerPacket( EnumConnectionState.PLAY, direction, packetClass );
    }

    public static void registerHandshaking( EnumPacketDirection direction, Class<? extends Packet<?>> packetClass ) {
        registerPacket( EnumConnectionState.HANDSHAKING, direction, packetClass );
    }

    public static void registerLogin( EnumPacketDirection direction, Class<? extends Packet<?>> packetClass ) {
        registerPacket( EnumConnectionState.LOGIN, direction, packetClass );
    }

    public static void registerStatus( EnumPacketDirection direction, Class<? extends Packet<?>> packetClass ) {
        registerPacket( EnumConnectionState.STATUS, direction, packetClass );
    }
}
