/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;

public class GooDripsBlock extends SingleDirectionalPlantBlock {
    private static final VoxelShape SHAPE = makeHangPlantShape( 15, 15 );

    public GooDripsBlock( Properties properties ) {
        super( properties, Direction.DOWN );
    }

    @Override
    public VoxelShape getShape( BlockState state ) {
        return SHAPE;
    }
}
