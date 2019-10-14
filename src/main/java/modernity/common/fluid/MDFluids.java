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

    // V I0.2.0
    public static final ModernizedWaterFluid MODERNIZED_WATER = register( "modernized_water", new ModernizedWaterFluid.Source() );
    public static final ModernizedWaterFluid FLOWING_MODERNIZED_WATER = register( "flowing_modernized_water", new ModernizedWaterFluid.Flowing() );

    public static final HeatrockFluid HEATROCK = register( "heatrock", new HeatrockFluid.Source() );
    public static final HeatrockFluid FLOWING_HEATROCK = register( "flowing_heatrock", new HeatrockFluid.Flowing() );

    public static final OilFluid OIL = register( "oil", new OilFluid.Source() );
    public static final OilFluid FLOWING_OIL = register( "flowing_oil", new OilFluid.Flowing() );

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
        if( fluidA instanceof WaterFluid && fluidB instanceof ModernizedWaterFluid ) {
            return true;
        } else if( fluidB instanceof WaterFluid && fluidA instanceof ModernizedWaterFluid ) {
            return true;
        } else {
            return fluidA.isEquivalentTo( fluidB );
        }
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
