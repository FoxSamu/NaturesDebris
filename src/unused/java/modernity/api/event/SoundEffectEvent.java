/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.api.event;

import modernity.client.sound.effects.ISoundSourceEffect;
import net.minecraft.client.audio.ISound;
import net.minecraftforge.eventbus.api.Event;

public class SoundEffectEvent extends Event {
    private final ISound sound;
    private ISoundSourceEffect effect = ISoundSourceEffect.NO_EFFECT;

    public SoundEffectEvent( ISound sound ) {
        this.sound = sound;
    }

    public ISound getSound() {
        return sound;
    }

    public ISoundSourceEffect getEffect() {
        return effect;
    }

    public void setEffect( ISoundSourceEffect effect ) {
        if( effect == null ) effect = ISoundSourceEffect.NO_EFFECT;
        this.effect = effect;
    }
}
