/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 20 - 2019
 * Author: rgsw
 */

package modernity.api.dimension;

import net.minecraft.util.math.BlockPos;

public interface IPrecipitationDimension {
    boolean isRaining();
    boolean isRainingAt( BlockPos pos );
}
