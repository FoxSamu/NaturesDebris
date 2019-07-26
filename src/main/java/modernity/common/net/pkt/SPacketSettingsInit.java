/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.net.pkt;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import modernity.client.util.ProxyClient;
import modernity.common.settings.LocalServerSettings;
import modernity.common.settings.ServerSettings;
import modernity.common.settings.core.AbstractSetting;
import modernity.net.IPacket;
import modernity.net.ProcessContext;

import java.util.HashMap;
import java.util.Map;


public class SPacketSettingsInit implements IPacket {
    private final HashMap<String, String> settings = new HashMap<>();

    public void addSetting( AbstractSetting<?> setting ) {
        settings.put( setting.getKey(), setting.serialize() );
    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeVarInt( settings.size() );
        for( Map.Entry<String, String> entry : settings.entrySet() ) {
            buf.writeString( entry.getKey() );
            buf.writeString( entry.getValue() );
        }
    }

    @Override
    public void read( PacketBuffer buf ) {
        int size = buf.readVarInt();
        for( int i = 0; i < size; i++ ) {
            String setting = buf.readString( 256 );
            String value = buf.readString( 1024 );
            settings.put( setting, value );
        }
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();
        if( ProxyClient.serverSettings() instanceof LocalServerSettings ) return;
        ServerSettings settings = ProxyClient.get().startRemoteServerSettings();

        for( Map.Entry<String, String> entry : this.settings.entrySet() ) {
            settings.getSetting( entry.getKey() ).deserialize( entry.getValue() );
        }
    }
}
