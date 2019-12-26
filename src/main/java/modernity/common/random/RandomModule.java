/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 26 - 2019
 * Author: rgsw
 */

package modernity.common.random;

import modernity.common.Modernity;
import modul.module.Module;

public class RandomModule extends Module {
    private final Modernity modernity;

    public RandomModule( Modernity modernity ) {
        this.modernity = modernity;
    }

    @Override
    protected void onInit() {
        Modernity.LOGGER.info( "Modernity instance: " + modernity );
    }
}
