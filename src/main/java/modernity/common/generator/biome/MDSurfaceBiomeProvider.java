/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   12 - 24 - 2019
 * Author: rgsw
 */

package modernity.common.generator.biome;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import modernity.api.util.EMDDimension;
import modernity.common.biome.MDBiomes;
import modernity.common.generator.biome.layer.MDLayerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Biome provider that generates the biome layer of the Modernity surface dimension. See {@link
 * MDLayerUtil#buildSurfaceProcedure(long)} for the actual layer building of the surface biome generator.
 */
public class MDSurfaceBiomeProvider extends BiomeProvider {
    private final Layer genBiomeLayer;
    private final Layer fullBiomeLayer;
    private final Biome[] biomes = MDBiomes.getBiomesFor( EMDDimension.SURFACE ).toArray( new Biome[ 0 ] );

    // MAYBE: Remove settings, just pass WorldInfo?
    public MDSurfaceBiomeProvider( MDSurfaceBiomeProviderSettings settings ) {
        WorldInfo info = settings.getWorldInfo();
        Layer[] layers = MDLayerUtil.buildSurfaceProcedure( info.getSeed() );
        genBiomeLayer = layers[ 0 ];
        fullBiomeLayer = layers[ 1 ];
    }

    @Override
    public Biome getBiome( int x, int y ) {
        return fullBiomeLayer.func_215738_a( x, y );
    }

    @Override // 'getGenBiome'
    public Biome func_222366_b( int x, int z ) {
        return genBiomeLayer.func_215738_a( x, z );
    }

    @Override
    public Biome[] getBiomes( int x, int z, int width, int length, boolean cacheFlag ) {
        return fullBiomeLayer.generateBiomes( x, z, width, length );
    }

    @Override
    public Set<Biome> getBiomesInSquare( int centerX, int centerZ, int sideLength ) {
        int xmin = centerX - sideLength >> 2;
        int zmin = centerZ - sideLength >> 2;
        int xmax = centerX + sideLength >> 2;
        int zmax = centerZ + sideLength >> 2;
        int xrange = xmax - xmin + 1;
        int zrange = zmax - zmin + 1;
        Set<Biome> out = Sets.newHashSet();
        Collections.addAll( out, this.genBiomeLayer.generateBiomes( xmin, zmin, xrange, zrange ) );
        return out;
    }

    @Nullable
    @Override
    public BlockPos findBiomePosition( int x, int z, int range, List<Biome> allowedBiomes, Random random ) {
        int xmin = x - range >> 2;
        int zmin = z - range >> 2;
        int xmax = x + range >> 2;
        int zmax = z + range >> 2;
        int xrange = xmax - xmin + 1;
        int zrange = zmax - zmin + 1;
        Biome[] biomes = genBiomeLayer.generateBiomes( xmin, zmin, xrange, zrange );
        BlockPos pos = null;
        int chance = 0;

        for( int i = 0; i < xrange * zrange; ++ i ) {
            int xp = xmin + i % xrange << 2;
            int zp = zmin + i / xrange << 2;
            if( allowedBiomes.contains( biomes[ i ] ) ) {
                if( pos == null || random.nextInt( chance + 1 ) == 0 ) {
                    pos = new BlockPos( xp, 0, zp );
                }

                ++ chance;
            }
        }

        return pos;
    }

    @Override
    public boolean hasStructure( Structure<?> structure ) {
        return hasStructureCache.computeIfAbsent( structure, struct -> {
            for( Biome biome : biomes ) {
                if( biome.hasStructure( struct ) ) {
                    return true;
                }
            }

            return false;
        } );
    }

    @Override
    public Set<BlockState> getSurfaceBlocks() {
        return ImmutableSet.of();
    }

}
