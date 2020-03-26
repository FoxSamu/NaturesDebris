/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.client.handler;

import modernity.api.event.SoundEffectEvent;
import modernity.api.reflect.FieldAccessor;
import modernity.client.ModernityClient;
import modernity.client.sound.system.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum SoundEventHandler {
    INSTANCE;

    private static final FieldAccessor<SoundHandler, SoundEngine> sndManagerField = new FieldAccessor<>( SoundHandler.class, "field_147694_f" );

    public void init() {
        SoundHandler handler = Minecraft.getInstance().getSoundHandler();
        sndManagerField.set( handler, new SoundManager( handler, Minecraft.getInstance().gameSettings, Minecraft.getInstance().getResourceManager() ) );
    }

    @SubscribeEvent
    public void onSoundEffect( SoundEffectEvent event ) {
        ModernityClient.get().getSoundEffectManager().onPlaySound( event );
    }
}
