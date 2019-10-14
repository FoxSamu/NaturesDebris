/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.common.tileentity;

import com.google.common.reflect.TypeToken;
import modernity.common.block.MDBlocks;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

/**
 * Holder class for Modernity tile entity types.
 */
@ObjectHolder( "modernity" )
@SuppressWarnings( { "unchecked", "ConstantConditions" } )
public final class MDTileEntitiyTypes {

    private static final RegistryHandler<TileEntityType<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final TileEntityType<NetherAltarTileEntity> NETHER_ALTAR = register( "nether_altar", create( NetherAltarTileEntity::new, MDBlocks.NETHER_ALTAR ) );

    public static void register( IForgeRegistry<TileEntityType<?>> registry ) {
        for( TileEntityType<?> type : ENTRIES ) {
            registry.register( type );
        }
    }

    private static <T extends TileEntity> TileEntityType.Builder<T> create( Supplier<? extends T> factory, Block... validBlocks ) {
        return TileEntityType.Builder.create( factory, validBlocks );
    }

    private static <T extends TileEntity> TileEntityType<T> register( String id, TileEntityType.Builder<T> builder, String... aliases ) {
        TileEntityType<T> type = builder.build( null );
        ENTRIES.register( id, type, aliases );
        return type;
    }

    public static void setup( RegistryEventHandler handler ) {
        TypeToken<TileEntityType<?>> token = new TypeToken<TileEntityType<?>>( TileEntityType.class ) {
        };
        handler.addHandler( (Class<TileEntityType<?>>) token.getRawType(), ENTRIES );
    }

    private MDTileEntitiyTypes() {
    }
}
