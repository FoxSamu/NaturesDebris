/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.util;

public enum ERainLevel {
    NONE( 0 ),
    DRIZZLE( 1 ),
    SHOWERS( 2 ),
    RAIN( 3 ),
    HAIL( 4 );

    private final int level;

    ERainLevel( int level ) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static ERainLevel lowest( ERainLevel a, ERainLevel b ) {
        if( a.level < b.level ) return a;
        else return b;
    }
}
