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

package natures.debris.api;

import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;

import java.lang.reflect.Field;

final class APIDelegate {
    static final IEventBus EVENT_BUS = BusBuilder.builder().startShutdown().build();
    static INaturesDebris instance;
    private static boolean found;

    private APIDelegate() {
    }

    static INaturesDebris get() {
        if (!found) {
            try {
                Class<?> cls = Class.forName("natures.debris.Bootstrap");
                Field field = cls.getField("PROXY");
                instance = (INaturesDebris) field.get(null);
            } catch (Exception ignored) {
            }
            found = true;
        }
        return instance;
    }

    static LifecyclePhase getLifecyclePhase() {
        try {
            Class<?> cls = Class.forName("natures.debris.Bootstrap");
            Field field = cls.getDeclaredField("lifecyclePhase");
            field.setAccessible(true);
            return (LifecyclePhase) field.get(null);
        } catch (Exception ignored) {
            return LifecyclePhase.UNLOADED;
        }
    }
}
