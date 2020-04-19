package modernity.tests;

public interface IModernityTest {
    default void registerListeners() {
    }

    default void setupRegistryHandler() {
    }

    default void preInit() {
    }

    default void init() {
    }

    default void postInit() {
    }
}
