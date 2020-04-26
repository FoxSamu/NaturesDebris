package modernity.api;

public interface IModernityInfo {
    String version();
    String versionName();
    boolean isClient();
    boolean isDedicatedServer();
    boolean isDevVersion();
}
