/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 18 - 2020
 * Author: rgsw
 */

package modernity.common.tileentity;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import modernity.client.render.tileentity.SoulLightRenderer;
import modernity.client.render.tileentity.WorkbenchRenderer;
import modernity.common.block.MDBlocks;
import modernity.common.block.farmland.FarmlandBlock;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Predicate;
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
    public static final TileEntityType<FarmlandTileEntity> FARMLAND = register( "farmland", create( FarmlandTileEntity::new, block -> block instanceof FarmlandBlock ) );

    private static <T extends TileEntity> TileEntityType.Builder<T> create( Supplier<? extends T> factory, Block... validBlocks ) {
        return TileEntityType.Builder.create( factory, validBlocks );
    }

    private static <T extends TileEntity> TileEntityType<T> create( Supplier<? extends T> factory, Predicate<Block> blockChecker ) {
        return new PredicateTEType<>( factory, blockChecker );
    }

    private static <T extends TileEntity> TileEntityType<T> register( String id, TileEntityType.Builder<T> builder, String... aliases ) {
        TileEntityType<T> type = builder.build( null );
        ENTRIES.register( id, type, aliases );
        return type;
    }

    private static <T extends TileEntity> TileEntityType<T> register( String id, TileEntityType<T> type, String... aliases ) {
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
        ClientRegistry.bindTileEntitySpecialRenderer( WorkbenchTileEntity.class, new WorkbenchRenderer() );
    }

    private MDTileEntitiyTypes() {
    }


    private static class PredicateTEType<T extends TileEntity> extends TileEntityType<T> {

        private final Predicate<Block> blockChecker;

        PredicateTEType( Supplier<? extends T> factory, Predicate<Block> blockChecker ) {
            super( factory, ImmutableSet.of(), null );
            this.blockChecker = blockChecker;
        }

        @Override
        public boolean isValidBlock( Block block ) {
            return blockChecker.test( block );
        }
    }
}
