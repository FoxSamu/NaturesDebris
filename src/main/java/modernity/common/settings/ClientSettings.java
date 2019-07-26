/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings;

import modernity.common.settings.core.Settings;
import modernity.common.settings.core.setting.BooleanSetting;

import java.io.File;

public class ClientSettings extends Settings {
    public static final File DEFAULT_LOCATION = new File( "./modernity/client.properties" );

    public final BooleanSetting walkingParticles = addSetting( new BooleanSetting( "walkingParticles", true ) );

    public ClientSettings( File file ) {
        super( "Client Settings", file );
        setHeader( "The Modernity\nClient Settings\n\nWarning: Removing this file will reset all client options of the Modernity..." );
    }
}
