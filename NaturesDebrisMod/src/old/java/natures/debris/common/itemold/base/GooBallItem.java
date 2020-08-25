/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.itemold.base;

import natures.debris.common.entity.GooBallEntity;
import natures.debris.common.entity.ThrownItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class GooBallItem extends ThrowableItem {
    public GooBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public ThrownItemEntity make(World world, LivingEntity thrower) {
        return new GooBallEntity(thrower, world);
    }

    @Override
    public ThrownItemEntity make(World world, double x, double y, double z) {
        return new GooBallEntity(x, y, z, world);
    }
}
