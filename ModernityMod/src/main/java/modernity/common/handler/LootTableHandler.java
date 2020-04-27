/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.handler;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public enum LootTableHandler {
    INSTANCE;

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON_INSTANCE
        = new GsonBuilder().registerTypeAdapter( RandomValueRange.class, new RandomValueRange.Serializer() )
                           .registerTypeAdapter( BinomialRange.class, new BinomialRange.Serializer() )
                           .registerTypeAdapter( ConstantRange.class, new ConstantRange.Serializer() )
                           .registerTypeAdapter( IntClamper.class, new IntClamper.Serializer() )
                           .registerTypeAdapter( LootPool.class, new LootPool.Serializer() )
                           .registerTypeAdapter( LootTable.class, new LootTable.Serializer() )
                           .registerTypeHierarchyAdapter( LootEntry.class, new LootEntryManager.Serializer() )
                           .registerTypeHierarchyAdapter( ILootFunction.class, new LootFunctionManager.Serializer() )
                           .registerTypeHierarchyAdapter( ILootCondition.class, new LootConditionManager.Serializer() )
                           .registerTypeHierarchyAdapter( LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer() )
                           .create();

    @SubscribeEvent
    public void onLoadLootTable( LootTableLoadEvent event ) {
        LootTable table = event.getTable();

        IResourceManager manager = Minecraft.getInstance().getResourceManager();

        ResourceLocation name = event.getName();

        ResourceLocation loc = new ResourceLocation(
            "modernity:loot_extensions/" + name.getPath() + ".json"
        );

        try {
            IResource resource = manager.getResource( loc );
            JsonElement element = new JsonParser().parse( new InputStreamReader( resource.getInputStream() ) );

            JsonArray array = JSONUtils.getJsonArray( element, "loot extension" );
            for( JsonElement el : array ) {
                JsonObject object = JSONUtils.getJsonObject( el, "pool hook" );

                if( object.has( "replace" ) ) {
                    String replace = object.get( "replace" ).getAsString();
                    table.removePool( replace );
                    LootPool pool = GSON_INSTANCE.fromJson( object.get( "replacement" ), LootPool.class );
                    table.addPool( pool );
                } else if( object.has( "remove" ) ) {
                    String replace = object.get( "remove" ).getAsString();
                    table.removePool( replace );
                } else if( object.has( "add" ) ) {
                    LootPool pool = GSON_INSTANCE.fromJson( object.get( "add" ), LootPool.class );
                    table.addPool( pool );
                }
            }
        } catch( FileNotFoundException ignored ) {
        } catch( Exception exc ) {
            LOGGER.warn( "Unable to extend loot table {} because of exception", name, exc );
        }
    }
}
