package natures.debris.common.item.group;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.item.Item;

public class ItemSubgroup {
    public static final ItemSubgroup NATURE = new ItemSubgroup();
    public static final ItemSubgroup LOGS = new ItemSubgroup();
    public static final ItemSubgroup PLANKS = new ItemSubgroup();
    public static final ItemSubgroup ROCK = new ItemSubgroup();
    public static final ItemSubgroup DARKROCK = new ItemSubgroup();
    public static final ItemSubgroup PLANK_SLABS = new ItemSubgroup();
    public static final ItemSubgroup ROCK_SLABS = new ItemSubgroup();
    public static final ItemSubgroup DARKROCK_SLABS = new ItemSubgroup();
    public static final ItemSubgroup PLANK_STAIRS = new ItemSubgroup();
    public static final ItemSubgroup ROCK_STAIRS = new ItemSubgroup();
    public static final ItemSubgroup DARKROCK_STAIRS = new ItemSubgroup();
    public static final ItemSubgroup PLANK_STEPS = new ItemSubgroup();
    public static final ItemSubgroup ROCK_STEPS = new ItemSubgroup();
    public static final ItemSubgroup DARKROCK_STEPS = new ItemSubgroup();

    public static final ItemSubgroup DECORATIONS = new ItemSubgroup();
    public static final ItemSubgroup FENCES = new ItemSubgroup();
    public static final ItemSubgroup ROCK_WALLS = new ItemSubgroup();
    public static final ItemSubgroup DARKROCK_WALLS = new ItemSubgroup();

    private final Map<Item, ItemWeightPair> items = Maps.newHashMap();
    private int nextWeight = 0;

    public void addItem(Item item, int weight) {
        items.put(item, new ItemWeightPair(item, weight));
        if (weight > nextWeight) nextWeight = weight + 1;
    }

    public void addItem(Item item) {
        items.put(item, new ItemWeightPair(item, nextWeight++));
    }

    public Stream<Item> getSortedItems() {
        return items.values()
                    .stream()
                    .sorted()
                    .map(pair -> pair.item);
    }

    private static class ItemWeightPair implements Comparable<ItemWeightPair> {
        final Item item;
        final int weight;

        ItemWeightPair(Item item, int weight) {
            this.item = item;
            this.weight = weight;
        }

        @Override
        public int compareTo(ItemWeightPair o) {
            return Integer.compare(weight, o.weight);
        }
    }
}
