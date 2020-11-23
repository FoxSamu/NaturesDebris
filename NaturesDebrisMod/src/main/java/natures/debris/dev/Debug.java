package natures.debris.dev;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.minecraft.util.text.TextFormatting;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.shadew.util.misc.ColorUtil;

import natures.debris.dev.info.DebugInfo;
import natures.debris.dev.info.TextDebugInfo;

public class Debug {
    private static final List<DebugInfo> DEBUGS = new ArrayList<>();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final TextDebugInfo HEADER = new TextDebugInfo("Nature's Debris debug info", 1, 0, ColorUtil.WHITE, 0);
    private static final TextDebugInfo TIME = new TextDebugInfo(() -> DATE_FORMAT.format(Date.from(Instant.now())), 1, 200, ColorUtil.WHITE, 0);

    @SuppressWarnings("ConstantConditions")
    public static void emit(DebugInfo info) {
        info.start(System.currentTimeMillis());
        DEBUGS.add(0, info);
        DEBUGS.add(0, new TextDebugInfo(DATE_FORMAT.format(Date.from(Instant.now())), 1, 0, TextFormatting.GRAY.getColor(), 0) {
            @Override
            public boolean isExpired(long currentTime) {
                return info.isExpired(currentTime);
            }
        });
    }

    public static void render(float partialTicks, double mouseX, double mouseY, boolean render) {
        if (render) {
            MatrixStack mat = new MatrixStack();
            mat.scale(1, 1, 1);
            mat.translate(10, 10, 0);
            mat.push();

            TIME.render(mat, System.currentTimeMillis(), partialTicks, mouseX, mouseY);
            double hh = HEADER.render(mat, System.currentTimeMillis(), partialTicks, mouseX, mouseY);
            mat.translate(0, hh + 1, 0);

            DEBUGS.forEach(info -> {
                double h = info.render(mat, System.currentTimeMillis(), partialTicks, mouseX, mouseY);
                mat.translate(0, h + 1, 0);
            });
        }

        DEBUGS.removeIf(info -> info.isExpired(System.currentTimeMillis()));
    }
}
