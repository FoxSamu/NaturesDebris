package natures.debris.common.block.plant.growing;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import natures.debris.common.block.plant.PlantBlock;

/**
 * The growing logic of a plant, which determines how a plant will grow, decay, and what other natural changes it can
 * make. While growing, a plant goes through two sequential stages: one stage where it checks whether the situation is
 * such that it should perform any growing, and one stage where it performs the actual growing. Obviously the second
 * phase is skipped if the first phase determined that growing is impossible.
 */
public interface IGrowLogic {
    /**
     * Checks whether the situation given through all parameters is valid for growing. If this method determines that
     * growth is supported in any way it should return the appropriate {@link GrowType} that indicates how the plant
     * responds to the current situation. The provided situation is supplemented by a {@link GrowContext} instance,
     * which on itself is another bunch of context-dependent properties. I.e. a plant may be grown by fertilisation
     * using an item, by a nearby block that provides fertilisation or just naturally, depending on such contexts,
     * different {@link GrowContext} instances are provided.
     *
     * @param block The {@link PlantBlock} specifying the main logic of the plant to be grown
     * @param state The {@linkplain BlockState block state} of the plant to be grown
     * @param world The {@linkplain IWorld world} where the plant is located in and must obviously be grown in
     * @param pos   The {@linkplain BlockPos position} of the plant in the world
     * @param rand  Any {@link Random} instance for providing randominess during growing. It's recommended to use this
     *              random number generator instead of {@link IWorld#getRandom()} or {@link Math#random()}, even though
     *              the passed {@link Random} instance is often {@link IWorld#getRandom()}.
     * @param ctx   The {@link GrowContext} instance, to give context-specific information.
     * @return The {@link GrowType} to perform.
     */
    GrowType canGrow(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx);
    void grow(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx);
    void decay(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx);
    void kill(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx);
    void heal(PlantBlock block, BlockState state, IWorld world, BlockPos pos, Random rand, GrowContext ctx);
}
