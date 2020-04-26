/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.client.sound.system;


// TODO Re-evaluate
//public class EFXSoundSource {
//    private static final MethodAccessor<SoundSource, SoundSource> allocateAccessor = new MethodAccessor<>( SoundSource.class, "func_216426_a" );
//    private static final FieldAccessor<SoundSource, Integer> idAccessor = new FieldAccessor<>( SoundSource.class, "field_216441_b" );
//
//    private final SoundSource wrapped;
//
//    private ISoundSourceEffect effect = ISoundSourceEffect.NO_EFFECT;
//
//    @Nullable
//    public static EFXSoundSource allocate() {
//        SoundSource src = allocateAccessor.call( null );
//        if( src == null ) return null;
//        return new EFXSoundSource( src );
//    }
//
//    private EFXSoundSource( SoundSource wrapped ) {
//        this.wrapped = wrapped;
//    }
//
//    public int getID() {
//        return idAccessor.get( wrapped );
//    }
//
//    public void cleanup() {
//        wrapped.func_216436_b();
//        effect.cleanup();
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
//    public void setEffect( ISoundSourceEffect effect ) {
//        this.effect = effect;
//    }
//
//    public void setPosition( Vec3d pos ) {
//        effect.applySoundEffect( this, pos, getID() );
//        MinecraftForge.EVENT_BUS.post( new UpdateSoundSourceEvent( getID(), pos, getWrapped(), this ) );
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
//
//    public SoundSource getWrapped() {
//        return wrapped;
//    }
//}
