package modernity.server;

import modernity.common.Modernity;
import net.minecraftforge.fml.LogicalSide;

public class ModernityServer extends Modernity {
    @Override
    public LogicalSide side() {
        return LogicalSide.SERVER;
    }
}
