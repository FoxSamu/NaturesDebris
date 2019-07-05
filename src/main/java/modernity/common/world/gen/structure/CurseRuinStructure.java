/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 5 - 2019
 */

package modernity.common.world.gen.structure;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.rgsw.MathUtil;
import net.rgsw.noise.FractalOpenSimplex3D;
import org.apache.commons.lang3.Validate;

import java.util.Random;

public class CurseRuinStructure extends Structure<CurseRuinStructure.Type> {
    public static final String NAME = "MDCurseRuin";

    @Override
    public boolean place( IWorld worldIn, IChunkGenerator<? extends IChunkGenSettings> generator, Random rand, BlockPos pos, Type config ) {
        return super.place( worldIn, generator, rand, pos, config );
    }

    @Override
    protected boolean hasStartAt( IChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ ) {
        int x = chunkPosX >> 3;
        int z = chunkPosZ >> 3;
        rand.setSeed( (long) ( x ^ z << 3 ) ^ chunkGen.getSeed() );
        rand.nextInt();
        if( rand.nextInt( 3 ) != 0 ) {
            return false;
        } else if( chunkPosX != ( x << 3 ) + 2 + rand.nextInt( 4 ) ) {
            return false;
        } else if( chunkPosZ != ( z << 3 ) + 2 + rand.nextInt( 4 ) ) {
            return false;
        } else {
            Biome biome = chunkGen.getBiomeProvider().getBiome( new BlockPos( ( chunkPosX << 4 ) + 9, 0, ( chunkPosZ << 4 ) + 9 ), Biomes.DEFAULT );
            if( biome == null ) {
                // Not the case, but just to suppress warning
                return false;
            }
            return chunkGen.hasStructure( biome, this );
        }
    }

    @Override
    protected boolean isEnabledIn( IWorld world ) {
        return world.getWorldInfo().isMapFeaturesEnabled();
    }

    @Override
    protected StructureStart makeStart( IWorld world, IChunkGenerator<?> generator, SharedSeedRandom random, int x, int z ) {
        Biome biome = generator.getBiomeProvider().getBiome( new BlockPos( ( x << 4 ) + 9, 0, ( z << 4 ) + 9 ), Biomes.DEFAULT );
        Validate.notNull( biome ); // Not the case
        Type type = (Type) generator.getStructureConfig( biome, this );
        if( type == null ) {
            type = Type.DEFAULT;
        }
        return new Start( x, z, biome, random, world.getSeed(), world, type );
    }

    @Override
    protected String getStructureName() {
        return NAME;
    }

    @Override
    public int getSize() {
        return 0;
    }

    public static class Start extends StructureStart {

        public Start() {
        }

        public Start( int x, int z, Biome biome, SharedSeedRandom rand, long seed, IWorld world, Type type ) {
            super( x, z, biome, rand, seed );

            components.add( new Piece( rand, x * 16 + 8, z * 16 + 8, world, type ) );

            this.recalculateStructureSize( world );
        }
    }

    public static class Piece extends StructurePiece {

        private static final double N000 = - 0.7;
        private static final double N001 = - 0;
        private static final double N010 = - 1;
        private static final double N011 = - 0.5;
        private static final double N100 = - 0.2;
        private static final double N101 = 1;
        private static final double N110 = - 0.4;
        private static final double N111 = 0.9;

        private int height = - 1;
        private int x;
        private int z;
        private FractalOpenSimplex3D noise;
        private Type type;

        private double[] noiseBuffer;

        public Piece() {
            super( 0 );
        }

        public Piece( Random rand, int x, int z, IWorld world, Type type ) {
            super( 0 );

            this.x = x;
            this.z = z;

            this.type = type;

            createNoise( rand.nextInt() );

            boundingBox = new MutableBoundingBox( - 2 + x, - 1, - 4 + z, 3 + x, 4, 4 + z );

            setCoordBaseMode( EnumFacing.SOUTH );
        }

        private void createNoise( int seed ) {
            noise = new FractalOpenSimplex3D( seed, 2.88156990284, 3 );
        }

        private double genNoise( int x, int y, int z ) {
            double dx = x / 5D;
            double dy = y / 3D;
            double dz = z / 8D;

            double noise = this.noise.generate( x, y, z );

            double n00 = MathUtil.lerp( N000, N001, dz );
            double n01 = MathUtil.lerp( N010, N011, dz );
            double n10 = MathUtil.lerp( N100, N101, dz );
            double n11 = MathUtil.lerp( N110, N111, dz );

            double n0 = MathUtil.lerp( n00, n01, dy );
            double n1 = MathUtil.lerp( n10, n11, dy );

            double n = MathUtil.lerp( n0, n1, dx );

            noise += n;
            return noise;
        }

        private void initNoiseBuffer() {
            if( noiseBuffer == null ) {
                noiseBuffer = new double[ 5 * 3 * 8 ];
                for( int x = 0; x <= 5; x++ ) {
                    for( int y = 0; y <= 3; y++ ) {
                        for( int z = 0; z <= 8; z++ ) {
                            noiseBuffer[ ( x * 3 + y ) * 8 + z ] = genNoise( x, y, z );
                        }
                    }
                }
            }
        }

        @Override
        protected void writeAdditional( NBTTagCompound nbt ) {
            nbt.putInt( "h", height );
            nbt.putInt( "x", x );
            nbt.putInt( "z", z );
            nbt.putInt( "s", noise.getSeed() );
            nbt.putByte( "t", (byte) type.ordinal() );
        }

        @Override
        protected void readAdditional( NBTTagCompound nbt, TemplateManager template ) {
            height = nbt.getInt( "h" );
            x = nbt.getInt( "x" );
            z = nbt.getInt( "z" );
            createNoise( nbt.getInt( "s" ) );
            type = Type.values()[ nbt.getByte( "t" ) ];
        }

        @Override
        public boolean addComponentParts( IWorld world, Random rand, MutableBoundingBox box, ChunkPos cpos ) {
            if( height < 0 ) {
                height = world.getHeight( Heightmap.Type.WORLD_SURFACE_WG, x, z ) + 1;
                boundingBox.offset( 0, height, 0 );
            }

            Config cfg = type.config;

            fillWithRandomBlocksAndNoise( world, box, 0, 0, 0, 5, 0, 8, true, 0, rand, Picker.selector( cfg::getFloor ) );
            fillWithRandomBlocksAndNoise( world, box, 0, 3, 0, 5, 3, 8, true, 0, rand, Picker.selector( cfg::getWallSlab ) );
            fillWithRandomBlocksAndNoise( world, box, 1, 3, 1, 4, 3, 7, true, 0, rand, Picker.selector( cfg::getRoof ) );
            fillWithRandomBlocksAndNoise( world, box, 0, 1, 0, 5, 2, 8, true, 0, rand, Picker.selector( cfg::getWall ) );
            fillWithAir( world, box, 1, 1, 1, 4, 2, 7 );

            return true;
        }

        private void fillWithRandomBlocksAndNoise( IWorld world, MutableBoundingBox box, int x0, int y0, int z0, int x1, int y1, int z1, boolean alwaysReplaceSolid, double noiseAddend, Random rand, BlockSelector selector ) {
            initNoiseBuffer();
            for( int y = y0; y <= y1; ++ y ) {
                for( int x = x0; x <= x1; ++ x ) {
                    for( int z = z0; z <= z1; ++ z ) {
                        double noise = getNoise( x, y, z ) + noiseAddend;
                        boolean replace = noise > 0;
                        if( alwaysReplaceSolid && ! getBlockStateFromPos( world, x, y, z, box ).isAir() ) {
                            replace = true;
                        }
                        if( replace ) {
                            selector.selectBlocks( rand, x, y, z, y == y0 || y == y1 || x == x0 || x == x1 || z == z0 || z == z1 );
                            setBlockState( world, selector.getBlockState(), x, y, z, box );
                        }
                    }
                }
            }
        }

        private double getNoise( int x, int y, int z ) {
            return noiseBuffer[ ( x * 3 + y ) * 8 + z ];
        }

        private int getShardType( IWorld world ) {
            int[] types = getShardTypes( world );
            return types[ type.config.getBiomeType() ];
        }

        private static int[] getShardTypes( IWorld world ) {
            long seed = world.getSeed();
            int[] order = { 1, 2, 3, 4 };
            Random rand = new Random( seed * 12539 );
            for( int i = 0; i < 4; i++ ) {
                int index = rand.nextInt( 4 );
                if( index == i ) continue;
                int tmp = order[ index ];
                order[ index ] = order[ i ];
                order[ i ] = tmp;
            }
            return order;
        }
    }

    public enum Type implements IFeatureConfig {
        DEFAULT( new DefaultConfig() );
        private final Config config;

        Type( Config config ) {
            this.config = config;
        }
    }

    public interface Config {
        IBlockState getCorner( Random rand );
        IBlockState getWall( Random rand );
        IBlockState getWallStairs( Random rand );
        IBlockState getWallSlab( Random rand );
        IBlockState getRoof( Random rand );
        IBlockState getFloor( Random rand );
        IBlockState getFlowerPot( Random rand );
        IBlockState getPath( Random rand );
        IBlockState getWorkbench( Random rand );

        boolean hasVines( Random rand );
        boolean hasSpiderWebs( Random rand );

        EntityType getSpawnerType( Random rand );

        int getBiomeType();
    }

    public static class DefaultConfig implements Config {

        @Override
        public IBlockState getCorner( Random rand ) {
            return Blocks.OAK_LOG.getDefaultState().with( BlockLog.AXIS, EnumFacing.Axis.Y );
        }

        @Override
        public IBlockState getWall( Random rand ) {
            return Blocks.COBBLESTONE.getDefaultState();
        }

        @Override
        public IBlockState getWallStairs( Random rand ) {
            return Blocks.COBBLESTONE_STAIRS.getDefaultState();
        }

        @Override
        public IBlockState getWallSlab( Random rand ) {
            return Blocks.COBBLESTONE_SLAB.getDefaultState();
        }

        @Override
        public IBlockState getRoof( Random rand ) {
            return Blocks.COBBLESTONE.getDefaultState();
        }

        @Override
        public IBlockState getFloor( Random rand ) {
            return Blocks.STONE_BRICKS.getDefaultState();
        }

        @Override
        public IBlockState getFlowerPot( Random rand ) {
            return null;
        }

        @Override
        public IBlockState getPath( Random rand ) {
            return null;
        }

        @Override
        public IBlockState getWorkbench( Random rand ) {
            return null;
        }

        @Override
        public boolean hasVines( Random rand ) {
            return false;
        }

        @Override
        public boolean hasSpiderWebs( Random rand ) {
            return false;
        }

        @Override
        public EntityType getSpawnerType( Random rand ) {
            return null;
        }

        @Override
        public int getBiomeType() {
            return 0;
        }
    }

    private interface Picker {
        IBlockState random( Random rand );

        static StructurePiece.BlockSelector selector( Picker picker ) {
            return new StructurePiece.BlockSelector() {
                @Override
                public void selectBlocks( Random rand, int x, int y, int z, boolean wall ) {
                    blockstate = picker.random( rand );
                }
            };
        }
    }
}
