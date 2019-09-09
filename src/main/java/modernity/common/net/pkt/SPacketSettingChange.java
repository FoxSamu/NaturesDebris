/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.net.pkt;

import modernity.client.util.ProxyClient;
import modernity.common.settings.RemoteServerSettings;
import modernity.common.settings.ServerSettings;
import modernity.net.IPacket;
import modernity.net.ProcessContext;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class SPacketSettingChange implements IPacket {
    private String setting;
    private String value;

    public SPacketSettingChange( String setting, String value ) {
        this.setting = setting;
        this.value = value;
    }

    public SPacketSettingChange() {

    }

    @Override
    public void write( PacketBuffer buf ) {
        buf.writeString( setting );
        buf.writeString( value );
    }

    @Override
    public void read( PacketBuffer buf ) {
        setting = buf.readString( 256 );
        value = buf.readString( 1024 );
    }

    @OnlyIn( Dist.CLIENT )
    @Override
    public void process( ProcessContext ctx ) {
        ctx.ensureMainThread();
        ServerSettings settings = ProxyClient.serverSettings();

        if( settings instanceof RemoteServerSettings ) {
            settings.getSetting( setting ).deserialize( value );
        }
    }
}
