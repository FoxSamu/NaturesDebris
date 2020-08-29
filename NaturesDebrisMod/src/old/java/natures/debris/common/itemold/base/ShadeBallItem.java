/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.itemold.base;

import natures.debris.common.entity.ShadeBallEntity;
import natures.debris.common.entity.ThrownItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class ShadeBallItem extends ThrowableItem {
    public ShadeBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public ThrownItemEntity make(World world, LivingEntity thrower) {
        return new ShadeBallEntity(thrower, world);
    }

    @Override
    public ThrownItemEntity make(World world, double x, double y, double z) {
        return new ShadeBallEntity(x, y, z, world);
    }
}