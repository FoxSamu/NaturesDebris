package net.shadew.ndebris.core.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.shadew.ndebris.core.blocks.ToolModifiable;

@Mixin({ShovelItem.class, HoeItem.class, AxeItem.class})
public class InjectToolModificationMixin {
    @Inject(
        method = "useOnBlock",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void redirectClickStateLookup(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
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
                    info.setReturnValue(ActionResult.PASS);
                    return;
                }

                if (!world.isClient) {
                    world.setBlockState(pos, newState, 11);
                    if (player != null) {
                        context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
                    }
                }

                info.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }
}
