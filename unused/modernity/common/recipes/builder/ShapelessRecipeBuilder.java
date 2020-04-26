/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.recipes.builder;

import com.google.common.collect.Lists;
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
import java.util.function.Consumer;

public class ShapelessRecipeBuilder {
    private final Item result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
    private String group;

    private ShapelessRecipeBuilder( IItemProvider result, int count ) {
        this.result = result.asItem();
        this.count = count;
    }

    public static ShapelessRecipeBuilder shapelessRecipe( IItemProvider result ) {
        return new ShapelessRecipeBuilder( result, 1 );
    }

    public static ShapelessRecipeBuilder shapelessRecipe( IItemProvider result, int count ) {
        return new ShapelessRecipeBuilder( result, count );
    }

    public static ShapelessRecipeBuilder shapelessRecipe( IResultData res ) {
        return new ShapelessRecipeBuilder( res.item(), res.count( 1 ) );
    }

    public static ShapelessRecipeBuilder shapelessRecipe( IResultData res, int count ) {
        return new ShapelessRecipeBuilder( res.item(), res.count( count ) );
    }

    public ShapelessRecipeBuilder ingredient( Tag<Item> tag ) {
        return ingredient( Ingredient.fromTag( tag ) );
    }

    public ShapelessRecipeBuilder ingredient( IItemProvider item ) {
        return ingredient( item, 1 );
    }

    public ShapelessRecipeBuilder ingredient( IItemProvider item, int quantity ) {
        ingredient( Ingredient.fromItems( item ), quantity );

        return this;
    }

    public ShapelessRecipeBuilder ingredient( IIngredientData item ) {
        return ingredient( item, 1 );
    }

    public ShapelessRecipeBuilder ingredient( IIngredientData item, int quantity ) {
        ingredient( item.makeIngredient(), quantity );

        return this;
    }

    public ShapelessRecipeBuilder ingredient( Ingredient ingredient ) {
        return ingredient( ingredient, 1 );
    }

    public ShapelessRecipeBuilder ingredient( Ingredient ingredient, int quantity ) {
        for( int i = 0; i < quantity; ++ i ) {
            ingredients.add( ingredient );
        }

        return this;
    }


    public ShapelessRecipeBuilder itemCriterion( IIngredientData data ) {
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

    public ShapelessRecipeBuilder itemCriterion( IIngredientData data, int count ) {
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

    public ShapelessRecipeBuilder itemCriterion( IIngredientData data, MinMaxBounds.IntBound count ) {
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

    public ShapelessRecipeBuilder criterion( String name, ICriterionInstance criterionIn ) {
        advancementBuilder.withCriterion( name, criterionIn );
        return this;
    }

    public ShapelessRecipeBuilder group( String groupIn ) {
        group = groupIn;
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
            ingredients,
            advancementBuilder,
            new ResourceLocation( id.getNamespace(), "recipes/" + path( result.getGroup() ) + id.getPath() )
        ) );
    }


    private String path( ItemGroup gr ) {
        return gr == null ? "" : gr.getPath() + "/";
    }

    /**
     * Makes sure that this recipe is valid and obtainable.
     */
    private void validate( ResourceLocation id ) {
        if( this.advancementBuilder.getCriteria().isEmpty() ) {
            throw new IllegalStateException( "No way of obtaining recipe " + id );
        }
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        public Result( ResourceLocation id, Item result, int count, String group, List<Ingredient> ingredients, Advancement.Builder advancementBuilder, ResourceLocation advancementId ) {
            this.id = id;
            this.result = result;
            this.count = count;
            this.group = group;
            this.ingredients = ingredients;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize( JsonObject json ) {
            if( ! this.group.isEmpty() ) {
                json.addProperty( "group", group );
            }

            JsonArray ingrs = new JsonArray();
            for( Ingredient ingredient : ingredients ) {
                ingrs.add( ingredient.serialize() );
            }
            json.add( "ingredients", ingrs );

            JsonObject out = new JsonObject();
            out.addProperty( "item", result.getRegistryName() + "" );
            if( count > 1 ) {
                out.addProperty( "count", count );
            }
            json.add( "result", out );
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return IRecipeSerializer.CRAFTING_SHAPELESS;
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