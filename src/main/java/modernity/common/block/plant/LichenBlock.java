/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.plant.growing.MossGrowLogic;

public class LichenBlock extends FacingPlantBlock {
    public LichenBlock( Properties properties ) {
        super( properties, 2, 0 );
        setGrowLogic( new MossGrowLogic( this ) );
    }
}
