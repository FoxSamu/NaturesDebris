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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.Set;

public class LayerBiomeProvider extends BiomeProvider {
    private final BiomeGenerator layerGenerator;
    private final Biome[] biomes;

    public LayerBiomeProvider( LayerBiomeProviderSettings settings ) {
        super( Sets.newHashSet( settings.getBiomes() ) );
        BiomeGenerator[] generators = settings.getGenerators();
        layerGenerator = generators[ 0 ];
        biomes = settings.getBiomes();
    }

    @Override
    public Biome getNoiseBiome( int x, int y, int z ) {
        return layerGenerator.getBiome( x, z );
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
