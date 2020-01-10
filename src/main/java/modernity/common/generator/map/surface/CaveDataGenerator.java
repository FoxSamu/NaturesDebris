/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.map.surface;

import modernity.api.util.IntArrays;
import modernity.common.generator.map.MapGenerator;
import modernity.common.generator.structure.MDStructures;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.WorldGenRegion;

public class CaveDataGenerator extends MapGenerator<SurfaceGenData> {
    public CaveDataGenerator( IWorld world ) {
        super( world );
    }

    @Override
    public void generate( WorldGenRegion region, SurfaceGenData data ) {
        int[] heightmap = data.getHeightmap();

        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();
        IChunk chunk = region.getChunk( cx, cz );

        IntArrays.add( heightmap, - 8 );

        MDStructures.CAVE.addCaves( data.getGenerator(), chunk, cx, cz, heightmap, world.getSeed() );
    }
}
