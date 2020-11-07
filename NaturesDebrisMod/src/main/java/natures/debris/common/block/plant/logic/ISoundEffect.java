package natures.debris.common.block.plant.logic;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import natures.debris.common.block.plant.Plant;

public interface ISoundEffect {
    ISoundEffect DEFAULT = dryWet(SoundType.PLANT, SoundType.WET_GRASS);

    SoundType sound(Plant plant, BlockState state, IBlockReader world, BlockPos pos);

    static ISoundEffect single(SoundType sound) {
        return (plant, state, world, pos) -> sound;
    }

    static ISoundEffect dryWet(SoundType dry, SoundType wet) {
        return (plant, state, world, pos) ->
                   plant.isWet(state) ? wet : dry;
    }
}
