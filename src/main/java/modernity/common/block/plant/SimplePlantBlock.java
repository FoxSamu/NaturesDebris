/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 06 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;

public class SimplePlantBlock extends SingleDirectionalPlantBlock {
    public static final VoxelShape MELION_SHAPE = makePlantShape( 12, 12 );
    public static final VoxelShape MILLIUM_SHAPE = makePlantShape( 14, 8 );
    public static final VoxelShape MINT_SHAPE = makePlantShape( 14, 9 );
    public static final VoxelShape HORSETAIL_SHAPE = makePlantShape( 14, 12 );
    public static final VoxelShape LAKEWEED_SHAPE = makePlantShape( 14, 10 );
    public static final VoxelShape HEATH_SHAPE = makePlantShape( 16, 7 );
    public static final VoxelShape SEEPWEED_SHAPE = makePlantShape( 15, 16 );
    public static final VoxelShape NUDWART_SHAPE = makePlantShape( 14, 9 );
    public static final VoxelShape MURK_FLOWERS_SHAPE = makePlantShape( 14, 12 );

    protected final VoxelShape shape;

    public SimplePlantBlock( Properties properties, VoxelShape shape ) {
        super( properties, Direction.UP );
        this.shape = shape;
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( MDBlockTags.DIRTLIKE );
    }

    @Override
    public VoxelShape getShape( BlockState state ) {
        return shape;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
