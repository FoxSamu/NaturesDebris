/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings.core;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import modernity.api.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public abstract class Settings implements Iterable<AbstractSetting<?>> {
    public static final Logger LOGGER = LogManager.getLogger();
    private final String name;
    private final SettingsFile file;
    private final HashMap<String, AbstractSetting<?>> settings = new HashMap<>();

    public Settings( String name, SettingsFile file ) {
        this.file = file;
        this.name = name;
    }

    public Settings( String name, File file ) {
        this( name, new SettingsFile( file ) );
    }

    public Settings( String name ) {
        this( name, new SettingsFile() );
    }

    public SettingsFile getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public <T> AbstractSetting<T> getSetting( String key ) {
        return (AbstractSetting<T>) settings.get( key );
    }

    public void flush() {
        LOGGER.info( "Flusing settings {}", name );
        HashSet<String> unused = new HashSet<>();

        for( Pair<String, String> prop : file ) {
            unused.add( prop.getLeft() );
        }

        for( AbstractSetting<?> setting : settings.values() ) {
            if( setting.isChanged() ) {
                setting.setSaved();
                String serialized = setting.serialize();
                file.set( setting.getKey(), serialized );
                LOGGER.debug( "Flushing changed setting {} with value {}, for settings {}", setting.getKey(), serialized, name );
            }
            unused.remove( setting.getKey() );
        }

        for( String key : unused ) {
            if( file.removeOld( key ) ) {
                LOGGER.info( "Removed old setting {} for settings {}", key, name );
            }
        }
    }

    public void reset() {
        LOGGER.info( "Resetting settings {} to last flush", name );
        for( AbstractSetting<?> setting : settings.values() ) {
            String val = file.get( setting.getKey() );
            if( val != null ) {
                setting.deserialize( val );
            } else {
                setting.setDefault();
            }
        }
    }

    public Settings forceSaving() {
        file.forceSaving();
        return this;
    }

    public Settings discardSaving() {
        file.discardSaving();
        return this;
    }

    public void setHeader( String header ) {
        file.setHeader( header );
    }

    public String getHeader() {
        return file.getHeader();
    }


    protected <T, S extends AbstractSetting<T>> S addSetting( S setting ) {
        settings.put( setting.getKey(), setting );
        return setting;
    }

    protected void preLoad() {

    }

    public void load( boolean mayFail ) {
        try {
            if( ! file.getPath().exists() ) {
                LOGGER.info( "Settings file for settings {} does not exist, it will be created...", name );
            }
            file.load( SettingsFile.OVERWRITE );
            LOGGER.info( "Loaded settings file for settings {}", name );
        } catch( IOException exc ) {
            LOGGER.error( "Did not load settings {} due to IOException!", name );
            if( ! mayFail ) {
                LOGGER.error( "Crashing game!" );
                CrashReport report = CrashReport.makeCrashReport( exc, "Loading modernity settings" );
                CrashReportCategory category = report.makeCategory( "Loading settings" );
                category.addDetail( "Settings to load", () -> name );
                category.addDetail( "File to load from", () -> file.getPath().toString() );
                category.addDetail( "File existed", () -> file.getPath().exists() + "" );
                throw new ReportedException( report );
            } else {
                LOGGER.error( "What went wrong:", exc );
                LOGGER.error( "Settings will be reset to default values...", exc );
            }
        }

        preLoad();

        HashSet<String> unused = new HashSet<>();
        for( Pair<String, String> prop : file ) {
            unused.add( prop.getLeft() );
        }

        for( AbstractSetting<?> setting : settings.values() ) {
            setting.remap( file, unused, name, LOGGER );

            String val = file.get( setting.getKey() );
            if( val != null ) {
                setting.deserialize( val );
                setting.skipSaving();
            } else {
                LOGGER.info( "Loaded unexisting setting {} for settings {}, using default value", setting.getKey(), name );
                setting.setDefault();
                setting.forceSaving();
            }
        }

        for( String key : unused ) {
            if( file.removeOld( key ) ) {
                LOGGER.info( "Removed old setting {} for settings {}", key, name );
            }
        }
        LOGGER.info( "Loaded settings {}", name );
    }

    public void save( boolean mayFail ) {
        flush();
        try {
            if( ! file.getPath().exists() ) {
                LOGGER.info( "Settings file for settings {} does not exist, it will be created...", name );
            }
            if( file.save() ) {
                LOGGER.info( "Saved settings {}", name );
            } else {
                LOGGER.info( "No need for saving settings {} as they didn't change...", name );
            }
        } catch( IOException exc ) {
            LOGGER.error( "Did not save settings {} due to IOException!", name );
            if( ! mayFail ) {
                LOGGER.error( "Crashing game!" );
                CrashReport report = CrashReport.makeCrashReport( exc, "Saving modernity settings" );
                CrashReportCategory category = report.makeCategory( "Saving settings" );
                category.addDetail( "Settings to save", () -> name );
                category.addDetail( "File to save to", () -> file.getPath().toString() );
                category.addDetail( "File existed", () -> file.getPath().exists() + "" );
                throw new ReportedException( report );
            } else {
                LOGGER.error( "What went wrong:", exc );
            }
        }
    }

    @Override
    public Iterator<AbstractSetting<?>> iterator() {
        return settings.values().iterator();
    }

    private static class Itr implements Iterator<AbstractSetting<?>> {

        private final Iterator<AbstractSetting<?>> backing;

        private Itr( Iterator<AbstractSetting<?>> backing ) {
            this.backing = backing;
        }

        @Override
        public boolean hasNext() {
            return backing.hasNext();
        }

        @Override
        public AbstractSetting<?> next() {
            return backing.next();
        }
    }
}
