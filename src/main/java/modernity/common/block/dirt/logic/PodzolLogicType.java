/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt.logic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PodzolLogicType implements IDecayingLogicType {
    @Override
    public IDirtLogicType getDecayed( World world, BlockPos pos, BlockState state ) {
        return MDDirtLogics.HUMUS_TYPE;
    }

    @Override
    public boolean allowOnFarmland() {
        return true;
    }

    @Override
    public boolean allowOnNormal() {
        return true;
    }

    @Override
    public boolean canSwitchTo( DirtLogic logic, IDirtLogicType switchTo ) {
        return true;
    }
}
