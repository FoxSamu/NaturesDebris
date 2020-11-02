package natures.debris.common.block;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class StrippableLogBlock extends RotatedPillarBlock {
    private final Supplier<Block> strippedBlock;

    public StrippableLogBlock(Properties props, Supplier<Block> stripped) {
        super(props);
        this.strippedBlock = stripped;
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rtr) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (heldItem.getItem() instanceof AxeItem) {
            world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1, 1);
            if (!world.isRemote) {
                Block stripped = strippedBlock.get();
                BlockState placeState = stripped.getDefaultState().with(AXIS, state.get(AXIS));
                world.setBlockState(pos, placeState, 11);
                heldItem.damageItem(1, player, p -> p.sendBreakAnimation(hand));
            }
            return ActionResultType.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, rtr);
    }
}
