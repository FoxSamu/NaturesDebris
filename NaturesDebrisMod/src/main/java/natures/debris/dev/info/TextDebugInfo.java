package natures.debris.dev.info;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.shadew.util.misc.ColorUtil;

public class TextDebugInfo extends DebugInfo {
    private final FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
    private final Supplier<String> string;
    private final float scale;
    private final int color;
    private final int delay;
    private final int indent;
    private long expireTime;

    public TextDebugInfo(String string, float scale, int indent, int color, int delay) {
        this.string = () -> string;
        this.scale = scale;
        this.color = color;
        this.delay = delay;
        this.indent = indent;
    }

    public TextDebugInfo(Supplier<String> string, float scale, int indent, int color, int delay) {
        this.string = string;
        this.scale = scale;
        this.color = color;
        this.delay = delay;
        this.indent = indent;
    }

    @Override
    public double render(MatrixStack stack, long currentTime, float partialTicks, double mouseX, double mouseY) {
        stack.push();
        stack.translate(indent, 0, 0);
        stack.scale(scale, scale, scale);

        String[] lines = string.get().split("\r\n|\r|\n");
        int w = 0;
        int h = 0;
        for (String ln : lines) {
            w = Math.max(w, fontRenderer.getStringWidth(ln));
            h += 10;
        }
        fill(stack, 0, 0, w, h, ColorUtil.rgba(0, 0, 0, 128));

        int y = 0;
        for (String ln : lines) {
            fontRenderer.drawWithShadow(stack, ln, 0, y + 1, color);
            y += 10;
        }

        stack.pop();

        return h * scale;
    }

    @Override
    public boolean isExpired(long currentTime) {
        return delay > 0 && currentTime > expireTime;
    }

    @Override
    public void start(long currentTime) {
        expireTime = currentTime + delay;
    }
}
