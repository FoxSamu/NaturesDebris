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

public class DefaultServerSettings extends ServerSettings {
    public static final File DEFAULT_LOCATION = new File( "./modernity/server.properties" );

    public DefaultServerSettings( File file ) {
        super( "Default Server Settings", file );
        setHeader( "The Modernity\nDefault Server Settings\n\nWarning: Removing this file will reset all default server options of the Modernity..." );
    }
}
