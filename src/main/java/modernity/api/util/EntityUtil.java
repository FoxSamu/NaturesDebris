/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 28 - 2020
 * Author: rgsw
 */

package modernity.api.util;

import modernity.api.reflect.FieldAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public final class EntityUtil {
    private static final FieldAccessor<Entity, Vec3d> motionMultiplierField = new FieldAccessor<>( Entity.class, "field_213328_B" );

    private EntityUtil() {
    }

    public static Vec3d getMotionMultiplier( Entity entity ) {
        return motionMultiplierField.get( entity );
    }

    public static void setMotionMultiplier( Entity entity, Vec3d multiplier ) {
        motionMultiplierField.set( entity, multiplier );
    }

    public static void setSmallerMotionMutliplier( Entity entity, Vec3d multiplier ) {
        Vec3d current = getMotionMultiplier( entity );
        if( multiplier.lengthSquared() < current.lengthSquared() ) {
            setMotionMultiplier( entity, multiplier );
        }
    }

    public static void suspendFallDistance( Entity entity, double suspension ) {
        entity.fallDistance *= 1 - suspension;
    }


}
