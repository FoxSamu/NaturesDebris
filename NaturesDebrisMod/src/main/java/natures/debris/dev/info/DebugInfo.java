package natures.debris.dev.info;

import net.minecraft.client.gui.AbstractGui;
import com.mojang.blaze3d.matrix.MatrixStack;

public abstract class DebugInfo extends AbstractGui {
    public abstract double render(MatrixStack stack, long currentTime, float partialTicks, double mouseX, double mouseY);
    public abstract boolean isExpired(long currentTime);
    public abstract void start(long currentTime);
}
