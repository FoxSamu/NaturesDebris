package net.shadew.ndebris.data.recipes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import net.shadew.ndebris.common.NaturesDebris;
import net.shadew.ndebris.common.block.NdBlocks;

/**
 * Recipe provider designed for generating stonecutter recipes. This provider automatically derives all other possible
 * stonecutting recipes from the registered recipes. For example, when rock can be stonecutted into rock bricks and rock
 * bricks can be stonecutted into rock bricks slabs, it automatically derives that rock can be turned into rock bricks
 * slabs and it will generate an extra recipe for that. This way we don't need to care about these 'recursive recipes',
 * which apparently aren't derived by Minecraft itself.
 */
public class NdStonecuttingRecipeProvider extends AbstractRecipesProvider {
    private Consumer<RecipeJsonProvider> consumer;

    private final Map<Item, Map<Item, Integer>> recipes = Maps.newHashMap();
    private final Map<Item, List<Pair<Item, Integer>>> compiled = Maps.newHashMap();
    private final Set<String> flushed = Sets.newHashSet();

    public NdStonecuttingRecipeProvider(DataGenerator gen) {
        super(gen);
    }


    @Override
    protected void generate(Consumer<RecipeJsonProvider> consumer) {
        this.consumer = consumer;

        // Rock
        register(NdBlocks.ROCK, NdBlocks.ROCK_BRICKS);
        register(NdBlocks.ROCK, NdBlocks.ROCK_PILLAR);
        register(NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_BRICKS);
        register(NdBlocks.SMOOTH_ROCK, NdBlocks.ROCK_TILES);
        register(NdBlocks.SMOOTH_ROCK, NdBlocks.CHISELED_ROCK);
        register(NdBlocks.SMOOTH_ROCK, NdBlocks.POLISHED_ROCK);

        register(NdBlocks.ROCK, NdBlocks.ROCK_SLAB, 2);
        register(NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_SLAB, 2);
        register(NdBlocks.ROCK_BRICKS, NdBlocks.ROCK_BRICKS_SLAB, 2);
        register(NdBlocks.MOSSY_ROCK_BRICKS, NdBlocks.MOSSY_ROCK_BRICKS_SLAB, 2);
        register(NdBlocks.CRACKED_ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS_SLAB, 2);
        register(NdBlocks.ROCK_TILES, NdBlocks.ROCK_TILES_SLAB, 2);
        register(NdBlocks.MOSSY_ROCK_TILES, NdBlocks.MOSSY_ROCK_TILES_SLAB, 2);
        register(NdBlocks.CRACKED_ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES_SLAB, 2);
        register(NdBlocks.SMOOTH_ROCK, NdBlocks.SMOOTH_ROCK_SLAB, 2);
        register(NdBlocks.POLISHED_ROCK, NdBlocks.POLISHED_ROCK_SLAB, 2);

        register(NdBlocks.ROCK, NdBlocks.ROCK_STAIRS);
        register(NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_STAIRS);
        register(NdBlocks.ROCK_BRICKS, NdBlocks.ROCK_BRICKS_STAIRS);
        register(NdBlocks.MOSSY_ROCK_BRICKS, NdBlocks.MOSSY_ROCK_BRICKS_STAIRS);
        register(NdBlocks.CRACKED_ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS_STAIRS);
        register(NdBlocks.ROCK_TILES, NdBlocks.ROCK_TILES_STAIRS);
        register(NdBlocks.MOSSY_ROCK_TILES, NdBlocks.MOSSY_ROCK_TILES_STAIRS);
        register(NdBlocks.CRACKED_ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES_STAIRS);
        register(NdBlocks.SMOOTH_ROCK, NdBlocks.SMOOTH_ROCK_STAIRS);
        register(NdBlocks.POLISHED_ROCK, NdBlocks.POLISHED_ROCK_STAIRS);

        register(NdBlocks.ROCK, NdBlocks.ROCK_STEP, 4);
        register(NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_STEP, 4);
        register(NdBlocks.ROCK_BRICKS, NdBlocks.ROCK_BRICKS_STEP, 4);
        register(NdBlocks.MOSSY_ROCK_BRICKS, NdBlocks.MOSSY_ROCK_BRICKS_STEP, 4);
        register(NdBlocks.CRACKED_ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS_STEP, 4);
        register(NdBlocks.ROCK_TILES, NdBlocks.ROCK_TILES_STEP, 4);
        register(NdBlocks.MOSSY_ROCK_TILES, NdBlocks.MOSSY_ROCK_TILES_STEP, 4);
        register(NdBlocks.CRACKED_ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES_STEP, 4);
        register(NdBlocks.SMOOTH_ROCK, NdBlocks.SMOOTH_ROCK_STEP, 4);
        register(NdBlocks.POLISHED_ROCK, NdBlocks.POLISHED_ROCK_STEP, 4);

        register(NdBlocks.ROCK, NdBlocks.ROCK_WALL);
        register(NdBlocks.MOSSY_ROCK, NdBlocks.MOSSY_ROCK_WALL);
        register(NdBlocks.ROCK_BRICKS, NdBlocks.ROCK_BRICKS_WALL);
        register(NdBlocks.MOSSY_ROCK_BRICKS, NdBlocks.MOSSY_ROCK_BRICKS_WALL);
        register(NdBlocks.CRACKED_ROCK_BRICKS, NdBlocks.CRACKED_ROCK_BRICKS_WALL);
        register(NdBlocks.ROCK_TILES, NdBlocks.ROCK_TILES_WALL);
        register(NdBlocks.MOSSY_ROCK_TILES, NdBlocks.MOSSY_ROCK_TILES_WALL);
        register(NdBlocks.CRACKED_ROCK_TILES, NdBlocks.CRACKED_ROCK_TILES_WALL);
        register(NdBlocks.SMOOTH_ROCK, NdBlocks.SMOOTH_ROCK_WALL);
        register(NdBlocks.POLISHED_ROCK, NdBlocks.POLISHED_ROCK_WALL);

        // Darkrock
        register(NdBlocks.DARKROCK, NdBlocks.DARKROCK_BRICKS);
        register(NdBlocks.DARKROCK, NdBlocks.DARKROCK_PILLAR);
        register(NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_BRICKS);
        register(NdBlocks.SMOOTH_DARKROCK, NdBlocks.DARKROCK_TILES);
        register(NdBlocks.SMOOTH_DARKROCK, NdBlocks.CHISELED_DARKROCK);
        register(NdBlocks.SMOOTH_DARKROCK, NdBlocks.POLISHED_DARKROCK);

        register(NdBlocks.DARKROCK, NdBlocks.DARKROCK_SLAB, 2);
        register(NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_SLAB, 2);
        register(NdBlocks.DARKROCK_BRICKS, NdBlocks.DARKROCK_BRICKS_SLAB, 2);
        register(NdBlocks.MOSSY_DARKROCK_BRICKS, NdBlocks.MOSSY_DARKROCK_BRICKS_SLAB, 2);
        register(NdBlocks.CRACKED_DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS_SLAB, 2);
        register(NdBlocks.DARKROCK_TILES, NdBlocks.DARKROCK_TILES_SLAB, 2);
        register(NdBlocks.MOSSY_DARKROCK_TILES, NdBlocks.MOSSY_DARKROCK_TILES_SLAB, 2);
        register(NdBlocks.CRACKED_DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES_SLAB, 2);
        register(NdBlocks.SMOOTH_DARKROCK, NdBlocks.SMOOTH_DARKROCK_SLAB, 2);
        register(NdBlocks.POLISHED_DARKROCK, NdBlocks.POLISHED_DARKROCK_SLAB, 2);

        register(NdBlocks.DARKROCK, NdBlocks.DARKROCK_STAIRS);
        register(NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_STAIRS);
        register(NdBlocks.DARKROCK_BRICKS, NdBlocks.DARKROCK_BRICKS_STAIRS);
        register(NdBlocks.MOSSY_DARKROCK_BRICKS, NdBlocks.MOSSY_DARKROCK_BRICKS_STAIRS);
        register(NdBlocks.CRACKED_DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS_STAIRS);
        register(NdBlocks.DARKROCK_TILES, NdBlocks.DARKROCK_TILES_STAIRS);
        register(NdBlocks.MOSSY_DARKROCK_TILES, NdBlocks.MOSSY_DARKROCK_TILES_STAIRS);
        register(NdBlocks.CRACKED_DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES_STAIRS);
        register(NdBlocks.SMOOTH_DARKROCK, NdBlocks.SMOOTH_DARKROCK_STAIRS);
        register(NdBlocks.POLISHED_DARKROCK, NdBlocks.POLISHED_DARKROCK_STAIRS);

        register(NdBlocks.DARKROCK, NdBlocks.DARKROCK_STEP);
        register(NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_STEP);
        register(NdBlocks.DARKROCK_BRICKS, NdBlocks.DARKROCK_BRICKS_STEP);
        register(NdBlocks.MOSSY_DARKROCK_BRICKS, NdBlocks.MOSSY_DARKROCK_BRICKS_STEP);
        register(NdBlocks.CRACKED_DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS_STEP);
        register(NdBlocks.DARKROCK_TILES, NdBlocks.DARKROCK_TILES_STEP);
        register(NdBlocks.MOSSY_DARKROCK_TILES, NdBlocks.MOSSY_DARKROCK_TILES_STEP);
        register(NdBlocks.CRACKED_DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES_STEP);
        register(NdBlocks.SMOOTH_DARKROCK, NdBlocks.SMOOTH_DARKROCK_STEP);
        register(NdBlocks.POLISHED_DARKROCK, NdBlocks.POLISHED_DARKROCK_STEP);

        register(NdBlocks.DARKROCK, NdBlocks.DARKROCK_WALL);
        register(NdBlocks.MOSSY_DARKROCK, NdBlocks.MOSSY_DARKROCK_WALL);
        register(NdBlocks.DARKROCK_BRICKS, NdBlocks.DARKROCK_BRICKS_WALL);
        register(NdBlocks.MOSSY_DARKROCK_BRICKS, NdBlocks.MOSSY_DARKROCK_BRICKS_WALL);
        register(NdBlocks.CRACKED_DARKROCK_BRICKS, NdBlocks.CRACKED_DARKROCK_BRICKS_WALL);
        register(NdBlocks.DARKROCK_TILES, NdBlocks.DARKROCK_TILES_WALL);
        register(NdBlocks.MOSSY_DARKROCK_TILES, NdBlocks.MOSSY_DARKROCK_TILES_WALL);
        register(NdBlocks.CRACKED_DARKROCK_TILES, NdBlocks.CRACKED_DARKROCK_TILES_WALL);
        register(NdBlocks.SMOOTH_DARKROCK, NdBlocks.SMOOTH_DARKROCK_WALL);
        register(NdBlocks.POLISHED_DARKROCK, NdBlocks.POLISHED_DARKROCK_WALL);

        compile();
        flush();
    }

    private void register(ItemConvertible from, ItemConvertible to) {
        register(from, to, 1);
    }

    private void register(ItemConvertible from, ItemConvertible to, int count) {
        Item fromItem = from.asItem();
        Item toItem = to.asItem();
        recipes.computeIfAbsent(fromItem, k -> Maps.newHashMap())
               .put(toItem, count);
    }

    private void compile() {
        for (Item item : recipes.keySet()) {
            List<Pair<Item, Integer>> results = Lists.newArrayList();
            compile(item, 1, results);
            compiled.put(item, results);
        }
    }

    private void compile(Item from, int countMul, List<Pair<Item, Integer>> out) {
        Map<Item, Integer> to = recipes.get(from);
        for (Map.Entry<Item, Integer> entry : to.entrySet()) {
            Item item = entry.getKey();
            int count = entry.getValue() * countMul;
            out.add(Pair.of(item, count));

            // Recursively walk recipes map
            // If you can stonecut stone to bricks and bricks to bricks slab, you can stonecut stone to bricks slab
            if (recipes.containsKey(item)) {
                compile(item, count, out);
            }
        }
    }

    private void flush() {
        for (Map.Entry<Item, List<Pair<Item, Integer>>> entry : compiled.entrySet()) {
            Item from = entry.getKey();

            List<Pair<Item, Integer>> results = entry.getValue();
            for (Pair<Item, Integer> result : results) {
                Item to = result.getFirst();
                int count = result.getSecond();

                String name = recipeName(from, to);

                // Sometimes multiple ways lead to the same item, meaning a recipe can be registered multiple times.
                // We track all the recipe IDs we flushed in a set so that we only register recipes that we didn't
                // register already. We can assume that when a recipe is registered twice, they are exactly the same and
                // the latter one can be safely omitted...
                if (!flushed.contains(name)) {
                    SingleItemRecipeJsonFactory.create(Ingredient.ofItems(from), to, count)
                                               .create(criterionName(from), hasItem(from))
                                               .offerTo(consumer, NaturesDebris.id(name));
                    flushed.add(name);
                }
            }
        }
    }

    private static String criterionName(Item from) {
        Identifier id = Registry.ITEM.getId(from);
        assert id != null;

        return String.format("has_%s", id.getPath());
    }

    private static String recipeName(Item from, Item to) {
        Identifier fromId = Registry.ITEM.getId(from);
        Identifier toId = Registry.ITEM.getId(to);
        assert fromId != null && toId != null; // If they were null they wouldn't have been registered at all...

        return String.format("%s:%s_to_%s_stonecutting", toId.getNamespace(), fromId.getPath(), toId.getPath());
    }

    @Override
    public String getName() {
        return "NaturesDebris/StonecuttingRecipes";
    }
}
