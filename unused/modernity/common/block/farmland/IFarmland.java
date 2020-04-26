/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 29 - 2020
 * Author: rgsw
 */

package modernity.common.block.farmland;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public interface IFarmland {
    Fertility getFertility();
    void setFertility( Fertility fertility );

    int getLevel();
    void setLevel( int level );

    default boolean decreaseLevel() {
        int l = getLevel();
        if( l > 0 ) {
            setLevel( l - 1 );
            return true;
        }
        return false;
    }

    default boolean isFertile() {
        return getFertility() == Fertility.FERTILE && getLevel() > 0;
    }

    default boolean isDecayed() {
        return getFertility() == Fertility.DECAYED && getLevel() > 0;
    }

    default boolean isWet() {
        return getFertility() == Fertility.WET;
    }

    default boolean isSalty() {
        return getFertility() == Fertility.SALTY && getLevel() > 0;
    }

    @Nullable
    static IFarmland get( IWorld world, BlockPos pos ) {
        BlockState state = world.getBlockState( pos );
        Block block = state.getBlock();
        if( block instanceof IFarmlandBlock ) {
            return ( (IFarmlandBlock) block ).getFarmland( world, pos );
        }
        return null;
    }
}
