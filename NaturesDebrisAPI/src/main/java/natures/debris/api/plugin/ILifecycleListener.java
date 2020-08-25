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

package natures.debris.api.plugin;

import natures.debris.api.INaturesDebris;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

/**
 * Plugins implementing this interface receive Nature's Debris lifecycle updates. Because Forge loads mods using
 * multiple threads, using the FML lifecycle events of your mod may cause concurrent issues when accessing Nature's
 * Debris. To ensure Nature's Debris is in a correct state for loading your plugin, use this listener instead.
 */
public interface ILifecycleListener {

    /**
     * Called after Nature's Debris finished construction-time loading. More specifically: this method is called once
     * the Nature's Debris has finished loading in the mod class constructor.
     *
     * @param naturesDebris The Nature's Debris instance
     */
    default void ndebrisConstruct(INaturesDebris naturesDebris) {
    }

    /**
     * Called after Nature's Debris finished setup-time loading. More specifically: this method is called once the
     * Nature's Debris has finished loading during the {@link FMLCommonSetupEvent}.
     *
     * @param naturesDebris The Nature's Debris instance
     */
    default void ndebrisSetup(INaturesDebris naturesDebris) {
    }

    /**
     * Called after Nature's Debris finished completion-time loading. More specifically: this method is called once the
     * Nature's Debris has finished loading during the {@link FMLLoadCompleteEvent}.
     *
     * @param naturesDebris The Nature's Debris instance
     */
    default void ndebrisLoaded(INaturesDebris naturesDebris) {
    }
}
