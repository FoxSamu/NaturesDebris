/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.generic.dimension;

import modernity.common.environment.satellite.SatelliteData;

/**
 * Dimension interface for dimensions with a satellite, and thus a {@link SatelliteData} instance.
 */
@FunctionalInterface
public interface ISatelliteDimension {
    SatelliteData getSatelliteData();
}
