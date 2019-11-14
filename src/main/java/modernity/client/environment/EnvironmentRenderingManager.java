/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.client.environment;

/**
 * Manager class for all live rendered environment factors.
 */
public final class EnvironmentRenderingManager {
    public static final Fog FOG = new Fog();
    public static final Sky SKY = new Sky();

    private EnvironmentRenderingManager() {
    }
}
