/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 29 - 2019
 */

package modernity.common.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.function.Supplier;

public class MDTileEntities {

    private static final ArrayList<TileEntityType<?>> ENTRIES = new ArrayList<>();

    public static final TileEntityType<TileEntityNetherAltar> NETHER_ALTAR = register( "nether_altar", TileEntityNetherAltar::new );

    public static void register( IForgeRegistry<TileEntityType<?>> registry ) {
        for( TileEntityType<?> type : ENTRIES ) {
            registry.register( type );
        }
    }

    private static final <T extends TileEntity> TileEntityType<T> register( String id, Supplier<T> factory ) {
        TileEntityType<T> type = new TileEntityType<>( factory, null );
        type.setRegistryName( "modernity:" + id );
        ENTRIES.add( type );
        return type;
    }
}
