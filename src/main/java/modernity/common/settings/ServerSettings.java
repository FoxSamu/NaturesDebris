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
import modernity.common.settings.core.SettingsFile;
import modernity.common.settings.core.setting.IntCycleSetting;

import java.io.File;

public abstract class ServerSettings extends Settings {

    public final IntCycleSetting moonPhase = addSetting( new IntCycleSetting( "moonPhase", 0, 0, 7 ) );
    public final IntCycleSetting settingsCmdPermission = addSetting( new IntCycleSetting( "settingsCmdPermission", 3, 0, 4 ) );

    public ServerSettings( String name, SettingsFile file ) {
        super( name, file );
    }

    public ServerSettings( String name, File file ) {
        super( name, file );
    }

    public ServerSettings( String name ) {
        super( name );
    }

}
