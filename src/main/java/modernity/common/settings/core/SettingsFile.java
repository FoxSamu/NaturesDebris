/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings.core;

import com.google.common.io.Files;
import modernity.api.util.Pair;

import java.io.*;
import java.util.*;

public class SettingsFile implements Iterable<Pair<String, String>> {
    public static final SettingMerger OVERWRITE = ( file, key, loaded, existing ) -> loaded;
    public static final SettingMerger MERGE = ( file, key, loaded, existing ) -> loaded == null ? existing : loaded;
    public static final SettingMerger OVERWRITE_EXISTING = ( file, key, loaded, existing ) -> existing == null ? null : loaded;
    public static final SettingMerger ADD_NOT_EXISTING = ( file, key, loaded, existing ) -> existing == null ? loaded : existing;
    public static final SettingMerger DO_NOT_MERGE = ( file, key, loaded, existing ) -> existing;
    public static final SettingMerger REMOVE_ALL = ( file, key, loaded, existing ) -> null;
    public static final SettingMerger INTERSECT_LOADED = ( file, key, loaded, existing ) -> existing == null || loaded == null ? null : loaded;
    public static final SettingMerger INTERSECT_EXISTING = ( file, key, loaded, existing ) -> existing == null || loaded == null ? null : existing;

    private final File path;
    private final HashMap<String, String> settings = new HashMap<>();
    private boolean changed;

    private String header;

    public SettingsFile( File path ) {
        this.path = path;
    }

    public SettingsFile() {
        this( null );
    }

    public File getPath() {
        return path;
    }

    public void set( String key, String value ) {
        if( key == null || value == null ) {
            throw new NullPointerException();
        }
        changed = true;
        settings.put( key, value );
    }

    public String get( String key ) {
        if( key == null ) {
            throw new NullPointerException();
        }
        return settings.get( key );
    }

    public String getOrDefault( String key, String def ) {
        if( key == null || def == null ) {
            throw new NullPointerException();
        }
        if( ! settings.containsKey( key ) ) {
            return def;
        }
        return settings.get( key );
    }

    public boolean isMissing( String key ) {
        if( key == null ) {
            throw new NullPointerException();
        }
        return ! settings.containsKey( key );
    }

    public boolean isAvailable( String key ) {
        if( key == null ) {
            throw new NullPointerException();
        }
        return settings.containsKey( key );
    }

    public boolean defineMissing( String key, String def ) {
        if( key == null || def == null ) {
            throw new NullPointerException();
        }

        if( settings.containsKey( key ) ) return false;
        settings.put( key, def );
        changed = true;
        return true;
    }

    public boolean removeOld( String key ) {
        if( key == null ) {
            throw new NullPointerException();
        }

        if( ! settings.containsKey( key ) ) return false;
        settings.remove( key );
        changed = true;
        return true;
    }

    public boolean remapOld( String oldkey, String newkey ) {
        if( oldkey == null || newkey == null ) {
            throw new NullPointerException();
        }
        if( ! settings.containsKey( oldkey ) ) {
            return false;
        }
        if( ! settings.containsKey( newkey ) ) {
            settings.put( newkey, settings.get( oldkey ) );
        }
        changed = true;
        settings.remove( oldkey );
        return true;
    }

    public void forceSaving() {
        changed = true;
    }

    public void discardSaving() {
        changed = false;
    }

    public boolean doesSaving() {
        return changed;
    }

    public boolean save() throws IOException {
        if( path == null ) {
            throw new IOException( "Saving virtual settings file" );
        }
        if( ! changed ) {
            return false;
        }
        if( ! path.exists() ) {
            Files.createParentDirs( path );
            path.createNewFile();
        }
        if( ! path.canWrite() ) {
            throw new IOException( "May not write to settings file: " + path );
        }
        FileOutputStream fos = new FileOutputStream( path );
        PrintStream stream = new PrintStream( fos );
        if( header != null ) {
            String[] headerLines = header.split( "[\\n\\r]" );
            for( String line : headerLines ) {
                if( line.isEmpty() ) {
                    stream.println( "#" );
                } else {
                    stream.println( "# " + line );
                }
            }
        }
        for( Map.Entry<String, String> entry : settings.entrySet() ) {
            if( entry.getValue().isEmpty() ) {
                stream.println( entry.getKey() );
            } else {
                stream.println( entry.getKey() + "=" + entry.getValue() );
            }
        }
        if( stream.checkError() ) {
            throw new IOException( "Failed to write settings file: " + path );
        }
        fos.close();
        changed = false;
        return true;
    }

    public void load( SettingMerger merger ) throws IOException {
        if( path == null ) {
            throw new IOException( "Loading virtual settings file" );
        }
        if( ! path.exists() ) {
            Files.createParentDirs( path );
            path.createNewFile();
            changed = true;
        }
        if( ! path.canRead() ) {
            throw new IOException( "May not read from settings file: " + path );
        }
        FileReader fr = new FileReader( path );
        Scanner scanner = new Scanner( fr );

        HashMap<String, String> otherSettingsMap = new HashMap<>();
        HashMap<String, String> mySettingsMap = new HashMap<>( settings );
        Set<String> keySet = new HashSet<>( settings.keySet() );
        clear();
        while( scanner.hasNextLine() ) {
            String line = scanner.nextLine().trim();
            if( scanner.ioException() != null ) {
                throw scanner.ioException();
            }
            if( line.isEmpty() || line.startsWith( "#" ) ) {
                continue; // Skip comments and empty lines...
            }
            int sep = line.indexOf( '=' );
            if( sep == - 1 ) { // Empty string case...
                otherSettingsMap.put( line, "" );
                keySet.add( line );
            } else {
                String key = line.substring( 0, sep );
                String value = line.substring( sep + 1 );
                otherSettingsMap.put( key, value );
                keySet.add( key );
            }
        }

        fr.close();

        for( String key : keySet ) {
            if( key == null ) continue;
            String my = mySettingsMap.get( key );
            String other = otherSettingsMap.get( key );
            String merged = merger.merge( this, key, other, my );
            if( merged != null ) {
                set( key, merged );
            }
        }
    }

    public void mergeWith( SettingsFile file, SettingMerger merger ) {
        HashMap<String, String> mySettingsMap = new HashMap<>( settings );
        Set<String> keySet = new HashSet<>( settings.keySet() );
        keySet.addAll( file.settings.keySet() );
        clear();

        for( String key : keySet ) {
            if( key == null ) continue;
            String my = mySettingsMap.get( key );
            String other = file.settings.get( key );
            String merged = merger.merge( this, key, other, my );
            if( merged != null ) {
                set( key, merged );
            }
        }
    }

    public void clear() {
        if( ! settings.isEmpty() ) changed = true;
        settings.clear();
    }

    public boolean isVirtual() {
        return path == null;
    }

    public void setHeader( String header ) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    @Override
    public Iterator<Pair<String, String>> iterator() {
        return new Itr( settings.keySet().iterator() );
    }

    private class Itr implements Iterator<Pair<String, String>> {
        private final Iterator<String> keyIterator;

        private Itr( Iterator<String> string ) {
            this.keyIterator = string;
        }

        @Override
        public boolean hasNext() {
            return keyIterator.hasNext();
        }

        @Override
        public Pair<String, String> next() {
            String str = keyIterator.next();
            return new Pair<>( str, settings.get( str ) );
        }

        @Override
        public void remove() {
            keyIterator.remove();
        }
    }

    public interface SettingMerger {
        String merge( SettingsFile file, String key, String loadedValue, String existingValue );
    }
}
