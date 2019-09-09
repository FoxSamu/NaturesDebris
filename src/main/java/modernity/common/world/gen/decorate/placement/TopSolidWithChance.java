/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 27 - 2019
 */

package modernity.common.world.gen.decorate.placement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.BasePlacement;
import net.minecraft.world.gen.placement.ChanceConfig;

import java.util.Random;

public class TopSolidWithChance extends BasePlacement<ChanceConfig> {
    public <C extends IFeatureConfig> boolean generate( IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkgen, Random random, BlockPos pos, ChanceConfig config, Feature<C> feature, C featureConfig ) {
        if( random.nextInt( config.chance ) == 0 ) {
            int x = random.nextInt( 16 ) + pos.getX();
            int z = random.nextInt( 16 ) + pos.getZ();
            feature.place( world, chunkgen, random, new BlockPos( x, world.getHeight( Heightmap.Type.OCEAN_FLOOR_WG, x, z ), z ), featureConfig );
        }

        return true;
    }
}
