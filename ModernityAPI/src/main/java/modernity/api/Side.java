/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved.
 * This file is part of the Modernity Plugin API and may be used,
 * included and distributed within other projects without further
 * permission, unless the copyright holder is not the original
 * author or the owner had forbidden the user to use this file.
 * Other terms and conditions still apply.
 *
 * For a full license, see LICENSE.txt.
 */

package modernity.api;


import net.minecraftforge.api.distmarker.Dist;

import java.util.function.Predicate;

/**
 * Predicate for a specific distribution type (client or dedicated server). This can either match both dists or one of
 * them.
 */
public enum Side implements Predicate<Dist> {

    /**
     * Matches both client and dedicated server distribution types: {@link Dist#CLIENT} and {@link
     * Dist#DEDICATED_SERVER}.
     */
    COMMON(null),

    /**
     * Matches the client distribution type only: {@link Dist#CLIENT}.
     */
    CLIENT(Dist.CLIENT),

    /**
     * Matches the server distribution type only: {@link Dist#DEDICATED_SERVER}.
     */
    SERVER(Dist.DEDICATED_SERVER);

    private final Dist dist;

    Side(Dist dist) {
        this.dist = dist;
    }

    /**
     * Returns the {@link Dist} this {@link Side} matches. {@link #COMMON} returns null.
     */
    public Dist getDist() {
        return dist;
    }

    /**
     * Checks whether this side matches on the client - whether it is either {@link #COMMON} or {@link #CLIENT}.
     */
    public boolean onClient() {
        return this == COMMON || this == CLIENT;
    }

    /**
     * Checks whether this side matches on the dedicated server - whether it is either {@link #COMMON} or {@link
     * #SERVER}.
     */
    public boolean onServer() {
        return this == COMMON || this == SERVER;
    }

    /**
     * Tests for the specific {@link Dist}. This is:
     * <ul>
     * <li>{@link #COMMON}: {@code true} (even if argument is null)</li>
     * <li>{@link #CLIENT}: {@code dist == }{@link Dist#CLIENT}</li>
     * <li>{@link #SERVER}: {@code dist == }{@link Dist#DEDICATED_SERVER}</li>
     * </ul>
     */
    @Override
    public boolean test(Dist dist) {
        return this == COMMON || dist == this.dist;
    }
}
