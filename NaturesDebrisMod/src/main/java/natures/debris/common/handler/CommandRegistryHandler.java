package natures.debris.common.handler;

import natures.debris.common.command.NdDebugCommand;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class CommandRegistryHandler {
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        NdDebugCommand.register(event.getCommandDispatcher());
    }
}
