/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public class DummyContainer extends Container {
    public DummyContainer() {
        super( null, 0 );
    }

    @Override
    public boolean canInteractWith( PlayerEntity playerIn ) {
        return true;
    }
}
