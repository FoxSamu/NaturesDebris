/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.map.surface;

import modernity.common.area.HeightmapsArea;
import modernity.common.area.core.AreaBox;
import modernity.common.area.core.ServerWorldAreaManager;
import modernity.common.generator.map.MapGenerator;
import modernity.generic.util.IntArrays;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;

public class CaveDataGenerator extends MapGenerator<SurfaceGenData> {
    public CaveDataGenerator(IWorld world) {
        super(world);
    }

    @Override
    public void generate(WorldGenRegion region, SurfaceGenData data) {
        int[] heightmap = data.getHeightmap();

        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();

        IntArrays.add(heightmap, -8);
        IntArrays.max(heightmap, 0);

        ServerWorldAreaManager.get(region.getWorld()).ifPresent(manager -> manager.addArea(new HeightmapsArea(
            manager.getWorld(),
            new AreaBox(cx * 16, 0, cz * 16, cx * 16 + 16, 256, cz * 16 + 16)
        ).applyCaveHeights(heightmap)));
    }
}
