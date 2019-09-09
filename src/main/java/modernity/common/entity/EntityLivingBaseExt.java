/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class EntityLivingBaseExt extends EntityLivingBase {
    protected EntityLivingBaseExt( EntityType<?> type, World world ) {
        super( type, world );
    }
}
