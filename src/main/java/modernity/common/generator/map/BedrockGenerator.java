/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 11 - 2020
 * Author: rgsw
 */

package modernity.common.generator.map;

import modernity.api.util.IIntScrambler;
import modernity.api.util.MovingBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.WorldGenRegion;
import net.rgsw.noise.Hash;

public class BedrockGenerator extends MapGenerator<IMapGenData> {

    private final int minHeight;
    private final int maxHeight;
    private final int diff;
    private final boolean ceiling;

    private final int seed;

    private final BlockState block;

    public BedrockGenerator( IWorld world, int minHeight, int maxHeight, boolean ceiling, IIntScrambler scrambler, BlockState block ) {
        super( world );
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.ceiling = ceiling;

        this.seed = scrambler.scramble( rand.nextInt() );
        this.block = block;

        this.diff = maxHeight - minHeight + 1;
    }

    public BedrockGenerator( IWorld world, int minHeight, int maxHeight, boolean ceiling, BlockState block ) {
        this( world, minHeight, maxHeight, ceiling, IIntScrambler.IDENTITY, block );
    }

    public BedrockGenerator( IWorld world, int minHeight, int maxHeight, boolean ceiling, IIntScrambler scrambler, Block block ) {
        this( world, minHeight, maxHeight, ceiling, scrambler, block.getDefaultState() );
    }

    public BedrockGenerator( IWorld world, int minHeight, int maxHeight, boolean ceiling, Block block ) {
        this( world, minHeight, maxHeight, ceiling, IIntScrambler.IDENTITY, block.getDefaultState() );
    }

    @Override
    public void generate( WorldGenRegion region, IMapGenData data ) {
        int cx = region.getMainChunkX();
        int cz = region.getMainChunkZ();

        MovingBlockPos mpos = new MovingBlockPos();

        for( int x = 0; x < 16; x++ ) {
            int gx = x + cx * 16;

            for( int z = 0; z < 16; z++ ) {
                int gz = z + cz * 16;

                for( int y = minHeight; y <= maxHeight; y++ ) {
                    int rand = Hash.hash3I( seed, gx, y, gz ) % diff;

                    int my = ceiling ? maxHeight - y : y - minHeight;

                    if( rand >= my ) {
                        mpos.setPos( gx, y, gz );
                        region.setBlockState( mpos, block, 2 );
                    }
                }
            }
        }
    }
}
