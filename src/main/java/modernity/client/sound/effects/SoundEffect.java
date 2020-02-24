/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.sound.effects;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import static org.lwjgl.openal.EXTEfx.*;

public class SoundEffect implements ISoundEffectLayer {
    private int type;
    private final int effectID;
    private final int slotID;

    public SoundEffect( int type ) {
        this.type = type;

        slotID = alGenAuxiliaryEffectSlots();
        alAuxiliaryEffectSloti(
            slotID,
            AL_EFFECTSLOT_AUXILIARY_SEND_AUTO,
            AL10.AL_TRUE
        );

        effectID = alGenEffects();

        alEffecti( effectID, AL_EFFECT_TYPE, type );

        alAuxiliaryEffectSloti( slotID, AL_EFFECTSLOT_EFFECT, effectID );
    }

    public void setType( int type ) {
        if( this.type == type ) return;

        alEffecti( effectID, AL_EFFECT_TYPE, type );
    }

    public void setParamI( int param, int value ) {
        alEffecti( effectID, param, value );
    }

    public void setParamF( int param, float value ) {
        alEffectf( effectID, param, value );
    }

    public void delete() {
        alDeleteEffects( effectID );
        alDeleteAuxiliaryEffectSlots( slotID );
    }

    @Override
    public void apply( int source, int index ) {
        AL11.alSource3i( source, AL_AUXILIARY_SEND_FILTER, slotID, index, AL_FILTER_NULL );
    }

    @Override
    protected void finalize() {
        delete();
    }
}
