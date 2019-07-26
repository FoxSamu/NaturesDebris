/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings;

import java.io.File;

public class DedicatedServerSettings extends SynchronizingServerSettings {
    public static final File DEFAULT_LOCATION = new File( "./modernity/dedicated_server.properties" );

    public DedicatedServerSettings( File file ) {
        super( "Dedicated Server Settings", file );
        setHeader( "The Modernity\nDedicated Server Settings\n\nWarning: Removing this file will reset all dedicated server options of the Modernity..." );
    }
}
