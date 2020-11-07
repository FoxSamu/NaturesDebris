package natures.debris.common.block.plant.logic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import natures.debris.common.block.plant.Plant;

public interface ILightingEffect {
    boolean emission(Plant plant, BlockState state, IBlockReader world, BlockPos pos);
    int luminance(Plant plant, BlockState state, IBlockReader world, BlockPos pos, int current);

    static ILightingEffect simple(int luminance, boolean emission) {
        return new ILightingEffect() {
            @Override
            public boolean emission(Plant plant, BlockState state, IBlockReader world, BlockPos pos) {
                return emission;
            }

            @Override
            public int luminance(Plant plant, BlockState state, IBlockReader world, BlockPos pos, int current) {
                return luminance;
            }
        };
    }

    static ILightingEffect simple(int luminance) {
        return simple(luminance, true);
    }
}
