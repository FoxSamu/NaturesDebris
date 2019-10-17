package modernity.api.dimension;

/**
 * Dimensions implementing this interface receive an initialization event on world load.
 */
@FunctionalInterface
public interface IInitializeDimension {
    void init();
}
