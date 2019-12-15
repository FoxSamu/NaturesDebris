/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.api.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.dimension.DimensionType;

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
