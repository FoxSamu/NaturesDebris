package natures.debris.common.block.plant.growing;

import java.util.function.BiConsumer;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class DispenserItemGrowContext extends ItemGrowContext {
    private final BlockPos dispenserPos;
    private final BlockState dispenserState;
    private final BiConsumer<ItemStack, Boolean> changedItemConsumer;

    public DispenserItemGrowContext(ItemStack item, BlockPos dispenserPos, BlockState dispenserState, BiConsumer<ItemStack, Boolean> changedItemConsumer) {
        super(item);
        this.dispenserPos = dispenserPos;
        this.dispenserState = dispenserState;
        this.changedItemConsumer = changedItemConsumer;
    }

    @Override
    public BlockPos sourcePos() {
        return dispenserPos;
    }

    @Override
    public BlockState sourceState() {
        return dispenserState;
    }

    @Override
    public boolean isDispenser() {
        return true;
    }

    @Override
    public void finish() {
        ItemStack stack = item();
        if (!isCancelled()) {
            stack.shrink(1);
        }
        changedItemConsumer.accept(stack, !isCancelled());
    }
}
