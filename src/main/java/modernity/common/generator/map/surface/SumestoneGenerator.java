/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.generator.map.surface;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDNatureBlocks;
import modernity.common.generator.map.LayerDepositGenerator;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;

public class SumestoneGenerator extends LayerDepositGenerator<SurfaceGenData> {
    public SumestoneGenerator( IWorld world ) {
        super( world, - 1, 18, 8 );
    }

    @Override
    protected void place( WorldGenRegion region, MovingBlockPos pos, SurfaceGenData data ) {
        if( region.getBlockState( pos ).getMaterial().blocksMovement() ) {
            region.setBlockState( pos, MDNatureBlocks.SUMESTONE.getDefaultState(), 2 );
        }
    }
}
