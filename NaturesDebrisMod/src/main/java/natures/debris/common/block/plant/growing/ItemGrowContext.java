package natures.debris.common.block.plant.growing;

import net.minecraft.item.ItemStack;

public class ItemGrowContext extends GrowContext {
    private final ItemStack item;
    private int consumed = 0;

    public ItemGrowContext(ItemStack item) {
        this.item = item;
    }

    @Override
    public ItemStack item() {
        return item;
    }

    @Override
    public boolean isItem() {
        return true;
    }

    @Override
    public void consume(int amount) {
        if (amount > 0)
            consumed += amount;
    }

    public int getConsumed() {
        return consumed;
    }
}
