/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.fluid;

import modernity.common.registry.RegistryHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("modernity")
public final class MDFluids {

    public static final RegularFluid MURKY_WATER = new MurkyWaterFluid.Source();
    public static final RegularFluid FLOWING_MURKY_WATER = new MurkyWaterFluid.Flowing();

    public static void register(RegistryHandler<Fluid> reg) {
        reg.add("murky_water", MURKY_WATER);
        reg.add("flowing_murky_water", FLOWING_MURKY_WATER);
    }

    @OnlyIn(Dist.CLIENT)
    public static void setupRenderLayers() {
        RenderTypeLookup.setRenderLayer(MURKY_WATER, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(FLOWING_MURKY_WATER, RenderType.getTranslucent());
    }

    private MDFluids() {
    }
}
