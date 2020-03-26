/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.common.environment.precipitation;

import net.minecraft.util.IStringSerializable;

public enum EPrecipitationLevel implements IStringSerializable {
    NONE( "none", PrecipitationLevel.NONE ),
    LIGHT( "light", PrecipitationLevel.LIGHT ),
    MODERATE( "moderate", PrecipitationLevel.MODERATE ),
    REASONABLE( "reasonable", PrecipitationLevel.REASONABLE ),
    HEAVY( "heavy", PrecipitationLevel.HEAVY );

    private final String name;
    private final int level;

    EPrecipitationLevel( String name, int level ) {
        this.name = name;
        this.level = level;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public static EPrecipitationLevel fromInt( int level ) {
        return values()[ level ];
    }
}
