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

package modernity.api;

import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;

import java.lang.reflect.Field;

final class ModernityHolder {
    static final IEventBus EVENT_BUS = BusBuilder.builder().startShutdown().build();
    static IModernity modernity;
    private static boolean found;

    private ModernityHolder() {
    }

    static IModernity get() {
        if( ! found ) {
            try {
                Class<?> cls = Class.forName("modernity.ModernityBootstrap");
                Field field = cls.getField("MODERNITY");
                modernity = (IModernity) field.get(null);
            } catch(Exception ignored) {
            }
            found = true;
        }
        return modernity;
    }
}
