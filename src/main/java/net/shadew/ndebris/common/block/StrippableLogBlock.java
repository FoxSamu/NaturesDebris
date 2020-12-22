package net.shadew.ndebris.common.block;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.function.Supplier;

import net.shadew.ndebris.core.blocks.ToolModifiable;

public class StrippableLogBlock extends PillarBlock implements ToolModifiable {
    private final Supplier<Block> strippedBlock;

    public StrippableLogBlock(Settings props, Supplier<Block> stripped) {
        super(props);
        this.strippedBlock = stripped;
    }

    @Override
    public TypedActionResult<BlockState> handleModification(BlockState state, World world, BlockPos pos, ItemStack item, Direction face, PlayerEntity player, ItemUsageContext context) {
        if (item.getItem().isIn(FabricToolTags.AXES)) {
            if (face != Direction.DOWN) {
                world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1, 1);
                return TypedActionResult.success(strippedBlock.get().getDefaultState().with(AXIS, state.get(AXIS)));
            }
        }
        return TypedActionResult.pass(null);
    }
}
