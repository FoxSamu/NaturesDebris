/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.fluidold;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * Holder class for Modernity fluid tags.
 */
public final class MDFluidTags {
    public static final Tag<Fluid> MURKY_WATER = new FluidTags.Wrapper(new ResourceLocation("modernity:murky_water"));
    public static final Tag<Fluid> MOLTEN_ROCK = new FluidTags.Wrapper(new ResourceLocation("modernity:molten_rock"));
    public static final Tag<Fluid> CLEAN_WATER = new FluidTags.Wrapper(new ResourceLocation("modernity:clean_water"));
    public static final Tag<Fluid> CLEANING = new FluidTags.Wrapper(new ResourceLocation("modernity:cleaning"));

    private MDFluidTags() {
    }
}