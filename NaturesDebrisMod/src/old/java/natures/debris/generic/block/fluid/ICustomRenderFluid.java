/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.generic.block.fluid;

import net.minecraft.fluid.IFluidState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Implementing fluids are rendered using Modernity's fluid render instead of Vanilla's fluid renderer, allowing gas
 * fluids to be rendered...
 *
 * @author RGSW
 */
public interface ICustomRenderFluid {

    /** Still fluid texture */
    @OnlyIn(Dist.CLIENT)
    ResourceLocation getStill();

    /** Flowing fluid texture */
    @OnlyIn(Dist.CLIENT)
    ResourceLocation getFlowing();

    /** Overlay texture, can be null */
    @OnlyIn(Dist.CLIENT)
    @Nullable
    ResourceLocation getOverlay();

    /** Color multiplier of the fluid */
    @OnlyIn(Dist.CLIENT)
    default int getColor(IFluidState state, BlockPos pos, ILightReader world) {
        return 0xffffffff;
    }

    @OnlyIn(Dist.CLIENT)
    default int getDefaultColor() {
        return 0xffffffff;
    }

    /** Slope weight of source blocks, usually 10 */
    default int getSourceSlopeWeight() {
        return 10;
    }
}
