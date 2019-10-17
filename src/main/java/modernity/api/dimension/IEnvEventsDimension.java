package modernity.api.dimension;

import modernity.common.environment.event.EnvironmentEventManager;

/**
 * Dimension interface for dimensions with environment events, and thus an {@link EnvironmentEventManager}.
 */
@FunctionalInterface
public interface IEnvEventsDimension {
    EnvironmentEventManager getEnvEventManager();
}
