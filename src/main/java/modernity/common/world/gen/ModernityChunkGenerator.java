/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 8 - 26 - 2019
 */

package modernity.common.world.gen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.LongSet;
import modernity.api.util.IntArrays;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.structure.MDStructures;
import modernity.common.world.gen.terrain.*;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ExpiringMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.rgsw.noise.FractalOpenSimplex3D;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModernityChunkGenerator implements IChunkGenerator<ModernityGenSettings> {
    private final ModernityGenSettings settings;

    private final World world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;

    private final ModernityTerrainGenerator terrain;
    private final ModernitySurfaceGenerator surface;
    private final ModernityCaveGenerator cave;
    private final ModernityTerrainDecorator decorator;

    private final NoiseBlockGenerator darkrockGen;

    protected final Map<Structure<? extends IFeatureConfig>, Long2ObjectMap<StructureStart>> structureStartCache = Maps.newHashMap();
    protected final Map<Structure<? extends IFeatureConfig>, Long2ObjectMap<LongSet>> structureReferenceCache = Maps.newHashMap();

    public ModernityChunkGenerator( World world, BiomeProvider provider, ModernityGenSettings settings ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );

        this.settings = settings;

        terrain = new ModernityTerrainGenerator( world, provider, settings );
        surface = new ModernitySurfaceGenerator( world, provider, settings );
        cave = new ModernityCaveGenerator( world, provider, settings );
        decorator = new ModernityTerrainDecorator( world, provider, this, settings );

        darkrockGen = new NoiseBlockGenerator(
                new FractalOpenSimplex3D( rand.nextInt(), 43.51234, 4 ).subtract( 0.3 ),
                BlockMatcher.forBlock( MDBlocks.ROCK ),
                MDBlocks.DARKROCK.getDefaultState()
        );
    }

    @Override
    public void makeBase( IChunk chunk ) {
        int cx = chunk.getPos().x;
        int cz = chunk.getPos().z;

        Biome[] biomes = provider.getBiomeBlock( cx * 16, cz * 16, 16, 16 );
        chunk.setBiomes( biomes );

        terrain.generateTerrain( chunk );
        int[] hm = surface.generateSurface( chunk );
        cave.generateCaves( chunk, hm );

        IntArrays.add( hm, - 8 );

        MDStructures.CAVE.addCaves( chunk, cx, cz, hm );

        darkrockGen.generate( chunk );

        chunk.createHeightMap( Heightmap.Type.WORLD_SURFACE_WG, Heightmap.Type.OCEAN_FLOOR_WG );
        chunk.setStatus( ChunkStatus.BASE );
    }

    @Override
    public void carve( WorldGenRegion region, GenerationStage.Carving carvingStage ) {
    }

    @Override
    public void decorate( WorldGenRegion region ) {
        decorator.decorate( region );
    }

    @Override
    public void spawnMobs( WorldGenRegion region ) {
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures( EnumCreatureType creatureType, BlockPos pos ) {
        return Lists.newArrayList();
    }

    @Nullable
    @Override
    public BlockPos findNearestStructure( World worldIn, String name, BlockPos pos, int radius, boolean p_211403_5_ ) {
        return null;
    }

    @Override
    public ModernityGenSettings getSettings() {
        return settings;
    }

    @Override
    public int spawnMobs( World worldIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs ) {
        return 0;
    }

    @Override
    public boolean hasStructure( Biome biome, Structure<? extends IFeatureConfig> structure ) {
        return biome.hasStructure( structure );
    }

    @Nullable
    @Override
    public IFeatureConfig getStructureConfig( Biome biome, Structure<? extends IFeatureConfig> struct ) {
        return biome.getStructureConfig( struct );
    }

    public Long2ObjectMap<StructureStart> getStructureReferenceToStartMap( Structure<? extends IFeatureConfig> struct ) {
        return this.structureStartCache.computeIfAbsent( struct, structure -> Long2ObjectMaps.synchronize( new ExpiringMap<>( 8192, 10000 ) ) );
    }

    public Long2ObjectMap<LongSet> getStructurePositionToReferenceMap( Structure<? extends IFeatureConfig> struct ) {
        return this.structureReferenceCache.computeIfAbsent( struct, structure -> Long2ObjectMaps.synchronize( new ExpiringMap<>( 8192, 10000 ) ) );
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return provider;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public int getGroundHeight() {
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return 256;
    }
}
