/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.plant;

import modernity.common.block.farmland.IFarmland;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public abstract class DirectionalPlantBlock extends PlantBlock {
    protected final Direction growDir;

    public DirectionalPlantBlock( Properties properties, Direction growDir ) {
        super( properties );
        this.growDir = growDir;
    }

    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return isBlockSideSustainable( state, world, pos, growDir );
    }

    public boolean canRemainOn( IWorldReader world, BlockPos pos, BlockState state, BlockState selfState ) {
        return canBlockSustain( world, pos, state );
    }

    @Override
    public boolean canRemain( IWorldReader world, BlockPos pos, BlockState state, Direction dir, BlockPos adj, BlockState adjState ) {
        if( dir.getOpposite() == growDir ) {
            return canRemainOn( world, adj, adjState, state );
        }
        return true;
    }

    @Override
    protected IFarmland getSupportingFarmland( IWorld world, BlockPos pos ) {
        return IFarmland.get( world, pos.offset( growDir, - 1 ) );
    }

    public Direction getGrowDirection() {
        return growDir;
    }
}
