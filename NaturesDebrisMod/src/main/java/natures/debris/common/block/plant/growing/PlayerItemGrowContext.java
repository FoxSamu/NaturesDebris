package natures.debris.common.block.plant.growing;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class PlayerItemGrowContext extends ItemGrowContext {
    private final PlayerEntity player;
    private final Hand hand;

    public PlayerItemGrowContext(PlayerEntity player, Hand hand) {
        super(player.getHeldItem(hand));
        this.player = player;
        this.hand = hand;
    }

    public PlayerItemGrowContext(ItemStack item, PlayerEntity player, Hand hand) {
        super(item);
        this.player = player;
        this.hand = hand;
    }

    @Override
    public Entity entity() {
        return player;
    }

    @Override
    public PlayerEntity player() {
        return player;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public void finish() {
        if (!isCancelled() && !player.abilities.isCreativeMode) {
            ItemStack item = item();
            item.shrink(1);
            player.setHeldItem(hand, item.getCount() == 0 ? ItemStack.EMPTY : item);
        }
    }
}
