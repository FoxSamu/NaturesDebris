/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 15 - 2019
 */

package modernity.common.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import modernity.common.fluid.FluidEntry;
import modernity.common.fluid.MDFluids;

public class MDRegistries {

    private static ForgeRegistry<FluidEntry> fluids;

    public static ForgeRegistry<FluidEntry> fluids() {
        return fluids;
    }

    public static void register() {
        RegistryBuilder<FluidEntry> fluids = new RegistryBuilder<>();
        fluids.setName( new ResourceLocation( "modernity:fluids" ) );
        fluids.setType( FluidEntry.class );
        fluids.setIDRange( 5, Integer.MAX_VALUE );
        fluids.add( (IForgeRegistry.BakeCallback<FluidEntry>) ( owner, stage ) -> MDFluids.inject() );
        MDRegistries.fluids = (ForgeRegistry<FluidEntry>) fluids.create();
    }
}
