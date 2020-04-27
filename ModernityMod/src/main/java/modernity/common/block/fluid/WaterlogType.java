/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.block.fluid;

import modernity.common.fluid.MDFluids;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.IStringSerializable;

import java.util.function.Supplier;

/**
 * Waterlog type, used to make blocks waterlogged in both vanilla and modernized water...
 */
public enum WaterlogType implements IStringSerializable {
    NONE( "none", Fluids.EMPTY::getDefaultState ),
    WATER( "water", Fluids.WATER::getDefaultState ),
    MURKY_WATER( "murky_water", MDFluids.MURKY_WATER::getDefaultState );

    private final String name;
    private final Supplier<IFluidState> fluidStateSupplier;

    WaterlogType( String name, Supplier<IFluidState> fluidStateSupplier ) {
        this.name = name;
        this.fluidStateSupplier = fluidStateSupplier;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns a still fluid state for this type
     */
    public IFluidState getFluidState() {
        return fluidStateSupplier.get();
    }

    /**
     * Checks whether this type is the empty type
     */
    public boolean isEmpty() {
        return this == NONE;
    }

    /**
     * Checks if this type can contain the specified fluid
     */
    public boolean canContain( Fluid fluid ) {
        if( fluid == Fluids.WATER && this != WATER ) {
            return true;
        }
        if( fluid == MDFluids.MURKY_WATER && this != MURKY_WATER ) {
            return true;
        }
        return fluid == Fluids.EMPTY && this != NONE;
    }

    /**
     * Returns the type for a specific fluid state
     */
    public static WaterlogType getType( IFluidState state ) {
        if( state.getFluid() == MDFluids.MURKY_WATER ) {
            return MURKY_WATER;
        }
        if( state.getFluid() == Fluids.WATER ) {
            return WATER;
        }
        return NONE;
    }
}
