/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 26 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant.crop;

import modernity.common.block.farmland.FarmlandBlock;
import modernity.common.block.farmland.IFarmlandLogic;
import modernity.common.block.plant.SingleDirectionalPlantBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public abstract class CropBlock extends SingleDirectionalPlantBlock {

    private final IntegerProperty age = getAgeProperty();

    public CropBlock( Properties properties ) {
        super( properties, Direction.UP );
    }

    protected abstract IntegerProperty getAgeProperty();

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        builder.add( getAgeProperty() );
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return world.getBlockState( pos.down() ).getBlock() instanceof FarmlandBlock;
    }

    public void die( World world, BlockPos pos, BlockState state ) {
        world.setBlockState( pos, Blocks.AIR.getDefaultState() );
    }

    public void grow( World world, BlockPos pos, BlockState state, IFarmlandLogic farmland ) {

    }
}
