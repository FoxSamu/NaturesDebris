/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 8 - 2019
 */

package modernity.common.block.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;

import modernity.common.fluid.RegularFluid;
import modernity.common.world.dim.MDDimensions;
import modernity.common.world.teleporter.ModernityTeleporter;

public class BlockPortalFluid extends BlockFluid {
    public BlockPortalFluid( String id, RegularFluid fluid, Properties builder ) {
        super( id, fluid, builder );
    }

    @Override
    public void onEntityCollision( IBlockState state, World world, BlockPos pos, Entity entity ) {
        if( ! world.isRemote && world instanceof WorldServer ) {
            DimensionType dimen = MDDimensions.MODERNITY.getType();
            if( world.getDimension().getType() == dimen ) {
                dimen = DimensionType.OVERWORLD;
            }
            WorldServer otherWorld = ( (WorldServer) world ).getServer().getWorld( dimen );
            entity.changeDimension( dimen, ModernityTeleporter.getTeleporter( otherWorld ) );
        }
    }
}
