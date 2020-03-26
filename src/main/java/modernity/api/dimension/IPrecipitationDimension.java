/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 25 - 2020
 * Author: rgsw
 */

package modernity.api.dimension;

import net.minecraft.util.math.BlockPos;

public interface IPrecipitationDimension {
    boolean isRaining();
    boolean isRainingAt( BlockPos pos );
    int getRainLevel();
    double getRainAmount();
}
