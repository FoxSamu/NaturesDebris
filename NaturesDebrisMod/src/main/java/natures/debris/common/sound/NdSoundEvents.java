package natures.debris.common.sound;

import com.google.common.collect.Lists;
import natures.debris.common.NaturesDebris;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

public final class NdSoundEvents {
    private static final List<SoundEvent> PREREGISTRY = Lists.newArrayList();

    public static final SoundEvent MUSIC_DISC_DARK = sound("music_disc.dark");
    public static final SoundEvent MUSIC_DISC_M1 = sound("music_disc.m1");


    public static void register(IForgeRegistry<SoundEvent> registry) {
        PREREGISTRY.forEach(registry::register);
        PREREGISTRY.clear(); // Cleanup used memory...
    }

    private static SoundEvent sound(String id) {
        ResourceLocation loc = NaturesDebris.resLoc(id);
        SoundEvent evt = new SoundEvent(loc);
        evt.setRegistryName(loc);
        PREREGISTRY.add(evt);
        return evt;
    }
}
