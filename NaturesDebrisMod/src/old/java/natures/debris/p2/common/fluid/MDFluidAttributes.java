/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.p2.common.fluid;

import modernity.util.TypeUtil;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.fluids.FluidAttributes;

public class MDFluidAttributes extends FluidAttributes {
    private final ICustomRenderFluid customRender;

    public MDFluidAttributes(Builder builder, Fluid fluid) {
        super(builder, fluid);

        customRender = TypeUtil.castOrNull(fluid, ICustomRenderFluid.class);
    }

    @Override
    public int getColor(ILightReader world, BlockPos pos) {
        if (customRender != null) {
            return customRender.getColor(world.getFluidState(pos), pos, world);
        }
        return super.getColor(world, pos);
    }

    @Override
    public int getColor() {
        if (customRender != null) {
            return customRender.getDefaultColor();
        }
        return super.getColor();
    }
}
