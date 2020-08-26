package natures.debris.data.recipes;

import natures.debris.common.NaturesDebris;
import natures.debris.common.block.NdBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

import java.util.function.Consumer;

public class NdRecipeProvider extends RecipeProvider {
    private Consumer<IFinishedRecipe> consumer;

    public NdRecipeProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        this.consumer = consumer;
        generic4x4("rock_bricks_4x4", NdBlocks.ROCK, NdBlocks.ROCK_BRICKS, 4);
        generic4x4("mossy_rock_bricks_4x4", NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_BRICKS, 4);
        generic4x4("rock_tiles_4x4", NdBlocks.SMOOTH_ROCK, NdBlocks.ROCK_TILES, 4);
        genericSmelting("smooth_rock_smelting", NdBlocks.ROCK, NdBlocks.SMOOTH_ROCK, 0.1);
        genericSmelting("cracked_rock_bricks_smelting", NdBlocks.ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS, 0.1);
        genericSmelting("cracked_rock_tiles_smelting", NdBlocks.ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES, 0.1);

        generic4x4("darkrock_bricks_4x4", NdBlocks.DARKROCK, NdBlocks.DARKROCK_BRICKS, 4);
        generic4x4("mossy_darkrock_bricks_4x4", NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_BRICKS, 4);
        generic4x4("darkrock_tiles_4x4", NdBlocks.SMOOTH_DARKROCK, NdBlocks.DARKROCK_TILES, 4);
        genericSmelting("smooth_darkrock_smelting", NdBlocks.DARKROCK, NdBlocks.SMOOTH_DARKROCK, 0.1);
        genericSmelting("cracked_darkrock_bricks_smelting", NdBlocks.DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS, 0.1);
        genericSmelting("cracked_darkrock_tiles_smelting", NdBlocks.DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES, 0.1);
    }

    private void generic4x4(String id, IItemProvider from, IItemProvider to, int count) {
        ShapedRecipeBuilder.shapedRecipe(to, count)
                           .key('#', from)
                           .patternLine("##")
                           .patternLine("##")
                           .addCriterion("has_ingredient", hasItem(Blocks.ACACIA_LOG))
                           .build(consumer, NaturesDebris.resLoc(id));
    }

    private void genericSmelting(String id, IItemProvider from, IItemProvider to, double xp) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(from), to, (float) xp, 200)
                            .addCriterion("has_ingredient", hasItem(from))
                            .build(consumer, NaturesDebris.resLoc(id));
    }

    private void genericSmelting2(String id, IItemProvider from1, IItemProvider from2, IItemProvider to, double xp) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(from1, from2), to, (float) xp, 200)
                            .addCriterion("has_ingredient_1", hasItem(from1))
                            .addCriterion("has_ingredient_2", hasItem(from2))
                            .build(consumer, NaturesDebris.resLoc(id));
    }

    @Override
    public String getName() {
        return "NaturesDebris/Recipes";
    }
}
