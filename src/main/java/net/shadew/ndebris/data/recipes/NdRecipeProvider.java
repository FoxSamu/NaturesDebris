package net.shadew.ndebris.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import java.util.function.Consumer;

import net.shadew.ndebris.common.NaturesDebris;
import net.shadew.ndebris.common.block.NdBlocks;

public class NdRecipeProvider extends AbstractRecipesProvider {
    private Consumer<RecipeJsonProvider> consumer;

    public NdRecipeProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void generate(Consumer<RecipeJsonProvider> consumer) {
        this.consumer = consumer;

        // Nature blocks
        shapeless("murky_humus_shapeless", NdBlocks.LEAFY_HUMUS, NdBlocks.MURKY_HUMUS, 1);
        smelting("murky_terracotta_smelting", NdBlocks.ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES, 0.35);

        // Wood blocks
        generic2x2("blackwood_2x2", NdBlocks.BLACKWOOD_LOG, NdBlocks.BLACKWOOD, 3);
        generic2x2("inver_wood_2x2", NdBlocks.INVER_LOG, NdBlocks.INVER_WOOD, 3);
        generic2x2("stripped_blackwood_2x2", NdBlocks.STRIPPED_BLACKWOOD_LOG, NdBlocks.STRIPPED_BLACKWOOD, 3);
        generic2x2("stripped_inver_wood_2x2", NdBlocks.STRIPPED_INVER_LOG, NdBlocks.STRIPPED_INVER_WOOD, 3);

        shapeless("blackwood_planks_from_log", NdBlocks.BLACKWOOD_LOG, NdBlocks.BLACKWOOD_PLANKS, 4);
        shapeless("blackwood_planks_from_wood", NdBlocks.BLACKWOOD, NdBlocks.BLACKWOOD_PLANKS, 4);
        shapeless("blackwood_planks_from_stripped_log", NdBlocks.STRIPPED_BLACKWOOD_LOG, NdBlocks.BLACKWOOD_PLANKS, 4);
        shapeless("blackwood_planks_from_stripped_wood", NdBlocks.STRIPPED_BLACKWOOD, NdBlocks.BLACKWOOD_PLANKS, 4);

        shapeless("inver_planks_from_log", NdBlocks.INVER_LOG, NdBlocks.INVER_PLANKS, 4);
        shapeless("inver_planks_from_wood", NdBlocks.INVER_WOOD, NdBlocks.INVER_PLANKS, 4);
        shapeless("inver_planks_from_stripped_log", NdBlocks.STRIPPED_INVER_LOG, NdBlocks.INVER_PLANKS, 4);
        shapeless("inver_planks_from_stripped_wood", NdBlocks.STRIPPED_INVER_WOOD, NdBlocks.INVER_PLANKS, 4);

        generic3x1("blackwood_slab_3x1", NdBlocks.BLACKWOOD_PLANKS, NdBlocks.BLACKWOOD_SLAB, 6);
        generic3x1("inver_slab_3x1", NdBlocks.INVER_PLANKS, NdBlocks.INVER_SLAB, 6);
        stairs("blackwood_stairs_stairs", NdBlocks.BLACKWOOD_PLANKS, NdBlocks.BLACKWOOD_STAIRS, 4);
        stairs("inver_stairs_stairs", NdBlocks.INVER_PLANKS, NdBlocks.INVER_STAIRS, 4);
        step("blackwood_step_step", NdBlocks.BLACKWOOD_PLANKS, NdBlocks.BLACKWOOD_STEP, 6);
        step("inver_step_step", NdBlocks.INVER_PLANKS, NdBlocks.INVER_STEP, 6);
        fence("blackwood_fence_fence", NdBlocks.BLACKWOOD_PLANKS, NdBlocks.BLACKWOOD_FENCE, 3);
        fence("inver_fence_fence", NdBlocks.INVER_PLANKS, NdBlocks.INVER_FENCE, 3);

        // For stonecutting recipes, see NdStonecuttingRecipeProvider

        // Rock
        generic2x2("rock_bricks_2x2", NdBlocks.ROCK, NdBlocks.ROCK_BRICKS, 4);
        generic2x2("mossy_rock_bricks_2x2", NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_BRICKS, 4);
        generic2x2("rock_tiles_2x2", NdBlocks.SMOOTH_ROCK, NdBlocks.ROCK_TILES, 4);
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

        generic3x2("rock_wall_3x2", NdBlocks.ROCK, NdBlocks.ROCK_WALL, 6);
        generic3x2("mossy_rock_wall_3x2", NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_WALL, 6);
        generic3x2("rock_bricks_wall_3x2", NdBlocks.ROCK_BRICKS, NdBlocks.ROCK_BRICKS_WALL, 6);
        generic3x2("mossy_rock_bricks_wall_3x2", NdBlocks.MOSSY_ROCK_BRICKS, NdBlocks.MOSSY_ROCK_BRICKS_WALL, 6);
        generic3x2("cracked_rock_bricks_wall_3x2", NdBlocks.CRACKED_ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS_WALL, 6);
        generic3x2("rock_tiles_wall_3x2", NdBlocks.ROCK_TILES, NdBlocks.ROCK_TILES_WALL, 6);
        generic3x2("mossy_rock_tiles_wall_3x2", NdBlocks.MOSSY_ROCK_TILES, NdBlocks.MOSSY_ROCK_TILES_WALL, 6);
        generic3x2("cracked_rock_tiles_wall_3x2", NdBlocks.CRACKED_ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES_WALL, 6);
        generic3x2("smooth_rock_wall_3x2", NdBlocks.SMOOTH_ROCK, NdBlocks.SMOOTH_ROCK_WALL, 6);
        generic3x2("polished_rock_wall_3x2", NdBlocks.POLISHED_ROCK, NdBlocks.POLISHED_ROCK_WALL, 6);

        // Darkrock
        generic2x2("darkrock_bricks_2x2", NdBlocks.DARKROCK, NdBlocks.DARKROCK_BRICKS, 4);
        generic2x2("mossy_darkrock_bricks_2x2", NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_BRICKS, 4);
        generic2x2("darkrock_tiles_2x2", NdBlocks.SMOOTH_DARKROCK, NdBlocks.DARKROCK_TILES, 4);
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

        generic3x2("darkrock_wall_3x2", NdBlocks.DARKROCK, NdBlocks.DARKROCK_WALL, 6);
        generic3x2("mossy_darkrock_wall_3x2", NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_WALL, 6);
        generic3x2("darkrock_bricks_wall_3x2", NdBlocks.DARKROCK_BRICKS, NdBlocks.DARKROCK_BRICKS_WALL, 6);
        generic3x2("mossy_darkrock_bricks_wall_3x2", NdBlocks.MOSSY_DARKROCK_BRICKS, NdBlocks.MOSSY_DARKROCK_BRICKS_WALL, 6);
        generic3x2("cracked_darkrock_bricks_wall_3x2", NdBlocks.CRACKED_DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS_WALL, 6);
        generic3x2("darkrock_tiles_wall_3x2", NdBlocks.DARKROCK_TILES, NdBlocks.DARKROCK_TILES_WALL, 6);
        generic3x2("mossy_darkrock_tiles_wall_3x2", NdBlocks.MOSSY_DARKROCK_TILES, NdBlocks.MOSSY_DARKROCK_TILES_WALL, 6);
        generic3x2("cracked_darkrock_tiles_wall_3x2", NdBlocks.CRACKED_DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES_WALL, 6);
        generic3x2("smooth_darkrock_wall_3x2", NdBlocks.SMOOTH_DARKROCK, NdBlocks.SMOOTH_DARKROCK_WALL, 6);
        generic3x2("polished_darkrock_wall_3x2", NdBlocks.POLISHED_DARKROCK, NdBlocks.POLISHED_DARKROCK_WALL, 6);

        // Limestone
        generic2x2("limestone_bricks_2x2", NdBlocks.LIMESTONE, NdBlocks.LIMESTONE_BRICKS, 4);
        generic2x2("limestone_tiles_2x2", NdBlocks.POLISHED_LIMESTONE, NdBlocks.LIMESTONE_TILES, 4);
        generic1x3("limestone_pillar_1x3", NdBlocks.LIMESTONE, NdBlocks.LIMESTONE_PILLAR, 3);
        shapeless("carved_limestone_shapeless", NdBlocks.POLISHED_LIMESTONE, NdBlocks.CARVED_LIMESTONE, 1);
        shapeless("polished_limestone_shapeless", NdBlocks.LIMESTONE, NdBlocks.POLISHED_LIMESTONE, 1);
        smelting("cracked_limestone_bricks_smelting", NdBlocks.LIMESTONE_BRICKS, NdBlocks.CRACKED_LIMESTONE_BRICKS, 0.1);
        smelting("cracked_limestone_tiles_smelting", NdBlocks.LIMESTONE_TILES, NdBlocks.CRACKED_LIMESTONE_TILES, 0.1);

        generic3x1("limestone_slab_3x1", NdBlocks.LIMESTONE, NdBlocks.LIMESTONE_SLAB, 6);
        generic3x1("limestone_bricks_slab_3x1", NdBlocks.LIMESTONE_BRICKS, NdBlocks.LIMESTONE_BRICKS_SLAB, 6);
        generic3x1("cracked_limestone_bricks_slab_3x1", NdBlocks.CRACKED_LIMESTONE_BRICKS, NdBlocks.CRACKED_LIMESTONE_BRICKS_SLAB, 6);
        generic3x1("limestone_tiles_slab_3x1", NdBlocks.LIMESTONE_TILES, NdBlocks.LIMESTONE_TILES_SLAB, 6);
        generic3x1("cracked_limestone_tiles_slab_3x1", NdBlocks.CRACKED_LIMESTONE_TILES, NdBlocks.CRACKED_LIMESTONE_TILES_SLAB, 6);
        generic3x1("polished_limestone_slab_3x1", NdBlocks.POLISHED_LIMESTONE, NdBlocks.POLISHED_LIMESTONE_SLAB, 6);

        stairs("limestone_stairs_stairs", NdBlocks.LIMESTONE, NdBlocks.LIMESTONE_STAIRS, 4);
        stairs("limestone_bricks_stairs_stairs", NdBlocks.LIMESTONE_BRICKS, NdBlocks.LIMESTONE_BRICKS_STAIRS, 4);
        stairs("cracked_limestone_bricks_stairs_stairs", NdBlocks.CRACKED_LIMESTONE_BRICKS, NdBlocks.CRACKED_LIMESTONE_BRICKS_STAIRS, 4);
        stairs("limestone_tiles_stairs_stairs", NdBlocks.LIMESTONE_TILES, NdBlocks.LIMESTONE_TILES_STAIRS, 4);
        stairs("cracked_limestone_tiles_stairs_stairs", NdBlocks.CRACKED_LIMESTONE_TILES, NdBlocks.CRACKED_LIMESTONE_TILES_STAIRS, 4);
        stairs("polished_limestone_stairs_stairs", NdBlocks.POLISHED_LIMESTONE, NdBlocks.POLISHED_LIMESTONE_STAIRS, 4);

        step("limestone_step_step", NdBlocks.LIMESTONE, NdBlocks.LIMESTONE_STEP, 6);
        step("limestone_bricks_step_step", NdBlocks.LIMESTONE_BRICKS, NdBlocks.LIMESTONE_BRICKS_STEP, 6);
        step("cracked_limestone_bricks_step_step", NdBlocks.CRACKED_LIMESTONE_BRICKS, NdBlocks.CRACKED_LIMESTONE_BRICKS_STEP, 6);
        step("limestone_tiles_step_step", NdBlocks.LIMESTONE_TILES, NdBlocks.LIMESTONE_TILES_STEP, 6);
        step("cracked_limestone_tiles_step_step", NdBlocks.CRACKED_LIMESTONE_TILES, NdBlocks.CRACKED_LIMESTONE_TILES_STEP, 6);
        step("polished_limestone_step_step", NdBlocks.POLISHED_LIMESTONE, NdBlocks.POLISHED_LIMESTONE_STEP, 6);

        generic3x2("limestone_wall_3x2", NdBlocks.LIMESTONE, NdBlocks.LIMESTONE_WALL, 6);
        generic3x2("limestone_bricks_wall_3x2", NdBlocks.LIMESTONE_BRICKS, NdBlocks.LIMESTONE_BRICKS_WALL, 6);
        generic3x2("cracked_limestone_bricks_wall_3x2", NdBlocks.CRACKED_LIMESTONE_BRICKS, NdBlocks.CRACKED_LIMESTONE_BRICKS_WALL, 6);
        generic3x2("limestone_tiles_wall_3x2", NdBlocks.LIMESTONE_TILES, NdBlocks.LIMESTONE_TILES_WALL, 6);
        generic3x2("cracked_limestone_tiles_wall_3x2", NdBlocks.CRACKED_LIMESTONE_TILES, NdBlocks.CRACKED_LIMESTONE_TILES_WALL, 6);
        generic3x2("polished_limestone_wall_3x2", NdBlocks.POLISHED_LIMESTONE, NdBlocks.POLISHED_LIMESTONE_WALL, 6);

        // Sumestone
        generic2x2("sumestone_bricks_2x2", NdBlocks.SUMESTONE, NdBlocks.SUMESTONE_BRICKS, 4);
        generic1x3("sumestone_pillar_1x3", NdBlocks.SUMESTONE, NdBlocks.SUMESTONE_PILLAR, 3);
        generic1x2("chiseled_sumestone_1x2", NdBlocks.SUMESTONE_SLAB, NdBlocks.CHISELED_SUMESTONE, 1);
        shapeless("polished_sumestone_shapeless", NdBlocks.SUMESTONE, NdBlocks.POLISHED_SUMESTONE, 1);
        smelting("cracked_sumestone_bricks_smelting", NdBlocks.SUMESTONE_BRICKS, NdBlocks.CRACKED_SUMESTONE_BRICKS, 0.1);

        generic3x1("sumestone_slab_3x1", NdBlocks.SUMESTONE, NdBlocks.SUMESTONE_SLAB, 6);
        generic3x1("sumestone_bricks_slab_3x1", NdBlocks.SUMESTONE_BRICKS, NdBlocks.SUMESTONE_BRICKS_SLAB, 6);
        generic3x1("cracked_sumestone_bricks_slab_3x1", NdBlocks.CRACKED_SUMESTONE_BRICKS, NdBlocks.CRACKED_SUMESTONE_BRICKS_SLAB, 6);
        generic3x1("polished_sumestone_slab_3x1", NdBlocks.POLISHED_SUMESTONE, NdBlocks.POLISHED_SUMESTONE_SLAB, 6);

        stairs("sumestone_stairs_stairs", NdBlocks.SUMESTONE, NdBlocks.SUMESTONE_STAIRS, 4);
        stairs("sumestone_bricks_stairs_stairs", NdBlocks.SUMESTONE_BRICKS, NdBlocks.SUMESTONE_BRICKS_STAIRS, 4);
        stairs("cracked_sumestone_bricks_stairs_stairs", NdBlocks.CRACKED_SUMESTONE_BRICKS, NdBlocks.CRACKED_SUMESTONE_BRICKS_STAIRS, 4);
        stairs("polished_sumestone_stairs_stairs", NdBlocks.POLISHED_SUMESTONE, NdBlocks.POLISHED_SUMESTONE_STAIRS, 4);

        step("sumestone_step_step", NdBlocks.SUMESTONE, NdBlocks.SUMESTONE_STEP, 6);
        step("sumestone_bricks_step_step", NdBlocks.SUMESTONE_BRICKS, NdBlocks.SUMESTONE_BRICKS_STEP, 6);
        step("cracked_sumestone_bricks_step_step", NdBlocks.CRACKED_SUMESTONE_BRICKS, NdBlocks.CRACKED_SUMESTONE_BRICKS_STEP, 6);
        step("polished_sumestone_step_step", NdBlocks.POLISHED_SUMESTONE, NdBlocks.POLISHED_SUMESTONE_STEP, 6);

        generic3x2("sumestone_wall_3x2", NdBlocks.SUMESTONE, NdBlocks.SUMESTONE_WALL, 6);
        generic3x2("sumestone_bricks_wall_3x2", NdBlocks.SUMESTONE_BRICKS, NdBlocks.SUMESTONE_BRICKS_WALL, 6);
        generic3x2("cracked_sumestone_bricks_wall_3x2", NdBlocks.CRACKED_SUMESTONE_BRICKS, NdBlocks.CRACKED_SUMESTONE_BRICKS_WALL, 6);
        generic3x2("polished_sumestone_wall_3x2", NdBlocks.POLISHED_SUMESTONE, NdBlocks.POLISHED_SUMESTONE_WALL, 6);

        // Dark Sumestone
        generic2x2("dark_sumestone_bricks_2x2", NdBlocks.DARK_SUMESTONE, NdBlocks.DARK_SUMESTONE_BRICKS, 4);
        generic1x3("dark_sumestone_pillar_1x3", NdBlocks.DARK_SUMESTONE, NdBlocks.DARK_SUMESTONE_PILLAR, 3);
        generic1x2("chiseled_dark_sumestone_1x2", NdBlocks.DARK_SUMESTONE_SLAB, NdBlocks.CHISELED_DARK_SUMESTONE, 1);
        shapeless("polished_dark_sumestone_shapeless", NdBlocks.DARK_SUMESTONE, NdBlocks.POLISHED_DARK_SUMESTONE, 1);
        smelting("cracked_dark_sumestone_bricks_smelting", NdBlocks.DARK_SUMESTONE_BRICKS, NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS, 0.1);

        generic3x1("dark_sumestone_slab_3x1", NdBlocks.DARK_SUMESTONE, NdBlocks.DARK_SUMESTONE_SLAB, 6);
        generic3x1("dark_sumestone_bricks_slab_3x1", NdBlocks.DARK_SUMESTONE_BRICKS, NdBlocks.DARK_SUMESTONE_BRICKS_SLAB, 6);
        generic3x1("cracked_dark_sumestone_bricks_slab_3x1", NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS, NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_SLAB, 6);
        generic3x1("polished_dark_sumestone_slab_3x1", NdBlocks.POLISHED_DARK_SUMESTONE, NdBlocks.POLISHED_DARK_SUMESTONE_SLAB, 6);

        stairs("dark_sumestone_stairs_stairs", NdBlocks.DARK_SUMESTONE, NdBlocks.DARK_SUMESTONE_STAIRS, 4);
        stairs("dark_sumestone_bricks_stairs_stairs", NdBlocks.DARK_SUMESTONE_BRICKS, NdBlocks.DARK_SUMESTONE_BRICKS_STAIRS, 4);
        stairs("cracked_dark_sumestone_bricks_stairs_stairs", NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS, NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_STAIRS, 4);
        stairs("polished_dark_sumestone_stairs_stairs", NdBlocks.POLISHED_DARK_SUMESTONE, NdBlocks.POLISHED_DARK_SUMESTONE_STAIRS, 4);

        step("dark_sumestone_step_step", NdBlocks.DARK_SUMESTONE, NdBlocks.DARK_SUMESTONE_STEP, 6);
        step("dark_sumestone_bricks_step_step", NdBlocks.DARK_SUMESTONE_BRICKS, NdBlocks.DARK_SUMESTONE_BRICKS_STEP, 6);
        step("cracked_dark_sumestone_bricks_step_step", NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS, NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_STEP, 6);
        step("polished_dark_sumestone_step_step", NdBlocks.POLISHED_DARK_SUMESTONE, NdBlocks.POLISHED_DARK_SUMESTONE_STEP, 6);

        generic3x2("dark_sumestone_wall_3x2", NdBlocks.DARK_SUMESTONE, NdBlocks.DARK_SUMESTONE_WALL, 6);
        generic3x2("dark_sumestone_bricks_wall_3x2", NdBlocks.DARK_SUMESTONE_BRICKS, NdBlocks.DARK_SUMESTONE_BRICKS_WALL, 6);
        generic3x2("cracked_dark_sumestone_bricks_wall_3x2", NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS, NdBlocks.CRACKED_DARK_SUMESTONE_BRICKS_WALL, 6);
        generic3x2("polished_dark_sumestone_wall_3x2", NdBlocks.POLISHED_DARK_SUMESTONE, NdBlocks.POLISHED_DARK_SUMESTONE_WALL, 6);
    }

    private void generic2x2(String id, ItemConvertible from, ItemConvertible to, int count) {
        ShapedRecipeJsonFactory.create(to, count)
                               .input('#', from)
                               .pattern("##")
                               .pattern("##")
                               .criterion("has_ingredient", hasItem(from))
                               .offerTo(consumer, NaturesDebris.id(id));
    }

    private void shapeless(String id, ItemConvertible from, ItemConvertible to, int count) {
        ShapelessRecipeJsonFactory.create(to, count)
                                  .input(from)
                                  .criterion("has_ingredient", hasItem(from))
                                  .offerTo(consumer, NaturesDebris.id(id));
    }

    private void fence(String id, ItemConvertible from, ItemConvertible to, int count) {
        ShapedRecipeJsonFactory.create(to, count)
                               .input('#', from)
                               .input('/', Items.STICK)
                               .pattern("#/#")
                               .pattern("#/#")
                               .criterion("has_ingredient", hasItem(from))
                               .offerTo(consumer, NaturesDebris.id(id));
    }

    private void generic1x2(String id, ItemConvertible from, ItemConvertible to, int count) {
        ShapedRecipeJsonFactory.create(to, count)
                               .input('#', from)
                               .pattern("#")
                               .pattern("#")
                               .criterion("has_ingredient", hasItem(from))
                               .offerTo(consumer, NaturesDebris.id(id));
    }

    private void generic1x3(String id, ItemConvertible from, ItemConvertible to, int count) {
        ShapedRecipeJsonFactory.create(to, count)
                               .input('#', from)
                               .pattern("#")
                               .pattern("#")
                               .pattern("#")
                               .criterion("has_ingredient", hasItem(from))
                               .offerTo(consumer, NaturesDebris.id(id));
    }

    private void generic3x1(String id, ItemConvertible from, ItemConvertible to, int count) {
        ShapedRecipeJsonFactory.create(to, count)
                               .input('#', from)
                               .pattern("###")
                               .criterion("has_ingredient", hasItem(from))
                               .offerTo(consumer, NaturesDebris.id(id));
    }

    private void generic3x2(String id, ItemConvertible from, ItemConvertible to, int count) {
        ShapedRecipeJsonFactory.create(to, count)
                               .input('#', from)
                               .pattern("###")
                               .pattern("###")
                               .criterion("has_ingredient", hasItem(from))
                               .offerTo(consumer, NaturesDebris.id(id));
    }

    private void stairs(String id, ItemConvertible from, ItemConvertible to, int count) {
        ShapedRecipeJsonFactory.create(to, count)
                               .input('#', from)
                               .pattern("#  ")
                               .pattern("## ")
                               .pattern("###")
                               .criterion("has_ingredient", hasItem(from))
                               .offerTo(consumer, NaturesDebris.id(id));
    }

    private void step(String id, ItemConvertible from, ItemConvertible to, int count) {
        ShapedRecipeJsonFactory.create(to, count)
                               .input('#', from)
                               .pattern("#  ")
                               .pattern("## ")
                               .criterion("has_ingredient", hasItem(from))
                               .offerTo(consumer, NaturesDebris.id(id));
    }

    private void smelting(String id, ItemConvertible from, ItemConvertible to, double xp) {
        CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(from), to, (float) xp, 200)
                                .criterion("has_ingredient", hasItem(from))
                                .offerTo(consumer, NaturesDebris.id(id));
    }

    @Override
    public String getName() {
        return "NaturesDebris/Recipes";
    }
}
