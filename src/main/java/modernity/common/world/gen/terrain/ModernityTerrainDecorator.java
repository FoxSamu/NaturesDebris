package modernity.common.world.gen.terrain;

import net.minecraft.block.BlockFalling;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;

import java.util.Random;

public class ModernityTerrainDecorator {

    private final World world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;
    private final IChunkGenerator chunkGen;

    private final ThreadLocal<double[]> noiseBuffer = new ThreadLocal<>();

    public ModernityTerrainDecorator( World world, BiomeProvider provider, IChunkGenerator chunkgen ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );
        this.chunkGen = chunkgen;
    }

    public void decorate( WorldGenRegion region ) {
        BlockFalling.fallInstantly = true;
        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();
        int x = cx * 16;
        int z = cz * 16;
        BlockPos cornerPos = new BlockPos( x, 0, z );

        Biome biome = region.getChunk( cx, cz ).getBiomes()[ 7 * 16 + 7 ];
        SharedSeedRandom ssrand = new SharedSeedRandom();
        long seed = ssrand.setDecorationSeed( region.getSeed(), x, z );

        for( GenerationStage.Decoration stage : GenerationStage.Decoration.values() ) {
            biome.decorate( stage, chunkGen, region, seed, ssrand, cornerPos );
        }

        BlockFalling.fallInstantly = false;
    }
}
