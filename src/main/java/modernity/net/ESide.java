/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 11 - 2019
 */

package modernity.net;

import net.minecraftforge.fml.LogicalSide;

/**
 * Enum holding {@link #CLIENT} and {@link #SERVER}. Used in networking.
 */
public enum ESide {
    CLIENT,
    SERVER;

    /**
     * Returns the opposite side of this side. That is {@link #CLIENT} when this is {@link #SERVER}, and {@link #SERVER}
     * when this is {@link #CLIENT}.
     * @return The opposite side of this side.
     */
    public ESide opposite() {
        return this == CLIENT ? SERVER : CLIENT;
    }

    /**
     * Returns whether this represents the {@linkplain #CLIENT client} side.
     * @return True when this is {@link #CLIENT}
     */
    public boolean isClient() {
        return this == CLIENT;
    }

    /**
     * Returns whether this represents the {@linkplain #SERVER server} side.
     * @return True when this is {@link #SERVER}
     */
    public boolean isServer() {
        return this == SERVER;
    }

    /**
     * Returns the equivalent {@link LogicalSide} of this side.
     * @return The equivalent {@link LogicalSide} of this side.
     */
    public LogicalSide toLogical() {
        return this == CLIENT ? LogicalSide.CLIENT : LogicalSide.SERVER;
    }

    /**
     * Returns the equivalent {@link ESide} of the specified {@link LogicalSide}.
     * @param side The {@link LogicalSide}
     * @return The equivalent {@link ESide}
     */
    public static ESide fromLogical( LogicalSide side ) {
        return side == LogicalSide.SERVER ? SERVER : CLIENT;
    }
}
