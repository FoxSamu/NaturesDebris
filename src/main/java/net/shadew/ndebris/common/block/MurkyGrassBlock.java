package net.shadew.ndebris.common.block;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

import java.util.Random;

import net.shadew.ndebris.core.blocks.ToolModifiable;

public class MurkyGrassBlock extends MurkyDirtBlock implements ToolModifiable {
    public MurkyGrassBlock(Settings props) {
        super(props);
    }

    protected boolean isGrowableDirt(BlockView world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == NdBlocks.MURKY_DIRT;
    }

    protected BlockState getDecayBlock() {
        return NdBlocks.MURKY_DIRT.getDefaultState();
    }

    private static boolean canSustainGrass(BlockState state, WorldView world, BlockPos pos) {
        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);
        if (upState.getBlock() == Blocks.SNOW && upState.get(SnowBlock.LAYERS) == 1) {
            return true;
        } else {
            int opacity = ChunkLightProvider.getRealisticOpacity(world, state, pos, upState, upPos, Direction.UP, upState.getOpacity(world, upPos));
            return opacity < world.getMaxLightLevel();
        }
    }

    private static boolean canGrow(BlockState state, WorldView world, BlockPos pos) {
        BlockPos up = pos.up();
        return canSustainGrass(state, world, pos) && !world.getFluidState(up).isIn(FluidTags.WATER);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (!canSustainGrass(state, world, pos)) {
            world.setBlockState(pos, getDecayBlock());
        } else {
            BlockState myState = getDefaultState();

            for (int i = 0; i < 4; ++i) {
                BlockPos randomPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                if (isGrowableDirt(world, randomPos) && canGrow(myState, world, randomPos)) {
                    world.setBlockState(randomPos, myState);
                }
            }
        }
    }

    @Override
    public TypedActionResult<BlockState> handleModification(BlockState state, World world, BlockPos pos, ItemStack item, Direction face, PlayerEntity player, ItemUsageContext context) {
        if (item.getItem().isIn(FabricToolTags.SHOVELS)) {
            if (face != Direction.DOWN) {
                world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1, 1);
                return TypedActionResult.success(NdBlocks.MURKY_GRASS_PATH.getDefaultState());
            }
        }
        return TypedActionResult.pass(null);
    }
}
