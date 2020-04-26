/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.block.dirt.logic;

public class BasicLogicType implements IDirtLogicType {
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
