/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 22 - 2019
 * Author: rgsw
 */

package modernity.common.generator.decorate.decorator;

import modernity.api.util.IBlockProvider;
import modernity.api.util.MovingBlockPos;
import modernity.common.generator.decorate.plants.IVegetation;
import modernity.common.generator.decorate.plants.PlantLayer;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;

import java.util.ArrayList;
import java.util.Random;

public class PlantDecorator implements IDecorator {
    private static final ThreadLocal<PlantLayer> PLANT_LAYER = ThreadLocal.withInitial( PlantLayer::new );

    private final ArrayList<IVegetation> vegetations = new ArrayList<>();

    private final Heightmap.Type heigtmapType;

    private final int radius;

    public PlantDecorator( Heightmap.Type heigtmapType, int radius ) {
        this.heigtmapType = heigtmapType;
        this.radius = radius;
    }

    @Override
    public void decorate( IWorld world, int cx, int cz, Biome biome, Random rand, ChunkGenerator<?> chunkGenerator ) {
        PlantLayer layer = PLANT_LAYER.get();
        layer.init( cx * 16, cz * 16 );

        for( int rx = - radius; rx <= radius; rx++ ) {
            for( int rz = - radius; rz <= radius; rz++ ) {
                fill( layer, world, rx + cx, rz + cz );
            }
        }

        MovingBlockPos mpos = new MovingBlockPos();
        for( int x = 0; x < 16; x++ ) {
            for( int z = 0; z < 16; z++ ) {
                int gx = cx * 16 + x;
                int gz = cz * 16 + z;
                IBlockProvider provider = layer.getPlant( gx, gz );
                if( provider != null ) {
                    int height = world.getHeight( heigtmapType, gx, gz );
                    provider.provide( world, mpos.setPos( gx, height, gz ), rand );
                }
            }
        }
    }

    private void fill( PlantLayer layer, IWorld world, int cx, int cz ) {
        long posSeed = cx * 371527891L + cz * 5828146L ^ world.getSeed();
        Random local = new Random( posSeed );

        int i = local.nextInt();
        for( IVegetation vegetation : vegetations ) {
            local.setSeed( posSeed * i );
            vegetation.generate( world, layer, cx, cz, local );
            i++;
        }
    }

    public void addVegetation( IVegetation vegetation ) {
        vegetations.add( vegetation );
    }

    public void removeVegetation( IVegetation vegetation ) {
        vegetations.add( vegetation );
    }
}
