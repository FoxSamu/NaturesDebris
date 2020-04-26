/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 26 - 2019
 * Author: rgsw
 */

package modernity.common.tileentity;

import modernity.common.ModernityOld;
import modernity.common.util.ISidedTickable;
import net.minecraft.tileentity.ITickableTileEntity;

public interface ISidedTickableTileEntity extends ITickableTileEntity, ISidedTickable {
    @Override
    default void tick() {
        ModernityOld.get().callSidedTick( this );
    }
}
