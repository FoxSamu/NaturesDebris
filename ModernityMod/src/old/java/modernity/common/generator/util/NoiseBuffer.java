/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.util;

public class NoiseBuffer {
    private final double[] values;
    private final int xSize;
    private final int ySize;
    private final int zSize;

    public NoiseBuffer(int xSize, int ySize, int zSize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.values = new double[xSize * ySize * zSize];
    }

    public NoiseBuffer(int xSize, int ySize, int zSize, double[] values) {
        this(xSize, ySize, zSize);
        System.arraycopy(values, 0, this.values, 0, Math.min(values.length, this.values.length));
    }

    public int xSize() {
        return xSize;
    }

    public int ySize() {
        return ySize;
    }

    public int zSize() {
        return zSize;
    }

    public int length() {
        return values.length;
    }

    private int index(int x, int y, int z) {
        return (x * zSize + z) * ySize + y;
    }

    public void set(int x, int y, int z, double value) {
        values[index(x, y, z)] = value;
    }

    public double get(int x, int y, int z) {
        return values[index(x, y, z)];
    }

    public void getArray(double[] doubles, int off) {
        System.arraycopy(values, off, doubles, 0, Math.min(values.length - off, doubles.length));
    }

    public void putArray(double[] doubles, int off) {
        System.arraycopy(doubles, 0, values, off, Math.min(values.length - off, doubles.length));
    }

    public double[] getArray() {
        double[] out = new double[length()];
        getArray(out, 0);
        return out;
    }
}
