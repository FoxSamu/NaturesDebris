/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings;

import modernity.common.settings.core.AbstractSetting;
import modernity.common.settings.core.SettingsFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class LocalServerSettings extends SynchronizingServerSettings {
    private static final Logger LOGGER = LogManager.getLogger();

    public final DefaultServerSettings defaultSettings;

    public LocalServerSettings( String name, File file, DefaultServerSettings defaultSettings ) {
        super( name, file );
        this.defaultSettings = defaultSettings;

        setHeader( "The Modernity\n" + name + "\n\nWarning: Removing this file will reset all server options of the Modernity for this save..." );
    }

    public void syncUnexisting() {
        getFile().mergeWith( defaultSettings.getFile(), SettingsFile.ADD_NOT_EXISTING );
    }

    public void revertDefault() {
        getFile().mergeWith( defaultSettings.getFile(), SettingsFile.MERGE );
    }

    public <T> void revertToDefaultSetting( AbstractSetting<T> setting ) {
        AbstractSetting<T> defaultSetting = defaultSettings.getSetting( setting.getKey() );
        setting.set( defaultSetting.get() );
    }

    @Override
    protected void preLoad() {
        LOGGER.info( "Syncing local settings ({}) with default ({})", getName(), defaultSettings.getName() );
        syncUnexisting();
    }
}
