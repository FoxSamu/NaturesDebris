package modernity.common.world.gen.terrain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.FractalOpenSimplex3D;

import modernity.api.util.EcoBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.ModernityGenSettings;

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
    }

    public void generateSurface( IChunk chunk ) {
        int cx = chunk.getPos().x;
        int cz = chunk.getPos().z;
        EcoBlockPos rpos = EcoBlockPos.retain();
        for( int x = 0; x < 16; x++ ) {
            for( int z = 0; z < 16; z++ ) {
                int ctrl = 0;
                for( int y = 255; y >= 0; y-- ) {
                    rpos.setPos( x, y, z );
                    if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                        ctrl = - 1;
                    } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                        ctrl = (int) ( 3 + 2 * depthNoise.generate( x + cx * 16, y, z + cz * 16 ) );
                        chunk.setBlockState( rpos, y < settings.getWaterLevel() - 1 ? DIRT : GRASS, false );
                    } else if( ctrl > 0 ) {
                        ctrl--;
                        chunk.setBlockState( rpos, DIRT, false );
                    }
                    if( y < 5 && y <= rand.nextInt( 5 ) ) {
                        chunk.setBlockState( rpos, BEDROCK, false );
                    }
                }
            }
        }

        rpos.release();
    }
}
