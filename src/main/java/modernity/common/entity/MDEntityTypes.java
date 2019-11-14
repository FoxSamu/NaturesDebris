/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.entity;

import com.google.common.reflect.TypeToken;
import modernity.client.render.entity.FallBlockRender;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Holder class for Modernity entity types.
 */
@ObjectHolder( "modernity" )
public final class MDEntityTypes {
    private static final RegistryHandler<EntityType<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final EntityType<FallBlockEntity> FALL_BLOCK = register( "fall_block", EntityType.Builder.create( FallBlockEntity::new, EntityClassification.MISC ).setTrackingRange( 10 ).setUpdateInterval( 20 ).setShouldReceiveVelocityUpdates( true ).size( 0.98F, 0.98F ) );

    private MDEntityTypes() {
    }

    @SuppressWarnings( "unchecked" )
    public static <T extends Entity> EntityType<T> register( String id, EntityType.Builder<?> builder, String... aliases ) {
        EntityType<T> entry = (EntityType<T>) builder.build( "modernity:" + id );
        ENTRIES.register( id, entry, aliases );
        return entry;
    }

    /**
     * Adds the registry handler to the {@link RegistryEventHandler}. This must be called internally only.
     */
    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<EntityType<?>> token = new TypeToken<EntityType<?>>( EntityType.class ) {
        };
        handler.addHandler( (Class<EntityType<?>>) token.getRawType(), ENTRIES );
    }

    /**
     * Initializes the entity renderers.
     */
    @OnlyIn( Dist.CLIENT )
    public static void initEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler( FallBlockEntity.class, new FallBlockRender.Factory() );
    }
}
