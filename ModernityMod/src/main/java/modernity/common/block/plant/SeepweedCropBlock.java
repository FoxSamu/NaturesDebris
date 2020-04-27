/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlockStateProperties;
import modernity.common.block.plant.growing.SaltyCropGrowLogic;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.shapes.VoxelShape;

public class SeepweedCropBlock extends CropBlock {
    private static final int[] HEIGHTS = {
        3, 6, 9, 12, 14, 16
    };

    private static final VoxelShape[] SHAPES = new VoxelShape[ 6 ];

    static {
        for( int i = 0; i < 6; i++ ) {
            SHAPES[ i ] = makePlantShape( 15, HEIGHTS[ i ] );
        }
    }

    public SeepweedCropBlock( Properties properties ) {
        super( properties );
        setGrowLogic( new SaltyCropGrowLogic( this ) );
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return MDBlockStateProperties.AGE_1_6;
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
