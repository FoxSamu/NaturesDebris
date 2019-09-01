/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class MDFluidTags {
    // Fluids assigned to this tag can be converted to portal fluid
    public static final Tag<Fluid> PORTALIZABLE = new FluidTags.Wrapper( new ResourceLocation( "modernity:portalizable" ) );


    public static final Tag<Fluid> OIL = new FluidTags.Wrapper( new ResourceLocation( "modernity:oil" ) );
}
