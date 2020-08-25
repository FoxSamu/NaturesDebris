/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.tileentity;

import modernity.api.util.ISidedTickable;
import natures.debris.common.ModernityOld;
import net.minecraft.tileentity.ITickableTileEntity;

public interface ISidedTickableTileEntity extends ITickableTileEntity, ISidedTickable {
    @Override
    default void tick() {
        ModernityOld.get().callSidedTick(this);
    }
}
