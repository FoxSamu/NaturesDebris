package modernity.api.dimension;

import modernity.common.environment.satellite.SatelliteData;

/**
 * Dimension interface for dimensions with a satellite, and thus a {@link SatelliteData} instance.
 */
@FunctionalInterface
public interface ISatelliteDimension {
    SatelliteData getSatelliteData();
}
