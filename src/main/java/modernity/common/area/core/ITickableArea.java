/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.area.core;

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
