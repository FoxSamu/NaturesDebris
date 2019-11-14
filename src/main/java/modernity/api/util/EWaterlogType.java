/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.util;

import modernity.common.fluid.MDFluids;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.IStringSerializable;

import java.util.function.Supplier;

/**
 * Waterlog type, used to make blocks waterlogged in both vanilla and modernized water...
 */
public enum EWaterlogType implements IStringSerializable {
    NONE( "none", Fluids.EMPTY::getDefaultState ),
    WATER( "water", Fluids.WATER::getDefaultState ),
    MODERNIZED_WATER( "modernized_water", MDFluids.MODERNIZED_WATER::getDefaultState );

    private final String name;
    private final Supplier<IFluidState> fluidStateSupplier;

    EWaterlogType( String name, Supplier<IFluidState> fluidStateSupplier ) {
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
        if( fluid == MDFluids.MODERNIZED_WATER && this != MODERNIZED_WATER ) {
            return true;
        }
        return fluid == Fluids.EMPTY && this != NONE;
    }

    /**
     * Returns the type for a specific fluid state
     */
    public static EWaterlogType getType( IFluidState state ) {
        if( state.getFluid() == MDFluids.MODERNIZED_WATER ) {
            return MODERNIZED_WATER;
        }
        if( state.getFluid() == Fluids.WATER ) {
            return WATER;
        }
        return NONE;
    }
}
