package natures.debris.common.sound;

import com.google.common.collect.Lists;
import natures.debris.common.NaturesDebris;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

public final class NdSoundEvents {
    private static final List<SoundEvent> REGISTRY = Lists.newArrayList();

    public static final SoundEvent MUSIC_DISC_DARK = sound("music_disc.dark");



    public static void register(IForgeRegistry<SoundEvent> registry) {
        REGISTRY.forEach(registry::register);
    }

    private static SoundEvent sound(String id) {
        ResourceLocation loc = NaturesDebris.resLoc(id);
        SoundEvent evt = new SoundEvent(loc);
        evt.setRegistryName(loc);
        REGISTRY.add(evt);
        return evt;
    }
}
