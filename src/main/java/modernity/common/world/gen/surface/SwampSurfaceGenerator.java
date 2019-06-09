package modernity.common.world.gen.surface;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.IChunk;
import net.rgsw.noise.FractalOpenSimplex2D;
import net.rgsw.noise.FractalOpenSimplex3D;

import modernity.api.util.EcoBlockPos;
import modernity.common.biome.BiomeBase;
import modernity.common.block.MDBlocks;
import modernity.common.world.gen.ModernityGenSettings;

import java.util.Random;

public class SwampSurfaceGenerator implements ISurfaceGenerator<ModernityGenSettings> {

    private static final IBlockState GRASS = MDBlocks.DARK_GRASS.getDefaultState();
    private static final IBlockState DIRT = MDBlocks.DARK_DIRT.getDefaultState();

    private FractalOpenSimplex2D marshNoise;
    private FractalOpenSimplex2D marshGroupNoise;

    @Override
    public void init( Random rand, ModernityGenSettings settings ) {
        marshNoise = new FractalOpenSimplex2D( rand.nextInt(), 3.26224, 3 );
        marshGroupNoise = new FractalOpenSimplex2D( rand.nextInt(), 31.46233, 3 );
    }

    @Override
    public void generateSurface( IChunk chunk, int cx, int cz, int x, int z, Random rand, BiomeBase biome, FractalOpenSimplex3D surfaceNoise, EcoBlockPos rpos, ModernityGenSettings settings ) {
        int ctrl = 0;
        for( int y = 255; y >= 0; y-- ) {
            rpos.setPos( x, y, z );
            if( ctrl >= 0 && ! chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = - 1;
            } else if( ctrl == - 1 && chunk.getBlockState( rpos ).getMaterial().blocksMovement() ) {
                ctrl = (int) ( 3 + 2 * surfaceNoise.generate( x + cx * 16, y, z + cz * 16 ) );

                if( y == settings.getWaterLevel() - 2 ) {
                    double groupNoise = marshGroupNoise.generateMultiplied( cx * 16 + x, cz * 16 + z, 8 ) + 1;
                    if( groupNoise > 0 ) {
                        double noise = marshNoise.generateMultiplied( cx * 16 + x, cz * 16 + z, 8 );
                        if( noise > 0 ) {
                            rpos.moveUp();
                            chunk.setBlockState( rpos, GRASS, false );
                            rpos.moveDown();
                        }
                    }
                }

                chunk.setBlockState( rpos, y < settings.getWaterLevel() - 1 ? DIRT : GRASS, false );
            } else if( ctrl > 0 ) {
                ctrl--;
                chunk.setBlockState( rpos, DIRT, false );
            }
        }
    }
}
