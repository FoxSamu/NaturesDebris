/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.common.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public class BreakBlockTrigger implements ICriterionTrigger<BreakBlockTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation( "modernity:break_block" );
    private final Map<PlayerAdvancements, Listeners> listenersMap = Maps.newHashMap();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener( PlayerAdvancements adv, Listener<Instance> listener ) {
        Listeners listeners = listenersMap.get( adv );
        if( listeners == null ) {
            listeners = new Listeners( adv );
            listenersMap.put( adv, listeners );
        }

        listeners.add( listener );
    }

    @Override
    public void removeListener( PlayerAdvancements adv, Listener<Instance> listener ) {
        Listeners listeners = listenersMap.get( adv );
        if( listeners != null ) {
            listeners.remove( listener );
            if( listeners.isEmpty() ) {
                listenersMap.remove( adv );
            }
        }

    }

    @Override
    public void removeAllListeners( PlayerAdvancements adv ) {
        listenersMap.remove( adv );
    }

    @Override
    public Instance deserializeInstance( JsonObject json, JsonDeserializationContext context ) {
        Block block = null;
        if( json.has( "block" ) ) {
            ResourceLocation resourcelocation = new ResourceLocation( JSONUtils.getString( json, "block" ) );
            block = Optional.ofNullable( ForgeRegistries.BLOCKS.getValue( resourcelocation ) )
                            .orElseThrow( () -> new JsonSyntaxException( "Unknown block type '" + resourcelocation + "'" ) );
        }

        Map<IProperty<?>, Object> stateMap = null;
        if( json.has( "state" ) ) {
            if( block == null ) {
                throw new JsonSyntaxException( "Can't define block state without a specific block type" );
            }

            StateContainer<Block, BlockState> sc = block.getStateContainer();

            for( Entry<String, JsonElement> entry : JSONUtils.getJsonObject( json, "state" ).entrySet() ) {
                IProperty<?> prop = sc.getProperty( entry.getKey() );
                if( prop == null ) {
                    throw new JsonSyntaxException( "Unknown block state property '" + entry.getKey() + "' for block '" + block.getRegistryName() + "'" );
                }

                String val = JSONUtils.getString( entry.getValue(), entry.getKey() );
                Optional<?> parsedVal = prop.parseValue( val );
                if( ! parsedVal.isPresent() ) {
                    throw new JsonSyntaxException( "Invalid block state value '" + val + "' for property '" + entry.getKey() + "' on block '" + block.getRegistryName() + "'" );
                }

                if( stateMap == null ) {
                    stateMap = Maps.newHashMap();
                }

                stateMap.put( prop, parsedVal.get() );
            }
        }

        LocationPredicate locPred = LocationPredicate.deserialize( json.get( "location" ) );
        ItemPredicate itemPred = ItemPredicate.deserialize( json.get( "item" ) );
        return new Instance( block, stateMap, locPred, itemPred );
    }

    public void trigger( ServerPlayerEntity player, BlockState brokenState, BlockPos pos, ItemStack item ) {
        Listeners listeners = listenersMap.get( player.getAdvancements() );
        if( listeners != null ) {
            listeners.trigger( brokenState, pos, player.getServerWorld(), item );
        }
    }

    public static class Instance extends CriterionInstance {
        private final Block block;
        private final Map<IProperty<?>, Object> properties;
        private final LocationPredicate location;
        private final ItemPredicate item;

        public Instance( @Nullable Block block, @Nullable Map<IProperty<?>, Object> properties, LocationPredicate location, ItemPredicate item ) {
            super( ID );
            this.block = block;
            this.properties = properties;
            this.location = location;
            this.item = item;
        }

        public static Instance breakBlock( Block block ) {
            return new Instance( block, null, LocationPredicate.ANY, ItemPredicate.ANY );
        }

        public boolean test( BlockState state, BlockPos pos, ServerWorld world, ItemStack stack ) {
            if( block != null && state.getBlock() != block ) {
                return false;
            } else {
                if( properties != null ) {
                    for( Entry<IProperty<?>, Object> pair : properties.entrySet() ) {
                        if( state.get( pair.getKey() ) != pair.getValue() ) {
                            return false;
                        }
                    }
                }

                return location.test( world, (float) pos.getX(), (float) pos.getY(), (float) pos.getZ() ) && item.test( stack );
            }
        }

        @Override
        public JsonElement serialize() {
            JsonObject obj = new JsonObject();
            if( block != null ) {
                obj.addProperty( "block", block.getRegistryName() + "" );
            }

            if( properties != null ) {
                JsonObject state = new JsonObject();

                for( Entry<IProperty<?>, Object> entry : properties.entrySet() ) {
                    state.addProperty( entry.getKey().getName(), Util.getValueName( entry.getKey(), entry.getValue() ) );
                }

                obj.add( "state", state );
            }

            obj.add( "location", location.serialize() );
            obj.add( "item", item.serialize() );

            return obj;
        }
    }

    static class Listeners {
        private final PlayerAdvancements advancements;
        private final Set<Listener<Instance>> listeners = Sets.newHashSet();

        Listeners( PlayerAdvancements advancements ) {
            this.advancements = advancements;
        }

        public boolean isEmpty() {
            return listeners.isEmpty();
        }

        public void add( Listener<Instance> listener ) {
            listeners.add( listener );
        }

        public void remove( Listener<Instance> listener ) {
            listeners.remove( listener );
        }

        public void trigger( BlockState state, BlockPos pos, ServerWorld world, ItemStack item ) {
            List<Listener<Instance>> triggered = null;

            for( Listener<Instance> listener : listeners ) {
                if( listener.getCriterionInstance().test( state, pos, world, item ) ) {
                    if( triggered == null ) {
                        triggered = Lists.newArrayList();
                    }

                    triggered.add( listener );
                }
            }

            if( triggered != null ) {
                for( Listener<Instance> listener : triggered ) {
                    listener.grantCriterion( advancements );
                }
            }

        }
    }
}