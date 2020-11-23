package natures.debris.common.dev;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.text.TextFormatting;

import net.shadew.util.data.Lazy;
import net.shadew.util.misc.ColorUtil;

public class DevMessage {
    private static final ThreadLocal<DevMessage> MESSAGES = ThreadLocal.withInitial(DevMessage::new);

    private final List<String> lines = new ArrayList<>();
    private final List<Supplier<String>> args = new ArrayList<>();
    private static final Pattern FORMAT = Pattern.compile("\\{([0-9]*)}|\\[([a-zA-Z]*)]");
    private Lazy<String> currentLazy;

    private DevMessage() {
    }

    public static DevMessage fast(String ln, Object... vals) {
        DevMessage msg = reset();
        msg.line(ln);
        for (Object o : vals) {
            msg.arg(o);
        }
        return msg;
    }

    public static DevMessage reset() {
        DevMessage msg = MESSAGES.get();
        msg.doReset();
        return msg;
    }

    public static DevMessage append() {
        return MESSAGES.get();
    }

    private void doReset() {
        if (currentLazy != null && !currentLazy.isLoaded()) {
            currentLazy.get();
        }
        currentLazy = null;
        lines.clear();
        args.clear();
    }

    private static Supplier<String> toSupplier(Object o) {
        if (o instanceof Supplier<?>) {
            return () -> ((Supplier<?>) o).get().toString();
        } else {
            return o::toString;
        }
    }

    public DevMessage line(String line) {
        lines.add(line);
        return this;
    }

    public DevMessage arg(Object arg) {
        args.add(toSupplier(arg));
        return this;
    }

    public DevMessage arg(Supplier<?> arg) {
        args.add(toSupplier(arg));
        return this;
    }

    private Supplier<String> createMessage() {
        currentLazy = Lazy.of(() -> {
            int cur = 0;
            StringBuffer buf = new StringBuffer();
            int ln = 0;
            for (String line : lines) {
                Matcher matcher = FORMAT.matcher(line);
                while (matcher.find()) {
                    if (matcher.start(1) > 0) {
                        String gr = matcher.group(1).trim();
                        int n;
                        if (gr.isEmpty()) {
                            n = cur++;
                        } else {
                            n = Integer.parseInt(gr);
                        }
                        if (n < args.size()) {
                            matcher.appendReplacement(buf, args.get(n).get());
                        } else {
                            matcher.appendReplacement(buf, matcher.group());
                        }
                    } else {
                        String gr = matcher.group(2).trim();
                        TextFormatting formatting = TextFormatting.getValueByName(gr.toUpperCase());
                        if (formatting != null) {
                            matcher.appendReplacement(buf, formatting.toString());
                        } else {
                            matcher.appendReplacement(buf, matcher.group());
                        }
                    }
                }
                matcher.appendTail(buf);

                ln++;
                if (ln != lines.size()) {
                    buf.append(System.lineSeparator());
                }
            }
            return buf.toString();
        });
        return currentLazy;
    }

    public void send(boolean condition, float scale, int indent, int color, int time) {
        if (condition) {
            IDevManager.get().text(createMessage(), scale, indent, color, time);
        }
    }

    public void send(boolean condition, int color, int time) {
        send(condition, 1, 0, color, time);
    }

    public void send(boolean condition, int time) {
        send(condition, ColorUtil.WHITE, time);
    }

    public void send(int color, int time) {
        send(true, color, time);
    }

    public void send(int time) {
        send(true, time);
    }
}
