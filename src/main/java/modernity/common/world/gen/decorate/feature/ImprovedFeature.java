package modernity.common.world.gen.decorate.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

public abstract class ImprovedFeature <C extends IFeatureConfig> extends Feature<C> {
    @Override
    public boolean place( IWorld world, IChunkGenerator<? extends IChunkGenSettings> gen, Random random, BlockPos pos, C config ) {
        return generate( world, gen, random, pos, config );
    }

    public abstract boolean generate( IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkGen, Random rand, BlockPos pos, C config );
}
