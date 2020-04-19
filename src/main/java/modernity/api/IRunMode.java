package modernity.api;

@FunctionalInterface
public interface IRunMode {
    IRunMode CLIENT = () -> "modernity.client.ModernityClient";
    IRunMode SERVER = () -> "modernity.server.ModernityServer";
    IRunMode DATA = () -> "modernity.data.ModernityData";

    String getClassName();
}
