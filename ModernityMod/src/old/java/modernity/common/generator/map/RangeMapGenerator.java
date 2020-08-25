/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.map;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.WorldGenRegion;
import modernity.api.util.math.ILongScrambler;

public abstract class RangeMapGenerator<D extends IMapGenData> extends MapGenerator<D> {
    protected final int radius;
    protected ILongScrambler seedScrambler = ILongScrambler.IDENTITY;

    public RangeMapGenerator(IWorld world, BiomeProvider biomeGen, int radius) {
        super(world, biomeGen);
        this.radius = radius;
    }

    public RangeMapGenerator(IWorld world, int radius) {
        this(world, null, radius);
    }

    @Override
    public void generate(WorldGenRegion region, D data) {
        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();

        IChunk chunk = region.getChunk(cx, cz);

        for(int x = -radius; x <= radius; x++) {
            for(int z = -radius; z <= radius; z++) {
                int ox = cx + x;
                int oz = cz + z;
                rand.setSeed(seedScrambler.scramble(world.getSeed() ^ ox * 58192931923L + oz * 42789215L));
                generateRecursively(region, chunk, cx, cz, ox, oz, data);
            }
        }
    }

    protected abstract void generateRecursively(IWorld world, IChunk chunk, int cx, int cz, int ox, int oz, D data);
}
