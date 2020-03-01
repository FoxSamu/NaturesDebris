/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 01 - 2020
 * Author: rgsw
 */

package modernity.common.fluid;

import modernity.api.block.fluid.ICustomRenderFluid;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for Modernity fluids.
 */
@ObjectHolder( "modernity" )
public final class MDFluids {
    private static final RegistryHandler<Fluid> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final MurkyWaterFluid MURKY_WATER = register( "murky_water", new MurkyWaterFluid.Source(), "modernized_water" );
    public static final MurkyWaterFluid FLOWING_MURKY_WATER = register( "flowing_murky_water", new MurkyWaterFluid.Flowing(), "flowing_modernized_water" );

    public static final CleanWaterFluid CLEAN_WATER = register( "clean_water", new CleanWaterFluid.Source() );
    public static final CleanWaterFluid FLOWING_CLEAN_WATER = register( "flowing_clean_water", new CleanWaterFluid.Flowing() );

    public static final MoltenRockFluid MOLTEN_ROCK = register( "molten_rock", new MoltenRockFluid.Source(), "heatrock" );
    public static final MoltenRockFluid FLOWING_MOLTEN_ROCK = register( "flowing_molten_rock", new MoltenRockFluid.Flowing(), "flowing_heatrock" );

    private static <T extends Fluid> T register( String id, T fluid, String... aliases ) {
        ENTRIES.register( id, fluid, aliases );
        return fluid;
    }

    public static void setup( RegistryEventHandler handler ) {
        handler.addHandler( Fluid.class, ENTRIES );
    }

    /**
     * Checks if two fluids are equivalent.
     */
    public static boolean areEquivalent( Fluid fluidA, Fluid fluidB ) {
        if( isWater( fluidA ) && isWater( fluidB ) ) {
            return true;
        } else {
            return fluidA.isEquivalentTo( fluidB );
        }
    }

    public static boolean isWater( Fluid fluid ) {
        return fluid instanceof WaterFluid || fluid instanceof MurkyWaterFluid || fluid instanceof CleanWaterFluid;
    }

    @OnlyIn( Dist.CLIENT )
    public static float getFluidHeight( IBlockReader world, BlockPos pos, Fluid fluid, IFluidState fluidState, int fall ) {
        if( Minecraft.getInstance().world == null ) {
            return fluidState.func_223408_f();
        }
        int weight = 0;
        float height = 0;

        int w = 10;
        if( fluid instanceof ICustomRenderFluid ) {
            w = ( (ICustomRenderFluid) fluid ).getSourceSlopeWeight();
        }

        for( int side = 0; side < 4; ++ side ) {
            BlockPos offPos = pos.add( - ( side & 1 ), 0, - ( side >> 1 & 1 ) );
            if( world.getFluidState( offPos.up( fall ) ).getFluid().isEquivalentTo( fluid ) ) {
                return 1;
            }

            IFluidState state = world.getFluidState( offPos );
            if( areEquivalent( state.getFluid(), fluid ) ) {
                if( state.isSource() ) {
                    if( w < 0 ) {
                        return state.func_223408_f();
                    } else {
                        height += state.func_223408_f() * w;
                        weight += w;
                    }
                } else {
                    height += state.func_223408_f();
                    ++ weight;
                }
            } else if( ! world.getBlockState( offPos ).getMaterial().isSolid() ) {
                ++ weight;
            }
        }

        return height / (float) weight;
    }

    private MDFluids() {
    }
}
