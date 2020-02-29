/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.plant.growing.HangMossGrowLogic;

public class HangingMossBlock extends HangingPlantBlock {
    public HangingMossBlock( Properties properties ) {
        super( properties, HANG_MOSS_SHAPE, HANG_MOSS_END_SHAPE );
        setGrowLogic( new HangMossGrowLogic() );
    }
}
