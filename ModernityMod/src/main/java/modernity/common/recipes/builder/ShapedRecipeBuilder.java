/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.recipes.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import modernity.common.recipes.data.IIngredientData;
import modernity.common.recipes.data.IResultData;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

public class ShapedRecipeBuilder {
    private final Item result;
    private final int count;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
    private String group;

    private ShapedRecipeBuilder( IItemProvider result, int count ) {
        this.result = result.asItem();
        this.count = count;
    }

    public static ShapedRecipeBuilder shapedRecipe( IItemProvider result ) {
        return shapedRecipe( result, 1 );
    }

    public static ShapedRecipeBuilder shapedRecipe( IItemProvider result, int count ) {
        return new ShapedRecipeBuilder( result, count );
    }

    public static ShapedRecipeBuilder shapedRecipe( IResultData res ) {
        return new ShapedRecipeBuilder( res.item(), res.count( 1 ) );
    }

    public static ShapedRecipeBuilder shapedRecipe( IResultData res, int count ) {
        return new ShapedRecipeBuilder( res.item(), res.count( count ) );
    }

    public ShapedRecipeBuilder key( Character symbol, Tag<Item> tag ) {
        return key( symbol, Ingredient.fromTag( tag ) );
    }

    public ShapedRecipeBuilder key( Character symbol, IItemProvider item ) {
        return key( symbol, Ingredient.fromItems( item ) );
    }

    public ShapedRecipeBuilder key( Character symbol, IIngredientData item ) {
        return key( symbol, item.makeIngredient() );
    }

    public ShapedRecipeBuilder key( Character symbol, Ingredient ingredient ) {
        if( key.containsKey( symbol ) ) {
            throw new IllegalArgumentException( "Symbol '" + symbol + "' is already defined!" );
        } else if( symbol == ' ' ) {
            throw new IllegalArgumentException( "Symbol ' ' (whitespace) is reserved and cannot be defined" );
        } else {
            key.put( symbol, ingredient );
            return this;
        }
    }

    public ShapedRecipeBuilder patternLine( String line ) {
        if( ! pattern.isEmpty() && line.length() != pattern.get( 0 ).length() ) {
            throw new IllegalArgumentException( "Pattern must be the same width on every line!" );
        } else {
            pattern.add( line );
            return this;
        }
    }

    public ShapedRecipeBuilder itemCriterion( IIngredientData data ) {
        data.addItemCriteria( ( str, pred ) -> advancementBuilder.withCriterion(
            str, new InventoryChangeTrigger.Instance(
                MinMaxBounds.IntBound.UNBOUNDED,
                MinMaxBounds.IntBound.UNBOUNDED,
                MinMaxBounds.IntBound.UNBOUNDED,
                new ItemPredicate[] { pred }
            )
        ) );
        return this;
    }

    public ShapedRecipeBuilder itemCriterion( IIngredientData data, int count ) {
        data.addItemCriteria( ( str, pred ) -> advancementBuilder.withCriterion(
            str, new InventoryChangeTrigger.Instance(
                MinMaxBounds.IntBound.UNBOUNDED,
                MinMaxBounds.IntBound.UNBOUNDED,
                MinMaxBounds.IntBound.UNBOUNDED,
                new ItemPredicate[] { pred }
            )
        ), count );
        return this;
    }

    public ShapedRecipeBuilder itemCriterion( IIngredientData data, MinMaxBounds.IntBound count ) {
        data.addItemCriteria( ( str, pred ) -> advancementBuilder.withCriterion(
            str, new InventoryChangeTrigger.Instance(
                MinMaxBounds.IntBound.UNBOUNDED,
                MinMaxBounds.IntBound.UNBOUNDED,
                MinMaxBounds.IntBound.UNBOUNDED,
                new ItemPredicate[] { pred }
            )
        ), count );
        return this;
    }

    public ShapedRecipeBuilder criterion( String name, ICriterionInstance crit ) {
        advancementBuilder.withCriterion( name, crit );
        return this;
    }

    public ShapedRecipeBuilder group( String group ) {
        this.group = group;
        return this;
    }

    public void build( Consumer<IFinishedRecipe> consumer ) {
        build( consumer, ForgeRegistries.ITEMS.getKey( result ) );
    }

    public void build( Consumer<IFinishedRecipe> consumer, String save ) {
        build( consumer, new ResourceLocation( save ) );
    }

    public void build( Consumer<IFinishedRecipe> consumer, ResourceLocation id ) {
        validate( id );
        advancementBuilder.withParentId( new ResourceLocation( "recipes/root" ) )
                          .withCriterion( "has_the_recipe", new RecipeUnlockedTrigger.Instance( id ) )
                          .withRewards( AdvancementRewards.Builder.recipe( id ) )
                          .withRequirementsStrategy( IRequirementsStrategy.OR );
        consumer.accept( new Result(
                             id,
                             result,
                             count,
                             group == null ? "" : group,
                             pattern,
                             key,
                             advancementBuilder,
                             new ResourceLocation( id.getNamespace(), "recipes/" + path( result.getGroup() ) + id.getPath() )
                         )
        );
    }

    private String path( ItemGroup gr ) {
        return gr == null ? "" : gr.getPath() + "/";
    }

    private void validate( ResourceLocation id ) {
        if( pattern.isEmpty() ) {
            throw new IllegalStateException( "No pattern is defined for shaped recipe " + id + "!" );
        } else {
            Set<Character> set = Sets.newHashSet( key.keySet() );
            set.remove( ' ' );

            for( String str : pattern ) {
                for( int i = 0; i < str.length(); ++ i ) {
                    char c = str.charAt( i );
                    if( ! key.containsKey( c ) && c != ' ' ) {
                        throw new IllegalStateException( "Pattern in recipe " + id + " uses undefined symbol '" + c + "'" );
                    }

                    set.remove( c );
                }
            }

            if( ! set.isEmpty() ) {
                throw new IllegalStateException( "Ingredients are defined but not used in pattern for recipe " + id );
            } else if( pattern.size() == 1 && pattern.get( 0 ).length() == 1 ) {
                throw new IllegalStateException( "Shaped recipe " + id + " only takes in a single item - should it be a shapeless recipe instead?" );
            } else if( advancementBuilder.getCriteria().isEmpty() ) {
                throw new IllegalStateException( "No way of obtaining recipe " + id );
            }
        }
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        public Result( ResourceLocation id, Item result, int count, String group, List<String> pattern, Map<Character, Ingredient> key, Advancement.Builder advancementBuilder, ResourceLocation advancementId ) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.pattern = pattern;
            this.key = key;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize( JsonObject json ) {
            if( ! group.isEmpty() ) {
                json.addProperty( "group", group );
            }

            JsonArray pat = new JsonArray();
            for( String s : pattern ) {
                pat.add( s );
            }
            json.add( "pattern", pat );


            JsonObject ingr = new JsonObject();
            for( Entry<Character, Ingredient> entry : key.entrySet() ) {
                ingr.add( String.valueOf( entry.getKey() ), entry.getValue().serialize() );
            }
            json.add( "key", ingr );

            JsonObject out = new JsonObject();
            out.addProperty( "item", result.getRegistryName() + "" );
            if( count > 1 ) {
                out.addProperty( "count", count );
            }
            json.add( "result", out );
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return IRecipeSerializer.CRAFTING_SHAPED;
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        @Nullable
        public JsonObject getAdvancementJson() {
            return advancementBuilder.serialize();
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementID() {
            return advancementId;
        }
    }
}