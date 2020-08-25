/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.generic.dimension;

/**
 * Dimensions implementing this interface receive an initialization event on world load.
 */
@FunctionalInterface
public interface IInitializeDimension {
    void init();
}
