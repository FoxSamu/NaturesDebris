/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.map.surface;

import natures.debris.common.biome.ModernityBiome;
import natures.debris.common.generator.map.IMapGenData;
import natures.debris.common.generator.util.BiomeBuffer;
import natures.debris.common.generator.util.NoiseBuffer;
import net.minecraft.world.gen.ChunkGenerator;

public class SurfaceGenData implements IMapGenData {
    private ChunkGenerator<?> generator;

    public ChunkGenerator<?> getGenerator() {
        return generator;
    }

    @Override
    public void init(ChunkGenerator<?> chunkGen) {
        this.generator = chunkGen;
    }

    private final int[] heightmap = new int[256];

    public int[] getHeightmap() {
        return heightmap;
    }



    private BiomeBuffer<ModernityBiome> biomeField;

    public BiomeBuffer<ModernityBiome> initBiomeField(int x, int z, int xs, int zs) {
        if (biomeField == null || biomeField.xSize() < xs || biomeField.zSize() < zs) {
            biomeField = new BiomeBuffer<>(xs, zs);
        }
        biomeField.setPos(x, z);
        return biomeField;
    }

    public BiomeBuffer<ModernityBiome> getBiomeField() {
        return biomeField;
    }



    private NoiseBuffer mainBuffer;

    public NoiseBuffer initMainBuffer(int xs, int ys, int zs) {
        if (mainBuffer == null || mainBuffer.xSize() < xs || mainBuffer.ySize() < ys || mainBuffer.zSize() < zs) {
            mainBuffer = new NoiseBuffer(xs, ys, zs);
        }
        return mainBuffer;
    }

    public NoiseBuffer getMainBuffer() {
        return mainBuffer;
    }



    private NoiseBuffer caveBuffer;

    public NoiseBuffer initCaveBuffer(int xs, int ys, int zs) {
        if (caveBuffer == null || caveBuffer.xSize() < xs || caveBuffer.ySize() < ys || caveBuffer.zSize() < zs) {
            caveBuffer = new NoiseBuffer(xs, ys, zs);
        }
        return caveBuffer;
    }

    public NoiseBuffer getCaveBuffer() {
        return caveBuffer;
    }



    public final HeightData heightData = new HeightData();

    public static final class HeightData {
        public double min;
        public double max;
    }
}
