/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant;

import modernity.common.blockold.fluid.IWaterloggedBlock;

public class PebblesBlock extends CavePlantBlock implements IWaterloggedBlock {
    public PebblesBlock(Properties properties) {
        super(properties, PEBBLES_SHAPE);
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }
}
