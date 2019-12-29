/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 29 - 2019
 * Author: rgsw
 */

package modernity.common.tileentity;

import com.google.common.reflect.TypeToken;
import modernity.client.render.tileentity.SoulLightRenderer;
import modernity.common.block.MDBlocks;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

/**
 * Holder class for Modernity tile entity types.
 */
@ObjectHolder( "modernity" )
@SuppressWarnings( { "unchecked", "ConstantConditions" } )
public final class MDTileEntitiyTypes {

    private static final RegistryHandler<TileEntityType<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final TileEntityType<SoulLightTileEntity> SOUL_LIGHT = register( "soul_light", create( SoulLightTileEntity::new, MDBlocks.SOUL_LIGHT ) );
    public static final TileEntityType<RockFurnaceTileEntity> ROCK_FURNACE = register( "rock_furnace", create( RockFurnaceTileEntity::new, MDBlocks.ROCK_FURNACE ) );
    public static final TileEntityType<WorkbenchTileEntity> WORKBENCH = register( "workbench", create( WorkbenchTileEntity::new, MDBlocks.BLACKWOOD_WORKBENCH, MDBlocks.INVER_WORKBENCH ) );

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

    @OnlyIn( Dist.CLIENT )
    public static void setupClient() {
        ClientRegistry.bindTileEntitySpecialRenderer( SoulLightTileEntity.class, new SoulLightRenderer() );
    }

    private MDTileEntitiyTypes() {
    }
}
