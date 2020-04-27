/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlockStateProperties;
import modernity.common.block.plant.growing.WetCropGrowLogic;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.shapes.VoxelShape;

public class MurkRiceCropBlock extends CropBlock {
    private static final int[] HEIGHTS = {
        3, 3, 5, 8, 11, 14, 15, 15
    };

    private static final VoxelShape[] SHAPES = new VoxelShape[ 8 ];

    static {
        for( int i = 0; i < 8; i++ ) {
            SHAPES[ i ] = makePlantShape( 16, HEIGHTS[ i ] );
        }
    }

    public MurkRiceCropBlock( Properties properties ) {
        super( properties );
        setGrowLogic( new WetCropGrowLogic( this ) );
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return MDBlockStateProperties.AGE_1_8;
    }

    @Override
    public OffsetType getOffsetType() {
        return super.getOffsetType();
    }

    @Override
    public VoxelShape getShape( BlockState state ) {
        return SHAPES[ state.get( age ) - 1 ];
    }
}
