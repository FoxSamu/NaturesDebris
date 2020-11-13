package natures.debris.common.block.plant.growing;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import natures.debris.core.util.OptionalUtil;
import natures.debris.core.util.TypeUtil;

public abstract class GrowContext {
    private boolean cancelled;

    public ItemStack item() {
        return ItemStack.EMPTY;
    }

    public boolean isItemTagged(ITag<Item> tag) {
        return item().getItem().isIn(tag);
    }

    public Entity entity() {
        return null;
    }

    public PlayerEntity player() {
        return TypeUtil.castOrNull(entity(), PlayerEntity.class);
    }

    public boolean isEntityTagged(ITag<EntityType<?>> tag) {
        return OptionalUtil.testOrFalse(entity(), entity -> tag.contains(entity.getType()));
    }

    public BlockPos sourcePos() {
        return null;
    }

    public BlockState sourceState() {
        return null;
    }

    public Direction sourceDirection() {
        return OptionalUtil.map(sourceState(), state -> state.get(DispenserBlock.FACING));
    }

    public boolean isItem() {
        return false;
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isDispenser() {
        return false;
    }

    public abstract void consume(int amount);

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void finish() {

    }
}
