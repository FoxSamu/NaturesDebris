/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.tileentity;

import modernity.common.ModernityOld;
import modernity.api.util.ISidedTickable;
import net.minecraft.tileentity.ITickableTileEntity;

public interface ISidedTickableTileEntity extends ITickableTileEntity, ISidedTickable {
    @Override
    default void tick() {
        ModernityOld.get().callSidedTick(this);
    }
}
