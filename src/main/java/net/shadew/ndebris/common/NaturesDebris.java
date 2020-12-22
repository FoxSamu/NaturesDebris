package net.shadew.ndebris.common;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import net.shadew.ndebris.common.block.NdBlocks;
import net.shadew.ndebris.common.item.NdItems;
import net.shadew.ndebris.common.sound.NdSoundEvents;

public class NaturesDebris implements ModInitializer {
    @Override
    public void onInitialize() {
        Reflection.initialize(
            NdBlocks.class,
            NdItems.class,
            NdSoundEvents.class
        );
    }

    public static Identifier id(String path) {
        int colon = path.indexOf(':');
        if (colon >= 0) {
            return new Identifier(path.substring(0, colon), path.substring(colon + 1));
        }
        return new Identifier("ndebris", path);
    }

    public static String idStr(String path) {
        if (path.indexOf(':') >= 0) {
            return path;
        }
        return "ndebris:" + path;
    }
}
