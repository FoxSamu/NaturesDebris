/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.sound.system;


// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class AudioBuffer {
//    @Nullable
//    private ByteBuffer buffer;
//    private final AudioFormat format;
//    private boolean initialized;
//    private int bufferID;
//
//    public AudioBuffer( ByteBuffer buffer, AudioFormat format ) {
//        this.buffer = buffer;
//        this.format = format;
//    }
//
//    OptionalInt use() {
//        if( ! initialized ) {
//            if( buffer == null ) {
//                return OptionalInt.empty();
//            }
//
//            int f = ALUtils.getALFormat( format );
//            int[] buffers = new int[ 1 ];
//            AL10.alGenBuffers( buffers );
//            if( ALUtils.logALErrors( "Creating buffer" ) ) {
//                return OptionalInt.empty();
//            }
//
//            AL10.alBufferData( buffers[ 0 ], f, buffer, (int) format.getSampleRate() );
//            if( ALUtils.logALErrors( "Assigning buffer data" ) ) {
//                return OptionalInt.empty();
//            }
//
//            bufferID = buffers[ 0 ];
//            initialized = true;
//            buffer = null;
//        }
//
//        return OptionalInt.of( bufferID );
//    }
//
//    public void delete() {
//        if( initialized ) {
//            AL10.alDeleteBuffers( bufferID );
//            if( ALUtils.logALErrors( "Deleting stream buffers" ) ) {
//                return;
//            }
//        }
//
//        initialized = false;
//    }
//
//    public OptionalInt useAndReset() {
//        OptionalInt id = use();
//        initialized = false;
//        return id;
//    }
//}