package modernity.common.util;

import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import modernity.common.command.MDCommands;
import modernity.common.world.dim.MDDimensions;

public class ProxyCommon {

    public void init() {

    }

    public void loadComplete() {

    }

    public void registerListeners() {

    }

    public boolean fancyGraphics() {
        return false;
    }

    @SubscribeEvent
    public void serverStart( FMLServerStartingEvent event ) {
        MDCommands.register( event.getCommandDispatcher() );
    }

    @SubscribeEvent
    public void onRegisterDimensions( RegisterDimensionsEvent event ) {
        MDDimensions.init( event.getMissingNames() );
    }
}
