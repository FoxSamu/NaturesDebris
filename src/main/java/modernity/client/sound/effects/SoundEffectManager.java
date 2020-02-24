/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.sound.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.EXTEfx;

import java.util.function.Predicate;

public class SoundEffectManager implements ISelectiveResourceReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();

    public final Minecraft mc = Minecraft.getInstance();

    private boolean loaded;
    private boolean available;

    private ISoundEffectLayer[] effects;

    public void onPlaySound( int src, Vec3d pos ) {
//        setup();

//        if( effects != null ) {
//            if( mc.world != null ) {
//                if( available && mc.world.dimension instanceof IReverbDimension ) {
//                    IReverbDimension dim = (IReverbDimension) mc.world.dimension;
//                    if( dim.hasReverb( pos ) ) {
//                        int i = 0;
//                        for( ISoundSourceEffect effect : effects ) {
//                            effect.apply( src, i );
//                            i ++;
//                        }
//                    }
//                }
//            }
//        }
    }

    private void setup() {
        if( loaded ) return;

        // Check if available
        available = AL.getCapabilities().ALC_EXT_EFX;
        if( available ) {

            effects = new SoundEffect[ 2 ];

            SoundEffect reverb = new SoundEffect( EXTEfx.AL_EFFECT_EAXREVERB );
            reverb.setParamF( EXTEfx.AL_EAXREVERB_DECAY_TIME, 4 );

            SoundEffect echo = new SoundEffect( EXTEfx.AL_EFFECT_ECHO );
            echo.setParamF( EXTEfx.AL_ECHO_FEEDBACK, 0.7F );
            echo.setParamF( EXTEfx.AL_ECHO_DELAY, 0.2F );

            effects[ 0 ] = echo;
            effects[ 1 ] = reverb;

            LOGGER.info( "Sound effects initialized" );
        } else {
            LOGGER.warn( "OpenAL EFX is not supported. Sound effects will not be available..." );
        }

        loaded = true;
    }

    @Override
    public void onResourceManagerReload( IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate ) {
        if( resourcePredicate.test( VanillaResourceType.SOUNDS ) ) {
            loaded = false;
        }
    }
}
