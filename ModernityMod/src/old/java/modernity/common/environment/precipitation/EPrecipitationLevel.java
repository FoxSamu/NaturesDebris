/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.environment.precipitation;

import net.minecraft.util.IStringSerializable;

public enum EPrecipitationLevel implements IStringSerializable {
    NONE("none", PrecipitationLevel.NONE),
    LIGHT("light", PrecipitationLevel.LIGHT),
    MODERATE("moderate", PrecipitationLevel.MODERATE),
    REASONABLE("reasonable", PrecipitationLevel.REASONABLE),
    HEAVY("heavy", PrecipitationLevel.HEAVY);

    private final String name;
    private final int level;

    EPrecipitationLevel(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public static EPrecipitationLevel fromInt(int level) {
        return values()[level];
    }

    @Override
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}
