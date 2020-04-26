/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.generic.dimension;

import modernity.common.environment.event.EnvironmentEventManager;

/**
 * Dimension interface for dimensions with environment events, and thus an {@link EnvironmentEventManager}.
 */
@FunctionalInterface
public interface IEnvEventsDimension {
    EnvironmentEventManager getEnvEventManager();
}
