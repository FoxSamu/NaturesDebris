package natures.debris.common.block.plant.logic;

public interface ILeveledGrowingContext extends IGrowingContext {
    int amount();
    boolean hasAmount(int amount);
    void consume(int amount);
}
