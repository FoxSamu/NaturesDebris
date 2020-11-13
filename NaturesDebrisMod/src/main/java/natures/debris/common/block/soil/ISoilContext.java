package natures.debris.common.block.soil;

public interface ISoilContext {
    Fertility getFertility();
    void setFertility(Fertility fertility);

    int getLevel();
    void setLevel(int level);

    boolean isWet();

    default boolean consume() {
        int l = getLevel();
        if (l > 0) {
            setLevel(l - 1);
            return true;
        }
        return false;
    }

    default boolean consume(int amount, int min) {
        int l = getLevel();
        if (l >= min) {
            setLevel(Math.max(0, l - amount));
            return true;
        }
        return false;
    }

    default boolean isFertile() {
        return getFertility() == Fertility.FERTILE && getLevel() > 0;
    }

    default boolean isFertile(int amount) {
        return getFertility() == Fertility.FERTILE && getLevel() > amount;
    }

    default boolean isDecayed() {
        return getFertility() == Fertility.DECAYED && getLevel() > 0;
    }

    default boolean isDecayed(int amount) {
        return getFertility() == Fertility.DECAYED && getLevel() > amount;
    }

    default boolean isSalty() {
        return getFertility() == Fertility.SALTY && getLevel() > 0;
    }

    default boolean isSalty(int amount) {
        return getFertility() == Fertility.SALTY && getLevel() > amount;
    }
}
