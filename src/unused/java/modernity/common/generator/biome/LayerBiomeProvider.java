/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 04 - 2020
 * Author: rgsw
 */

package modernity.common.generator.biome;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LayerBiomeProvider extends BiomeProvider {
    private final BiomeGenerator baseGenerator;
    private final BiomeGenerator fullGenerator;
    private final Biome[] biomes;

    public LayerBiomeProvider( LayerBiomeProviderSettings settings ) {
        BiomeGenerator[] generators = settings.getGenerators();
        baseGenerator = generators[ 0 ];
        fullGenerator = generators[ 1 ];
        biomes = settings.getBiomes();
    }

    @Override
    public Biome getBiome( int x, int z ) {
        return fullGenerator.getBiome( x, z );
    }

    @Override
    public Biome func_222366_b( int x, int z ) {
        return baseGenerator.getBiome( x, z );
    }

    @Override
    public Biome[] getBiomes( int x, int z, int width, int length, boolean cacheFlag ) {
        return fullGenerator.getBiomes( x, z, width, length );
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
        Collections.addAll( out, baseGenerator.getBiomes( xmin, zmin, xrange, zrange ) );
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
        Biome[] biomes = baseGenerator.getBiomes( xmin, zmin, xrange, zrange );
        BlockPos pos = null;
        int chance = 0;

        for( int i = 0; i < xrange * zrange; ++ i ) {
            int xp = xmin + i % xrange << 2;
            int zp = zmin + i / xrange << 2;
            if( allowedBiomes.contains( biomes[ i ] ) ) {
                if( pos == null || random.nextInt( chance + 1 ) == 0 ) {
                    pos = new BlockPos( xp, 0, zp );
                }

                chance++;
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
        if( topBlocksCache.isEmpty() ) {
            for( Biome biome : biomes ) {
                topBlocksCache.add( biome.getSurfaceBuilderConfig().getTop() );
            }
        }

        return topBlocksCache;
    }
}
