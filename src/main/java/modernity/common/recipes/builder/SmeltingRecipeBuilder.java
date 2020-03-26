/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modernity.common.recipes.builder;

import com.google.gson.JsonObject;
import modernity.common.recipes.data.IIngredientData;
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
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SmeltingRecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final float experience;
    private final int time;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
    private String group;
    private final CookingRecipeSerializer<?> serializer;

    private SmeltingRecipeBuilder( IItemProvider result, Ingredient ingredient, float experience, int time, CookingRecipeSerializer<?> serializer ) {
        this.result = result.asItem();
        this.ingredient = ingredient;
        this.experience = experience;
        this.time = time;
        this.serializer = serializer;
    }

    public static SmeltingRecipeBuilder smeltingRecipe( Ingredient ingredient, IItemProvider result, float experience, int time, CookingRecipeSerializer<?> serializer ) {
        return new SmeltingRecipeBuilder( result, ingredient, experience, time, serializer );
    }

    public static SmeltingRecipeBuilder blastingRecipe( Ingredient ingredient, IItemProvider result, float experience, int time ) {
        return smeltingRecipe( ingredient, result, experience, time, IRecipeSerializer.BLASTING );
    }

    public static SmeltingRecipeBuilder smeltingRecipe( Ingredient ingredient, IItemProvider result, float experience, int time ) {
        return smeltingRecipe( ingredient, result, experience, time, IRecipeSerializer.SMELTING );
    }

    public static SmeltingRecipeBuilder smokingRecipe( Ingredient ingredient, IItemProvider result, float experience, int time ) {
        return smeltingRecipe( ingredient, result, experience, time, IRecipeSerializer.SMOKING );
    }

    public static SmeltingRecipeBuilder smeltingRecipe( IIngredientData ingredient, IItemProvider result, float experience, int time, CookingRecipeSerializer<?> serializer ) {
        return new SmeltingRecipeBuilder( result, ingredient.makeIngredient(), experience, time, serializer );
    }

    public static SmeltingRecipeBuilder blastingRecipe( IIngredientData ingredient, IItemProvider result, float experience, int time ) {
        return smeltingRecipe( ingredient, result, experience, time, IRecipeSerializer.BLASTING );
    }

    public static SmeltingRecipeBuilder smeltingRecipe( IIngredientData ingredient, IItemProvider result, float experience, int time ) {
        return smeltingRecipe( ingredient, result, experience, time, IRecipeSerializer.SMELTING );
    }

    public static SmeltingRecipeBuilder smokingRecipe( IIngredientData ingredient, IItemProvider result, float experience, int time ) {
        return smeltingRecipe( ingredient, result, experience, time, IRecipeSerializer.SMOKING );
    }

    public SmeltingRecipeBuilder itemCriterion( IIngredientData data ) {
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

    public SmeltingRecipeBuilder itemCriterion( IIngredientData data, int count ) {
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

    public SmeltingRecipeBuilder itemCriterion( IIngredientData data, MinMaxBounds.IntBound count ) {
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

    public SmeltingRecipeBuilder criterion( String name, ICriterionInstance criterion ) {
        advancementBuilder.withCriterion( name, criterion );
        return this;
    }

    public SmeltingRecipeBuilder group( String group ) {
        this.group = group;
        return this;
    }

    public void build( Consumer<IFinishedRecipe> consumer ) {
        build( consumer, ForgeRegistries.ITEMS.getKey( result ) );
    }

    public void build( Consumer<IFinishedRecipe> consumer, String save ) {
        ResourceLocation saveId = new ResourceLocation( save );
        build( consumer, saveId );
    }

    public void build( Consumer<IFinishedRecipe> consumer, ResourceLocation id ) {
        validate( id );
        advancementBuilder.withParentId( new ResourceLocation( "recipes/root" ) )
                          .withCriterion( "has_the_recipe", new RecipeUnlockedTrigger.Instance( id ) )
                          .withRewards( AdvancementRewards.Builder.recipe( id ) )
                          .withRequirementsStrategy( IRequirementsStrategy.OR );

        consumer.accept( new Result(
            id,
            group == null ? "" : group,
            ingredient,
            result,
            experience,
            time,
            advancementBuilder,
            new ResourceLocation( id.getNamespace(), "recipes/" + path( result.getGroup() ) + id.getPath() ),
            serializer
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
        private final float experience;
        private final int cookingTime;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<? extends AbstractCookingRecipe> serializer;

        public Result( ResourceLocation id, String group, Ingredient ingredient, Item result, float experience, int time, Advancement.Builder advancementBuilder, ResourceLocation advancementId, IRecipeSerializer<? extends AbstractCookingRecipe> serializer ) {
            this.id = id;
            this.group = group;
            this.ingredient = ingredient;
            this.result = result;
            this.experience = experience;
            this.cookingTime = time;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
            this.serializer = serializer;
        }

        @Override
        public void serialize( JsonObject json ) {
            if( ! group.isEmpty() ) {
                json.addProperty( "group", group );
            }

            json.add( "ingredient", ingredient.serialize() );
            json.addProperty( "result", result.getRegistryName() + "" );
            json.addProperty( "experience", experience );
            json.addProperty( "cookingtime", cookingTime );
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return serializer;
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