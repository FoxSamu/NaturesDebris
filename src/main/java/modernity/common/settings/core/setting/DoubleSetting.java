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

public class DoubleSetting extends AbstractSetting<Double> {
    private final double def;

    public DoubleSetting( String key, double def ) {
        super( key );
        this.def = def;
    }

    @Override
    public Double getDefault() {
        return def;
    }

    @Override
    public String serialize() {
        return get() + "";
    }

    @Override
    public void deserialize( String string ) {
        try {
            set( Double.parseDouble( string ) );
        } catch( Throwable thr ) {
            setDefault();
        }
    }
}
