package natures.debris.common.block.plantold.sys;

import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import net.shadew.util.misc.OptionalUtil;

import natures.debris.core.util.TypeUtil;
import natures.debris.common.block.plantold.PlantBlock;
import natures.debris.common.block.plantold.sys.growing.GrowContext;
import natures.debris.common.block.plantold.sys.growing.GrowType;
import natures.debris.common.block.plantold.sys.growing.IGrowLogic;
import natures.debris.common.block.plantold.sys.growing.PlayerItemGrowContext;

public class PlantUtil {
    public static PlantBlock getPlant(IWorld world, BlockPos pos) {
        return TypeUtil.castOrNull(world.getBlockState(pos).getBlock(), PlantBlock.class);
    }

    public static PlantBlock getPlant(BlockState state) {
        return TypeUtil.castOrNull(state.getBlock(), PlantBlock.class);
    }

    public static boolean grow(IWorld world, BlockPos pos, Supplier<? extends GrowContext> context) {
        BlockState state = world.getBlockState(pos);
        return OptionalUtil.testOrFalse(getPlant(state), plant -> {
            IGrowLogic logic = plant.getGrowLogic();
            GrowContext ctx = context.get();
            GrowType growType = logic.canGrow(plant, state, world, pos, world.getRandom(), ctx);
            if (growType != GrowType.NONE && growType != null) {
                switch (growType) {
                    case GROW:
                        logic.grow(plant, state, world, pos, world.getRandom(), ctx);
                        break;
                    case DECAY:
                        logic.decay(plant, state, world, pos, world.getRandom(), ctx);
                        break;
                    case KILL:
                        logic.kill(plant, state, world, pos, world.getRandom(), ctx);
                        break;
                    case HEAL:
                        logic.heal(plant, state, world, pos, world.getRandom(), ctx);
                        break;
                }
            } else {
                ctx.cancel();
            }
            ctx.finish();
            return !ctx.isCancelled();
        });
    }

    public static boolean feedItemAsPlayer(IWorld world, BlockPos pos, PlayerEntity player, Hand hand) {
        return grow(world, pos, () -> new PlayerItemGrowContext(player, hand));
    }

    public static boolean feedItemAsPlayer(IWorld world, BlockPos pos, ItemStack stack, PlayerEntity player, Hand hand) {
        return grow(world, pos, () -> new PlayerItemGrowContext(stack, player, hand));
    }
}
