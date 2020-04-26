/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.client.sound.effects;

import modernity.generic.dimension.ISoundEffectDimension;
import modernity.generic.event.SoundEffectEvent;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openal.AL;

public class SoundEffectManager {
    private static final Logger LOGGER = LogManager.getLogger();

    public final Minecraft mc = Minecraft.getInstance();

    private boolean loaded;
    private boolean available;

    public void onPlaySound( SoundEffectEvent event ) {
        checkAvailability();

        if( available ) {
            Minecraft mc = Minecraft.getInstance();
            if( mc.world != null && mc.world.dimension instanceof ISoundEffectDimension ) {
                ISoundEffectDimension sed = (ISoundEffectDimension) mc.world.dimension;
                sed.handleSoundEffect( event );
            }
        }
    }

    private void checkAvailability() {
        if( loaded ) return;

        // Check if available
        available = AL.getCapabilities().ALC_EXT_EFX;
        if( ! available ) {
            LOGGER.warn( "OpenAL EFX is not supported. Sound effects will not be available..." );
        }

        loaded = true;
    }
}
