package modernity.common.world.gen.terrain;

import com.google.common.reflect.TypeToken;
import modernity.api.util.EMDDimension;
import modernity.api.util.MovingBlockPos;
import modernity.common.biome.MDBiomes;
import modernity.common.biome.ModernityBiome;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.MDSurfaceGenSettings;
import modernity.common.world.gen.surface.ISurfaceGenerator;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.FractalPerlin3D;
import net.rgsw.noise.INoise3D;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Random;

/**
 * Generates the surface of the Modernity's surface dimension.
 */
@SuppressWarnings( "unchecked" )
public class MDSurfaceSurfaceGenerator {

    private final IWorld world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider biomeGen;

    private final INoise3D surfaceNoise;

    private final MDSurfaceGenSettings settings;

    private final ThreadLocal<int[]> heightmapLocal = ThreadLocal.withInitial( () -> new int[ 256 ] );

    public MDSurfaceSurfaceGenerator( IWorld world, BiomeProvider biomeGen, MDSurfaceGenSettings settings ) {
        this.world = world;
        this.seed = world.getSeed();
        this.biomeGen = biomeGen;
        this.rand = new Random( seed );

        this.settings = settings;

        this.surfaceNoise = new FractalPerlin3D( rand.nextInt(), 6.348456, 0.52, 6.348456, 6 );

        for( ModernityBiome biome : MDBiomes.getBiomesFor( EMDDimension.SURFACE ) ) {
            Class<? extends ISurfaceGenerator> genClass = biome.getSurfaceGen().getClass();
            TypeToken<? extends ISurfaceGenerator> token = TypeToken.of( genClass );
            TypeToken<ISurfaceGenerator<?>> sgtoken = (TypeToken<ISurfaceGenerator<?>>) token.getSupertype( ISurfaceGenerator.class );
            Type raw = sgtoken.getType();
            ParameterizedType paraType = (ParameterizedType) raw;
            Type[] type = paraType.getActualTypeArguments();

            if( type[ 0 ] == MDSurfaceGenSettings.class ) {
                biome.getSurfaceGen().init( rand, settings );
            }
        }
    }

    /**
     * Builds the surface in the specified chunk.
     * @return A map of the lowest heights of the terrain. This height map limits the generation of caves.
     */
    public int[] buildSurface( IChunk chunk ) {
        MovingBlockPos mpos = new MovingBlockPos();
        int cx = chunk.getPos().x;
        int cz = chunk.getPos().z;

        int[] caveHeightmap = heightmapLocal.get();

        for( int x = 0; x < 16; x++ ) {
            for( int z = 0; z < 16; z++ ) {
                for( int y = 4; y >= 0; y-- ) {
                    mpos.setPos( x, y, z );
                    if( y <= rand.nextInt( 5 ) ) {
                        chunk.setBlockState( mpos, MDBlocks.MODERN_BEDROCK.getDefaultState(), false );
                    }
                }

                mpos.setPos( x + cx * 16, 0, z + cz * 16 );
                Biome biome = chunk.getBiome( mpos );
                if( biome instanceof ModernityBiome ) {
                    ModernityBiome mbiome = (ModernityBiome) biome;

                    Class<? extends ISurfaceGenerator> genClass = mbiome.getSurfaceGen().getClass();
                    TypeToken<? extends ISurfaceGenerator> token = TypeToken.of( genClass );
                    TypeToken<ISurfaceGenerator<?>> sgtoken = (TypeToken<ISurfaceGenerator<?>>) token.getSupertype( ISurfaceGenerator.class );
                    Type raw = sgtoken.getType();
                    ParameterizedType paraType = (ParameterizedType) raw;
                    Type[] type = paraType.getActualTypeArguments();

                    if( type[ 0 ] == MDSurfaceGenSettings.class ) {
                        mbiome.getSurfaceGen().buildSurface( chunk, cx, cz, x, z, rand, mbiome, surfaceNoise, mpos, settings );
                    }
                }


                int caveHeight = 0;
                for( int y = 0; y < 256; y++ ) {
                    mpos.setPos( x, y, z );
                    if( ! chunk.getBlockState( mpos ).getMaterial().blocksMovement() ) {
                        caveHeight = y - 1;
                        break;
                    }
                }
                caveHeightmap[ x + z * 16 ] = caveHeight;
            }
        }
        return caveHeightmap;
    }
}
