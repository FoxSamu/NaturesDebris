/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.generic.dimension;

import net.minecraft.util.math.Vec3d;

@FunctionalInterface
@Deprecated
public interface IReverbDimension {
    boolean hasReverb( Vec3d pos );
}
