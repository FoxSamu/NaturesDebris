/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved.
 * This file is part of the Modernity Plugin API and may be used,
 * included and distributed within other projects without further
 * permission, unless the copyright holder is not the original
 * author or the owner had forbidden the user to use this file.
 * Other terms and conditions still apply.
 *
 * For a full license, see LICENSE.txt.
 */

package modernity.api.plugin;

import modernity.api.IModernity;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

/**
 * Plugins implementing this interface receive Modernity lifecycle updates. Because Forge loads mods using multiple
 * threads, using the FML lifecycle events of your mod may cause concurrent issues when accessing the Modernity. To
 * ensure the Modernity is in a correct state for loading your plugin, use this listener instead.
 */
public interface ILifecycleListener {

    /**
     * Called after the Modernity finished construction-time loading. More specifically: this method is called once the
     * Modernity has finished loading in the mod class constructor.
     *
     * @param modernity The Modernity instance
     */
    default void modernityConstruct( IModernity modernity ) {
    }

    /**
     * Called after the Modernity finished setup-time loading. More specifically: this method is called once the
     * Modernity has finished loading during the {@link FMLCommonSetupEvent}.
     *
     * @param modernity The Modernity instance
     */
    default void modernitySetup( IModernity modernity ) {
    }

    /**
     * Called after the Modernity finished completion-time loading. More specifically: this method is called once the
     * Modernity has finished loading during the {@link FMLLoadCompleteEvent}.
     *
     * @param modernity The Modernity instance
     */
    default void modernityLoaded( IModernity modernity ) {
    }
}
