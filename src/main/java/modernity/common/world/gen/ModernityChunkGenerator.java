package modernity.common.world.gen;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;

import modernity.common.world.gen.terrain.ModernityTerrainGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ModernityChunkGenerator implements IChunkGenerator<ModernityChunkGenSettings> {
    private final ModernityChunkGenSettings settings = new ModernityChunkGenSettings();

    private final World world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;

    private final ModernityTerrainGenerator terrain;

    public ModernityChunkGenerator( World world, BiomeProvider provider ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );

        terrain = new ModernityTerrainGenerator( world, provider );
    }

    @Override
    public void makeBase( IChunk chunk ) {
        terrain.generateTerrain( chunk );
    }

    @Override
    public void carve( WorldGenRegion region, GenerationStage.Carving carvingStage ) {

    }

    @Override
    public void decorate( WorldGenRegion region ) {

    }

    @Override
    public void spawnMobs( WorldGenRegion region ) {

    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures( EnumCreatureType creatureType, BlockPos pos ) {
        return null;
    }

    @Nullable
    @Override
    public BlockPos findNearestStructure( World worldIn, String name, BlockPos pos, int radius, boolean p_211403_5_ ) {
        return null;
    }

    @Override
    public ModernityChunkGenSettings getSettings() {
        return null;
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
        return null;
    }

    @Override
    public Long2ObjectMap<LongSet> getStructurePositionToReferenceMap( Structure<? extends IFeatureConfig> structureIn ) {
        return null;
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return null;
    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public int getGroundHeight() {
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return 0;
    }
}
