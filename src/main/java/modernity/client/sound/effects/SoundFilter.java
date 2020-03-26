/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.sound.effects;


// TODO Re-evaluate
//public class SoundFilter implements ISoundEffectLayer {
//    private int type;
//    private final int filterID;
//
//    public SoundFilter( int type ) {
//        this.type = type;
//
//        filterID = alGenFilters();
//
//        alFilteri( filterID, AL_FILTER_TYPE, type );
//    }
//
//    public void setType( int type ) {
//        if( this.type == type ) return;
//
//        alFilteri( filterID, AL_EFFECT_TYPE, type );
//    }
//
//    public void setParamI( int param, int value ) {
//        alFilteri( filterID, param, value );
//    }
//
//    public void setParamF( int param, float value ) {
//        alFilterf( filterID, param, value );
//    }
//
//    public void delete() {
//        alDeleteFilters( filterID );
//    }
//
//    @Override
//    public void apply( int source, int index ) {
//        AL11.alSource3i( source, AL_AUXILIARY_SEND_FILTER, AL_EFFECTSLOT_NULL, index, filterID );
//    }
//
//    @Override
//    protected void finalize() {
//        delete();
//    }
//}
