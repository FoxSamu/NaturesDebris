/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

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
