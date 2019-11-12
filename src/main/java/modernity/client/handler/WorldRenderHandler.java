package modernity.client.handler;

import modernity.client.ModernityClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum WorldRenderHandler {
    INSTANCE;

    @SubscribeEvent
    public void onRenderWorldLast( RenderWorldLastEvent event ) {
        ModernityClient.get().getAreaRenderManager().renderAreas( event.getPartialTicks() );

        // TODO: Add render last particles and render them here if queued...
    }
}
