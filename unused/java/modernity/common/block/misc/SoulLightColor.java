/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.misc;

import java.util.Random;

public enum SoulLightColor {
    ORANGE( 1, 0.3, 0.05 ),
    LIGHTBLUE( 0.5, 0.7, 1 ),
    BLUE( 0.05, 0.3, 1 ),
    GREEN( 0.3, 1, 0.1 ),
    YELLOW( 1, 0.9, 0.1 ),
    WHITE( 1, 1, 1 ),
    MAGENTA( 0.7, 0.2, 1 ),
    DEEP_ORANGE( 1, 0.04, 0 ),
    INDIGO( 0.04, 0, 1 ),
    GOLD( 1, 0.5, 0 ),
    PURPLE( 0.3, 0, 1 ),
    LIGHTGREEN( 0.7, 1, 0.5 ),
    LIGHTRED( 1, 0.6, 0.6 ),
    CYAN( 0.05, 0.8, 1 ),
    LIME( 0.6, 1, 0.05 ),
    DARKBLUE( 0, 0, 1 ),
    RED( 1, 0, 0 ),
    LIGHT_YELLOW( 1, 1, 0.6 ),
    PINK( 1, 0.2, 0.6 ),
    TEAL( 0, 1, 0.6 );

    public static final SoulLightColor DEFAULT = ORANGE;

    public final float red;
    public final float green;
    public final float blue;

    SoulLightColor( double red, double green, double blue ) {
        this.red = (float) red;
        this.green = (float) green;
        this.blue = (float) blue;
    }

    public static SoulLightColor fromOrdinal( int ordinal ) {
        if( ordinal < 0 || ordinal >= values().length ) {
            return DEFAULT;
        }
        return values()[ ordinal ];
    }

    public static SoulLightColor random( Random rand ) {
        return values()[ rand.nextInt( values().length ) ];
    }
}
