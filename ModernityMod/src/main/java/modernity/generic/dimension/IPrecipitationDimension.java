/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.generic.dimension;

import net.minecraft.util.math.BlockPos;

public interface IPrecipitationDimension {
    boolean isRaining();
    boolean isRainingAt( BlockPos pos );
    int getRainLevel();
    double getRainAmount();
}
