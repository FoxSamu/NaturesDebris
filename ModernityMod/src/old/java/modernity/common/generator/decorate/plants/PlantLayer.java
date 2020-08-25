/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator.decorate.plants;

import modernity.common.generator.blocks.IBlockGenerator;

public class PlantLayer {
    private final IBlockGenerator[] plants = new IBlockGenerator[256];

    private int x;
    private int z;

    public IBlockGenerator getPlant(int x, int z) {
        x -= this.x;
        z -= this.z;
        if(x < 0 || x >= 16 || z < 0 || z >= 16) {
            return null;
        }

        return plants[x * 16 + z];
    }

    public void setPlant(int x, int z, IBlockGenerator plant) {
        x -= this.x;
        z -= this.z;
        if(x < 0 || x >= 16 || z < 0 || z >= 16) {
            return;
        }
        plants[x * 16 + z] = plant;
    }

    public void init(int x, int z) {
        for(int i = 0; i < 256; i++) {
            plants[i] = null;
        }
        this.x = x;
        this.z = z;
    }
}
