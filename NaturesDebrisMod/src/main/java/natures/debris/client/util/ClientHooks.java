package natures.debris.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.debug.DebugRenderer;

public final class ClientHooks {
    public static void onRenderDebug(DebugRenderer renderer, MatrixStack matrices, IRenderTypeBuffer.Impl buf, double camx, double camy, double camz) {
        renderer.pathfinding.render(matrices, buf, camx, camy, camz);
    }
}
