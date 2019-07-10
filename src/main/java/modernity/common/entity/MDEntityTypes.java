/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import modernity.client.render.entity.RenderFallBlock;

import java.util.ArrayList;

public class MDEntityTypes {
    private static final ArrayList<EntityType<?>> ENTRIES = new ArrayList<>();

    public static final EntityType<EntityFallBlock> FALL_BLOCK = register( "fall_block", EntityType.Builder.create( EntityFallBlock.class, EntityFallBlock::new ).tracker( 160, 20, true ) );

    public static <T extends Entity> EntityType<T> register( String id, EntityType.Builder<T> builder ) {
        EntityType<T> entry = builder.build( "modernity:" + id );
        entry.setRegistryName( new ResourceLocation( "modernity", id ) );
        ENTRIES.add( entry );
        return entry;
    }

    public static void register( IForgeRegistry<EntityType<?>> registry ) {
        registry.registerAll( ENTRIES.toArray( new EntityType[ 0 ] ) );
    }

    @OnlyIn( Dist.CLIENT )
    public static void registerClient() {
        RenderingRegistry.registerEntityRenderingHandler( EntityFallBlock.class, new RenderFallBlock.Factory() );
    }
}
