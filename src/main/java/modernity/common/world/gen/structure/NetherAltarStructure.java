/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 1 - 2019
 */

package modernity.common.world.gen.structure;

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
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.extensions.IForgeBlockState;

import modernity.common.block.MDBlocks;
import modernity.common.world.gen.util.WorldGenUtil;

import java.util.Random;

public class NetherAltarStructure extends Structure<NoFeatureConfig> {
    public static final String NAME = "MDNetherAltar";

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
        return true;
    }

    @Override
    protected StructureStart makeStart( IWorld world, IChunkGenerator<?> generator, SharedSeedRandom random, int x, int z ) {
        Biome biome = generator.getBiomeProvider().getBiome( new BlockPos( x * 16 + 7, 0, z * 16 + 7 ), Biomes.NETHER );
        return new Start( x, z, biome, random, world.getSeed(), world );
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

        public Start( int x, int z, Biome biome, SharedSeedRandom rand, long seed, IWorld world ) {
            super( x, z, biome, rand, seed );

            components.add( new Piece( rand, x * 16 + 8, z * 16 + 8, world ) );

            this.recalculateStructureSize( world );
        }
    }

    public static class Piece extends StructurePiece {

        private int height = - 1;
        private int x;
        private int z;

        public Piece() {
            super( 0 );
        }

        public Piece( Random rand, int x, int z, IWorld world ) {
            super( 0 );

            this.x = x;
            this.z = z;

            boundingBox = new MutableBoundingBox( - 5 + x, - 1, - 5 + z, 5 + x, 12, 5 + z );

            setCoordBaseMode( EnumFacing.NORTH );
        }

        @Override
        protected void writeAdditional( NBTTagCompound nbt ) {
            nbt.putInt( "h", height );
            nbt.putInt( "x", x );
            nbt.putInt( "z", z );
        }

        @Override
        protected void readAdditional( NBTTagCompound nbt, TemplateManager template ) {
            height = nbt.getInt( "h" );
            x = nbt.getInt( "x" );
            z = nbt.getInt( "z" );
        }

        @Override
        public boolean addComponentParts( IWorld world, Random rand, MutableBoundingBox box, ChunkPos cpos ) {
            if( height < 0 ) {
                BlockPos pos = WorldGenUtil.randomFloorHeight( world, x, z, rand, 31, 120, IForgeBlockState::isAir );

                if( pos.getY() < 0 ) {
                    height = rand.nextInt( 89 ) + 31;
                } else {
                    height = pos.getY();
                    boundingBox.offset( 0, height, 0 );
                }
            }

            EnumFacing doorFacing = EnumFacing.Plane.HORIZONTAL.random( rand );
            int doorx = 0;
            int doorz = 0;
            int three = 3;
            if( doorFacing.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ) {
                three = - 3;
            }
            if( doorFacing.getAxis() == EnumFacing.Axis.Z ) {
                doorz = three;
            } else {
                doorx = three;
            }
            for( int x = - 3; x <= 3; x++ ) {
                for( int z = - 3; z <= 3; z++ ) {

                    int ax = Math.abs( x );
                    int az = Math.abs( z );

                    boolean outside = ax + az >= 5;
                    if( outside ) continue;

                    int lx = x + 5;
                    int lz = z + 5;
                    setBlockState( world, Blocks.RED_NETHER_BRICKS.getDefaultState(), lx, - 1, lz, box );
                    replaceAirAndLiquidDownwards( world, Blocks.RED_NETHER_BRICKS.getDefaultState(), lx, - 2, lz, box );


                    boolean edge = ax == 3 && az < 2 || az == 3 || ax == 2 && az == 2;
                    if( edge ) {
                        int h = rand.nextInt( 4 ) + 6;
                        boolean slab = rand.nextBoolean();
                        boolean door = x == doorx && z == doorz;

                        // Walls
                        for( int y = 0; y < h; y++ ) {
                            if( ! door || y >= 3 ) {
                                if( y >= 5 && getBlockStateFromPos( world, lx, y, lz, box ).isSolid() ) {
                                    slab = false; // Make sure we don't generate extra slab in blocks
                                    break;
                                }
                                setBlockState( world, Blocks.NETHER_BRICKS.getDefaultState(), lx, y, lz, box );
                            } else {

                                // Door: only generate when not blocked
                                if( ! getBlockStateFromPos( world, lx, y, lz, box ).isSolid() ) {
                                    setBlockState( world, Blocks.AIR.getDefaultState(), lx, y, lz, box );
                                }
                            }
                        }

                        // Extra slab for height randominess
                        if( slab ) {
                            setBlockState( world, Blocks.NETHER_BRICK_SLAB.getDefaultState(), lx, h, lz, box );
                        }
                    } else {
                        for( int y = 0; y < 5; y++ ) {
                            setBlockState( world, Blocks.AIR.getDefaultState(), lx, y, lz, box );
                        }

                        if( ax == 2 || az == 2 ) {
                            setBlockState( world, Blocks.NETHER_BRICK_FENCE.getDefaultState(), lx, 4, lz, box );
                        }
                    }

                    if( x == 0 && z == 0 ) {
                        setBlockState( world, MDBlocks.NETHER_ALTAR.getDefaultState(), lx, 0, lz, box );
                    }
                }
            }
            return true;
        }
    }
}