/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.common.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MDFluids {
    public static final ModernizedWaterFluid MODERNIZED_WATER = new ModernizedWaterFluid.Source();
    public static final ModernizedWaterFluid MODERNIZED_WATER_FLOWING = new ModernizedWaterFluid.Flowing();
    public static final PortalFluid PORTAL = new PortalFluid.Source();
    public static final PortalFluid PORTAL_FLOWING = new PortalFluid.Flowing();

    private static final List<FluidEntry> VANILLA_ENTRIES = IRegistry.FLUID.stream()
                                                                           .map( FluidEntry::new )
                                                                           .collect( Collectors.toList() );

    public static void register( IForgeRegistry<FluidEntry> registry ) {
        for( FluidEntry entry : VANILLA_ENTRIES ) {
            registry.register( entry );
        }

        registry.register( new FluidEntry( new ResourceLocation( "modernity:modernized_water" ), MODERNIZED_WATER ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:modernized_water_flowing" ), MODERNIZED_WATER_FLOWING ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:portal" ), PORTAL ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:portal_flowing" ), PORTAL_FLOWING ) );
    }

    public static void inject( int id, FluidEntry entry ) {
        IRegistry.FLUID.register( id, Objects.requireNonNull( entry.getRegistryName() ), entry.getFluid() );
    }

    public static void calculateStateRegistry( ObjectIntIdentityMap<IFluidState> fluidStateMap, IForgeRegistryInternal<FluidEntry> registry ) {
        for( FluidEntry entry : registry ) {
            for( IFluidState state : entry.getFluid().getStateContainer().getValidStates() ) {
                fluidStateMap.add( state );
            }
        }

        for( Object obj : fluidStateMap ) {
            IFluidState state = (IFluidState) obj;
            // STATE_REGISTRY does not seem to be used anywhere, though we still repopulate it just in case
            Fluid.STATE_REGISTRY.put( state, fluidStateMap.get( state ) );
        }
    }
}
