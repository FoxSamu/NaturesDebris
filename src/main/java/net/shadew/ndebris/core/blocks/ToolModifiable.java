package net.shadew.ndebris.core.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface ToolModifiable {
    TypedActionResult<BlockState> handleModification(BlockState state, World world, BlockPos pos, ItemStack item, Direction face, PlayerEntity player, ItemUsageContext context);
}
