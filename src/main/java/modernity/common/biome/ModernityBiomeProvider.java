/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 5 - 2019
 */

package modernity.common.biome;

import com.google.common.collect.Sets;
import modernity.common.world.gen.layer.MDLayerUtil;
import modernity.common.world.gen.structure.MDStructures;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ModernityBiomeProvider extends BiomeProvider {
    private final BiomeCache cache = new BiomeCache( this );
    private final GenLayer genBiomes;
    /** A GenLayer containing a factory to generate biome arrays for {@llink #getBiomes(int, int, int, int, boolean)} */
    private final GenLayer biomeFactoryLayer;
    private final Biome[] biomes = {
            MDBiomes.MEADOW,
            MDBiomes.FOREST,
            MDBiomes.WATERLANDS
    };

    public ModernityBiomeProvider( WorldInfo info ) {
//        GenLayer[] layers = LayerUtil.buildOverworldProcedure( info.getSeed(), info.getTerrainType(), settings );
        GenLayer[] layers = MDLayerUtil.buildModernityProcedure( info.getSeed() );
        this.genBiomes = layers[ 0 ];
        this.biomeFactoryLayer = layers[ 1 ];
    }

    @Nullable
    public Biome getBiome( BlockPos pos, @Nullable Biome defaultBiome ) {
        return this.cache.getBiome( pos.getX(), pos.getZ(), defaultBiome );
    }

    public Biome[] getBiomes( int startX, int startZ, int xSize, int zSize ) {
        return this.genBiomes.generateBiomes( startX, startZ, xSize, zSize, Biomes.DEFAULT );
    }

    public Biome[] getBiomes( int x, int z, int width, int length, boolean cacheFlag ) {
        return cacheFlag && width == 16 && length == 16 && ( x & 15 ) == 0 && ( z & 15 ) == 0 ? this.cache.getCachedBiomes( x, z ) : this.biomeFactoryLayer.generateBiomes( x, z, width, length, Biomes.DEFAULT );
    }

    public Set<Biome> getBiomesInSquare( int centerX, int centerZ, int sideLength ) {
        int x1 = centerX - sideLength >> 2;
        int z1 = centerZ - sideLength >> 2;
        int x2 = centerX + sideLength >> 2;
        int z2 = centerZ + sideLength >> 2;
        int rangeX = x2 - x1 + 1;
        int rangeZ = z2 - z1 + 1;
        Set<Biome> biomes = Sets.newHashSet();
        Collections.addAll( biomes, this.genBiomes.generateBiomes( x1, z1, rangeX, rangeZ, null ) );
        return biomes;
    }

    @Nullable
    public BlockPos findBiomePosition( int x, int z, int range, List<Biome> allowedBiomes, Random random ) {
        int x1 = x - range >> 2;
        int z1 = z - range >> 2;
        int x2 = x + range >> 2;
        int z2 = z + range >> 2;
        int rangeX = x2 - x1 + 1;
        int rangeZ = z2 - z1 + 1;
        Biome[] biomes = this.genBiomes.generateBiomes( x1, z1, rangeX, rangeZ, null );
        BlockPos pos = null;
        int found = 0;

        for( int i = 0; i < rangeX * rangeZ; ++ i ) {
            int xp = x1 + i % rangeX << 2;
            int zp = z1 + i / rangeX << 2;
            if( allowedBiomes.contains( biomes[ i ] ) ) {
                if( pos == null || random.nextInt( found + 1 ) == 0 ) {
                    pos = new BlockPos( xp, 0, zp );
                }

                ++ found;
            }
        }

        return pos;
    }

    public boolean hasStructure( Structure<?> structure ) {
        if( structure == MDStructures.CAVE ) return true;
        return this.hasStructureCache.computeIfAbsent( structure, struct -> {
            for( Biome biome : this.biomes ) {
                if( biome.hasStructure( struct ) ) {
                    return true;
                }
            }

            return false;
        } );
    }

    public Set<IBlockState> getSurfaceBlocks() {
//        if( this.topBlocksCache.isEmpty() ) {
//            for( Biome biome : this.biomes ) {
//                this.topBlocksCache.add( biome.getSurfaceBuilderConfig().getTop() );
//            }
//        }

        return this.topBlocksCache;
    }

    public void tick() {
        this.cache.cleanupCache();
    }
}
