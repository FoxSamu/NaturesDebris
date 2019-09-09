/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings;

import modernity.common.net.pkt.SPacketSettingChange;
import modernity.common.net.pkt.SPacketSettingsInit;
import modernity.common.settings.core.AbstractSetting;
import modernity.common.settings.core.SettingsFile;
import modernity.common.util.ProxyCommon;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
import java.util.function.Consumer;

public abstract class SynchronizingServerSettings extends ServerSettings implements Consumer<AbstractSetting<?>> {
    public SynchronizingServerSettings( String name, SettingsFile file ) {
        super( name, file );
    }

    public SynchronizingServerSettings( String name, File file ) {
        super( name, file );
    }

    public SynchronizingServerSettings( String name ) {
        super( name );
    }

    public void shareWith( EntityPlayerMP entity ) {
        LOGGER.info( "Sharing remote settings with player: {}", entity.getName().getString() );
        SPacketSettingsInit packet = new SPacketSettingsInit();
        for( AbstractSetting<?> setting : this ) {
            packet.addSetting( setting );
        }
        ProxyCommon.network().sendToPlayer( packet, entity );
    }


    @Override
    protected <T, S extends AbstractSetting<T>> S addSetting( S setting ) {
        setting.addHandler( this );
        return super.addSetting( setting );
    }

    @Override
    public void accept( AbstractSetting<?> setting ) {
        ProxyCommon.network().sendToAll( new SPacketSettingChange( setting.getKey(), setting.serialize() ) );
    }
}
