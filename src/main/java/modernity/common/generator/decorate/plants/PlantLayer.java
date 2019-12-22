/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.plants;

import modernity.api.util.IBlockProvider;

public class PlantLayer {
    private final IBlockProvider[] plants = new IBlockProvider[ 256 ];

    private int x;
    private int z;

    public IBlockProvider getPlant( int x, int z ) {
        x -= this.x;
        z -= this.z;
        if( x < 0 || x >= 16 || z < 0 || z >= 16 ) {
            return null;
        }

        return plants[ x * 16 + z ];
    }

    public void setPlant( int x, int z, IBlockProvider plant ) {
        x -= this.x;
        z -= this.z;
        if( x < 0 || x >= 16 || z < 0 || z >= 16 ) {
            return;
        }
        plants[ x * 16 + z ] = plant;
    }

    public void init( int x, int z ) {
        for( int i = 0; i < 256; i++ ) {
            plants[ i ] = null;
        }
        this.x = x;
        this.z = z;
    }
}
