package modernity.common.world.gen;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.entity.EnumCreatureType;
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

import modernity.common.world.gen.terrain.ModernitySurfaceGenerator;
import modernity.common.world.gen.terrain.ModernityTerrainDecorator;
import modernity.common.world.gen.terrain.ModernityTerrainGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ModernityChunkGenerator implements IChunkGenerator<ModernityGenSettings> {
    private final ModernityGenSettings settings;

    private final World world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;

    private final ModernityTerrainGenerator terrain;
    private final ModernitySurfaceGenerator surface;
    private final ModernityTerrainDecorator decorator;

    public ModernityChunkGenerator( World world, BiomeProvider provider, ModernityGenSettings settings ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );

        this.settings = settings;

        terrain = new ModernityTerrainGenerator( world, provider, settings );
        surface = new ModernitySurfaceGenerator( world, provider, settings );
        decorator = new ModernityTerrainDecorator( world, provider, this );
    }

    @Override
    public void makeBase( IChunk chunk ) {
        int cx = chunk.getPos().x;
        int cz = chunk.getPos().z;

        Biome[] biomes = provider.getBiomeBlock( cx * 16, cz * 16, 16, 16 );
        chunk.setBiomes( biomes );

        terrain.generateTerrain( chunk );
        surface.generateSurface( chunk );
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
    public boolean hasStructure( Biome biomeIn, Structure<? extends IFeatureConfig> structureIn ) {
        return false;
    }

    @Nullable
    @Override
    public IFeatureConfig getStructureConfig( Biome biomeIn, Structure<? extends IFeatureConfig> structureIn ) {
        return null;
    }

    @Override
    public Long2ObjectMap<StructureStart> getStructureReferenceToStartMap( Structure<? extends IFeatureConfig> structureIn ) {
        return new Long2ObjectOpenHashMap<>();
    }

    @Override
    public Long2ObjectMap<LongSet> getStructurePositionToReferenceMap( Structure<? extends IFeatureConfig> structureIn ) {
        return new Long2ObjectOpenHashMap<>();
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
