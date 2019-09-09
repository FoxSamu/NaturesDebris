/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.fluid;

import modernity.api.block.fluid.ICustomRenderFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.IWorldReaderBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    public static final HeatrockFluid HEATROCK_FLUID = new HeatrockFluid.Source();
    public static final HeatrockFluid HEATROCK_FLUID_FLOWING = new HeatrockFluid.Flowing();
    public static final OilFluid OIL = new OilFluid.Source();
    public static final OilFluid OIL_FLOWING = new OilFluid.Flowing();

    private static final List<FluidEntry> VANILLA_ENTRIES = IRegistry.FLUID
            .stream()
            .map( obj -> new FluidEntry( obj ).setRegistryName( IRegistry.FLUID.getKey( obj ) ) )
            .collect( Collectors.toList() );

    public static void register( IForgeRegistry<FluidEntry> registry ) {
        for( FluidEntry entry : VANILLA_ENTRIES ) {
            registry.register( entry );
        }

        registry.register( new FluidEntry( new ResourceLocation( "modernity:modernized_water" ), MODERNIZED_WATER ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:modernized_water_flowing" ), MODERNIZED_WATER_FLOWING ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:portal" ), PORTAL ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:portal_flowing" ), PORTAL_FLOWING ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:heatrock" ), HEATROCK_FLUID ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:heatrock_flowing" ), HEATROCK_FLUID_FLOWING ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:oil" ), OIL ) );
        registry.register( new FluidEntry( new ResourceLocation( "modernity:oil_flowing" ), OIL_FLOWING ) );
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

    @OnlyIn( Dist.CLIENT )
    public static float getFluidHeight( IWorldReaderBase world, BlockPos pos, Fluid fluid, IFluidState fluidState, int fall ) {
        if( Minecraft.getInstance().world == null ) {
            return fluidState.getHeight();
        }
        int weight = 0;
        float height = 0.0F;

        int w = 10;
        if( fluid instanceof ICustomRenderFluid ) {
            w = ( (ICustomRenderFluid) fluid ).getSourceSlopeWeight();
        }

        for( int side = 0; side < 4; ++ side ) {
            BlockPos offPos = pos.add( - ( side & 1 ), 0, - ( side >> 1 & 1 ) );
            if( world.getFluidState( offPos.up( fall ) ).getFluid().isEquivalentTo( fluid ) ) {
                return 1.0F;
            }

            IFluidState state = world.getFluidState( offPos );
            if( state.getFluid().isEquivalentTo( fluid ) ) {
                if( state.isSource() ) {
                    if( w < 0 ) {
                        return state.getHeight();
                    } else {
                        height += state.getHeight() * w;
                        weight += w;
                    }
                } else {
                    height += state.getHeight();
                    ++ weight;
                }
            } else if( ! world.getBlockState( offPos ).getMaterial().isSolid() ) {
                ++ weight;
            }
        }

        return height / (float) weight;
    }
}
