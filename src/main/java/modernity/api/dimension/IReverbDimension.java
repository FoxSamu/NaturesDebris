/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 24 - 2020
 * Author: rgsw
 */

package modernity.api.dimension;

import net.minecraft.util.math.Vec3d;

@FunctionalInterface
public interface IReverbDimension {
    boolean hasReverb( Vec3d pos );
}
