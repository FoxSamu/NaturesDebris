/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.environment;

/**
 * Manager class for all live rendered environment factors.
 */
public final class EnvironmentRenderingManager {
    public static final Fog FOG = new Fog();
    public static final Sky SKY = new Sky();
    public static final Precipitation PRECIPITATION = new Precipitation();
    public static final Light LIGHT = new Light();

    private static float caveFactor;

    public static float getCaveFactor() {
        return caveFactor;
    }

    public static void setCaveFactor( float caveFactor ) {
        EnvironmentRenderingManager.caveFactor = caveFactor;
    }

    private EnvironmentRenderingManager() {
    }
}
