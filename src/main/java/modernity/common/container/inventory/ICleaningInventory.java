/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.container.inventory;

import modernity.common.fluid.MDFluidTags;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;

public interface ICleaningInventory extends IInventory {
    Fluid getFluid();
    int getFluidAmount();

    void setFluid( Fluid fluid );
    void setFluidAmount( int amount );

    default boolean fillWith( Fluid fluid ) {
        if( ! fluid.isIn( MDFluidTags.CLEANING ) ) return false;
        int amount = getFluidAmount();
        if( amount >= 300 ) return false;
        Fluid current = getFluid();
        if( current == null || amount == 0 || current == fluid ) {
            if( current == null ) amount = 0;
            if( current != fluid ) setFluid( fluid );
            amount += 100;
            if( amount > 300 ) amount = 300;
            setFluidAmount( amount );
            return true;
        }
        return false;
    }

    default void drain() {
        setFluidAmount( 0 );
        setFluid( null );
    }
}
