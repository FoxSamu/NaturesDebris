package modernity.common.util;

import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.common.world.dim.MDDimensions;

public class ProxyCommon {

    public void init() {

    }

    public void loadComplete() {

    }

    public void registerListeners() {

    }

    @SubscribeEvent
    public void onRegisterDimensions( RegisterDimensionsEvent event ) {
        MDDimensions.init( event.getMissingNames() );
    }
}
