/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.common.block.MDBlockTags;
import modernity.common.block.MDPlantBlocks;
import modernity.common.block.plant.growing.FertilityGrowLogic;
import modernity.common.entity.MDEntityTags;
import modernity.generic.util.EntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class WireplantBlock extends SimplePlantBlock {
    public WireplantBlock( Properties properties ) {
        super( properties, makePlantShape( 16, 10 ) );
        setGrowLogic( new FertilityGrowLogic( this ) );
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( ! entity.getType().isContained( MDEntityTags.WIREPLANT_IMMUNE ) ) {
            EntityUtil.setSmallerMotionMutliplier( state, entity, new Vec3d( 0.65, 0.65, 0.65 ) );
            EntityUtil.suspendFallDistance( entity, 0.5 );
        }
    }

    @Override
    @SuppressWarnings( "deprecation" )
    public boolean isReplaceable( BlockState state, BlockItemUseContext useContext ) {
        return false;
    }

    @Override
    public boolean canBlockSustain( IWorldReader world, BlockPos pos, BlockState state ) {
        return state.isIn( MDBlockTags.DIRTLIKE );
    }

    @Override
    public void growAt( World world, BlockPos pos ) {
        if( world.rand.nextInt( 5 ) == 0 ) {
            world.setBlockState( pos, MDPlantBlocks.FLOWERED_WIREPLANT.computeStateForPos( world, pos ), 3 );
        } else {
            world.setBlockState( pos, MDPlantBlocks.WIREPLANT.computeStateForPos( world, pos ), 3 );
        }
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }
}
