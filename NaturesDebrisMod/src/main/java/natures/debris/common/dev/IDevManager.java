package natures.debris.common.dev;

import natures.debris.common.NaturesDebris;

public interface IDevManager {
    default void text(Object text, float scale, int indent, int color, int time) {

    }

    static IDevManager get(boolean working) {
        if (!working) return SilentDevManager.INSTANCE;
        return NaturesDebris.get().getDevManager();
    }

    static IDevManager get() {
        return NaturesDebris.get().getDevManager();
    }
}
