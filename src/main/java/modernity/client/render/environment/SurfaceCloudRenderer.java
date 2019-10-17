package modernity.client.render.environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.client.IRenderHandler;

public class SurfaceCloudRenderer implements IRenderHandler {
    @Override
    public void render( int ticks, float partialTicks, ClientWorld world, Minecraft mc ) {
        // Clouds are handled in sky renderer
    }
}
