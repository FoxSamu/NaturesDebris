/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.common.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class FluidEntry implements IForgeRegistryEntry<FluidEntry> {
    private ResourceLocation regName;
    private final Fluid fluid;

    public FluidEntry( ResourceLocation regName, Fluid fluid ) {
        this.regName = regName;
        this.fluid = fluid;
    }


    public FluidEntry( Fluid fluid ) {
        this.fluid = fluid;
    }

    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public FluidEntry setRegistryName( ResourceLocation name ) {
        if( regName != null )
            throw new IllegalStateException( String.format( "Registry name already set to %s", regName ) );
        regName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return regName;
    }

    @Override
    public Class<FluidEntry> getRegistryType() {
        return FluidEntry.class;
    }
}
