/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   04 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import modernity.api.util.CTMUtil;
import modernity.api.util.MovingBlockPos;
import modernity.client.model.farmlandctm.FarmlandConnectedTextureModel;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;

public interface ITopTextureConnectionBlock {

    default boolean canConnectTo( IEnviromentBlockReader world, MovingBlockPos pos, BlockState state ) {
        return state.getBlock() == this;
    }

    @OnlyIn( Dist.CLIENT )
    default void fillModelData( IEnviromentBlockReader world, BlockPos pos, BlockState state, IModelData data ) {
        int connections = 0;
        MovingBlockPos mpos = new MovingBlockPos();

        if( canConnectTo( world, mpos.setPos( pos ).moveNorth(), world.getBlockState( mpos ) ) )
            connections |= CTMUtil.UP;
        if( canConnectTo( world, mpos.setPos( pos ).moveEast(), world.getBlockState( mpos ) ) )
            connections |= CTMUtil.RIGHT;
        if( canConnectTo( world, mpos.setPos( pos ).moveSouth(), world.getBlockState( mpos ) ) )
            connections |= CTMUtil.DOWN;
        if( canConnectTo( world, mpos.setPos( pos ).moveWest(), world.getBlockState( mpos ) ) )
            connections |= CTMUtil.LEFT;
        if( canConnectTo( world, mpos.setPos( pos ).moveNorth().moveEast(), world.getBlockState( mpos ) ) )
            connections |= CTMUtil.UPRIGHT;
        if( canConnectTo( world, mpos.setPos( pos ).moveNorth().moveWest(), world.getBlockState( mpos ) ) )
            connections |= CTMUtil.UPLEFT;
        if( canConnectTo( world, mpos.setPos( pos ).moveSouth().moveEast(), world.getBlockState( mpos ) ) )
            connections |= CTMUtil.DOWNRIGHT;
        if( canConnectTo( world, mpos.setPos( pos ).moveSouth().moveWest(), world.getBlockState( mpos ) ) )
            connections |= CTMUtil.DOWNLEFT;

        data.setData( FarmlandConnectedTextureModel.CONNECTIONS, connections );
    }
}
