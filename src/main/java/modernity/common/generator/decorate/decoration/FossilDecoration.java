/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 08 - 2020
 * Author: rgsw
 */

package modernity.common.generator.decorate.decoration;

import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlocks;
import modernity.common.block.base.AxisBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.*;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.redgalaxy.exc.UnexpectedCaseException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

import static net.minecraft.util.Direction.Axis.*;
import static net.minecraft.util.Direction.*;

@SuppressWarnings( "unused" )
public class FossilDecoration implements IDecoration {

    private static final FossilPart BONE_5 = new Bone( 5 );
    private static final FossilPart BONE_6 = new Bone( 6 );
    private static final FossilPart BONE_7 = new Bone( 7 );
    private static final FossilPart BONE_8 = new Bone( 8 );

    private static final FossilPart[] BONES = { BONE_5, BONE_6, BONE_7, BONE_8 };

    private static final FossilPart SKULL_1 = new Skull1();
    private static final FossilPart SKULL_2 = new Skull2();

    private static final FossilPart LARGE_SKULL = new LargeSkull();

    private static final FossilPart[] SKULLS = { SKULL_1, SKULL_1, SKULL_2, SKULL_2, LARGE_SKULL };

    @Override
    public void generate( IWorld world, BlockPos pos, Random rand, ChunkGenerator<?> chunkGenerator ) {
        int intactness = computeIntactness( rand );
        FossilWorld fossilWorld = new FossilWorld( world, EAST, UP, SOUTH, pos, rand, intactness );

        int type = rand.nextInt( 5 );
        if( type == 0 ) {
            int count = rand.nextInt( 4 ) + 1;
            if( rand.nextInt( 5 ) == 0 ) {
                count += rand.nextInt( 5 );
            }
            addBones( fossilWorld, rand, count );
        } else if( type == 1 ) {
            addBones( fossilWorld, rand, rand.nextInt( 4 ) );
            addLargeSkeleton( fossilWorld, rand );
        } else if( type == 2 ) {
            addBones( fossilWorld, rand, rand.nextInt( 3 ) );
            addSmallSkeleton( fossilWorld, rand );
        } else if( type == 3 ) {
            if( rand.nextInt( 5 ) == 0 ) {
                addBones( fossilWorld, rand, rand.nextInt( 4 ) );
                addLargeSpine( fossilWorld, rand );
            } else {
                addBones( fossilWorld, rand, rand.nextInt( 2 ) );
                addSmallSpine( fossilWorld, rand );
            }
        } else {
            int count = rand.nextInt( 4 ) + 1;
            if( rand.nextInt( 5 ) == 0 ) {
                count += rand.nextInt( 5 );
            }
            addBones( fossilWorld, rand, count );
            addSkull( fossilWorld, rand );
        }
    }

    private static void addBones( FossilWorld world, Random rand, int count ) {
        MovingBlockPos mpos = new MovingBlockPos();
        for( int i = 0; i < count; i++ ) {
            FossilPart part = BONES[ rand.nextInt( 4 ) ];

            mpos.addPos(
                rand.nextInt( 6 ) - rand.nextInt( 6 ),
                rand.nextInt( 6 ) - rand.nextInt( 6 ),
                rand.nextInt( 6 ) - rand.nextInt( 6 )
            );

            FossilWorld local = world.child( randomTransform( rand ), mpos );
            part.generate( local, rand );
        }
    }

    private static void addLargeSkeleton( FossilWorld world, Random rand ) {
        int ribCount = rand.nextInt( 3 ) + 3;
        Rib rib = new Rib( rand.nextInt( 4 ) + 4, rand.nextInt( 2 ) + 1 );
        Spine spine = new Spine( ribCount, rand.nextInt( 3 ) + 1, rib );

        FossilPart skull = LARGE_SKULL;
        if( rand.nextInt( 3 ) == 0 ) {
            skull = rand.nextBoolean() ? SKULL_1 : SKULL_2;
        }

        Skeleton skeleton = new Skeleton( skull, spine );

        FossilWorld local = world.child( randomRotation( rand, rand.nextBoolean() ? UP : DOWN ), BlockPos.ZERO );
        skeleton.generate( local, rand );
    }

    private static void addSmallSkeleton( FossilWorld world, Random rand ) {
        int ribCount = rand.nextInt( 3 ) + 2;
        Rib rib = new Rib( rand.nextInt( 3 ) + 4, 1 );
        Spine spine = new Spine( ribCount, rand.nextInt( 3 ) + 1, rib );

        FossilPart skull = rand.nextBoolean() ? SKULL_1 : SKULL_2;

        Skeleton skeleton = new Skeleton( skull, spine );

        FossilWorld local = world.child( randomRotation( rand, rand.nextBoolean() ? UP : DOWN ), BlockPos.ZERO );
        skeleton.generate( local, rand );
    }

    private static void addLargeSpine( FossilWorld world, Random rand ) {
        int ribCount = rand.nextInt( 3 ) + 3;
        Rib rib = new Rib( rand.nextInt( 4 ) + 4, rand.nextInt( 2 ) + 1 );
        Spine spine = new Spine( ribCount, rand.nextInt( 3 ) + 1, rib );

        FossilWorld local = world.child( randomRotation( rand, rand.nextBoolean() ? UP : DOWN ), BlockPos.ZERO );
        spine.generate( local, rand );
    }

    private static void addSmallSpine( FossilWorld world, Random rand ) {
        int ribCount = rand.nextInt( 3 ) + 2;
        Rib rib = new Rib( rand.nextInt( 3 ) + 4, 1 );
        Spine spine = new Spine( ribCount, rand.nextInt( 3 ) + 1, rib );

        FossilWorld local = world.child( randomRotation( rand, rand.nextBoolean() ? UP : DOWN ), BlockPos.ZERO );
        spine.generate( local, rand );
    }

    private static void addSkull( FossilWorld world, Random rand ) {
        FossilPart part = SKULLS[ rand.nextInt( 5 ) ];

        MovingBlockPos mpos = new MovingBlockPos();

        mpos.addPos(
            rand.nextInt( 6 ) - rand.nextInt( 6 ),
            rand.nextInt( 6 ) - rand.nextInt( 6 ),
            rand.nextInt( 6 ) - rand.nextInt( 6 )
        );

        FossilWorld local = world.child( randomTransform( rand ), mpos );
        part.generate( local, rand );
    }


    private static int computeIntactness( Random rand ) {
        if( rand.nextInt( 2 ) == 0 )
            return - 1;
        else if( rand.nextInt( 2 ) == 0 )
            return 90 + rand.nextInt( 11 );
        else if( rand.nextInt( 2 ) == 0 )
            return 80 + rand.nextInt( 21 );
        else if( rand.nextInt( 2 ) == 0 )
            return 70 + rand.nextInt( 31 );
        else if( rand.nextInt( 2 ) == 0 )
            return 60 + rand.nextInt( 41 );
        else
            return 50 + rand.nextInt( 51 );
    }

    private static BlockState bone( Axis axis ) {
        return MDBlocks.BLACKBONE_BLOCK.getDefaultState().with( AxisBlock.AXIS, axis );
    }

    private static Direction[] randomTransform( Random rand ) {
        ArrayList<Axis> remaining = new ArrayList<>( Arrays.asList( Axis.values() ) );
        Axis[] axes = new Axis[ 3 ];
        axes[ 0 ] = remaining.remove( rand.nextInt( remaining.size() ) );
        axes[ 1 ] = remaining.remove( rand.nextInt( remaining.size() ) );
        axes[ 2 ] = remaining.remove( rand.nextInt( remaining.size() ) );

        Direction[] out = new Direction[ 3 ];
        out[ 0 ] = randomDirection( axes[ 0 ], rand );
        out[ 1 ] = randomDirection( axes[ 1 ], rand );
        out[ 2 ] = randomDirection( axes[ 2 ], rand );
        return out;
    }

    private static Direction[] randomRotation( Random rand, Direction up ) {
        Rotation rotation = Rotation.values()[ rand.nextInt( 4 ) ];

        return new Direction[] {
            rotation.rotate( EAST ),
            up,
            rotation.rotate( SOUTH )
        };
    }

    private static Direction randomDirection( Axis axis, Random rand ) {
        return Direction.getFacingFromAxisDirection( axis, rand.nextBoolean() ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE );
    }

    @FunctionalInterface
    private interface FossilPart {
        void generate( FossilWorld world, Random rand );
    }

    private static class FossilWorld implements IBlockReader, IWorldGenerationReader {
        private final IBlockReader reader;
        private final IWorldGenerationReader writer;
        private FossilWorld parent;
        private final Direction x;
        private final Direction y;
        private final Direction z;
        private final BlockPos pos;
        private final MovingBlockPos mpos = new MovingBlockPos();
        private final Random rand;
        private final int intactness;

        private FossilWorld( IBlockReader reader, IWorldGenerationReader writer, Direction x, Direction y, Direction z, BlockPos pos, Random rand, int intactness ) {
            this.reader = reader;
            this.writer = writer;
            this.x = x;
            this.y = y;
            this.z = z;
            this.pos = pos.toImmutable();
            this.rand = rand;
            this.intactness = intactness;
        }

        FossilWorld( FossilWorld world, Direction x, Direction y, Direction z, BlockPos pos, Random rand, int intactness ) {
            this( world, world, x, y, z, pos, rand, intactness );
            parent = world;
        }

        FossilWorld( IWorld world, Direction x, Direction y, Direction z, BlockPos pos, Random rand, int intactness ) {
            this( world, world, x, y, z, pos, rand, intactness );
        }

        FossilWorld( IWorld world, Direction[] dirs, BlockPos pos, Random rand, int intactness ) {
            this( world, world, dirs[ 0 ], dirs[ 1 ], dirs[ 2 ], pos, rand, intactness );
        }

        FossilWorld( FossilWorld world, Direction[] dirs, BlockPos pos, Random rand, int intactness ) {
            this( world, world, dirs[ 0 ], dirs[ 1 ], dirs[ 2 ], pos, rand, intactness );
            parent = world;
        }

        private MovingBlockPos movePos( BlockPos local ) {
            return mpos.setPos( pos )
                       .move( x, local.getX() )
                       .move( y, local.getY() )
                       .move( z, local.getZ() );
        }

        public FossilWorld child( Direction x, Direction y, Direction z, BlockPos pos ) {
            return new FossilWorld( this, x, y, z, pos, rand, - 1 );
        }

        public FossilWorld child( Direction[] d, BlockPos pos ) {
            return new FossilWorld( this, d[ 0 ], d[ 1 ], d[ 2 ], pos, rand, - 1 );
        }

        public void add( BlockPos pos, FossilPart part ) {
            part.generate( child( EAST, UP, SOUTH, pos ), rand );
        }

        public void add( BlockPos pos, Direction x, Direction y, Direction z, FossilPart part ) {
            part.generate( child( x, y, z, pos ), rand );
        }

        private Direction transformInner( Direction dir ) {
            switch( dir ) {
                case NORTH: return z.getOpposite();
                case SOUTH: return z;
                case WEST: return x.getOpposite();
                case EAST: return x;
                case DOWN: return y.getOpposite();
                case UP: return y;
                default: throw new UnexpectedCaseException( "7th direction?" );
            }
        }

        public Direction transform( Direction dir ) {
            Direction out = transformInner( dir );
            if( parent != null ) {
                return parent.transform( out );
            }
            return out;
        }

        private Axis transformInner( Axis ax ) {
            switch( ax ) {
                case X: return x.getAxis();
                case Y: return y.getAxis();
                case Z: return z.getAxis();
                default: throw new UnexpectedCaseException( "4th axis?" );
            }
        }

        public Axis transform( Axis ax ) {
            Axis out = transformInner( ax );
            if( parent != null ) {
                return parent.transform( out );
            }
            return out;
        }

        public boolean setBone( BlockPos pos, Axis axis ) {
            return setBlockState( pos, bone( transform( axis ) ), 2 | 16 );
        }

        @Override
        public boolean setBlockState( BlockPos pos, BlockState newState, int flags ) {
            boolean place = true;
            if( ( flags & 128 ) == 0 ) {
                if( intactness >= 0 && rand.nextInt( 100 ) >= intactness ) {
                    place = false;
                }
            }
            return ! place || writer.setBlockState( movePos( pos ), newState, flags );
        }

        @Override
        public boolean removeBlock( BlockPos pos, boolean isMoving ) {
            return writer.removeBlock( movePos( pos ), isMoving );
        }

        @Override
        public boolean destroyBlock( BlockPos pos, boolean dropBlock ) {
            return writer.destroyBlock( movePos( pos ), dropBlock );
        }

        @Override
        public boolean hasBlockState( BlockPos pos, Predicate<BlockState> pred ) {
            return writer.hasBlockState( movePos( pos ), pred );
        }

        @Override
        public BlockPos getHeight( Heightmap.Type heightmapType, BlockPos pos ) {
            return writer.getHeight( heightmapType, movePos( pos ) );
        }

        @Nullable
        @Override
        public TileEntity getTileEntity( BlockPos pos ) {
            return reader.getTileEntity( movePos( pos ) );
        }

        @Override
        public BlockState getBlockState( BlockPos pos ) {
            return reader.getBlockState( movePos( pos ) );
        }

        @Override
        public IFluidState getFluidState( BlockPos pos ) {
            return reader.getFluidState( movePos( pos ) );
        }
    }

    private static class Bone implements FossilPart {
        private final int length;

        private Bone( int length ) {
            this.length = length;
        }

        @Override
        public void generate( FossilWorld world, Random rand ) {
            MovingBlockPos mpos = new MovingBlockPos();
            for( int i = 1; i < length - 1; i++ ) {
                mpos.origin().moveUp( i );
                world.setBone( mpos, Axis.Y );
            }

            world.setBone( mpos.origin().moveWest(), Axis.Y );
            world.setBone( mpos.origin().moveEast(), Axis.Y );
            world.setBone( mpos.origin().moveUp( length - 1 ).moveWest(), Axis.Y );
            world.setBone( mpos.origin().moveUp( length - 1 ).moveEast(), Axis.Y );
        }
    }

    private static abstract class FillingPart implements FossilPart {
        static void add( FossilWorld world, MovingBlockPos mpos, int x, int y, int z, Axis axis ) {
            mpos.origin().addPos( x, y, z );
            world.setBone( mpos, axis );
        }

        static void add( FossilWorld world, MovingBlockPos mpos, int x1, int y1, int z1, int x2, int y2, int z2, Axis axis ) {
            for( int x = x1; x <= x2; x++ ) {
                for( int y = y1; y <= y2; y++ ) {
                    for( int z = z1; z <= z2; z++ ) {
                        add( world, mpos, x, y, z, axis );
                    }
                }
            }
        }
    }

    private static class Skull1 extends FillingPart {
        @Override
        public void generate( FossilWorld world, Random rand ) {
            MovingBlockPos mpos = new MovingBlockPos();
            add( world, mpos, 0, 0, - 1, 3, 0, - 1, X );
            add( world, mpos, 0, 0, 1, 3, 0, 1, X );
            add( world, mpos, 2, 0, 0, 3, 0, 0, X );

            add( world, mpos, 1, 2, - 1, 2, 2, 1, X );

            add( world, mpos, 0, 0, 0, Y );
            add( world, mpos, 2, 1, 0, Y );
            add( world, mpos, 0, 1, - 1, 0, 2, 1, Y );
        }
    }

    private static class Skull2 extends FillingPart {
        @Override
        public void generate( FossilWorld world, Random rand ) {
            MovingBlockPos mpos = new MovingBlockPos();
            add( world, mpos, 0, 0, - 1, 3, 0, - 1, X );
            add( world, mpos, 0, 0, 1, 3, 0, 1, X );
            add( world, mpos, 1, 2, - 1, 2, 2, 1, X );

            add( world, mpos, 3, 1, - 1, 3, 1, 1, Z );

            add( world, mpos, 1, 1, - 1, Y );
            add( world, mpos, 1, 1, 1, Y );
            add( world, mpos, 0, 1, - 1, 0, 2, 1, Y );
        }
    }

    private static class LargeSkull extends FillingPart {
        @Override
        public void generate( FossilWorld world, Random rand ) {
            MovingBlockPos mpos = new MovingBlockPos();
            add( world, mpos, 1, 0, 2, 4, 0, 2, X );
            add( world, mpos, 0, 1, 2, 3, 1, 2, X );
            add( world, mpos, 0, 2, 2, 2, 2, 2, X );
            add( world, mpos, 1, 3, 2, 3, 3, 2, X );

            add( world, mpos, 1, 0, - 2, 4, 0, - 2, X );
            add( world, mpos, 0, 1, - 2, 3, 1, - 2, X );
            add( world, mpos, 0, 2, - 2, 2, 2, - 2, X );
            add( world, mpos, 1, 3, - 2, 3, 3, - 2, X );

            add( world, mpos, 1, 3, - 1, 4, 3, 1, X );

            add( world, mpos, 5, 0, - 1, 5, 0, 1, Z );
            add( world, mpos, 5, 1, - 1, Z );
            add( world, mpos, 5, 1, 1, Z );
            add( world, mpos, 5, 2, 0, Z );

            add( world, mpos, 0, 0, - 1, 0, 3, 1, Y );
        }
    }

    private static class Rib extends FillingPart {
        private final int length;
        private final int ext;

        private Rib( int length, int ext ) {
            this.length = length;
            this.ext = ext;
        }

        @Override
        public void generate( FossilWorld world, Random rand ) {
            MovingBlockPos mpos = new MovingBlockPos();
            add( world, mpos, 0, 0, 0, ext - 1, 0, 0, X );
            add( world, mpos, ext, 1, 0, ext, length - 2, 0, Y );
            add( world, mpos, 0, length - 1, 0, ext - 1, length - 1, 0, X );
        }
    }

    private static class Spine extends FillingPart {
        private final int ribs;
        private final int tailLen;
        private final FossilPart ribPart;

        private Spine( int ribs, int tailLen, FossilPart ribPart ) {
            this.ribs = ribs;
            this.tailLen = tailLen;
            this.ribPart = ribPart;
        }

        @Override
        public void generate( FossilWorld world, Random rand ) {
            int l = ribs * 2 + tailLen;

            MovingBlockPos mpos = new MovingBlockPos();
            add( world, mpos, 0, 0, 0, l - 1, 0, 0, X );

            for( int i = 0; i < ribs; i++ ) {
                int x = 1 + i * 2;

                add( world, mpos, x, 0, - 1, Z );
                add( world, mpos, x, 0, 1, Z );

                if( rand.nextInt( 3 ) != 0 ) {
                    world.add( mpos.origin().add( x, 0, 2 ), SOUTH, DOWN, EAST, ribPart );
                }

                if( rand.nextInt( 3 ) != 0 ) {
                    world.add( mpos.origin().add( x, 0, - 2 ), NORTH, DOWN, EAST, ribPart );
                }
            }
        }
    }

    private static class Skeleton implements FossilPart {
        private final FossilPart skull;
        private final FossilPart spine;

        private Skeleton( FossilPart skull, FossilPart spine ) {
            this.skull = skull;
            this.spine = spine;
        }

        @Override
        public void generate( FossilWorld world, Random rand ) {
            MovingBlockPos mpos = new MovingBlockPos();
            world.add( mpos.origin(), EAST, UP, SOUTH, skull );
            world.add( mpos.origin().moveWest(), WEST, UP, SOUTH, spine );
        }
    }

    private static class Nope implements FossilPart {
        @Override
        public void generate( FossilWorld world, Random rand ) {
        }
    }
}
