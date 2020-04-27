/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public final class MDEntityTags {
    public static final Tag<EntityType<?>> NETTLES_IMMUNE = tag( "nettles_immune" );
    public static final Tag<EntityType<?>> TURUPT_IMMUNE = tag( "turupt_immune" );
    public static final Tag<EntityType<?>> FOXGLOVE_IMMUNE = tag( "foxglove_immune" );
    public static final Tag<EntityType<?>> WIREPLANT_IMMUNE = tag( "wireplant_immune" );
    public static final Tag<EntityType<?>> WATER_WIRE_IMMUNE = tag( "water_wire_immune" );
    public static final Tag<EntityType<?>> SHADE_BLUE_IMMUNE = tag( "shade_blue_immune" );

    private MDEntityTags() {
    }

    private static Tag<EntityType<?>> tag( String id ) {
        return new EntityTypeTags.Wrapper( new ResourceLocation( "modernity", id ) );
    }
}
