/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 22 - 2019
 * Author: rgsw
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

    private EnvironmentRenderingManager() {
    }
}
