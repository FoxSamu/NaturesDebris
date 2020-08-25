/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.sound.system;

// TODO Re-evaluate
//public class EffectSoundSource {
//    private static final MethodAccessor<SoundSource, SoundSource> allocateAccessor = new MethodAccessor<>( SoundSource.class, "func_216426_a" );
//
//    private final SoundSource wrapped;
//
//    @Nullable
//    public static EffectSoundSource allocate() {
//        SoundSource src = allocateAccessor.call( null );
//        if( src == null ) return null;
//        return new EffectSoundSource( src );
//    }
//
//    private EffectSoundSource( SoundSource wrapped ) {
//        this.wrapped = wrapped;
//    }
//
//    public void cleanup() {
//        wrapped.func_216436_b();
//    }
//
//    public void play() {
//        wrapped.func_216438_c();
//    }
//
//    public void pause() {
//        wrapped.func_216439_d();
//    }
//
//    public void resume() {
//        wrapped.func_216437_e();
//    }
//
//    public void stop() {
//        wrapped.func_216418_f();
//    }
//
//    public boolean isStopped() {
//        return wrapped.func_216435_g();
//    }
//
//    public void setPosition( Vec3d pos ) {
//        wrapped.func_216420_a( pos );
//    }
//
//    public void setPitch( float pitch ) {
//        wrapped.func_216422_a( pitch );
//    }
//
//    public void setLooping( boolean looping ) {
//        wrapped.func_216425_a( looping );
//    }
//
//    public void setVolume( float gain ) {
//        wrapped.func_216430_b( gain );
//    }
//
//    public void setNoDistanceModel() {
//        wrapped.func_216419_h();
//    }
//
//    public void setMaxDistance( float dist ) {
//        wrapped.func_216423_c( dist );
//    }
//
//    public void setRelative( boolean relative ) {
//        wrapped.func_216432_b( relative );
//    }
//
//    public void setBuffer( AudioStreamBuffer buff ) {
//        wrapped.func_216429_a( buff );
//    }
//
//    public void setStream( IAudioStream str ) {
//        wrapped.func_216433_a( str );
//    }
//
//    public void streamUpdate() {
//        wrapped.func_216434_i();
//    }
//}
