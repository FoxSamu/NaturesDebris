/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 30 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.util.Direction;

public class DoubleStandingPlantBlock extends DoubleDirectionalPlantBlock {
    public DoubleStandingPlantBlock( Properties properties ) {
        super( properties, Direction.UP );
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
