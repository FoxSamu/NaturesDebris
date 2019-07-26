/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings;

public class RemoteServerSettings extends ServerSettings {
    public RemoteServerSettings() {
        super( "Remote Server Settings" );
    }

    @Override
    public void save( boolean mayFail ) {
        LOGGER.info( "Did not save remote settings {} as they synchronize from a server", getName() );
    }

    @Override
    public void load( boolean mayFail ) {
        LOGGER.info( "Did not load remote settings {} as they synchronize from a server", getName() );
    }

    @Override
    public void flush() {
        LOGGER.info( "Did not flush remote settings {} as they synchronize from a server", getName() );
    }
}
