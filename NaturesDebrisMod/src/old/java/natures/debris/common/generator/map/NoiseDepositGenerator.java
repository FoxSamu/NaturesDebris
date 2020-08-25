/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.generator.map;

import natures.debris.generic.util.MovingBlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;

public abstract class NoiseDepositGenerator<D extends IMapGenData> extends MapGenerator<D> {
    private final ThreadLocal<double[]> noiseBufferLocal = ThreadLocal.withInitial(() -> new double[9 * 9 * 129]);

    public NoiseDepositGenerator(IWorld world) {
        super(world);
    }

    /**
     * Generates a noise buffer and interpolates between it. The resulting value is used to generate blocks.
     */
    @Override
    public void generate(WorldGenRegion region, D data) {
        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();
        double[] buffer = fillNoiseBuffer(cx * 16, cz * 16, data);
        MovingBlockPos rpos = new MovingBlockPos();
        for (int x = 0; x < 8; x++) {
            int i0 = x * 9;
            int i1 = (x + 1) * 9;

            for (int z = 0; z < 8; z++) {
                int i00 = (i0 + z) * 129;
                int i01 = (i0 + z + 1) * 129;
                int i10 = (i1 + z) * 129;
                int i11 = (i1 + z + 1) * 129;

                for (int y = 0; y < 128; y++) {
                    // Values
                    double noise000 = buffer[i00 + y];
                    double noise001 = buffer[i01 + y];
                    double noise100 = buffer[i10 + y];
                    double noise101 = buffer[i11 + y];
                    double noise010 = buffer[i00 + y + 1];
                    double noise011 = buffer[i01 + y + 1];
                    double noise110 = buffer[i10 + y + 1];
                    double noise111 = buffer[i11 + y + 1];

                    // Step along X axis
                    double delta00 = (noise100 - noise000) * 0.5D;
                    double delta01 = (noise101 - noise001) * 0.5D;
                    double delta10 = (noise110 - noise010) * 0.5D;
                    double delta11 = (noise111 - noise011) * 0.5D;

                    double current00 = noise000;
                    double current01 = noise001;
                    double current10 = noise010;
                    double current11 = noise011;

                    // Step X axis
                    for (int xo = 0; xo < 2; xo++) {
                        double current0 = current00;
                        double current1 = current10;

                        // Step along Z axis
                        double delta0 = (current01 - current00) * 0.5D;
                        double delta1 = (current11 - current10) * 0.5D;

                        int bx = x * 2 + xo + cx * 16;

                        // Step Z axis
                        for (int zo = 0; zo < 2; zo++) {
                            // Step along Y axis
                            double delta = (current1 - current0) * 0.5D;

                            double current = current1 - delta;

                            int bz = z * 2 + zo + cz * 16;

                            // Step Y axis
                            for (int yo = 0; yo < 1; yo++) {
                                double noise = current += delta;

                                int by = y + yo;

                                rpos.setPos(bx, by, bz);

                                place(region, rpos, noise, data);
                            }

                            current0 += delta0;
                            current1 += delta1;
                        }

                        current00 += delta00;
                        current01 += delta01;
                        current10 += delta10;
                        current11 += delta11;
                    }
                }
            }
        }
    }

    /**
     * Creates the actual noise buffer (caching the noise array in a thread local field for reusing)
     *
     * @param cx The chunk x (chunk coords)
     * @param cz The chunk z (chunk coords)
     * @return The generated noise buffer.
     */
    private double[] fillNoiseBuffer(int cx, int cz, D data) {
        double[] buffer = noiseBufferLocal.get();
        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 9; z++) {
                for (int y = 0; y < 129; y++) {
                    int index = (x * 9 + z) * 129 + y;
                    int bx = cx + x * 2;
                    int bz = cz + z * 2;
                    buffer[index] = generateNoise(bx, y, bz, data);
                }
            }
        }
        return buffer;
    }

    /**
     * Generates noise for a specific position.
     */
    protected abstract double generateNoise(int x, int y, int z, D data);


    protected abstract void place(WorldGenRegion region, MovingBlockPos pos, double noise, D data);
}
