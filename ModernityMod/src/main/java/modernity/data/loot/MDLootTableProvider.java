/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.data.loot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MDLootTableProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final DataGenerator dataGenerator;
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = ImmutableList.of( Pair.of( BlockLootData::new, LootParameterSets.BLOCK ) );

    public MDLootTableProvider( DataGenerator generator ) {
        dataGenerator = generator;
    }

    @Override
    public void act( DirectoryCache cache ) {
        Path path = dataGenerator.getOutputFolder();
        Map<ResourceLocation, LootTable> exports = Maps.newHashMap();
        getTables().forEach( pair -> pair.getFirst().get().accept( ( id, lootBuilder ) -> {
            if( exports.put( id, lootBuilder.setParameterSet( pair.getSecond() ).build() ) != null ) {
                throw new IllegalStateException( "Duplicate loot table " + id );
            }
        } ) );
        ValidationTracker validation = new ValidationTracker( LootParameterSets.GENERIC, id -> null, exports::get );

        validate( exports, validation );

        Multimap<String, String> problems = validation.func_227527_a_();
        if( ! problems.isEmpty() ) {
            problems.forEach( ( table, problem ) -> LOGGER.warn( "Found validation problem in " + table + ": " + problem ) );
            throw new IllegalStateException( "Failed to validate loot tables, see logs" );
        } else {
            exports.forEach( ( id, table ) -> {
                Path tablePath = getPath( path, id );

                try {
                    IDataProvider.save( GSON, cache, LootTableManager.toJson( table ), tablePath );
                } catch( IOException exc ) {
                    LOGGER.error( "Couldn't save loot table {}", tablePath, exc );
                }

            } );
        }
    }

    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return tables;
    }

    protected void validate( Map<ResourceLocation, LootTable> map, ValidationTracker tracker ) {
//        for( ResourceLocation resourcelocation : Sets.difference( LootTables.func_215796_a(), map.keySet() ) ) {
//            results.addProblem( "Missing built-in table: " + resourcelocation );
//        }

        map.forEach( ( id, table ) -> LootTableManager.func_227508_a_( tracker, id, table ) );
    }

    private static Path getPath( Path path, ResourceLocation id ) {
        return path.resolve( "data/" + id.getNamespace() + "/loot_tables/" + id.getPath() + ".json" );
    }

    @Override
    public String getName() {
        return "MDLootTables";
    }
}