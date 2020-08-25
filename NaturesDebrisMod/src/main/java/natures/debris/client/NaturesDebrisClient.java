package natures.debris.client;

import natures.debris.api.INaturesDebris;
import natures.debris.api.util.ISidedTickable;
import natures.debris.common.NaturesDebris;

public class NaturesDebrisClient extends NaturesDebris {


    @Override
    public void tickSided(ISidedTickable sidedTickable) {
        sidedTickable.serverTick();
        sidedTickable.clientTick();
    }

    public static NaturesDebrisClient get() {
        return (NaturesDebrisClient) INaturesDebris.get();
    }
}
