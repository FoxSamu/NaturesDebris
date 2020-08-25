/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.data.recipes;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import modernity.common.recipes.data.ItemPredicateBuilder;
import modernity.generic.data.IRecipeData;
import net.minecraft.advancements.criterion.EnterBlockTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class MDRecipeProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected final DataGenerator generator;

    public MDRecipeProvider(DataGenerator generatorIn) {
        this.generator = generatorIn;
    }

    @Override
    public void act(DirectoryCache cache) {
        Path out = generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        registerRecipes(rec -> {
            if(!set.add(rec.getID())) {
                throw new IllegalStateException("Duplicate recipe " + rec.getID());
            } else {
                saveRecipe(cache, rec.getRecipeJson(), out.resolve("data/" + rec.getID().getNamespace() + "/recipes/" + rec.getID().getPath() + ".json"));
                JsonObject json = rec.getAdvancementJson();
                if(json != null) {
                    saveRecipeAdvancement(cache, json, out.resolve("data/" + rec.getID().getNamespace() + "/advancements/" + rec.getAdvancementID().getPath() + ".json"));
                }
            }
        });
    }

    private void saveRecipe(DirectoryCache cache, JsonObject recipeJson, Path path) {
        try {
            String s = GSON.toJson(recipeJson);
            String s1 = HASH_FUNCTION.hashUnencodedChars(s).toString();
            if(!Objects.equals(cache.getPreviousHash(path), s1) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());

                try(BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                    bufferedwriter.write(s);
                }
            }

            cache.recordHash(path, s1);
        } catch(IOException ioexception) {
            LOGGER.error("Couldn't save recipe {}", path, ioexception);
        }
    }

    protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject advancementJson, Path path) {
        try {
            String s = GSON.toJson(advancementJson);
            String s1 = HASH_FUNCTION.hashUnencodedChars(s).toString();
            if(!Objects.equals(cache.getPreviousHash(path), s1) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());

                try(BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                    bufferedwriter.write(s);
                }
            }

            cache.recordHash(path, s1);
        } catch(IOException ioexception) {
            LOGGER.error("Couldn't save recipe advancement {}", path, ioexception);
        }
    }

    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        for(IRecipeData type : RecipeData.getRecipes()) {
            type.build(consumer);
        }
    }



    protected EnterBlockTrigger.Instance enteredBlock(Block blockIn) {
        return new EnterBlockTrigger.Instance(blockIn, null);
    }

    protected InventoryChangeTrigger.Instance hasItem(MinMaxBounds.IntBound amount, IItemProvider itemIn) {
        return this.hasItem(ItemPredicateBuilder.create().item(itemIn).count(amount).build());
    }

    protected InventoryChangeTrigger.Instance hasItem(IItemProvider itemIn) {
        return this.hasItem(ItemPredicate.Builder.create().item(itemIn).build());
    }

    protected InventoryChangeTrigger.Instance hasItem(Tag<Item> tagIn) {
        return this.hasItem(ItemPredicate.Builder.create().tag(tagIn).build());
    }

    protected InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicates) {
        return new InventoryChangeTrigger.Instance(MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, MinMaxBounds.IntBound.UNBOUNDED, predicates);
    }

    @Override
    public String getName() {
        return "MDRecipes";
    }
}
