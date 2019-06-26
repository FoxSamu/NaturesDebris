/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 26 - 2019
 */

package modernity.common.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import modernity.common.registry.MDRegistries;

public class MDFluids {
    public static final ModernizedWaterFluid MODERNIZED_WATER = new ModernizedWaterFluid.Source();
    public static final ModernizedWaterFluid MODERNIZED_WATER_FLOWING = new ModernizedWaterFluid.Flowing();
    public static final PortalFluid PORTAL = new PortalFluid.Source();
    public static final PortalFluid PORTAL_FLOWING = new PortalFluid.Flowing();

    public static void register( IForgeRegistry<FluidEntry> registry ) {
        registry.register( new FluidEntry( new ResourceLocation( "modernity:modernized_water" ), MODERNIZED_WATER ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:modernized_water_flowing" ), MODERNIZED_WATER_FLOWING ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:portal" ), PORTAL ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:portal_flowing" ), PORTAL_FLOWING ) );
    }

    public static void inject() {
        ForgeRegistry<FluidEntry> entries = MDRegistries.fluids();
        for( FluidEntry entry : entries ) {
            int id = entries.getID( entry );
            ResourceLocation name = entry.getRegistryName();

            IRegistry.FLUID.register( id, name, entry.getFluid() );

            for( IFluidState state : entry.getFluid().getStateContainer().getValidStates() ) {
                Fluid.STATE_REGISTRY.add( state );
            }
        }
    }
}
