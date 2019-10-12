package modernity.common.world.gen;

import modernity.api.util.IntArrays;
import modernity.common.world.gen.map.DarkrockGenerator;
import modernity.common.world.gen.structure.MDStructures;
import modernity.common.world.gen.terrain.MDSurfaceCaveGenerator;
import modernity.common.world.gen.terrain.MDSurfaceDecorator;
import modernity.common.world.gen.terrain.MDSurfaceSurfaceGenerator;
import modernity.common.world.gen.terrain.MDSurfaceTerrainGenerator;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;

public class MDSurfaceChunkGenerator extends ChunkGenerator<MDSurfaceGenSettings> {
    private final MDSurfaceTerrainGenerator terrain;
    private final MDSurfaceSurfaceGenerator surface;
    private final MDSurfaceCaveGenerator cave;
    private final MDSurfaceDecorator decorator;

    private final DarkrockGenerator darkrockGenerator;

    public MDSurfaceChunkGenerator( IWorld world, BiomeProvider biomeProvider, MDSurfaceGenSettings settings ) {
        super( world, biomeProvider, settings );

        terrain = new MDSurfaceTerrainGenerator( world, biomeProvider, settings );
        surface = new MDSurfaceSurfaceGenerator( world, biomeProvider, settings );
        cave = new MDSurfaceCaveGenerator( world, biomeProvider, settings );
        decorator = new MDSurfaceDecorator( world, biomeProvider, this, settings );
        darkrockGenerator = new DarkrockGenerator( world );
    }

    public MDSurfaceChunkGenerator( IWorld world, BiomeProvider provider ) {
        this( world, provider, new MDSurfaceGenSettings() );
    }

    @Override
    public void generateSurface( IChunk chunk ) {
    }

    @Override
    public int getGroundHeight() {
        return 72;
    }

    @Override
    public void makeBase( IWorld world, IChunk chunk ) {
        int cx = chunk.getPos().x;
        int cz = chunk.getPos().z;

        terrain.generateTerrain( chunk );
        int[] hm = surface.buildSurface( chunk );
        cave.generateCaves( chunk, hm );

        IntArrays.add( hm, - 8 );

        MDStructures.CAVE.addCaves( this, chunk, cx, cz, hm, world.getSeed() );

        WorldGenRegion region = (WorldGenRegion) world;
        darkrockGenerator.generate( region );
    }

    @Override
    public void carve( IChunk chunk, GenerationStage.Carving carvingSettings ) {
    }

    @Override
    public void decorate( WorldGenRegion region ) {
        decorator.decorate( region );
    }

    public int getHeight( int x, int z, Heightmap.Type type ) {
        return world.getHeight( type, x, z );
    }

    @Override
    public int func_222529_a( int x, int z, Heightmap.Type type ) {
        return getHeight( x, z, type );
    }

    public static class Factory implements IChunkGeneratorFactory<MDSurfaceGenSettings, MDSurfaceChunkGenerator> {

        @Override
        public MDSurfaceChunkGenerator create( World world, BiomeProvider biomeProvider, MDSurfaceGenSettings settings ) {
            return new MDSurfaceChunkGenerator( world, biomeProvider, settings );
        }
    }
}
