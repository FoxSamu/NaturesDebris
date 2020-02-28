/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 28 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.plant.growing.IPlantResources;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class DoublePlantBlock extends DoubleDirectionalPlantBlock {
    public static final VoxelShape REGULAR_SHAPE = makePlantShape( 15, 16 );

    public static final Type MURK_LAVENDER = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 2 ).chance( 6, 16 ).requireWet() );
        return new VoxelShape[] { REGULAR_SHAPE };
    };

    public static final Type GLOBE_THISTLE = plant -> {
        plant.setSpreadingLogic( IPlantResources.fertile( 1, 2 ).chance( 6, 16 ).requireWet() );
        return new VoxelShape[] { REGULAR_SHAPE };
    };

    private final VoxelShape bottom;
    private final VoxelShape top;

    protected DoublePlantBlock( Properties properties ) {
        this( properties, VoxelShapes.fullCube(), VoxelShapes.fullCube() );
    }

    @Deprecated
    public DoublePlantBlock( Properties properties, VoxelShape shape ) {
        this( properties, shape, shape );
    }

    @Deprecated
    public DoublePlantBlock( Properties properties, VoxelShape bottom, VoxelShape top ) {
        super( properties, Direction.UP );
        this.bottom = bottom;
        this.top = top;
    }

    public DoublePlantBlock( Properties properties, Type type ) {
        super( properties, Direction.UP );
        VoxelShape[] shapes = type.setup( this );
        if( shapes.length == 0 ) {
            bottom = VoxelShapes.fullCube();
            top = VoxelShapes.fullCube();
        } else if( shapes.length == 1 ) {
            bottom = shapes[ 0 ];
            top = shapes[ 0 ];
        } else {
            bottom = shapes[ 0 ];
            top = shapes[ 1 ];
        }
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    @Override
    public VoxelShape getShape( BlockState state ) {
        return state.get( TYPE ) == ROOT ? bottom : top;
    }

    @FunctionalInterface
    public interface Type {
        VoxelShape[] setup( DoublePlantBlock plant );
    }
}