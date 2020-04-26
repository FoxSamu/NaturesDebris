/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.generic.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.dimension.DimensionType;

/**
 * Represents a region in a specific dimension.
 */
public class DimensionRegion {
    private final AxisAlignedBB box;
    private final DimensionType dimen;

    public DimensionRegion( AxisAlignedBB box, DimensionType dimen ) {
        this.box = box;
        this.dimen = dimen;
    }

    public AxisAlignedBB getRegion() {
        return box;
    }

    public DimensionType getDimension() {
        return dimen;
    }
}
