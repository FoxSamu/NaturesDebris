/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 06 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class DoubleStandingPlantBlock extends DoubleDirectionalPlantBlock {
    public static final VoxelShape REGULAR_SHAPE = makePlantShape( 15, 16 );

    private final VoxelShape bottom;
    private final VoxelShape top;

    public DoubleStandingPlantBlock( Properties properties ) {
        this( properties, VoxelShapes.fullCube(), VoxelShapes.fullCube() );
    }

    public DoubleStandingPlantBlock( Properties properties, VoxelShape shape ) {
        this( properties, shape, shape );
    }

    public DoubleStandingPlantBlock( Properties properties, VoxelShape bottom, VoxelShape top ) {
        super( properties, Direction.UP );
        this.bottom = bottom;
        this.top = top;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    @Override
    public VoxelShape getShape( BlockState state ) {
        return state.get( TYPE ) == ROOT ? bottom : top;
    }
}
