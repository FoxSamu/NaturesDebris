/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.p2.common;

import modernity.MDInfo;
import modernity.api.IModernity;
import modernity.api.IModernityInfo;
import modernity.api.LifecyclePhase;
import modernity.api.event.ModernityLifecycleEvent;
import modernity.api.util.ISidedTickable;
import natures.debris.p2.common.registry.RegistryHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class Modernity implements IModernity {
    public static final Logger LOGGER = LogManager.getLogger("Modernity");

    public static final IEventBus EVENT_BUS = IModernity.eventBus();

    /*
     * LOADING LIFECYCLE
     */

    private LifecyclePhase lifecyclePhase = LifecyclePhase.UNLOADED;

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static <T> T injected() {
        return null;
    }

    // Mod Construction
    public void construct() {
        IModernity.eventBus().start();
        RegistryHandler.setup();

        lifecyclePhase = LifecyclePhase.CONSTRUCTED;
        EVENT_BUS.post(new ModernityLifecycleEvent(lifecyclePhase, this));
    }

    // Mod Loading Setup
    public void setup() {

        lifecyclePhase = LifecyclePhase.SET_UP;
        EVENT_BUS.post(new ModernityLifecycleEvent(lifecyclePhase, this));
    }

    // Mod Loading Complete
    public void complete() {

        lifecyclePhase = LifecyclePhase.LOADED;
        EVENT_BUS.post(new ModernityLifecycleEvent(lifecyclePhase, this));
    }

    @Override
    public IModernityInfo info() {
        return MDInfo.INSTANCE;
    }

    @Override
    public LifecyclePhase getLifecyclePhase() {
        return lifecyclePhase;
    }

    @Override
    public void tickSided(ISidedTickable sidedTickable) {
        MinecraftServer server = getServer();
        if (server != null && server.isOnExecutionThread()) {
            sidedTickable.serverTick();
        }
    }

    public static Modernity get() {
        return ModernityBootstrap.MODERNITY;
    }

    public MinecraftServer getServer() {
        return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
    }
}
