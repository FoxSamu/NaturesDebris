/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.api.util;

import modernity.common.fluid.MDFluids;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.IStringSerializable;

import java.util.function.Supplier;

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

    public IFluidState getFluidState() {
        return fluidStateSupplier.get();
    }

    public boolean isEmpty() {
        return this == NONE;
    }

    public boolean canContain( Fluid fluid ) {
        if( fluid == Fluids.WATER && this != WATER ) {
            return true;
        }
        if( fluid == MDFluids.MODERNIZED_WATER && this != MODERNIZED_WATER ) {
            return true;
        }
        return fluid == Fluids.EMPTY && this != NONE;
    }

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
