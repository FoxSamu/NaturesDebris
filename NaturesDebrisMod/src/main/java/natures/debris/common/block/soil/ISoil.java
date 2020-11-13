package natures.debris.common.block.soil;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface ISoil {
    Fertility getFertility(IWorld world, BlockPos pos, BlockState state);
    void setFertility(IWorld world, BlockPos pos, BlockState state, Fertility fertility);

    int getLevel(IWorld world, BlockPos pos, BlockState state);
    void setLevel(IWorld world, BlockPos pos, BlockState state, int level);

    boolean isWet(IWorld world, BlockPos pos, BlockState state);

    default ISoilContext context(IWorld world, BlockPos pos) {
        return new ISoilContext() {
            @Override
            public Fertility getFertility() {
                return ISoil.this.getFertility(world, pos, world.getBlockState(pos));
            }

            @Override
            public void setFertility(Fertility fertility) {
                ISoil.this.setFertility(world, pos, world.getBlockState(pos), fertility);
            }

            @Override
            public int getLevel() {
                return ISoil.this.getLevel(world, pos, world.getBlockState(pos));
            }

            @Override
            public void setLevel(int level) {
                ISoil.this.setLevel(world, pos, world.getBlockState(pos), level);
            }

            @Override
            public boolean isWet() {
                return ISoil.this.isWet(world, pos, world.getBlockState(pos));
            }
        };
    }
}
