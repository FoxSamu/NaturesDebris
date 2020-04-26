/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.util.Direction;

public abstract class SingleDirectionalPlantBlock extends DirectionalPlantBlock {
    public SingleDirectionalPlantBlock( Properties properties, Direction growDir ) {
        super( properties, growDir );
    }

}
