/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 12 - 2019
 */

package modernity.common.world.gen.terrain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.FractalOpenSimplex3D;

import modernity.api.util.EMDDimension;
import modernity.api.util.EcoBlockPos;
import modernity.common.biome.BiomeBase;
import modernity.common.biome.MDBiomes;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.ModernityGenSettings;

import java.util.List;
import java.util.Random;

public class ModernitySurfaceGenerator {

    // TODO: Use biome surface builders

    private static final IBlockState GRASS = MDBlocks.DARK_GRASS.getDefaultState();
    private static final IBlockState DIRT = MDBlocks.DARK_DIRT.getDefaultState();
    private static final IBlockState BEDROCK = MDBlocks.MODERN_BEDROCK.getDefaultState();

    private final World world;
    private final Random rand;
    private final BiomeProvider provider;

    private final FractalOpenSimplex3D depthNoise;
    private final ModernityGenSettings settings;

    public ModernitySurfaceGenerator( World world, BiomeProvider provider, ModernityGenSettings settings ) {
        this.world = world;
        long seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );
        this.settings = settings;

        depthNoise = new FractalOpenSimplex3D( rand.nextInt(), settings.getSurfaceNoiseSizeX(), settings.getSurfaceNoiseSizeY(), settings.getSurfaceNoiseSizeZ(), settings.getSurfaceNoiseSizeOctaves() );

        List<BiomeBase> biomes = MDBiomes.getBiomesFor( EMDDimension.SURFACE );
        for( BiomeBase biome : biomes ) {
            biome.getSurfaceGen().init( rand, settings );
        }
    }

    public void generateSurface( IChunk chunk ) {
        int cx = chunk.getPos().x;
        int cz = chunk.getPos().z;
        EcoBlockPos rpos = EcoBlockPos.retain();
        for( int x = 0; x < 16; x++ ) {
            for( int z = 0; z < 16; z++ ) {
                for( int y = 4; y >= 0; y-- ) {
                    rpos.setPos( x, y, z );
                    if( y <= rand.nextInt( 5 ) ) {
                        chunk.setBlockState( rpos, BEDROCK, false );
                    }

                    BiomeBase biome = (BiomeBase) chunk.getBiomes()[ z << 4 | x ];
                    biome.getSurfaceGen().generateSurface( chunk, cx, cz, x, z, rand, biome, depthNoise, rpos, settings );
                }
            }
        }

        rpos.release();
    }
}
