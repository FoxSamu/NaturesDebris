/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.api.util;

import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.util.IStringSerializable;

import modernity.common.fluid.MDFluids;

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
