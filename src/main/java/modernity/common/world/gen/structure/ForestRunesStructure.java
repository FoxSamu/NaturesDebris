package modernity.common.world.gen.structure;

import modernity.common.world.gen.structure.util.RotatingPiece;
import modernity.common.world.gen.structure.util.StructureUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class ForestRunesStructure extends Structure<NoFeatureConfig> {
    public static final String NAME = "MDForRun";

    public ForestRunesStructure() {
        super( NoFeatureConfig::deserialize );
    }

    @Override
    public boolean hasStartAt( ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ ) {
        int regX = chunkPosX >> 3;
        int regZ = chunkPosZ >> 3;
        rand.setSeed( (long) ( regX ^ regZ << 3 ) ^ chunkGen.getSeed() );
        rand.nextInt();
        if( rand.nextInt( 1 ) != 0 ) {
            return false;
        } else if( chunkPosX != ( regX << 3 ) + 2 + rand.nextInt( 4 ) ) {
            return false;
        } else if( chunkPosZ != ( regZ << 3 ) + 2 + rand.nextInt( 4 ) ) {
            return false;
        } else {
            Biome biome = chunkGen.getBiomeProvider().getBiome( new BlockPos( ( chunkPosX << 3 ) + 9, 0, ( chunkPosZ << 3 ) + 9 ) );
            return chunkGen.hasStructure( biome, MDStructures.FOREST_RUNES );
        }
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
        return 2;
    }

    public static class Start extends StructureStart {
        public Start( Structure<?> structure, int x, int z, Biome biome, MutableBoundingBox box, int ref, long seed ) {
            super( structure, x, z, biome, box, ref, seed );
        }

        @Override
        public void init( ChunkGenerator<?> chunkGen, TemplateManager templates, int chunkX, int chunkZ, Biome biome ) {
            components.add( new Piece( chunkX * 16 + 9, chunkZ * 16 + 9, Direction.byHorizontalIndex( rand.nextInt( 4 ) ) ) );

            recalculateStructureSize();
        }
    }

    public static class Piece extends RotatingPiece {
        private final int x, z;
        private int height = - 1;

        protected Piece( int x, int z, Direction coordBase ) {
            super( MDStructurePieceTypes.FOREST_RUNES, 0 );
            this.x = x;
            this.z = z;
            setCoordBaseMode( coordBase );
            recomputeBox( x, 64, z );
        }

        public Piece( TemplateManager type, CompoundNBT nbt ) {
            super( MDStructurePieceTypes.FOREST_RUNES, nbt );
            height = nbt.getShort( "height" );
            x = nbt.getInt( "x" );
            z = nbt.getInt( "z" );
        }

        @Override
        protected void readAdditional( CompoundNBT nbt ) {
            nbt.putShort( "height", (short) height );
            nbt.putInt( "x", x );
            nbt.putInt( "z", z );
        }

        @Override
        public boolean addComponentParts( IWorld world, Random rand, MutableBoundingBox box, ChunkPos chunkPos ) {
            // TODO
            return false;
        }

        public void setHeight( int height ) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public void computeHeight( IWorld world, MutableBoundingBox mbox ) {
            if( height >= 0 ) return;
            int height = 0;
            int count = 0;

            int minX = Math.max( mbox.minX, boundingBox.minX );
            int minZ = Math.max( mbox.minZ, boundingBox.minZ );
            int maxX = Math.min( mbox.maxX, boundingBox.maxX );
            int maxZ = Math.min( mbox.maxZ, boundingBox.maxZ );

            for( int x = minX; x <= maxX; x++ ) {
                for( int z = minZ; z <= maxZ; z++ ) {
                    height += world.getHeight( Heightmap.Type.WORLD_SURFACE_WG, x, z );
                    count++;
                }
            }

            height /= count;
            setHeight( height );
            recomputeBox( x, height, z );
        }

        public void recomputeBox( int x, int y, int z ) {
            boundingBox = StructureUtil.getOrientedBox(
                x, y, z,
                -5, -5, -5,
                10, 10, 10,
                getCoordBaseMode() != null ? getCoordBaseMode() : Direction.NORTH
            );
        }
    }
}