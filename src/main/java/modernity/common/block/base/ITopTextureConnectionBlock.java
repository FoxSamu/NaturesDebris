/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 16 - 2020
 * Author: rgsw
 */

package modernity.common.block.base;

import modernity.api.util.CTMUtil;
import modernity.api.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public interface ITopTextureConnectionBlock {
    @OnlyIn( Dist.CLIENT )
    ModelProperty<Integer> CONNECTIONS = new ModelProperty<>();

    default boolean canConnectTo( BlockState state ) {
        return state.getBlock() == this;
    }

    @OnlyIn( Dist.CLIENT )
    default void fillModelData( IEnviromentBlockReader world, BlockPos pos, BlockState state, IModelData data ) {
        int connections = 0;
        MovingBlockPos mpos = new MovingBlockPos();

        if( canConnectTo( world.getBlockState( mpos.setPos( pos ).moveNorth() ) ) ) connections |= CTMUtil.UP;
        if( canConnectTo( world.getBlockState( mpos.setPos( pos ).moveEast() ) ) ) connections |= CTMUtil.RIGHT;
        if( canConnectTo( world.getBlockState( mpos.setPos( pos ).moveSouth() ) ) ) connections |= CTMUtil.DOWN;
        if( canConnectTo( world.getBlockState( mpos.setPos( pos ).moveWest() ) ) ) connections |= CTMUtil.LEFT;
        if( canConnectTo( world.getBlockState( mpos.setPos( pos ).moveNorth().moveEast() ) ) )
            connections |= CTMUtil.UPRIGHT;
        if( canConnectTo( world.getBlockState( mpos.setPos( pos ).moveNorth().moveWest() ) ) )
            connections |= CTMUtil.UPLEFT;
        if( canConnectTo( world.getBlockState( mpos.setPos( pos ).moveSouth().moveEast() ) ) )
            connections |= CTMUtil.DOWNRIGHT;
        if( canConnectTo( world.getBlockState( mpos.setPos( pos ).moveSouth().moveWest() ) ) )
            connections |= CTMUtil.DOWNLEFT;

        data.setData( CONNECTIONS, connections );
    }
}
