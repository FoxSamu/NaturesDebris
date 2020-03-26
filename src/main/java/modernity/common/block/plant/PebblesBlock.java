/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 31 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.fluid.IWaterloggedBlock;

public class PebblesBlock extends CavePlantBlock implements IWaterloggedBlock {
    public PebblesBlock( Properties properties ) {
        super( properties, PEBBLES_SHAPE );
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }
}
