/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.sound.system;

import com.google.common.collect.Sets;
import net.minecraft.client.audio.SoundSystem;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.MemoryStack;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Set;

import static modernity.client.sound.system.ALUtils.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;

@OnlyIn( Dist.CLIENT )
public class SourceManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private long deviceID;
    private long contextID;
    private static final IHandler DUMMY_HANDLER = new IHandler() {
        @Override
        @Nullable
        public EFXSoundSource createSoundSource() {
            return null;
        }

        @Override
        public boolean removeSource( EFXSoundSource src ) {
            return false;
        }

        @Override
        public void cleanup() {
        }

        @Override
        public int getMaxSources() {
            return 0;
        }

        @Override
        public int getSourceCount() {
            return 0;
        }
    };

    private IHandler staticHandler = DUMMY_HANDLER;
    private IHandler streamingHandler = DUMMY_HANDLER;

    public void setup() {
        deviceID = createDevice();

        ALCCapabilities alcCaps = ALC.createCapabilities( this.deviceID );
        if( logALCErrors( this.deviceID, "Get capabilities" ) ) {
            throw new IllegalStateException( "Failed to get OpenAL capabilities" );
        } else if( ! alcCaps.OpenALC11 ) {
            throw new IllegalStateException( "OpenAL 1.1 not supported" );
        } else {
            contextID = alcCreateContext( deviceID, (IntBuffer) null );
            alcMakeContextCurrent( contextID );

            int avgSourceCount = computeAverageSourceCount();
            int streamCount = MathHelper.clamp( (int) MathHelper.sqrt( avgSourceCount ), 2, 8 );
            int staticCount = MathHelper.clamp( avgSourceCount - streamCount, 8, 255 );

            staticHandler = new HandlerImpl( staticCount );
            streamingHandler = new HandlerImpl( streamCount );

            ALCapabilities alCaps = AL.createCapabilities( alcCaps );
            logALErrors( "Initialization" );

            if( ! alCaps.AL_EXT_source_distance_model ) {
                throw new IllegalStateException( "AL_EXT_source_distance_model is not supported" );
            } else {
                alEnable( 512 );
                if( ! alCaps.AL_EXT_LINEAR_DISTANCE ) {
                    throw new IllegalStateException( "AL_EXT_LINEAR_DISTANCE is not supported" );
                } else {
                    logALErrors( "Enable per-source distance models" );
                    LOGGER.info( "OpenAL initialized." );
                }
            }
        }
    }

    private int computeAverageSourceCount() {
        int out;
        try( MemoryStack stack = MemoryStack.stackPush() ) {
            int attributeCount = alcGetInteger( deviceID, ALC_ATTRIBUTES_SIZE );
            if( logALCErrors( deviceID, "Get attributes size" ) ) {
                throw new IllegalStateException( "Failed to get OpenAL attributes" );
            }

            IntBuffer attrs = stack.mallocInt( attributeCount );
            alcGetIntegerv( deviceID, ALC_ALL_ATTRIBUTES, attrs );
            if( logALCErrors( deviceID, "Get attributes" ) ) {
                throw new IllegalStateException( "Failed to get OpenAL attributes" );
            }

            int iterator = 0;

            int value;
            while( true ) {
                if( iterator >= attributeCount ) {
                    return 30;
                }

                int attr = attrs.get( iterator++ );
                if( attr == 0 ) {
                    return 30;
                }

                value = attrs.get( iterator++ );
                if( attr == ALC_MONO_SOURCES ) {
                    break;
                }
            }

            out = value;
        }

        return out;
    }

    private static long createDevice() {
        for( int attempt = 0; attempt < 3; ++ attempt ) {
            long deviceID = alcOpenDevice( (ByteBuffer) null );
            if( deviceID != 0L && ! logALCErrors( deviceID, "Open device" ) ) {
                return deviceID;
            }
        }

        throw new IllegalStateException( "Failed to open OpenAL device" );
    }

    public void close() {
        staticHandler.cleanup();
        streamingHandler.cleanup();
        alcDestroyContext( contextID );
        if( deviceID != 0L ) {
            alcCloseDevice( deviceID );
        }

    }

    @Nullable
    public EFXSoundSource getSoundSource( SoundSystem.Mode mode ) {
        return ( mode == SoundSystem.Mode.STREAMING ? streamingHandler : staticHandler ).createSoundSource();
    }

    public void release( EFXSoundSource src ) {
        if( ! staticHandler.removeSource( src ) && ! streamingHandler.removeSource( src ) ) {
            throw new IllegalStateException( "Tried to release source of unknown channel" );
        }
    }

    public String getDebugString() {
        return String.format(
            "Modernity Sounds: %d/%d + %d/%d",
            staticHandler.getSourceCount(), staticHandler.getMaxSources(),
            streamingHandler.getSourceCount(), streamingHandler.getMaxSources()
        );
    }

    @OnlyIn( Dist.CLIENT )
    private static class HandlerImpl implements IHandler {
        private final int maxSources;
        private final Set<EFXSoundSource> sources = Sets.newIdentityHashSet();

        HandlerImpl( int maxSources ) {
            this.maxSources = maxSources;
        }

        @Override
        @Nullable
        public EFXSoundSource createSoundSource() {
            if( sources.size() >= maxSources ) {
                return null;
            } else {
                EFXSoundSource src = EFXSoundSource.allocate();
                if( src != null ) {
                    sources.add( src );
                }

                return src;
            }
        }

        @Override
        public boolean removeSource( EFXSoundSource src ) {
            if( ! sources.remove( src ) ) {
                return false;
            } else {
                src.cleanup();
                return true;
            }
        }

        @Override
        public void cleanup() {
            sources.forEach( EFXSoundSource::cleanup );
            sources.clear();
        }

        @Override
        public int getMaxSources() {
            return maxSources;
        }

        @Override
        public int getSourceCount() {
            return sources.size();
        }
    }

    @OnlyIn( Dist.CLIENT )
    public interface IHandler {
        @Nullable
        EFXSoundSource createSoundSource();

        boolean removeSource( EFXSoundSource src );

        void cleanup();

        int getMaxSources();

        int getSourceCount();
    }
}