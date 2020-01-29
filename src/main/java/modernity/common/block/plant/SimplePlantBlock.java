/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class SimplePlantBlock extends SingleDirectionalPlantBlock {
    public static final VoxelShape MELION_SHAPE = makeCuboidShape( 2, 0, 2, 14, 12, 14 );
    public static final VoxelShape MILLIUM_SHAPE = makeCuboidShape( 1, 0, 1, 15, 8, 15 );
    public static final VoxelShape MINT_SHAPE = makeCuboidShape( 1, 0, 1, 15, 9, 15 );
    public static final VoxelShape HORSETAIL_SHAPE = makeCuboidShape( 1, 0, 1, 15, 12, 15 );

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
    @SuppressWarnings( "deprecation" )
    public VoxelShape getShape( BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx ) {
        Vec3d off = state.getOffset( world, pos );
        return shape.withOffset( off.x, off.y, off.z );
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
