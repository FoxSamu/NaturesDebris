/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.dimension;

/**
 * Dimensions implementing this interface receive an initialization event on world load.
 */
@FunctionalInterface
public interface IInitializeDimension {
    void init();
}
