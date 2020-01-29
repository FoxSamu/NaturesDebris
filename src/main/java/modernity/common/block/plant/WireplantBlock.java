/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.util.EntityUtil;
import modernity.common.block.MDBlockTags;
import modernity.common.entity.MDEntityTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class WireplantBlock extends SimplePlantBlock {
    public WireplantBlock( Properties properties ) {
        super( properties, makeCuboidShape( 0, 0, 0, 16, 10, 16 ) );
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( ! entity.getType().isContained( MDEntityTags.WIREPLANT_IMMUNE ) ) {
            EntityUtil.setSmallerMotionMutliplier( entity, new Vec3d( 0.5, 0.5, 0.5 ) );
            EntityUtil.suspendFallDistance( entity, 0.5 );
        }
    }

    @Override
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
        return false;
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( MDBlockTags.DIRTLIKE );
    }
}
