package modernity.api.dimension;

import modernity.common.environment.satellite.SatelliteData;

/**
 * Contract for a dimension to have {@link SatelliteData}.
 */
@FunctionalInterface
public interface ISatelliteDimension {
    SatelliteData getSatelliteData();
}
