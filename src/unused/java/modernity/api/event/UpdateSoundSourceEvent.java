/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.api.event;

import modernity.client.sound.system.EFXSoundSource;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.eventbus.api.Event;

public class UpdateSoundSourceEvent extends Event {
    private final int id;
    private final Vec3d pos;
    private final SoundSource src;
    private final EFXSoundSource efx;

    public UpdateSoundSourceEvent( int id, Vec3d pos, SoundSource src, EFXSoundSource efx ) {
        this.id = id;
        this.pos = pos;
        this.src = src;
        this.efx = efx;
    }

    public int getId() {
        return id;
    }

    public Vec3d getPos() {
        return pos;
    }

    public SoundSource getSource() {
        return src;
    }

    public EFXSoundSource getEFXSource() {
        return efx;
    }
}
