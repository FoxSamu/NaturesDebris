/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.sound.system;


// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public final class ALUtils {
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    private static String initALErrorMessage( int id ) {
//        switch( id ) {
//            case AL10.AL_INVALID_NAME:
//                return "Invalid name parameter.";
//            case AL10.AL_INVALID_ENUM:
//                return "Invalid enumerated parameter value.";
//            case AL10.AL_INVALID_VALUE:
//                return "Invalid parameter parameter value.";
//            case AL10.AL_INVALID_OPERATION:
//                return "Invalid operation.";
//            case AL10.AL_OUT_OF_MEMORY:
//                return "Unable to allocate memory.";
//            default:
//                return "An unrecognized error occurred.";
//        }
//    }
//
//    public static boolean logALErrors( String place ) {
//        int i = AL10.alGetError();
//        if( i != 0 ) {
//            LOGGER.error( "{}: {}", place, initALErrorMessage( i ) );
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private static String initALCErrorMessage( int id ) {
//        switch( id ) {
//            case ALC10.ALC_INVALID_DEVICE:
//                return "Invalid device.";
//            case ALC10.ALC_INVALID_CONTEXT:
//                return "Invalid context.";
//            case ALC10.ALC_INVALID_ENUM:
//                return "Illegal enum.";
//            case ALC10.ALC_INVALID_VALUE:
//                return "Invalid value.";
//            case ALC10.ALC_OUT_OF_MEMORY:
//                return "Unable to allocate memory.";
//            default:
//                return "An unrecognized error occurred.";
//        }
//    }
//
//    public static boolean logALCErrors( long device, String place ) {
//        int i = ALC10.alcGetError( device );
//        if( i != 0 ) {
//            LOGGER.error( "{}{}: {}", place, device, initALCErrorMessage( i ) );
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public static int getALFormat( AudioFormat format ) {
//        Encoding encoding = format.getEncoding();
//        int channels = format.getChannels();
//        int sampleSize = format.getSampleSizeInBits();
//        if( encoding.equals( Encoding.PCM_UNSIGNED ) || encoding.equals( Encoding.PCM_SIGNED ) ) {
//            if( channels == 1 ) {
//                if( sampleSize == 8 ) {
//                    return AL10.AL_FORMAT_MONO8;
//                }
//
//                if( sampleSize == 16 ) {
//                    return AL10.AL_FORMAT_MONO16;
//                }
//            } else if( channels == 2 ) {
//                if( sampleSize == 8 ) {
//                    return AL10.AL_FORMAT_STEREO8;
//                }
//
//                if( sampleSize == 16 ) {
//                    return AL10.AL_FORMAT_STEREO16;
//                }
//            }
//        }
//
//        throw new IllegalArgumentException( "Invalid audio format: " + format );
//    }
//}