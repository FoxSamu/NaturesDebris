package modernity.common.world.gen.terrain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.FractalOpenSimplex3D;

import modernity.api.util.EcoBlockPos;
import modernity.common.block.MDBlocks;

import java.util.Random;

public class ModernitySurfaceGenerator {

    private static final double DEPTH_NOISE_SIZE_H = 28.733918;
    private static final double DEPTH_NOISE_SIZE_V = 1.4252741;

    private static final IBlockState GRASS = MDBlocks.GRASS.getDefaultState();
    private static final IBlockState DIRT = MDBlocks.DIRT.getDefaultState();

    private final World world;
    private final long seed;
    private final Random rand;
    private final BiomeProvider provider;

    private final FractalOpenSimplex3D depthNoise;

    public ModernitySurfaceGenerator( World world, BiomeProvider provider ) {
        this.world = world;
        this.seed = world.getSeed();
        this.provider = provider;
        this.rand = new Random( seed );

        depthNoise = new FractalOpenSimplex3D( rand.nextInt(), DEPTH_NOISE_SIZE_H, DEPTH_NOISE_SIZE_V, DEPTH_NOISE_SIZE_H, 4 );
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
                        chunk.setBlockState( rpos, GRASS, false );
                    } else if( ctrl > 0 ) {
                        ctrl--;
                        chunk.setBlockState( rpos, DIRT, false );
                    }
                }
            }
        }

        rpos.release();
    }
}
