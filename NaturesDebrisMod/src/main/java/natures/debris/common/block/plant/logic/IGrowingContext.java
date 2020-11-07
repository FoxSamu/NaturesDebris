package natures.debris.common.block.plant.logic;

public interface IGrowingContext {
    void continueGrow(int amount);
    void continueDecay(int amount);
    void continueHeal(int amount);
    void continueKill();

    void consume();
    boolean consumed();
}
