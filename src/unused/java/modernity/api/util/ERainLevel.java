/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.api.util;

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
