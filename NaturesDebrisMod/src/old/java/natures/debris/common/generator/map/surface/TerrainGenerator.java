/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.map.surface;

import modernity.api.util.math.MathUtil;
import natures.debris.common.biome.ModernityBiome;
import natures.debris.common.blockold.MDNatureBlocks;
import natures.debris.common.generator.map.MapGenerator;
import natures.debris.common.generator.util.BiomeBuffer;
import natures.debris.common.generator.util.BiomeMetrics;
import natures.debris.common.generator.util.NoiseBuffer;
import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.WorldGenRegion;
import net.rgsw.noise.FractalPerlin2D;
import net.rgsw.noise.INoise2D;
import net.rgsw.noise.INoise3D;
import net.rgsw.noise.InverseFractalPerlin3D;

import static natures.debris.common.generator.MurkSurfaceGeneration.*;

/**
 * Generates the main terrain shapes of the Modernity's surface dimension using only rock.
 */
public class TerrainGenerator extends MapGenerator<SurfaceGenData> {
    private static final BlockState AIR = Blocks.AIR.getDefaultState();
    private static final BlockState WATER = MDNatureBlocks.MURKY_WATER.getDefaultState();
    private static final BlockState ROCK = MDNatureBlocks.ROCK.getDefaultState();

    private static final int SEGMENTS_X = 4;                                    // Amount of segments along x-axis
    private static final int SEGMENTS_Y = 32;                                   // Amount of segments along y-axis
    private static final int SEGMENTS_Z = 4;                                    // Amount of semgents along z-axis

    private static final int SEG_SIZE_X = 16 / SEGMENTS_X;                      // The size of a segment along x-axis
    private static final int SEG_SIZE_Y = 256 / SEGMENTS_Y;                     // The size of a segment along y-axis
    private static final int SEG_SIZE_Z = 16 / SEGMENTS_Z;                      // The size of a semgent along z-axis

    private static final int BUFF_SIZE_X = SEGMENTS_X + 1;                      // The x-size of the noise buffer
    private static final int BUFF_SIZE_Y = SEGMENTS_Y + 1;                      // The y-size of the noise buffer
    private static final int BUFF_SIZE_Z = SEGMENTS_Z + 1;                      // The z-size of the noise buffer

    private static final double MAIN_NOISE_SCALE_XZ = 1 / (4 * 684.412);      // The scale of main noise along x and z
    private static final double MAIN_NOISE_SCALE_Y = 1 / (8 * 684.412);       // The scale of main noise along y

    private static final double MIX_NOISE_SCALE_XZ = 1 / (4 * 4.277);         // The scale of mix noise along x and z
    private static final double MIX_NOISE_SCALE_Y = 1 / (8 * 4.277);          // The scale of mix noise along y

    private static final double DEPTH_NOISE_SCALE = 16;                         // The scale of depth noise

    private static final double MAIN_NOISE_MULTIPLIER = 6;                      // Multiplier of the main noise values
    private static final double MIX_NOISE_MULTIPLIER = 4;                       // Multiplier of the mix noise values
    private static final double DEPTH_NOISE_MULTIPLIER = 4;                     // Multiplier of the depth noise values

    private static final float[] BIOME_WEIGHTS;                                 // Biome weight lookup table

    static {
        BIOME_WEIGHTS = new float[BIOME_MIX_DIAMETER * BIOME_MIX_DIAMETER];

        for (int x = -BIOME_MIX_RADIUS; x <= BIOME_MIX_RADIUS; ++x) {
            for (int z = -BIOME_MIX_RADIUS; z <= BIOME_MIX_RADIUS; ++z) {
                double dx = (double) x / BIOME_MIX_RADIUS * 2;
                double dz = (double) z / BIOME_MIX_RADIUS * 2;
                float weight = 10 / MathHelper.sqrt(dx * dx + dz * dz + 0.2);
                BIOME_WEIGHTS[x + BIOME_MIX_RADIUS + (z + BIOME_MIX_RADIUS) * BIOME_MIX_DIAMETER] = weight;
            }
        }
    }


    private final INoise3D noiseA;                          // Main noise A
    private final INoise3D noiseB;                          // Main noise B
    private final INoise3D mixNoise;                        // Mixing noise
    private final INoise2D depthNoise;                      // Depth noise


    public TerrainGenerator(IWorld world, BiomeProvider biomeGen) {
        super(world, biomeGen);

        noiseA = new InverseFractalPerlin3D(rand.nextInt(), MAIN_NOISE_SCALE_XZ, MAIN_NOISE_SCALE_Y, MAIN_NOISE_SCALE_XZ, 16);
        noiseB = new InverseFractalPerlin3D(rand.nextInt(), MAIN_NOISE_SCALE_XZ, MAIN_NOISE_SCALE_Y, MAIN_NOISE_SCALE_XZ, 16);
        mixNoise = new InverseFractalPerlin3D(rand.nextInt(), MIX_NOISE_SCALE_XZ, MIX_NOISE_SCALE_Y, MIX_NOISE_SCALE_XZ, 8);

        depthNoise = new FractalPerlin2D(rand.nextInt(), DEPTH_NOISE_SCALE, 16);
    }

    /**
     * Generates the terrain in the specified chunk.
     *
     * @param region The chunk region to generate terrain in
     * @param data   A generator data instance
     */
    @Override
    public void generate(WorldGenRegion region, SurfaceGenData data) {
        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();

        NoiseBuffer buffer = fillNoiseBuffer(cx, cz, data);

        MovingBlockPos rpos = new MovingBlockPos();
        for (int x0 = 0; x0 < SEGMENTS_X; x0++) {
            int x1 = x0 + 1;

            for (int z0 = 0; z0 < SEGMENTS_Z; z0++) {
                int z1 = z0 + 1;

                for (int y0 = 0; y0 < SEGMENTS_Y; y0++) {
                    int y1 = y0 + 1;

                    // Noise values
                    double noise00 = buffer.get(x0, y0, z0);
                    double noise01 = buffer.get(x0, y0, z1);
                    double noise10 = buffer.get(x1, y0, z0);
                    double noise11 = buffer.get(x1, y0, z1);

                    // Interpolation deltas
                    double delta00 = (buffer.get(x0, y1, z0) - noise00) / SEG_SIZE_Y;
                    double delta01 = (buffer.get(x0, y1, z1) - noise01) / SEG_SIZE_Y;
                    double delta10 = (buffer.get(x1, y1, z0) - noise10) / SEG_SIZE_Y;
                    double delta11 = (buffer.get(x1, y1, z1) - noise11) / SEG_SIZE_Y;

                    for (int y = 0; y < SEG_SIZE_Y; y++) {

                        // Noise values
                        double noise0 = noise00;
                        double noise1 = noise01;

                        // Interpolation deltas
                        double delta0 = (noise10 - noise00) / SEG_SIZE_X;
                        double delta1 = (noise11 - noise01) / SEG_SIZE_X;

                        for (int x = 0; x < SEG_SIZE_X; x++) {

                            // Noise values
                            double noise = noise0;

                            // Interpolation deltas
                            double delta = (noise1 - noise0) / SEG_SIZE_Z;

                            for (int z = 0; z < SEG_SIZE_Z; z++) {

                                int posx = x + x0 * SEG_SIZE_X + cx * 16;
                                int posy = y + y0 * SEG_SIZE_Y;
                                int posz = z + z0 * SEG_SIZE_Z + cz * 16;
                                rpos.setPos(posx, posy, posz);

                                placeBlock(region, rpos, noise);

                                // Interpolation
                                noise += delta;
                            }

                            // Interpolation
                            noise0 += delta0;
                            noise1 += delta1;
                        }

                        // Interpolation
                        noise00 += delta00;
                        noise01 += delta01;
                        noise10 += delta10;
                        noise11 += delta11;
                    }
                }
            }
        }
    }

    /**
     * Generates a block in the world region, based on the generated noise value.
     *
     * @param region The world region to place the block in
     * @param rpos   The position to generate at
     * @param noise  The noise value
     */
    private void placeBlock(WorldGenRegion region, MovingBlockPos rpos, double noise) {
        BlockState state = AIR;

        if (rpos.getY() < MAIN_HEIGHT) {
            state = WATER;
        }

        if (noise > 0) {
            state = ROCK;
        }

        region.setBlockState(rpos, state, 2);
    }

    /**
     * Get a list of biomes in the specified region.
     *
     * @param cx   Chunk X
     * @param cz   Chunk Z
     * @param data A generator data instance
     */
    private void computeBiomes(int cx, int cz, SurfaceGenData data) {
        int x = cx * SEGMENTS_X;
        int z = cz * SEGMENTS_Z;

        int xSize = BUFF_SIZE_X + BIOME_MIX_RADIUS * 2;
        int zSize = BUFF_SIZE_Z + BIOME_MIX_RADIUS * 2;

        int xMax = BUFF_SIZE_X + BIOME_MIX_RADIUS;
        int zMax = BUFF_SIZE_Z + BIOME_MIX_RADIUS;

        int min = -BIOME_MIX_RADIUS;

        BiomeBuffer<ModernityBiome> biomes = data.initBiomeField(min, min, xSize, zSize);
        for (int xi = min; xi < xMax; xi++) {
            int xp = x + xi;
            for (int zi = min; zi < zMax; zi++) {
                int zp = z + zi;
                biomes.set(xi, zi, (ModernityBiome) biomeGen.getNoiseBiome(xp, 0, zp));
            }
        }
    }

    /**
     * Generates the depth noise value and applies some transformation. This results in a separation between lower and
     * higher areas.
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return The computed depth noise value
     */
    private double genDepthNoise(int x, int z) {
        double dpt = depthNoise.generateMultiplied(x, z, DEPTH_NOISE_MULTIPLIER);
        if (dpt < 0) {
            // Higher, rougher area's: keep rougness of depth noise
            dpt *= -1;
            dpt /= 4;
        } else {
            // Lower, flatter area's: extrapolate and clamp to 4 to make flat
            dpt *= 15;
            if (dpt > 4) {
                dpt = 4;
            }
        }

        dpt -= 3;
        dpt /= 4;
        return -dpt; // Reverse because lower area's are high values and vice versa
    }

    /**
     * Generates the noise values of the two main noise fields and mixes them using the mixing noise field.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return The mixed noise value
     */
    private double genMixedNoise(int x, int y, int z) {
        double a = noiseA.generateMultiplied(x, y, z, MAIN_NOISE_MULTIPLIER);
        double b = noiseB.generateMultiplied(x, y, z, MAIN_NOISE_MULTIPLIER);
        double mix = mixNoise.generateMultiplied(x, y, z, MIX_NOISE_MULTIPLIER);

        // Interpolate to [0, 1]
        mix = MathUtil.unlerp(-1, 1, mix);

        return MathUtil.lerp(a, b, MathUtil.clamp(mix, 0, 1));
    }

    /**
     * Looks up the biome weight for the specified offset coordinate from the biome weight array
     *
     * @param x Offset X
     * @param z Offset Z
     * @return The looked-up biome weight
     */
    private static float getBiomeWeight(int x, int z) {
        return BIOME_WEIGHTS[x + BIOME_MIX_RADIUS + (z + BIOME_MIX_RADIUS) * BIOME_MIX_DIAMETER];
    }

    /**
     * Computes the height values for the specified coordinates and stores them in the generator data object.
     *
     * @param x    Chunk-local X
     * @param z    Chunk-local Z
     * @param cx   Chunk X
     * @param cz   Chunk Z
     * @param data A generator data instance
     */
    private void computeHeights(int x, int z, int cx, int cz, SurfaceGenData data) {
        BiomeBuffer<ModernityBiome> biomes = data.getBiomeField();

        double scl = 0;
        double dpt = 0;
        double var = 0;
        double tot = 0;

        ModernityBiome centerBiome = biomes.get(x, z);
        double centerDepth = centerBiome.metrics().depth;

        for (int x1 = -BIOME_MIX_RADIUS; x1 <= BIOME_MIX_RADIUS; x1++) {
            for (int z1 = -BIOME_MIX_RADIUS; z1 <= BIOME_MIX_RADIUS; z1++) {
                ModernityBiome biome = biomes.get(x + x1, z + z1);
                BiomeMetrics metrics = biome.metrics();

                double wgt = getBiomeWeight(x1, z1);
                if (metrics.depth > centerDepth) {
                    wgt /= 2;
                }

                wgt *= metrics.blendWeight;

                var += metrics.variation * wgt;
                scl += metrics.scale * wgt;
                dpt += metrics.depth * wgt;
                tot += wgt;
            }
        }

        scl /= tot * SEG_SIZE_Y;
        dpt /= tot;
        var /= tot * SEG_SIZE_Y;

        dpt += MAIN_HEIGHT;
        dpt /= 8;

        double depthNoise = genDepthNoise(x + cx * SEGMENTS_X, z + cz * SEGMENTS_Z) * var;
        dpt += depthNoise;

        double minh = dpt - scl;
        double maxh = dpt + scl;

        data.heightData.min = minh;
        data.heightData.max = maxh;
    }

    /**
     * Fills the noise buffer for a specific chunk.
     *
     * @param cx   Chunk X
     * @param cz   Chunk Z
     * @param data A generator data instance
     * @return The filled noise buffer
     */
    private NoiseBuffer fillNoiseBuffer(int cx, int cz, SurfaceGenData data) {
        computeBiomes(cx, cz, data);

        NoiseBuffer buff = data.initMainBuffer(BUFF_SIZE_X, BUFF_SIZE_Y, BUFF_SIZE_Z);
        for (int x = 0; x < BUFF_SIZE_X; x++) {
            for (int z = 0; z < BUFF_SIZE_Z; z++) {

                computeHeights(x, z, cx, cz, data);

                double minh = data.heightData.min;
                double maxh = data.heightData.max;

                for (int y = 0; y < BUFF_SIZE_Y; y++) {
                    double density = MathUtil.lerp(-1, 1, MathUtil.unlerp(minh, maxh, y));
                    double noise = genMixedNoise(x + cx * SEGMENTS_X, y, z + cz * SEGMENTS_Z) - density;
                    buff.set(x, y, z, noise);
                }
            }
        }
        return buff;
    }
}
