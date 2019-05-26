package modernity.common.world.gen.decorate.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public interface IBlockProvider {
    boolean provide( IWorld world, BlockPos pos, Random rand );
}
