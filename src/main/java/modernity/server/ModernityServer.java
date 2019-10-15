package modernity.server;

import modernity.common.Modernity;
import net.minecraftforge.fml.LogicalSide;

/**
 * Dedicated server side proxy class of the Modernity. This class does nothing more than indicating we're on the
 * dedicated server, all loading and initialization is handled by the {@linkplain Modernity common proxy class}.
 */
public class ModernityServer extends Modernity {
    @Override
    public LogicalSide side() {
        return LogicalSide.SERVER;
    }
}
