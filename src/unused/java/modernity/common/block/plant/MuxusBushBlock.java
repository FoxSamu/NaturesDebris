/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.plant;

import modernity.api.util.EntityUtil;
import modernity.common.block.plant.growing.BushGrowLogic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class MuxusBushBlock extends BushBlock {
    public MuxusBushBlock( Properties properties ) {
        super( properties );
        setGrowLogic( new BushGrowLogic( this ) );
    }

    @Override
    public void onEntityCollision( BlockState state, World world, BlockPos pos, Entity entity ) {
        if( entity instanceof PlayerEntity && ( (PlayerEntity) entity ).abilities.isFlying ) return;
        entity.setMotion( entity.getMotion().mul( 0.65, 0.65, 0.65 ) );
        EntityUtil.suspendFallDistance( entity, 0.2 );
    }

    @Override
    public boolean canRemain( IWorldReader world, BlockPos pos, BlockState state, Direction dir, BlockPos adj, BlockState adjState ) {
        return dir != Direction.DOWN || canBlockSustain( world, adj, adjState );
    }

    public boolean canBlockSustain( IWorldReader reader, BlockPos pos, BlockState state ) {
        return state.getBlock() == this || isBlockSideSustainable( state, reader, pos, Direction.UP );
    }

    @Override
    public boolean provide( IWorld world, BlockPos pos, Random rand ) {
        return false;
    }
}
