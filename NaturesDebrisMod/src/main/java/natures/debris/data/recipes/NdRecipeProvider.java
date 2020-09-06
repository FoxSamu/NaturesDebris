package natures.debris.data.recipes;

import java.util.function.Consumer;

import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

import natures.debris.common.NaturesDebris;
import natures.debris.common.block.NdBlocks;

public class NdRecipeProvider extends RecipeProvider {
    private Consumer<IFinishedRecipe> consumer;

    public NdRecipeProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        this.consumer = consumer;

        // Nature blocks
        genericShapeless("murky_humus_shapeless", NdBlocks.LEAFY_HUMUS, NdBlocks.MURKY_HUMUS, 1);
        smelting("murky_terracotta_smelting", NdBlocks.ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES, 0.35);

        // Wood blocks
        generic4x4("blackwood_4x4", NdBlocks.BLACKWOOD_LOG, NdBlocks.BLACKWOOD, 3);
        generic4x4("inver_wood_4x4", NdBlocks.INVER_LOG, NdBlocks.INVER_WOOD, 3);
        generic4x4("stripped_blackwood_4x4", NdBlocks.STRIPPED_BLACKWOOD_LOG, NdBlocks.STRIPPED_BLACKWOOD, 3);
        generic4x4("stripped_inver_wood_4x4", NdBlocks.STRIPPED_INVER_LOG, NdBlocks.STRIPPED_INVER_WOOD, 3);
        smelting("charcoal_from_blackwood_log_smelting", NdBlocks.BLACKWOOD_LOG, Items.CHARCOAL, 0.35);

        // For stonecutting recipes, see NdStonecuttingRecipeProvider

        // Rock
        generic4x4("rock_bricks_4x4", NdBlocks.ROCK, NdBlocks.ROCK_BRICKS, 4);
        generic4x4("mossy_rock_bricks_4x4", NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_BRICKS, 4);
        generic4x4("rock_tiles_4x4", NdBlocks.SMOOTH_ROCK, NdBlocks.ROCK_TILES, 4);
        generic1x3("rock_pillar_1x3", NdBlocks.ROCK, NdBlocks.ROCK_PILLAR, 3);
        generic1x2("chiseled_rock_1x2", NdBlocks.SMOOTH_ROCK_SLAB, NdBlocks.CHISELED_ROCK, 1);
        smelting("smooth_rock_smelting", NdBlocks.ROCK, NdBlocks.SMOOTH_ROCK, 0.1);
        smelting("cracked_rock_bricks_smelting", NdBlocks.ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS, 0.1);
        smelting("cracked_rock_tiles_smelting", NdBlocks.ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES, 0.1);

        generic3x1("rock_slab_3x1", NdBlocks.ROCK, NdBlocks.ROCK_SLAB, 6);
        generic3x1("mossy_rock_slab_3x1", NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_SLAB, 6);
        generic3x1("rock_bricks_slab_3x1", NdBlocks.ROCK_BRICKS, NdBlocks.ROCK_BRICKS_SLAB, 6);
        generic3x1("mossy_rock_bricks_slab_3x1", NdBlocks.MOSSY_ROCK_BRICKS, NdBlocks.MOSSY_ROCK_BRICKS_SLAB, 6);
        generic3x1("cracked_rock_bricks_slab_3x1", NdBlocks.CRACKED_ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS_SLAB, 6);
        generic3x1("rock_tiles_slab_3x1", NdBlocks.ROCK_TILES, NdBlocks.ROCK_TILES_SLAB, 6);
        generic3x1("mossy_rock_tiles_slab_3x1", NdBlocks.MOSSY_ROCK_TILES, NdBlocks.MOSSY_ROCK_TILES_SLAB, 6);
        generic3x1("cracked_rock_tiles_slab_3x1", NdBlocks.CRACKED_ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES_SLAB, 6);
        generic3x1("smooth_rock_slab_3x1", NdBlocks.SMOOTH_ROCK, NdBlocks.SMOOTH_ROCK_SLAB, 6);
        generic3x1("polished_rock_slab_3x1", NdBlocks.POLISHED_ROCK, NdBlocks.POLISHED_ROCK_SLAB, 6);

        stairs("rock_stairs_stairs", NdBlocks.ROCK, NdBlocks.ROCK_STAIRS, 4);
        stairs("mossy_rock_stairs_stairs", NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_STAIRS, 4);
        stairs("rock_bricks_stairs_stairs", NdBlocks.ROCK_BRICKS, NdBlocks.ROCK_BRICKS_STAIRS, 4);
        stairs("mossy_rock_bricks_stairs_stairs", NdBlocks.MOSSY_ROCK_BRICKS, NdBlocks.MOSSY_ROCK_BRICKS_STAIRS, 4);
        stairs("cracked_rock_bricks_stairs_stairs", NdBlocks.CRACKED_ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS_STAIRS, 4);
        stairs("rock_tiles_stairs_stairs", NdBlocks.ROCK_TILES, NdBlocks.ROCK_TILES_STAIRS, 4);
        stairs("mossy_rock_tiles_stairs_stairs", NdBlocks.MOSSY_ROCK_TILES, NdBlocks.MOSSY_ROCK_TILES_STAIRS, 4);
        stairs("cracked_rock_tiles_stairs_stairs", NdBlocks.CRACKED_ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES_STAIRS, 4);
        stairs("smooth_rock_stairs_stairs", NdBlocks.SMOOTH_ROCK, NdBlocks.SMOOTH_ROCK_STAIRS, 4);
        stairs("polished_rock_stairs_stairs", NdBlocks.POLISHED_ROCK, NdBlocks.POLISHED_ROCK_STAIRS, 4);

        step("rock_step_step", NdBlocks.ROCK, NdBlocks.ROCK_STEP, 6);
        step("mossy_rock_step_step", NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_STEP, 6);
        step("rock_bricks_step_step", NdBlocks.ROCK_BRICKS, NdBlocks.ROCK_BRICKS_STEP, 6);
        step("mossy_rock_bricks_step_step", NdBlocks.MOSSY_ROCK_BRICKS, NdBlocks.MOSSY_ROCK_BRICKS_STEP, 6);
        step("cracked_rock_bricks_step_step", NdBlocks.CRACKED_ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS_STEP, 6);
        step("rock_tiles_step_step", NdBlocks.ROCK_TILES, NdBlocks.ROCK_TILES_STEP, 6);
        step("mossy_rock_tiles_step_step", NdBlocks.MOSSY_ROCK_TILES, NdBlocks.MOSSY_ROCK_TILES_STEP, 6);
        step("cracked_rock_tiles_step_step", NdBlocks.CRACKED_ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES_STEP, 6);
        step("smooth_rock_step_step", NdBlocks.SMOOTH_ROCK, NdBlocks.SMOOTH_ROCK_STEP, 6);
        step("polished_rock_step_step", NdBlocks.POLISHED_ROCK, NdBlocks.POLISHED_ROCK_STEP, 6);

        // Darkrock
        generic4x4("darkrock_bricks_4x4", NdBlocks.DARKROCK, NdBlocks.DARKROCK_BRICKS, 4);
        generic4x4("mossy_darkrock_bricks_4x4", NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_BRICKS, 4);
        generic4x4("darkrock_tiles_4x4", NdBlocks.SMOOTH_DARKROCK, NdBlocks.DARKROCK_TILES, 4);
        generic1x3("darkrock_pillar_1x3", NdBlocks.DARKROCK, NdBlocks.DARKROCK_PILLAR, 3);
        generic1x2("chiseled_darkrock_1x2", NdBlocks.SMOOTH_DARKROCK_SLAB, NdBlocks.CHISELED_DARKROCK, 1);
        smelting("smooth_darkrock_smelting", NdBlocks.DARKROCK, NdBlocks.SMOOTH_DARKROCK, 0.1);
        smelting("cracked_darkrock_bricks_smelting", NdBlocks.DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS, 0.1);
        smelting("cracked_darkrock_tiles_smelting", NdBlocks.DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES, 0.1);

        generic3x1("darkrock_slab_3x1", NdBlocks.DARKROCK, NdBlocks.DARKROCK_SLAB, 6);
        generic3x1("mossy_darkrock_slab_3x1", NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_SLAB, 6);
        generic3x1("darkrock_bricks_slab_3x1", NdBlocks.DARKROCK_BRICKS, NdBlocks.DARKROCK_BRICKS_SLAB, 6);
        generic3x1("mossy_darkrock_bricks_slab_3x1", NdBlocks.MOSSY_DARKROCK_BRICKS, NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB, 6);
        generic3x1("cracked_darkrock_bricks_slab_3x1", NdBlocks.CRACKED_DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB, 6);
        generic3x1("darkrock_tiles_slab_3x1", NdBlocks.DARKROCK_TILES, NdBlocks.DARKROCK_TILES_SLAB, 6);
        generic3x1("mossy_darkrock_tiles_slab_3x1", NdBlocks.MOSSY_DARKROCK_TILES, NdBlocks.MOSSY_DARKROCK_TILES_SLAB, 6);
        generic3x1("cracked_darkrock_tiles_slab_3x1", NdBlocks.CRACKED_DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES_SLAB, 6);
        generic3x1("smooth_darkrock_slab_3x1", NdBlocks.SMOOTH_DARKROCK, NdBlocks.SMOOTH_DARKROCK_SLAB, 6);
        generic3x1("polished_darkrock_slab_3x1", NdBlocks.POLISHED_DARKROCK, NdBlocks.POLISHED_DARKROCK_SLAB, 6);

        stairs("darkrock_stairs_stairs", NdBlocks.DARKROCK, NdBlocks.DARKROCK_STAIRS, 4);
        stairs("mossy_darkrock_stairs_stairs", NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_STAIRS, 4);
        stairs("darkrock_bricks_stairs_stairs", NdBlocks.DARKROCK_BRICKS, NdBlocks.DARKROCK_BRICKS_STAIRS, 4);
        stairs("mossy_darkrock_bricks_stairs_stairs", NdBlocks.MOSSY_DARKROCK_BRICKS, NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS, 4);
        stairs("cracked_darkrock_bricks_stairs_stairs", NdBlocks.CRACKED_DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS, 4);
        stairs("darkrock_tiles_stairs_stairs", NdBlocks.DARKROCK_TILES, NdBlocks.DARKROCK_TILES_STAIRS, 4);
        stairs("mossy_darkrock_tiles_stairs_stairs", NdBlocks.MOSSY_DARKROCK_TILES, NdBlocks.MOSSY_DARKROCK_TILES_STAIRS, 4);
        stairs("cracked_darkrock_tiles_stairs_stairs", NdBlocks.CRACKED_DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES_STAIRS, 4);
        stairs("smooth_darkrock_stairs_stairs", NdBlocks.SMOOTH_DARKROCK, NdBlocks.SMOOTH_DARKROCK_STAIRS, 4);
        stairs("polished_darkrock_stairs_stairs", NdBlocks.POLISHED_DARKROCK, NdBlocks.POLISHED_DARKROCK_STAIRS, 4);

        step("darkrock_step_step", NdBlocks.DARKROCK, NdBlocks.DARKROCK_STEP, 6);
        step("mossy_darkrock_step_step", NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_STEP, 6);
        step("darkrock_bricks_step_step", NdBlocks.DARKROCK_BRICKS, NdBlocks.DARKROCK_BRICKS_STEP, 6);
        step("mossy_darkrock_bricks_step_step", NdBlocks.MOSSY_DARKROCK_BRICKS, NdBlocks.MOSSY_DARKROCK_BRICKS_STEP, 6);
        step("cracked_darkrock_bricks_step_step", NdBlocks.CRACKED_DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS_STEP, 6);
        step("darkrock_tiles_step_step", NdBlocks.DARKROCK_TILES, NdBlocks.DARKROCK_TILES_STEP, 6);
        step("mossy_darkrock_tiles_step_step", NdBlocks.MOSSY_DARKROCK_TILES, NdBlocks.MOSSY_DARKROCK_TILES_STEP, 6);
        step("cracked_darkrock_tiles_step_step", NdBlocks.CRACKED_DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES_STEP, 6);
        step("smooth_darkrock_step_step", NdBlocks.SMOOTH_DARKROCK, NdBlocks.SMOOTH_DARKROCK_STEP, 6);
        step("polished_darkrock_step_step", NdBlocks.POLISHED_DARKROCK, NdBlocks.POLISHED_DARKROCK_STEP, 6);
    }

    private void generic4x4(String id, IItemProvider from, IItemProvider to, int count) {
        ShapedRecipeBuilder.shapedRecipe(to, count)
                           .key('#', from)
                           .patternLine("##")
                           .patternLine("##")
                           .addCriterion("has_ingredient", hasItem(from))
                           .build(consumer, NaturesDebris.resLoc(id));
    }

    private void genericShapeless(String id, IItemProvider from, IItemProvider to, int count) {
        ShapelessRecipeBuilder.shapelessRecipe(to, count)
                              .addIngredient(from)
                              .addCriterion("has_ingredient", hasItem(from))
                              .build(consumer, NaturesDebris.resLoc(id));
    }

    private void generic1x2(String id, IItemProvider from, IItemProvider to, int count) {
        ShapedRecipeBuilder.shapedRecipe(to, count)
                           .key('#', from)
                           .patternLine("#")
                           .patternLine("#")
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

    private void generic3x1(String id, IItemProvider from, IItemProvider to, int count) {
        ShapedRecipeBuilder.shapedRecipe(to, count)
                           .key('#', from)
                           .patternLine("###")
                           .addCriterion("has_ingredient", hasItem(from))
                           .build(consumer, NaturesDebris.resLoc(id));
    }

    private void stairs(String id, IItemProvider from, IItemProvider to, int count) {
        ShapedRecipeBuilder.shapedRecipe(to, count)
                           .key('#', from)
                           .patternLine("#  ")
                           .patternLine("## ")
                           .patternLine("###")
                           .addCriterion("has_ingredient", hasItem(from))
                           .build(consumer, NaturesDebris.resLoc(id));
    }

    private void step(String id, IItemProvider from, IItemProvider to, int count) {
        ShapedRecipeBuilder.shapedRecipe(to, count)
                           .key('#', from)
                           .patternLine("#  ")
                           .patternLine("## ")
                           .addCriterion("has_ingredient", hasItem(from))
                           .build(consumer, NaturesDebris.resLoc(id));
    }

    private void smelting(String id, IItemProvider from, IItemProvider to, double xp) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(from), to, (float) xp, 200)
                            .addCriterion("has_ingredient", hasItem(from))
                            .build(consumer, NaturesDebris.resLoc(id));
    }

    @Override
    public String getName() {
        return "NaturesDebris/Recipes";
    }
}
