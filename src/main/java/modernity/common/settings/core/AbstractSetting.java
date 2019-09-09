/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings.core;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public abstract class AbstractSetting <T> {
    private final String key;
    private final HashSet<String> oldAliases = new HashSet<>();
    private boolean changed;
    private boolean synchronizes;
    private T value;
    private final ArrayList<Consumer<? super AbstractSetting<T>>> eventHandlers = new ArrayList<>();

    public AbstractSetting( String key ) {
        if( key == null ) throw new NullPointerException();
        this.key = key;
    }

    public T get() {
        return value;
    }

    public void set( T value ) {
        this.value = value;
        changed = true;
        triggerHandler();
    }

    protected void setSilently( T value ) {
        this.value = value;
        changed = true;
    }

    public void setDefault() {
        this.value = getDefault();
        changed = true;
        triggerHandler();
    }

    public String getKey() {
        return key;
    }

    public void forceSaving() {
        changed = true;
    }

    public void skipSaving() {
        changed = false;
    }

    @OnlyIn( Dist.CLIENT )
    public String formatValue() {
        return serialize();
    }

    public String getTranslationKey() {
        return "settings.modernity." + getKey();
    }

    public String getDescTranslationKey() {
        return getTranslationKey() + ".desc";
    }

    public AbstractSetting<T> formerly( String key ) {
        oldAliases.add( key );
        return this;
    }

    public AbstractSetting<T> synchronize( boolean synchronize ) {
        synchronizes = synchronize;
        return this;
    }

    public AbstractSetting<T> synchronize() {
        return synchronize( true );
    }

    boolean remap( SettingsFile file, HashSet<String> unused, String settingsName, Logger logger ) {
        unused.remove( key );
        for( String alias : oldAliases ) {
            unused.remove( alias );
            if( file.remapOld( alias, key ) ) {
                logger.info( "Remapped deprecated key {} to {} in settings {}", alias, key, settingsName );
                return true;
            }
        }
        return false;
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean doesSynchronize() {
        return synchronizes;
    }

    public void setSaved() {
        changed = false;
    }

    public abstract T getDefault();
    public abstract String serialize();
    public abstract void deserialize( String string );

    public void getSuggestions( String input, Consumer<String> output ) {
    }

    public boolean accepts( String input ) {
        return true;
    }

    public void addHandler( Consumer<? super AbstractSetting<T>> handler ) {
        if( handler == null ) return;
        eventHandlers.add( handler );
    }

    public void removeHandler( Consumer<? super AbstractSetting<T>> handler ) {
        eventHandlers.remove( handler );
    }

    protected void triggerHandler() {
        for( Consumer<? super AbstractSetting<T>> handler : eventHandlers ) {
            if( handler != null ) {
                handler.accept( this );
            }
        }
    }
}
