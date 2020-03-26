/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.dimension.DimensionType;

// TODO Re-evaluate as Forge implemented ITeleporter again - is this still necessary?
public interface ICustomDimensionTravelEntity {
    default Entity customTravelDimension( DimensionType to, Teleporter tp ) {
        return null;
    }
    boolean preTravelDimension( DimensionType to, Teleporter tp );
    void postTravelDimension( DimensionType to, Teleporter tp, Entity newEntity );
}
