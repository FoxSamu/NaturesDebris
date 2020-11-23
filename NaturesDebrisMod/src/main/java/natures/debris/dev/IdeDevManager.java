package natures.debris.dev;

import java.util.function.Supplier;

import natures.debris.common.dev.IDevManager;
import natures.debris.dev.info.TextDebugInfo;

public class IdeDevManager implements IDevManager {
    public static final IdeDevManager INSTANCE = new IdeDevManager();

    @Override
    public void text(Object text, float scale, int indent, int color, int time) {
        if (text instanceof Supplier) {
            Debug.emit(new TextDebugInfo(
                           () -> ((Supplier<?>) text).get().toString(),
                           scale, indent, color, time
                       )
            );
        } else {
            Debug.emit(new TextDebugInfo(
                           text.toString(),
                           scale, indent, color, time
                       )
            );
        }
    }
}
