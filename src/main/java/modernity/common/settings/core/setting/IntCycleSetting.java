/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings.core.setting;

import modernity.common.settings.core.CycleableSetting;

import java.util.function.Consumer;

public class IntCycleSetting extends CycleableSetting<Integer> {
    private final int def;
    private final int min;
    private final int max;

    public IntCycleSetting( String key, int def, int min, int max ) {
        super( key );
        if( max < min ) {
            int tmp = min;
            min = max;
            max = tmp;
        }
        this.def = Math.min( Math.max( def, min ), max );
        this.min = min;
        this.max = max;
    }

    @Override
    public void set( Integer value ) {
        super.set( Math.min( Math.max( value, min ), max ) );
    }

    @Override
    public Integer getDefault() {
        return def;
    }

    @Override
    public String serialize() {
        return get() + "";
    }

    @Override
    public void deserialize( String string ) {
        try {
            set( Integer.parseInt( string ) );
        } catch( Throwable t ) {
            setDefault();
        }
    }

    @Override
    public void cycle( boolean reverse ) {
        int next = get() + ( reverse ? - 1 : 1 );
        if( next < min ) next = max;
        if( next > max ) next = min;
        set( next );
    }

    public void getSuggestions( String input, Consumer<String> output ) {
        for( int i = min; i <= max; i++ ) {
            String str = input + "";
            if( str.equals( input ) ) {
                output.accept( str );
            }
        }
        for( int i = min; i <= max; i++ ) {
            String str = input + "";
            if( str.startsWith( input ) ) {
                output.accept( str );
            }
        }
    }

    @Override
    public boolean accepts( String input ) {
        try {
            int i = Integer.parseInt( input );
            return i >= min && i <= max;
        } catch( Throwable t ) {
            return false;
        }
    }
}
