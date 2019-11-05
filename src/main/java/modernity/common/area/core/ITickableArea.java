package modernity.common.area.core;

public interface ITickableArea extends IServerTickableArea, IClientTickableArea {
    void tick();

    @Override
    default void tickClient() {
        tick();
    }

    @Override
    default void tickServer() {
        tick();
    }
}
