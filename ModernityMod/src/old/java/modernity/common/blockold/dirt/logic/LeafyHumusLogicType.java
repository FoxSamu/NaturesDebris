/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.dirt.logic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LeafyHumusLogicType implements IDecayingLogicType {
    @Override
    public IDirtLogicType getDecayed(World world, BlockPos pos, BlockState state) {
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
    public boolean canSwitchTo(DirtLogic logic, IDirtLogicType switchTo) {
        return true;
    }
}
