package natures.debris.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ShovelItem;

public class HookedShovelItem extends ShovelItem {
    public HookedShovelItem(IItemTier tier, float attackDamage, float attackSpeed, Properties props) {
        super(tier, attackDamage, attackSpeed, props);
    }

    public static void registerShovelAction(Block from, BlockState to) {
        SHOVEL_LOOKUP.put(from, to);
    }
}
