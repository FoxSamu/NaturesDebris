/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 21 - 2020
 * Author: rgsw
 */

package modernity.common.container.inventory;

import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class TrackingCleaningInventory extends Inventory implements ICleaningInventory {
    private IIntArray array = new IntArray( 7 );

    public TrackingCleaningInventory( int count ) {
        super( count );
    }

    public void setArray( IIntArray array ) {
        this.array = array;
    }

    public IIntArray getArray() {
        return array;
    }

    @Override
    public Fluid getFluid() {
        return ( (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS ).getValue( array.get( 6 ) );
    }

    @Override
    public int getFluidAmount() {
        return array.get( 4 );
    }

    @Override
    public void setFluid( Fluid fluid ) {
        array.set( 4, ( (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS ).getID( fluid ) );
    }

    @Override
    public void setFluidAmount( int amount ) {
        array.set( 4, amount );
    }
}
