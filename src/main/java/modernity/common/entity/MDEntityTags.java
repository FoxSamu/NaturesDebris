/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 12 - 2020
 * Author: rgsw
 */

package modernity.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public final class MDEntityTags {
    public static final Tag<EntityType<?>> NETTLES_IMMUNE = tag( "nettles_immune" );

    private MDEntityTags() {
    }

    private static Tag<EntityType<?>> tag( String id ) {
        return new EntityTypeTags.Wrapper( new ResourceLocation( "modernity", id ) );
    }
}
