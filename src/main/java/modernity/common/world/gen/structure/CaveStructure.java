/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 16 - 2019
 */

package modernity.common.world.gen.structure;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.BitArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

/**
 * Structure to store information about caves, used to check if a player is in a cave or not. This does not generate
 * anything.
 */
public class CaveStructure extends Structure<NoFeatureConfig> {
    public static final String NAME = "MDCave";

    public CaveStructure() {
        super( dynamic -> IFeatureConfig.NO_FEATURE_CONFIG );
    }

    @Override
    public boolean hasStartAt( ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ ) {
        return true;
    }

    @Override
    public IStartFactory getStartFactory() {
        return Start::new;
    }

    @Override
    public String getStructureName() {
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

    public void addCaves( ChunkGenerator<?> cg, IChunk chunk, int x, int z, int[] hm, long seed ) {
        Start start = new Start( this, x, z, chunk.getBiomes()[ 0 ], new MutableBoundingBox( 0, 0, 15, 15 ), 0, seed );
        start.init( cg, null, x, z, chunk.getBiomes()[ 0 ] );
        start.applyLimitMap( hm );

        chunk.putStructureStart( getStructureName(), start );
        chunk.addStructureReference( getStructureName(), ChunkPos.asLong( x, z ) );
    }

    public static class Start extends StructureStart {
        public Start( Structure<?> structure, int x, int z, Biome biome, MutableBoundingBox box, int ref, long seed ) {
            super( structure, x, z, biome, box, ref, seed );
            bounds = new MutableBoundingBox( 0, 0, 0, 15, 255, 15 );
            bounds.offset( x, 0, z );
        }

        public Start( int x, int z, Biome biome ) {
            this( MDStructures.CAVE, x, z, biome, new MutableBoundingBox( 0, 0, 0, 15, 255, 15 ), 0, 0L );
            init( null, null, x, z, biome );
        }

        @Override
        public void init( ChunkGenerator<?> generator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome ) {
            components.add( new Piece( 0 ) );
        }

        public void applyLimitMap( int[] hm ) {
            Piece p = (Piece) components.get( 0 );
            p.applyLimitMap( hm );
        }

        public int getLimit( int x, int z ) {
            Piece p = (Piece) components.get( 0 );
            return p.getLimit( x, z );
        }

        public boolean contains( int x, int y, int z ) {
            return y < getLimit( x, z );
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

    public static class Piece extends StructurePiece {
        private final BitArray limitMap = new BitArray( 9, 256 );

        protected Piece( int chain ) {
            super( MDStructurePieceTypes.CAVE_DATA, chain );
            boundingBox = new MutableBoundingBox( 0, 0, 15, 15 );
        }

        public Piece( TemplateManager manager, CompoundNBT nbt ) {
            super( MDStructurePieceTypes.CAVE_DATA, nbt );
            long[] caveLimits = nbt.getLongArray( "CaveLimits" );
            int len = Math.min( caveLimits.length, limitMap.getBackingLongArray().length );
            System.arraycopy( caveLimits, 0, limitMap.getBackingLongArray(), 0, len );
        }

        @Override // MCP: WHAT?! This must be 'writeAdditional' as it is called in 'write'...
        protected void readAdditional( CompoundNBT nbt ) {
            nbt.putLongArray( "CaveLimits", limitMap.getBackingLongArray() );
        }

        @Override
        public boolean addComponentParts( IWorld worldIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos p_74875_4_ ) {
            return false;
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

        public void applyLimitMap( int[] hm ) {
            for( int i = 0; i < hm.length; i++ ) {
                limitMap.setAt( i, Math.max( hm[ i ], 0 ) );
            }
        }

        public int getLimit( int x, int z ) {
            x &= 15;
            z &= 15;
            return limitMap.getAt( x + z * 16 );
        }

        public long[] getLimitsAsLongMap() {
            return limitMap.getBackingLongArray();
        }

        public void setLimitsFromLongMap( long[] map ) {
            long[] into = limitMap.getBackingLongArray();
            System.arraycopy( map, 0, into, 0, map.length );
        }

        public boolean contains( int x, int y, int z ) {
            return y < getLimit( x, z );
        }
    }
}
