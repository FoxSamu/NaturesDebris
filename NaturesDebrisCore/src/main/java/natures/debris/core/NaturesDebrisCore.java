package natures.debris.core;

import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;

import natures.debris.api.INaturesDebris;
import natures.debris.api.INaturesDebrisInfo;
import natures.debris.NdInfo;

public abstract class NaturesDebrisCore implements INaturesDebris {
    public static final IEventBus CORE_EVENT_BUS = new BusBuilder().build();

    @Override
    public INaturesDebrisInfo info() {
        return NdInfo.INSTANCE;
    }

    public static NaturesDebrisCore get() {
        return (NaturesDebrisCore) INaturesDebris.get();
    }
}
