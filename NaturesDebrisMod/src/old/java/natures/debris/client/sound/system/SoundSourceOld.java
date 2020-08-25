/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.client.sound.system;


// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class SoundSourceOld {
//    private static final Logger LOGGER = LogManager.getLogger();
//    private final int srcID;
//    private AtomicBoolean playing = new AtomicBoolean( true );
//    private int streamPacketSize = 16384;
//    @Nullable
//    private AudioStream stream;
//
//    @Nullable
//    public static SoundSourceOld allocate() {
//        int[] idArr = new int[ 1 ];
//        alGenSources( idArr );
//        return logALErrors( "Allocate new source" ) ? null : new SoundSourceOld( idArr[ 0 ] );
//    }
//
//    private SoundSourceOld( int srcID ) {
//        this.srcID = srcID;
//    }
//
//    public void cleanup() {
//        if( playing.compareAndSet( true, false ) ) {
//            alSourceStop( srcID );
//            logALErrors( "Stop" );
//            if( stream != null ) {
//                try {
//                    stream.close();
//                } catch( IOException ex ) {
//                    LOGGER.error( "Failed to close audio stream", ex );
//                }
//
//                removeProcessedBuffers();
//                stream = null;
//            }
//
//            alDeleteSources( srcID );
//            logALErrors( "Cleanup" );
//        }
//
//    }
//
//    public void play() {
//        alSourcePlay( srcID );
//    }
//
//    private int getState() {
//        return ! playing.get() ? AL_STOPPED : alGetSourcei( srcID, AL_SOURCE_STATE );
//    }
//
//    public void pause() {
//        if( getState() == AL_PLAYING ) {
//            alSourcePause( srcID );
//        }
//
//    }
//
//    public void resume() {
//        if( getState() == AL_PAUSED ) {
//            alSourcePlay( srcID );
//        }
//
//    }
//
//    public void stop() {
//        if( playing.get() ) {
//            alSourceStop( srcID );
//            logALErrors( "Stop" );
//        }
//    }
//
//    public boolean isStopped() {
//        return getState() == AL_STOPPED;
//    }
//
//    public void setPosition( Vec3d pos ) {
//        alSourcefv( srcID, AL_POSITION, new float[] {
//            (float) pos.x,
//            (float) pos.y,
//            (float) pos.z
//        } );
//    }
//
//    public void setPitch( float pitch ) {
//        alSourcef( srcID, AL_PITCH, pitch );
//    }
//
//    public void setLooping( boolean looping ) {
//        alSourcei( srcID, AL_LOOPING, looping ? AL_TRUE : AL_FALSE );
//    }
//
//    public void setVolume( float gain ) {
//        alSourcef( srcID, AL_GAIN, gain );
//    }
//
//    public void setNoDistanceModel() {
//        alSourcei( srcID, AL_DISTANCE_MODEL, AL_NONE );
//    }
//
//    public void setMaxDistance( float dist ) {
//        alSourcei( srcID, AL_DISTANCE_MODEL, AL_LINEAR_DISTANCE );
//        alSourcef( srcID, AL_MAX_DISTANCE, dist );
//        alSourcef( srcID, AL_ROLLOFF_FACTOR, 1 );
//        alSourcef( srcID, AL_REFERENCE_DISTANCE, 0 );
//    }
//
//    public void setRelative( boolean relative ) {
//        alSourcei( srcID, AL_SOURCE_RELATIVE, relative ? AL_TRUE : AL_FALSE );
//    }
//
//    public void setBuffer( AudioBuffer buff ) {
//        buff.use().ifPresent( i -> alSourcei( srcID, AL_BUFFER, i ) );
//    }
//
//    public void setStream( IAudioStream str ) {
//        stream = new AudioStream( str );
//        AudioFormat format = stream.getFormat();
//        streamPacketSize = computeStreamPacketSize( format, 1 );
//        streamAndQueueBuffers( 4 );
//    }
//
//    private static int computeStreamPacketSize( AudioFormat format, int mult ) {
//        return (int) ( ( mult * format.getSampleSizeInBits() ) / 8F * format.getChannels() * format.getSampleRate() );
//    }
//
//    private void streamAndQueueBuffers( int count ) {
//        if( stream != null ) {
//            try {
//                for( int i = 0; i < count; ++ i ) {
//                    ByteBuffer buff = stream.stream( streamPacketSize );
//                    if( buff != null ) {
//                        new AudioBuffer( buff, stream.getFormat() )
//                            .useAndReset()
//                            .ifPresent( id -> alSourceQueueBuffers( srcID, id ) );
//                    }
//                }
//            } catch( IOException exc ) {
//                LOGGER.error( "Failed to read from audio stream", exc );
//            }
//        }
//
//    }
//
//    public void streamUpdate() {
//        if( stream != null ) {
//            int cnt = removeProcessedBuffers();
//            streamAndQueueBuffers( cnt );
//        }
//    }
//
//    private int removeProcessedBuffers() {
//        int count = alGetSourcei( srcID, AL_BUFFERS_PROCESSED );
//        if( count > 0 ) {
//            int[] bufferIDs = new int[ count ];
//
//            alSourceUnqueueBuffers( srcID, bufferIDs );
//            logALErrors( "Unqueue buffers" );
//
//            alDeleteBuffers( bufferIDs );
//            logALErrors( "Remove processed buffers" );
//        }
//
//        return count;
//    }
//}
