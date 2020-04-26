/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 22 - 2020
 * Author: rgsw
 */

package modernity.common.item.base;

import modernity.common.entity.ShadeBallEntity;
import modernity.common.entity.ThrownItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class ShadeBallItem extends ThrowableItem {
    public ShadeBallItem( Properties properties ) {
        super( properties );
    }

    @Override
    public ThrownItemEntity make( World world, LivingEntity thrower ) {
        return new ShadeBallEntity( thrower, world );
    }

    @Override
    public ThrownItemEntity make( World world, double x, double y, double z ) {
        return new ShadeBallEntity( x, y, z, world );
    }
}
