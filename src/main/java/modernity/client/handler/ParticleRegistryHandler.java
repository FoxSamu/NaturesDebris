/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.client.handler;

import modernity.common.particle.MDParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Listens to particle factory registry event and registers particle factories then.
 */
public enum ParticleRegistryHandler {
    INSTANCE;

    @SubscribeEvent
    public void registerParticleFactories( ParticleFactoryRegisterEvent event ) {
        System.out.println( "Registering particle factories" );
        MDParticleTypes.setupFactories( Minecraft.getInstance().particles );
    }
}
