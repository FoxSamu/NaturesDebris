/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings.core.setting;

import modernity.common.settings.core.AbstractSetting;

public class IntSetting extends AbstractSetting<Integer> {
    private final int def;

    public IntSetting( String key ) {
        this( key, 0 );
    }

    public IntSetting( String key, int def ) {
        super( key );
        this.def = def;
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
}
