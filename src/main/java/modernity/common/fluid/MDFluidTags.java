/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * Holder class for Modernity fluid tags.
 */
public final class MDFluidTags {
    // Fluids assigned to this tag can be converted to portal fluid
    public static final Tag<Fluid> PORTALIZABLE = new FluidTags.Wrapper( new ResourceLocation( "modernity:portalizable" ) );


    public static final Tag<Fluid> OIL = new FluidTags.Wrapper( new ResourceLocation( "modernity:oil" ) );
    public static final Tag<Fluid> MODERNIZED_WATER = new FluidTags.Wrapper( new ResourceLocation( "modernity:modernized_water" ) );
    public static final Tag<Fluid> HEATROCK = new FluidTags.Wrapper( new ResourceLocation( "modernity:heatrock" ) );

    private MDFluidTags() {
    }
}
