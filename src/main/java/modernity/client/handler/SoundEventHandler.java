/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.handler;

import modernity.api.reflect.FieldAccessor;
import modernity.client.sound.system.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundHandler;

public enum SoundEventHandler {
    INSTANCE;

    private static final FieldAccessor<SoundHandler, SoundEngine> sndManagerField = new FieldAccessor<>( SoundHandler.class, "field_147694_f" );


    //    @SubscribeEvent
//    public void onSoundEngineInit( SoundSetupEvent event )
    public void init() {
        SoundHandler handler = Minecraft.getInstance().getSoundHandler();
        sndManagerField.set( handler, new SoundManager( handler, Minecraft.getInstance().gameSettings, Minecraft.getInstance().getResourceManager() ) );
    }
}
