/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.area.core;

@FunctionalInterface
public interface ITickableArea extends IServerTickableArea, IClientTickableArea {
    void tick();

    @Override
    default void tickClient() {
        tick();
    }

    @Override
    default void tickServer() {
        tick();
    }
}
