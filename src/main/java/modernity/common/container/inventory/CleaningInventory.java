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
import net.minecraft.item.ItemStack;

public class CleaningInventory extends Inventory implements ICleaningInventory {
    private Fluid fluid;
    private int amount;

    public CleaningInventory( Fluid fluid, int amount, int itemCount ) {
        super( itemCount );
        this.fluid = fluid;
        this.amount = amount;
    }

    public CleaningInventory( Fluid fluid, int amount, ItemStack... stacks ) {
        super( stacks );
        this.fluid = fluid;
        this.amount = amount;
    }

    public CleaningInventory( int itemCount ) {
        super( itemCount );
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public int getFluidAmount() {
        return fluid == null ? 0 : amount;
    }

    @Override
    public void setFluid( Fluid fluid ) {
        this.fluid = fluid;
    }

    @Override
    public void setFluidAmount( int amount ) {
        this.amount = amount;
    }
}
