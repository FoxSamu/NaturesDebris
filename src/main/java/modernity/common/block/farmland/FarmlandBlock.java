/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import modernity.common.tileentity.FarmlandTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FarmlandBlock extends Block {
    public static final EnumProperty<Fertility> FERTILITY = EnumProperty.create( "fertility", Fertility.class );
    public static final EnumProperty<Wetness> WETNESS = EnumProperty.create( "wetness", Wetness.class );

    public FarmlandBlock( Properties properties ) {
        super( properties );
    }

    @Override
    protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
        super.fillStateContainer( builder );
        builder.add( FERTILITY, WETNESS );
    }

    @Override
    public boolean hasTileEntity( BlockState state ) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
        return new FarmlandTileEntity();
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public void onReplaced( BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving ) {
        if( ! ( newState.getBlock() instanceof FarmlandBlock ) ) {
            world.removeTileEntity( pos );
        }
    }
}
