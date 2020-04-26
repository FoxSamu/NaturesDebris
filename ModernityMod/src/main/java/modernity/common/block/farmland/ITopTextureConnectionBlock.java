/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import modernity.client.model.farmland.FarmlandBakedModel;
import modernity.generic.util.CTMUtil;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;

public interface ITopTextureConnectionBlock {
    default boolean canConnectTo( ILightReader world, MovingBlockPos pos, BlockState state ) {
        return state.getBlock() == this;
    }

    @OnlyIn( Dist.CLIENT )
    default void fillModelData( ILightReader world, BlockPos pos, BlockState state, IModelData data ) {
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

        data.setData( FarmlandBakedModel.CONNECTIONS, connections );
    }
}
