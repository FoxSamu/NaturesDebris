/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.sound.effects;

// TODO Re-evaluate
//public class SoundEffect implements ISoundEffectLayer {
//    private int type;
//    private final int effectID;
//    private final int slotID;
//
//    public SoundEffect( int type ) {
//        this.type = type;
//
//        slotID = alGenAuxiliaryEffectSlots();
//        alAuxiliaryEffectSloti(
//            slotID,
//            AL_EFFECTSLOT_AUXILIARY_SEND_AUTO,
//            AL10.AL_TRUE
//        );
//
//        effectID = alGenEffects();
//
//        alEffecti( effectID, AL_EFFECT_TYPE, type );
//
//        alAuxiliaryEffectSloti( slotID, AL_EFFECTSLOT_EFFECT, effectID );
//    }
//
//    public void setType( int type ) {
//        if( this.type == type ) return;
//
//        alEffecti( effectID, AL_EFFECT_TYPE, type );
//    }
//
//    public void setParamI( int param, int value ) {
//        alEffecti( effectID, param, value );
//    }
//
//    public void setParamF( int param, float value ) {
//        alEffectf( effectID, param, value );
//    }
//
//    public void delete() {
//        alDeleteEffects( effectID );
//        alDeleteAuxiliaryEffectSlots( slotID );
//    }
//
//    public void setGain( float gain ) {
//        alAuxiliaryEffectSlotf(
//            slotID,
//            AL_EFFECTSLOT_GAIN,
//            gain
//        );
//    }
//
//    @Override
//    public void apply( int source, int index ) {
//        AL11.alSource3i( source, AL_AUXILIARY_SEND_FILTER, slotID, index, AL_FILTER_NULL );
//    }
//
//    @Override
//    protected void finalize() {
//        delete();
//    }
//}
