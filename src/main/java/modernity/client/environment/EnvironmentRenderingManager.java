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
