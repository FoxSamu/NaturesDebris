/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 26 - 2019
 */

package modernity.common.settings.core;

public abstract class CycleableSetting <T> extends AbstractSetting<T> {
    public CycleableSetting( String key ) {
        super( key );
    }

    public abstract void cycle( boolean reverse );

    @Override
    public CycleableSetting<T> formerly( String key ) {
        return (CycleableSetting<T>) super.formerly( key );
    }
}
