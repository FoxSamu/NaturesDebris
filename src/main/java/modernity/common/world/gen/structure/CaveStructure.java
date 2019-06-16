/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 16 - 2019
 */

package modernity.common.world.gen.structure;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BitArray;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;

import java.util.List;
import java.util.Random;

/**
 * Structure to store information about caves, used to check if a player is in a cave or not. This does not generate
 * anything.
 */
public class CaveStructure extends Structure<NoFeatureConfig> {
    public static final String NAME = "MDCave";
    @Override
    protected boolean hasStartAt( IChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ ) {
        return true;
    }

    @Override
    protected boolean isEnabledIn( IWorld worldIn ) {
        return true;
    }

    @Override
    protected StructureStart makeStart( IWorld world, IChunkGenerator<?> generator, SharedSeedRandom random, int x, int z ) {
        return new Start( x, z, world.getBiome( new BlockPos( x * 16 + 7, 0, z * 16 + 7 ) ), random, world.getSeed() );
    }

    @Override
    protected String getStructureName() {
        return NAME;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean isPositionInsideStructure( IWorld world, BlockPos pos ) {
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;
        IChunk chunk = world.getChunk( cx, cz );
        Start s = (Start) chunk.getStructureStart( getStructureName() );
        return s != null && s.contains( pos.getX(), pos.getY(), pos.getZ() );
    }

    @SuppressWarnings( "ConstantConditions" )
    private List<StructureStart> getStarts( IWorld world, int x, int z ) {
        List<StructureStart> starts = Lists.newArrayList();

        Long2ObjectMap<StructureStart> refToStartMap = world.getChunkProvider().getChunkGenerator().getStructureReferenceToStartMap( this );
        Long2ObjectMap<LongSet> posToRefMap = world.getChunkProvider().getChunkGenerator().getStructurePositionToReferenceMap( this );

        long lpos = ChunkPos.asLong( x, z );

        LongSet refs = posToRefMap.get( lpos );
        if( refs == null ) {
            refs = world.getChunkProvider().getChunkOrPrimer( x, z, true ).getStructureReferences( this.getStructureName() );
            posToRefMap.put( lpos, refs );
        }

        for( long ref : refs ) {
            StructureStart start = refToStartMap.get( ref );
            if( start != null ) {
                starts.add( start );
            } else {
                ChunkPos refPos = new ChunkPos( ref );
                IChunk chunk = world.getChunkProvider().getChunkOrPrimer( refPos.x, refPos.z, true );
                start = chunk.getStructureStart( this.getStructureName() );
                if( start != null ) {
                    refToStartMap.put( ref, start );
                    starts.add( start );
                }
            }
        }

        return starts;
    }

    public void addCaves( IChunk chunk, int x, int z, int[] hm ) {
        Start start = new Start( x, z, chunk.getBiomes()[ 0 ], new SharedSeedRandom(), 0 );
        start.applyLimitMap( hm );

        chunk.putStructureStart( getStructureName(), start );
        chunk.addStructureReference( getStructureName(), ChunkPos.asLong( x, z ) );
    }

    public static class Start extends StructureStart {
        private final BitArray limitMap = new BitArray( 9, 256 );

        public Start() {
        }

        public Start( int x, int z, Biome biome, SharedSeedRandom random, long seed ) {
            super( x, z, biome, random, seed );
            boundingBox = new MutableBoundingBox( 0, 0, 0, 15, 255, 15 );
            boundingBox.offset( x, 0, z );
        }

        public void applyLimitMap( int[] hm ) {
            for( int i = 0; i < hm.length; i++ ) {
                limitMap.setAt( i, Math.max( hm[ i ], 0 ) );
            }
        }

        public int getLimit( int x, int z ) {
            x = x & 15;
            z = z & 15;
            return limitMap.getAt( x + z * 16 );
        }

        public boolean contains( int x, int y, int z ) {
            return y < getLimit( x, z );
        }

        @Override
        public void writeAdditional( NBTTagCompound tagCompound ) {
            tagCompound.putLongArray( "CaveLimits", limitMap.getBackingLongArray() );
        }

        @Override
        public void readAdditional( NBTTagCompound tagCompound ) {
            long[] caveLimits = tagCompound.getLongArray( "CaveLimits" );
            int len = Math.min( caveLimits.length, limitMap.getBackingLongArray().length );
            System.arraycopy( caveLimits, 0, limitMap.getBackingLongArray(), 0, len );
        }

        public BlockPos randomPosInCave( Random rand, int xoff, int zoff ) {
            int x = rand.nextInt( 16 );
            int z = rand.nextInt( 16 );
            int y = rand.nextInt( getLimit( x, z ) + 1 );
            return new BlockPos( x + xoff, y, z + zoff );
        }

        public BlockPos randomPosNotInCave( Random rand, int xoff, int zoff ) {
            int x = rand.nextInt( 16 );
            int z = rand.nextInt( 16 );
            int y = 255 - rand.nextInt( 255 - getLimit( x, z ) );
            return new BlockPos( x + xoff, y, z + zoff );
        }
    }
}
