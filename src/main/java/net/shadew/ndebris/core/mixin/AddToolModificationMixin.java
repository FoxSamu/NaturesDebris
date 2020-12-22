package net.shadew.ndebris.core.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import net.shadew.ndebris.core.blocks.ToolModifiable;

@Mixin({PickaxeItem.class, SwordItem.class, ShearsItem.class})
public class AddToolModificationMixin extends Item {

    private AddToolModificationMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof ToolModifiable) {
            ToolModifiable modifiable = (ToolModifiable) block;
            ItemStack item = context.getStack();
            Direction side = context.getSide();
            PlayerEntity player = context.getPlayer();
            TypedActionResult<BlockState> stateResult = modifiable.handleModification(state, world, pos, item, side, player, context);

            if (stateResult.getResult().isAccepted()) {
                BlockState newState = stateResult.getValue();
                if (newState == null) {
                    return ActionResult.PASS;
                }

                world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1, 1);
                if (!world.isClient) {
                    world.setBlockState(pos, newState, 11);
                    if (player != null) {
                        context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
                    }
                }

                return ActionResult.success(world.isClient);
            }
        }
        return super.useOnBlock(context);
    }
}
