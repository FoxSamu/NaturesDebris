package net.shadew.ndebris.common.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.shadew.ndebris.common.NaturesDebris;

public abstract class NdSoundEvents {
    public static final SoundEvent MUSIC_DISC_DARK = sound("music_disc.dark");
    public static final SoundEvent MUSIC_DISC_M1 = sound("music_disc.m1");

    private static SoundEvent sound(String name) {
        Identifier id = NaturesDebris.id(name);
        SoundEvent snd = new SoundEvent(id);
        Registry.register(Registry.SOUND_EVENT, id, snd);
        return snd;
    }
}
