package modernity.common.world.gen.decorate.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Random;

public interface IBlockProvider {
    boolean provide( IWorld world, BlockPos pos, Random rand );

    class ChooseRandom implements IBlockProvider {
        private final IBlockProvider[] providers;

        public ChooseRandom( IBlockProvider... providers ) { this.providers = providers; }

        @Override
        public boolean provide( IWorld world, BlockPos pos, Random rand ) {
            return providers[ rand.nextInt( providers.length ) ].provide( world, pos, rand );
        }
    }
}
