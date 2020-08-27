package natures.debris.data.recipes;

import natures.debris.common.NaturesDebris;
import natures.debris.common.block.NdBlocks;
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

        // Rock
        generic4x4("rock_bricks_4x4", NdBlocks.ROCK, NdBlocks.ROCK_BRICKS, 4);
        generic4x4("mossy_rock_bricks_4x4", NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_BRICKS, 4);
        generic4x4("rock_tiles_4x4", NdBlocks.SMOOTH_ROCK, NdBlocks.ROCK_TILES, 4);
        generic1x3("rock_pillar_1x3", NdBlocks.ROCK, NdBlocks.ROCK_PILLAR, 3);
        smelting("smooth_rock_smelting", NdBlocks.ROCK, NdBlocks.SMOOTH_ROCK, 0.1);
        smelting("cracked_rock_bricks_smelting", NdBlocks.ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS, 0.1);
        smelting("cracked_rock_tiles_smelting", NdBlocks.ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES, 0.1);
        stonecutting("rock_bricks_stonecutting", NdBlocks.ROCK, NdBlocks.ROCK_BRICKS);
        stonecutting("mossy_rock_bricks_stonecutting", NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_BRICKS);
        stonecutting("rock_tiles_stonecutting", NdBlocks.SMOOTH_ROCK, NdBlocks.ROCK_TILES);
        stonecutting("chiseled_rock_stonecutting", NdBlocks.SMOOTH_ROCK, NdBlocks.CHISELED_ROCK);
        stonecutting("polished_rock_stonecutting", NdBlocks.SMOOTH_ROCK, NdBlocks.POLISHED_ROCK);
        stonecutting("rock_pillar_stonecutting", NdBlocks.ROCK, NdBlocks.ROCK_PILLAR);

        // Darkrock
        generic4x4("darkrock_bricks_4x4", NdBlocks.DARKROCK, NdBlocks.DARKROCK_BRICKS, 4);
        generic4x4("mossy_darkrock_bricks_4x4", NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_BRICKS, 4);
        generic4x4("darkrock_tiles_4x4", NdBlocks.SMOOTH_DARKROCK, NdBlocks.DARKROCK_TILES, 4);
        generic1x3("darkrock_pillar_1x3", NdBlocks.DARKROCK, NdBlocks.DARKROCK_PILLAR, 3);
        smelting("smooth_darkrock_smelting", NdBlocks.DARKROCK, NdBlocks.SMOOTH_DARKROCK, 0.1);
        smelting("cracked_darkrock_bricks_smelting", NdBlocks.DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS, 0.1);
        smelting("cracked_darkrock_tiles_smelting", NdBlocks.DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES, 0.1);
        stonecutting("darkrock_bricks_stonecutting", NdBlocks.DARKROCK, NdBlocks.DARKROCK_BRICKS);
        stonecutting("mossy_darkrock_bricks_stonecutting", NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_BRICKS);
        stonecutting("darkrock_tiles_stonecutting", NdBlocks.SMOOTH_DARKROCK, NdBlocks.DARKROCK_TILES);
        stonecutting("chiseled_darkrock_stonecutting", NdBlocks.SMOOTH_DARKROCK, NdBlocks.CHISELED_DARKROCK);
        stonecutting("polished_darkrock_stonecutting", NdBlocks.SMOOTH_DARKROCK, NdBlocks.POLISHED_DARKROCK);
        stonecutting("darkrock_pillar_stonecutting", NdBlocks.DARKROCK, NdBlocks.DARKROCK_PILLAR);
    }

    private void generic4x4(String id, IItemProvider from, IItemProvider to, int count) {
        ShapedRecipeBuilder.shapedRecipe(to, count)
                           .key('#', from)
                           .patternLine("##")
                           .patternLine("##")
                           .addCriterion("has_ingredient", hasItem(from))
                           .build(consumer, NaturesDebris.resLoc(id));
    }

    private void generic1x3(String id, IItemProvider from, IItemProvider to, int count) {
        ShapedRecipeBuilder.shapedRecipe(to, count)
                           .key('#', from)
                           .patternLine("#")
                           .patternLine("#")
                           .patternLine("#")
                           .addCriterion("has_ingredient", hasItem(from))
                           .build(consumer, NaturesDebris.resLoc(id));
    }

    private void smelting(String id, IItemProvider from, IItemProvider to, double xp) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(from), to, (float) xp, 200)
                            .addCriterion("has_ingredient", hasItem(from))
                            .build(consumer, NaturesDebris.resLoc(id));
    }

    private void stonecutting(String id, IItemProvider from, IItemProvider to) {
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(from), to)
                               .addCriterion("has_ingredient", hasItem(from))
                               .build(consumer, NaturesDebris.resLoc(id));
    }

    @Override
    public String getName() {
        return "NaturesDebris/Recipes";
    }
}
