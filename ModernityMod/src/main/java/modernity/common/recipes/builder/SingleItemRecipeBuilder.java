/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.recipes.builder;

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
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SingleItemRecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final int count;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
    private String group;
    private final IRecipeSerializer<?> serializer;

    private SingleItemRecipeBuilder( IRecipeSerializer<?> serializer, Ingredient ingredient, IItemProvider result, int count ) {
        this.serializer = serializer;
        this.result = result.asItem();
        this.ingredient = ingredient;
        this.count = count;
    }

    public static SingleItemRecipeBuilder stonecutting( Ingredient in, IItemProvider out ) {
        return new SingleItemRecipeBuilder( IRecipeSerializer.STONECUTTING, in, out, 1 );
    }

    public static SingleItemRecipeBuilder stonecutting( Ingredient in, IItemProvider out, int count ) {
        return new SingleItemRecipeBuilder( IRecipeSerializer.STONECUTTING, in, out, count );
    }

    public static SingleItemRecipeBuilder stonecutting( IIngredientData in, IResultData out ) {
        return new SingleItemRecipeBuilder( IRecipeSerializer.STONECUTTING, in.makeIngredient(), out.item(), out.count( 1 ) );
    }

    public static SingleItemRecipeBuilder stonecutting( IIngredientData in, IResultData out, int count ) {
        return new SingleItemRecipeBuilder( IRecipeSerializer.STONECUTTING, in.makeIngredient(), out.item(), out.count( count ) );
    }

    public SingleItemRecipeBuilder itemCriterion( IIngredientData data ) {
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

    public SingleItemRecipeBuilder itemCriterion( IIngredientData data, int count ) {
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

    public SingleItemRecipeBuilder itemCriterion( IIngredientData data, MinMaxBounds.IntBound count ) {
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

    public SingleItemRecipeBuilder criterion( String name, ICriterionInstance crit ) {
        advancementBuilder.withCriterion( name, crit );
        return this;
    }

    public SingleItemRecipeBuilder group( String group ) {
        this.group = group;
        return this;
    }

    public void build( Consumer<IFinishedRecipe> consumer ) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey( result );
        build( consumer, id );
    }

    public void build( Consumer<IFinishedRecipe> consumer, String name ) {
        build( consumer, new ResourceLocation( name ) );
    }

    public void build( Consumer<IFinishedRecipe> consumer, ResourceLocation id ) {
        validate( id );
        advancementBuilder.withParentId( new ResourceLocation( "recipes/root" ) ).withCriterion( "has_the_recipe", new RecipeUnlockedTrigger.Instance( id ) ).withRewards( AdvancementRewards.Builder.recipe( id ) ).withRequirementsStrategy( IRequirementsStrategy.OR );
        consumer.accept( new Result(
            id,
            serializer,
            group == null ? "" : group,
            ingredient,
            result,
            count,
            advancementBuilder,
            new ResourceLocation( id.getNamespace(), "recipes/" + path( result.getGroup() ) + id.getPath() )
        ) );
    }

    private String path( ItemGroup gr ) {
        return gr == null ? "" : gr.getPath() + "/";
    }

    private void validate( ResourceLocation id ) {
        if( advancementBuilder.getCriteria().isEmpty() ) {
            throw new IllegalStateException( "No way of obtaining recipe " + id );
        }
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final Item result;
        private final int count;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<?> serializer;

        public Result( ResourceLocation id, IRecipeSerializer<?> serializer, String group, Ingredient ingredient, Item result, int count, Advancement.Builder advancementBuilder, ResourceLocation advancementId ) {
            this.id = id;
            this.serializer = serializer;
            this.group = group;
            this.ingredient = ingredient;
            this.result = result;
            this.count = count;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize( JsonObject json ) {
            if( ! group.isEmpty() ) {
                json.addProperty( "group", group );
            }

            json.add( "ingredient", ingredient.serialize() );
            json.addProperty( "result", result.getRegistryName() + "" );
            json.addProperty( "count", count );
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return serializer;
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