/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.gen.structure;

import modernity.common.area.ForestRunesArea;
import modernity.common.area.core.AreaBox;
import modernity.common.block.MDBlocks;
import modernity.common.block.base.HorizontalFacingBlock;
import modernity.common.block.base.HorizontalPortalFrameBlock;
import modernity.common.block.base.PortalCornerBlock;
import modernity.common.world.gen.structure.util.RotatingPiece;
import modernity.common.world.gen.structure.util.StructureUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
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
    private static final BlockState BRICKS = MDBlocks.DARK_STONE_BRICKS.getDefaultState();
    private static final BlockState INSCRIBED_BRICKS = MDBlocks.INSCRIBED_DARK_STONE_BRICKS.getDefaultState();
    private static final BlockState LOG_X = Blocks.DARK_OAK_LOG.getDefaultState().with( BlockStateProperties.AXIS, Direction.Axis.X );
    private static final BlockState LOG_Y = Blocks.DARK_OAK_LOG.getDefaultState().with( BlockStateProperties.AXIS, Direction.Axis.Y );
    private static final BlockState LOG_Z = Blocks.DARK_OAK_LOG.getDefaultState().with( BlockStateProperties.AXIS, Direction.Axis.Z );
    private static final BlockState AIR = Blocks.AIR.getDefaultState();
    private static final BlockState STAIRS_N = Blocks.DARK_OAK_STAIRS.getDefaultState().with( HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH );
    private static final BlockState STAIRS_E = Blocks.DARK_OAK_STAIRS.getDefaultState().with( HorizontalBlock.HORIZONTAL_FACING, Direction.EAST );
    private static final BlockState STAIRS_S = Blocks.DARK_OAK_STAIRS.getDefaultState().with( HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH );
    private static final BlockState STAIRS_W = Blocks.DARK_OAK_STAIRS.getDefaultState().with( HorizontalBlock.HORIZONTAL_FACING, Direction.WEST );
    private static final BlockState STAIRS_UP_N = STAIRS_N.with( BlockStateProperties.HALF, Half.TOP );
    private static final BlockState STAIRS_UP_E = STAIRS_E.with( BlockStateProperties.HALF, Half.TOP );
    private static final BlockState STAIRS_UP_S = STAIRS_S.with( BlockStateProperties.HALF, Half.TOP );
    private static final BlockState STAIRS_UP_W = STAIRS_W.with( BlockStateProperties.HALF, Half.TOP );
    private static final BlockState PLANKS = Blocks.DARK_OAK_PLANKS.getDefaultState();
    private static final BlockState FENCE = Blocks.DARK_OAK_FENCE.getDefaultState();
    private static final BlockState LANTERN = Blocks.LANTERN.getDefaultState();
    private static final BlockState FRAME_X = MDBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.X );
    private static final BlockState FRAME_Z = MDBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.Z );
    private static final BlockState FRAME_Y = MDBlocks.VERTICAL_PORTAL_FRAME.getDefaultState();
    private static final BlockState CORNER = MDBlocks.PORTAL_CORNER.getDefaultState().with( PortalCornerBlock.STATE, PortalCornerBlock.State.EXHAUSTED );

    private static final BlockState[] GOLD_BRICKS = {
        MDBlocks.DARK_STONE_BRICKS_CURSE.getDefaultState(),
        MDBlocks.DARK_STONE_BRICKS_CYEN.getDefaultState(),
        MDBlocks.DARK_STONE_BRICKS_FYREN.getDefaultState(),
        MDBlocks.DARK_STONE_BRICKS_TIMEN.getDefaultState(),
        MDBlocks.DARK_STONE_BRICKS_RGSW.getDefaultState(),
        MDBlocks.DARK_STONE_BRICKS_PORTAL.getDefaultState(),
        MDBlocks.DARK_STONE_BRICKS_NATURE.getDefaultState()
    };

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

        private long areaID = 0;

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
            areaID = nbt.getLong( "area" );
        }

        @Override
        protected void readAdditional( CompoundNBT nbt ) {
            nbt.putShort( "height", (short) height );
            nbt.putInt( "x", x );
            nbt.putInt( "z", z );
            nbt.putLong( "area", areaID );
        }

        @Override
        public boolean addComponentParts( IWorld world, Random rand, MutableBoundingBox box, ChunkPos chunkPos ) {
            computeHeight( world, box, rand );

            if( areaID == 0 ) {
                AreaBox areaBox = AreaBox.makeFromMutableBB( boundingBox ).grow( 1 );
                ForestRunesArea area = ForestRunesArea.create( world.getWorld(), areaBox );
                areaID = area.getReferenceID();
            }

            fillWithBlocks( world, box, 1, 0, 1, 8, 0, 8, BRICKS, BRICKS, false );

            fillWithBlocks( world, box, 0, 0, 2, 0, 0, 7, LOG_Z, LOG_Z, false );
            fillWithBlocks( world, box, 9, 0, 2, 9, 0, 7, LOG_Z, LOG_Z, false );
            fillWithBlocks( world, box, 2, 0, 0, 7, 0, 0, LOG_X, LOG_X, false );
            fillWithBlocks( world, box, 2, 0, 9, 7, 0, 9, LOG_X, LOG_X, false );

            fillWithBlocks( world, box, 1, - 1, 0, 1, 7, 0, LOG_Y, LOG_Y, false );
            fillWithBlocks( world, box, 0, - 1, 1, 0, 7, 1, LOG_Y, LOG_Y, false );
            fillWithBlocks( world, box, 1, - 1, 9, 1, 7, 9, LOG_Y, LOG_Y, false );
            fillWithBlocks( world, box, 0, - 1, 8, 0, 7, 8, LOG_Y, LOG_Y, false );
            fillWithBlocks( world, box, 8, - 1, 0, 8, 7, 0, LOG_Y, LOG_Y, false );
            fillWithBlocks( world, box, 9, - 1, 1, 9, 7, 1, LOG_Y, LOG_Y, false );
            fillWithBlocks( world, box, 8, - 1, 9, 8, 7, 9, LOG_Y, LOG_Y, false );
            fillWithBlocks( world, box, 9, - 1, 8, 9, 7, 8, LOG_Y, LOG_Y, false );

            replaceAirAndLiquidDownwards( world, LOG_Y, 1, - 2, 0, box );
            replaceAirAndLiquidDownwards( world, LOG_Y, 0, - 2, 1, box );
            replaceAirAndLiquidDownwards( world, LOG_Y, 1, - 2, 9, box );
            replaceAirAndLiquidDownwards( world, LOG_Y, 0, - 2, 8, box );
            replaceAirAndLiquidDownwards( world, LOG_Y, 8, - 2, 0, box );
            replaceAirAndLiquidDownwards( world, LOG_Y, 9, - 2, 1, box );
            replaceAirAndLiquidDownwards( world, LOG_Y, 8, - 2, 9, box );
            replaceAirAndLiquidDownwards( world, LOG_Y, 9, - 2, 8, box );

            fillWithBlocks( world, box, 1, 1, 1, 8, 5, 8, AIR, AIR, false );

            fillWithBlocks( world, box, 0, 1, 2, 0, 6, 7, BRICKS, BRICKS, false );
            fillWithBlocks( world, box, 9, 1, 2, 9, 6, 7, BRICKS, BRICKS, false );
            fillWithBlocks( world, box, 2, 1, 0, 7, 6, 0, BRICKS, BRICKS, false );
            fillWithBlocks( world, box, 2, 1, 9, 7, 6, 9, BRICKS, BRICKS, false );

            fillWithBlocks( world, box, 0, 7, 2, 0, 7, 7, LOG_Z, LOG_Z, false );
            fillWithBlocks( world, box, 9, 7, 2, 9, 7, 7, LOG_Z, LOG_Z, false );
            fillWithBlocks( world, box, 2, 7, 0, 7, 7, 0, LOG_X, LOG_X, false );
            fillWithBlocks( world, box, 2, 7, 9, 7, 7, 9, LOG_X, LOG_X, false );

            setBlockState( world, BRICKS, 1, 1, 1, box );
            setBlockState( world, BRICKS, 8, 1, 1, box );
            setBlockState( world, BRICKS, 8, 1, 8, box );
            setBlockState( world, BRICKS, 1, 1, 8, box );

            setBlockState( world, BRICKS, 1, 7, 1, box );
            setBlockState( world, BRICKS, 8, 7, 1, box );
            setBlockState( world, BRICKS, 8, 7, 8, box );
            setBlockState( world, BRICKS, 1, 7, 8, box );

            setBlockState( world, LOG_Y, 1, 8, 1, box );
            setBlockState( world, LOG_Y, 8, 8, 1, box );
            setBlockState( world, LOG_Y, 8, 8, 8, box );
            setBlockState( world, LOG_Y, 1, 8, 8, box );

            fillWithBlocks( world, box, 1, 8, 2, 1, 8, 7, LOG_Z, LOG_Z, false );
            fillWithBlocks( world, box, 8, 8, 2, 8, 8, 7, LOG_Z, LOG_Z, false );
            fillWithBlocks( world, box, 2, 8, 1, 7, 8, 1, LOG_X, LOG_X, false );
            fillWithBlocks( world, box, 2, 8, 8, 7, 8, 8, LOG_X, LOG_X, false );

            fillWithBlocks( world, box, 2, 8, 2, 7, 8, 7, PLANKS, PLANKS, false );
            fillWithBlocks( world, box, 4, 8, 4, 5, 8, 5, BRICKS, BRICKS, false );

            setBlockState( world, BRICKS, 3, 8, 3, box );
            setBlockState( world, BRICKS, 6, 8, 3, box );
            setBlockState( world, BRICKS, 6, 8, 6, box );
            setBlockState( world, BRICKS, 3, 8, 6, box );

            fillWithBlocks( world, box, 1, 2, 1, 1, 6, 1, FENCE, FENCE, false );
            fillWithBlocks( world, box, 1, 2, 8, 1, 6, 8, FENCE, FENCE, false );
            fillWithBlocks( world, box, 8, 2, 8, 8, 6, 8, FENCE, FENCE, false );
            fillWithBlocks( world, box, 8, 2, 1, 8, 6, 1, FENCE, FENCE, false );

            fillWithBlocks( world, box, 1, 1, 2, 1, 1, 7, STAIRS_W, STAIRS_W, false );
            fillWithBlocks( world, box, 8, 1, 2, 8, 1, 7, STAIRS_E, STAIRS_E, false );
            fillWithBlocks( world, box, 2, 1, 1, 7, 1, 1, STAIRS_N, STAIRS_N, false );
            fillWithBlocks( world, box, 2, 1, 8, 7, 1, 8, STAIRS_S, STAIRS_S, false );

            fillWithBlocks( world, box, 1, 7, 2, 1, 7, 7, STAIRS_UP_W, STAIRS_UP_W, false );
            fillWithBlocks( world, box, 8, 7, 2, 8, 7, 7, STAIRS_UP_E, STAIRS_UP_E, false );
            fillWithBlocks( world, box, 2, 7, 1, 7, 7, 1, STAIRS_UP_N, STAIRS_UP_N, false );
            fillWithBlocks( world, box, 2, 7, 8, 7, 7, 8, STAIRS_UP_S, STAIRS_UP_S, false );

            fillWithBlocks( world, box, 3, 1, 0, 3, 4, 0, LOG_Y, LOG_Y, false );
            fillWithBlocks( world, box, 6, 1, 0, 6, 4, 0, LOG_Y, LOG_Y, false );
            fillWithBlocks( world, box, 4, 4, 0, 5, 4, 0, LOG_X, LOG_X, false );
            fillWithBlocks( world, box, 4, 1, 0, 5, 2, 1, AIR, AIR, false );
            fillWithBlocks( world, box, 4, 3, 0, 5, 3, 0, FENCE, FENCE, false );
            setBlockState( world, STAIRS_E, 6, 1, 1, box );
            setBlockState( world, STAIRS_W, 3, 1, 1, box );

            fillWithBlocks( world, box, 3, 7, 3, 3, 7, 6, FENCE, FENCE, false );
            fillWithBlocks( world, box, 6, 7, 3, 6, 7, 6, FENCE, FENCE, false );
            fillWithBlocks( world, box, 4, 7, 3, 5, 7, 3, FENCE, FENCE, false );
            fillWithBlocks( world, box, 4, 7, 6, 5, 7, 6, FENCE, FENCE, false );

            setBlockState( world, LANTERN, 3, 6, 3, box );
            setBlockState( world, LANTERN, 3, 6, 6, box );
            setBlockState( world, LANTERN, 6, 6, 6, box );
            setBlockState( world, LANTERN, 6, 6, 3, box );

            setBlockState( world, FRAME_Y, 3, 1, 3, box );
            setBlockState( world, FRAME_Y, 3, 1, 6, box );
            setBlockState( world, FRAME_Y, 6, 1, 6, box );
            setBlockState( world, FRAME_Y, 6, 1, 3, box );

            setBlockState( world, CORNER, 3, 2, 3, box );
            setBlockState( world, CORNER, 3, 2, 6, box );
            setBlockState( world, CORNER, 6, 2, 6, box );
            setBlockState( world, CORNER, 6, 2, 3, box );

            fillWithBlocks( world, box, 4, 1, 3, 5, 1, 3, FRAME_X, FRAME_X, false );
            fillWithBlocks( world, box, 4, 1, 6, 5, 1, 6, FRAME_X, FRAME_X, false );
            fillWithBlocks( world, box, 3, 1, 4, 3, 1, 5, FRAME_Z, FRAME_Z, false );
            fillWithBlocks( world, box, 6, 1, 4, 6, 1, 5, FRAME_Z, FRAME_Z, false );

            for( Direction dir : Direction.Plane.HORIZONTAL ) {
                Direction.Axis axis = dir.getAxis();
                BlockState log = LOG_Y.with( LogBlock.AXIS, axis );
                Direction.AxisDirection ad = dir.getAxisDirection();
                int wallOff = ad == Direction.AxisDirection.POSITIVE ? 9 : 0;

                int x1 = axis == Direction.Axis.X ? wallOff : 2;
                int x2 = axis == Direction.Axis.X ? wallOff : 7;
                int z1 = axis == Direction.Axis.Z ? wallOff : 2;
                int z2 = axis == Direction.Axis.Z ? wallOff : 7;

                setBlockState( world, log, x1, 1, z1, box );
                setBlockState( world, log, x2, 1, z2, box );
                setBlockState( world, log, x1, 6, z1, box );
                setBlockState( world, log, x2, 6, z2, box );

                if( axis == Direction.Axis.X ) {
                    for( int z = z1 + 1; z < z2; z++ ) {
                        for( int y = 2; y <= 5; y++ ) {
                            BlockState state = getBlockStateFromPos( world, x1, y, z, box );
                            if( state == BRICKS ) {
                                if( rand.nextInt( 5 ) == 0 ) {
                                    state = GOLD_BRICKS[ rand.nextInt( GOLD_BRICKS.length ) ];
                                    state = state.with( HorizontalFacingBlock.FACING, dir.getOpposite() );
                                    setBlockState( world, state, x1, y, z, box );
                                }
                            }
                        }
                    }
                } else if( axis == Direction.Axis.Z ) {
                    for( int x = x1 + 1; x < x2; x++ ) {
                        for( int y = 2; y <= 5; y++ ) {
                            BlockState state = getBlockStateFromPos( world, x, y, z1, box );
                            if( state == BRICKS ) {
                                if( rand.nextInt( 5 ) == 0 ) {
                                    state = GOLD_BRICKS[ rand.nextInt( GOLD_BRICKS.length ) ];
                                    state = state.with( HorizontalFacingBlock.FACING, dir.getOpposite() );
                                    setBlockState( world, state, x, y, z1, box );
                                }
                            }
                        }
                    }
                }
            }

            for( int x = 0; x < 10; x++ ) {
                for( int z = 0; z < 10; z++ ) {
                    for( int y = 0; y < 10; y++ ) {
                        BlockState state = getBlockStateFromPos( world, x, y, z, box );
                        if( state == BRICKS ) {
                            if( rand.nextInt( 5 ) == 0 ) {
                                setBlockState( world, INSCRIBED_BRICKS, x, y, z, box );
                            }
                        }
                    }
                }
            }

            return true;
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

        public void computeHeight( IWorld world, MutableBoundingBox mbox, Random rand ) {
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
            setHeight( height - rand.nextInt( 4 ) );
            recomputeBox( x, getHeight(), z );
        }

        public void recomputeBox( int x, int y, int z ) {
            boundingBox = StructureUtil.getOrientedBox(
                x, y, z,
                - 5, 0, - 5,
                10, 10, 10,
                getCoordBaseMode() != null ? getCoordBaseMode() : Direction.NORTH
            );
        }
    }
}