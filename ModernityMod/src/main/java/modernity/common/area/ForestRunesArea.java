/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 15 - 2020
 * Author: rgsw
 */

package modernity.common.area;

import modernity.common.area.core.*;
import modernity.common.block.MDBuildingBlocks;
import modernity.common.block.portal.AbstractPortalFrameBlock;
import modernity.common.block.portal.HorizontalPortalFrameBlock;
import modernity.common.block.portal.PortalCornerBlock;
import modernity.common.block.portal.PortalCornerState;
import modernity.common.world.dimen.MDDimensions;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.List;
import java.util.Random;

public class ForestRunesArea extends MessagingArea<ForestRunesArea> implements IServerTickableArea, IParticleSpawningArea {
    private static final BlockPos[] CORNERS = {
        new BlockPos( 4, 3, 4 ),
        new BlockPos( 4, 3, 7 ),
        new BlockPos( 7, 3, 7 ),
        new BlockPos( 7, 3, 4 )
    };

    private final PortalCornerState[] states = {
        PortalCornerState.EXHAUSTED,
        PortalCornerState.EXHAUSTED,
        PortalCornerState.EXHAUSTED,
        PortalCornerState.EXHAUSTED
    };

    private boolean active;

    public ForestRunesArea( World world, AreaBox box ) {
        super( MDAreas.FOREST_RUNES, world, box );

        registerMessage( 0, ActivationMessage.class, () -> new ActivationMessage( false ) );
    }

    public static ForestRunesArea create( World world, AreaBox box ) {
        ForestRunesArea area = new ForestRunesArea( world, box );
        ServerWorldAreaManager
            .get( world )
            .ifPresent( manager -> manager.addArea( area ) );
        return area;
    }

    public static ForestRunesArea createAt( World world, int x, int z ) {
        int height = 512;
        MovingBlockPos mpos = new MovingBlockPos();
        for( int mx = x - 3; mx < x + 3; mx++ ) {
            for( int mz = z - 3; mz < z + 3; mz++ ) {
                int h = 512;
                for( int y = 255; y >= 0; y-- ) {
                    mpos.setPos( mx, y, mz );
                    BlockState state = world.getBlockState( mpos );
                    if( state.isSolid() && ! state.isIn( BlockTags.LEAVES ) ) {
                        h = y - 1;
                        break;
                    }
                }
                if( h < height ) height = h;
            }
        }

        AreaBox box = AreaBox.makeWithSize( x - 6, height - 1, z - 6, 12, 12, 12 );
        ForestRunesArea area = create( world, box );
        area.buildActivePortal();
        return area;
    }

    public static ForestRunesArea findAt( World world, int x, int z ) {
        ForestRunesArea[] area = new ForestRunesArea[ 1 ];

        ServerWorldAreaManager
            .get( world )
            .ifPresent( manager -> {
                int cx = x >> 4;
                int cz = z >> 4;
                boolean[] br = { false };
                for( int r = 0; r < 13; r++ ) {
                    for( int mx = - r; mx <= r; mx++ ) {
                        for( int mz = - r; mz <= r; mz++ ) {
                            if( Math.abs( mx ) == r || Math.abs( mz ) == r ) {
                                IAreaReferenceChunk chunk = manager.getChunk( mx + cx, mz + cz );
                                chunk.referenceStream().forEach( ref -> {
                                    Area a = manager.getLoadedArea( ref );
                                    if( a instanceof ForestRunesArea && ( (ForestRunesArea) a ).isActive() ) {
                                        area[ 0 ] = (ForestRunesArea) a;
                                        br[ 0 ] = true;
                                    }
                                } );
                                if( br[ 0 ] ) break;
                            }
                        }
                    }
                }
            } );

        return area[ 0 ];
    }

    public static ForestRunesArea get( World world, long ref ) {
        ForestRunesArea[] area = new ForestRunesArea[ 1 ];
        ServerWorldAreaManager
            .get( world )
            .ifPresent( manager -> {
                Area a = manager.getLoadedArea( ref );
                if( a instanceof ForestRunesArea ) {
                    area[ 0 ] = (ForestRunesArea) a;
                }
            } );
        return area[ 0 ];
    }

    public void buildActivePortal() {
        for( int x = 2; x <= 9; x++ ) {
            for( int z = 2; z <= 9; z++ ) {
                if( ( x == 2 || x == 9 ) && ( z == 2 || z == 9 ) ) {
                    continue;
                }
                for( int y = 2; y <= 6; y++ ) {
                    setBlockState( new BlockPos( x, y, z ), Blocks.AIR.getDefaultState() );
                }
                setBlockState( new BlockPos( x, 1, z ), MDBuildingBlocks.DARK_STONE_BRICKS.getDefaultState() );
            }
        }

        setBlockState( new BlockPos( 4, 2, 4 ), MDBuildingBlocks.VERTICAL_PORTAL_FRAME.getDefaultState() );
        setBlockState( new BlockPos( 4, 2, 7 ), MDBuildingBlocks.VERTICAL_PORTAL_FRAME.getDefaultState() );
        setBlockState( new BlockPos( 7, 2, 7 ), MDBuildingBlocks.VERTICAL_PORTAL_FRAME.getDefaultState() );
        setBlockState( new BlockPos( 7, 2, 4 ), MDBuildingBlocks.VERTICAL_PORTAL_FRAME.getDefaultState() );

        setBlockState( new BlockPos( 4, 2, 5 ), MDBuildingBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.Z ) );
        setBlockState( new BlockPos( 4, 2, 6 ), MDBuildingBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.Z ) );
        setBlockState( new BlockPos( 7, 2, 5 ), MDBuildingBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.Z ) );
        setBlockState( new BlockPos( 7, 2, 6 ), MDBuildingBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.Z ) );
        setBlockState( new BlockPos( 5, 2, 4 ), MDBuildingBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.X ) );
        setBlockState( new BlockPos( 6, 2, 4 ), MDBuildingBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.X ) );
        setBlockState( new BlockPos( 5, 2, 7 ), MDBuildingBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.X ) );
        setBlockState( new BlockPos( 6, 2, 7 ), MDBuildingBlocks.HORIZONTAL_PORTAL_FRAME.getDefaultState().with( HorizontalPortalFrameBlock.DIRECTION, Direction.Axis.X ) );

        // Set tot EYE state because these block changes still cause the portal to activate
        setBlockState( new BlockPos( 4, 3, 4 ), MDBuildingBlocks.PORTAL_CORNER.getDefaultState().with( PortalCornerBlock.STATE, PortalCornerState.EYE ) );
        setBlockState( new BlockPos( 4, 3, 7 ), MDBuildingBlocks.PORTAL_CORNER.getDefaultState().with( PortalCornerBlock.STATE, PortalCornerState.EYE ) );
        setBlockState( new BlockPos( 7, 3, 7 ), MDBuildingBlocks.PORTAL_CORNER.getDefaultState().with( PortalCornerBlock.STATE, PortalCornerState.EYE ) );
        setBlockState( new BlockPos( 7, 3, 4 ), MDBuildingBlocks.PORTAL_CORNER.getDefaultState().with( PortalCornerBlock.STATE, PortalCornerState.EYE ) );

    }

    public boolean isActive() {
        return active;
    }

    private void activate() {
        active = true;

        MovingBlockPos mpos = new MovingBlockPos();
        for( int x = 4; x <= 7; x++ ) {
            for( int z = 4; z <= 7; z++ ) {
                mpos.setPos( x, 2, z );
                BlockState state = getBlockState( mpos );
                if( state.getBlock() instanceof AbstractPortalFrameBlock ) {
                    setBlockState( mpos, state.with( AbstractPortalFrameBlock.ACTIVE, true ) );
                }
            }
        }

        for( BlockPos pos : CORNERS ) {
            BlockState state = getBlockState( pos );
            if( state.getBlock() instanceof PortalCornerBlock ) {
                setBlockState( pos, state.with( PortalCornerBlock.STATE, PortalCornerState.ACTIVE ) );
            }
        }

        sendMessage( 8, new ActivationMessage( true ) );
    }

    private void deactivate() {
        active = false;

        MovingBlockPos mpos = new MovingBlockPos();
        for( int x = 4; x <= 7; x++ ) {
            for( int z = 4; z <= 7; z++ ) {
                mpos.setPos( x, 2, z );
                BlockState state = getBlockState( mpos );
                if( state.getBlock() instanceof AbstractPortalFrameBlock ) {
                    setBlockState( mpos, state.with( AbstractPortalFrameBlock.ACTIVE, false ) );
                }
            }
        }

        for( BlockPos pos : CORNERS ) {
            BlockState state = getBlockState( pos );
            if( state.getBlock() instanceof PortalCornerBlock ) {
                if( state.get( PortalCornerBlock.STATE ) == PortalCornerState.ACTIVE ) {
                    setBlockState( pos, state.with( PortalCornerBlock.STATE, PortalCornerState.EXHAUSTED ) );
                }
            }
        }

        sendMessage( 8, new ActivationMessage( false ) );
    }

    private void cornerUpdate( PortalCornerState state ) {
        if( state == PortalCornerState.ACTIVE ) return;

        int eyes = 0;
        for( int i = 0; i < 4; i++ ) {
            if( states[ i ] == PortalCornerState.EYE ) {
                eyes++;
            }
        }
        if( eyes == 4 ) {
            if( ! active ) activate();
        } else {
            if( active ) deactivate();
        }
    }

    private byte[] serializeStates() {
        return new byte[] {
            getStateByte( 0 ),
            getStateByte( 1 ),
            getStateByte( 2 ),
            getStateByte( 3 )
        };
    }

    private byte getStateByte( int index ) {
        return (byte) ( states[ index ] == null ? - 1 : states[ index ].ordinal() );
    }

    private void deserializeStates( byte[] b ) {
        for( int i = 0; i < 4; i++ ) {
            if( i >= b.length ) return;
            if( b[ i ] >= 0 )
                states[ i ] = PortalCornerState.values()[ b[ i ] ];
            else states[ i ] = null;
        }
    }

    @Override
    public void write( CompoundNBT nbt, SerializeType type ) {
        if( type == SerializeType.FILESYSTEM ) {
            nbt.putByteArray( "states", serializeStates() );
        }
        nbt.putBoolean( "active", active );
    }

    @Override
    public void read( CompoundNBT nbt, SerializeType type ) {
        if( type == SerializeType.FILESYSTEM ) {
            deserializeStates( nbt.getByteArray( "states" ) );
        }
        active = nbt.getBoolean( "active" );
    }

    @Override
    public void tickServer() {
        if( world.isAreaLoaded( box.minX, box.minY, box.minZ, box.maxX - 1, box.maxY - 1, box.maxZ - 1 ) ) {
            for( int i = 0; i < 4; i++ ) {
                BlockPos pos = CORNERS[ i ];
                BlockState state = getBlockState( pos );
                if( state.getBlock() == MDBuildingBlocks.PORTAL_CORNER ) {
                    PortalCornerState s = state.get( PortalCornerBlock.STATE );
                    if( s != states[ i ] ) {
                        states[ i ] = s;
                        cornerUpdate( s );
                    }
                } else if( states[ i ] != null ) {
                    states[ i ] = null;
                    cornerUpdate( null );
                }
            }
        }

        if( isActive() ) {

            AxisAlignedBB portalRegion = new AxisAlignedBB(
                box.minX + 5,
                box.minY + 2,
                box.minZ + 5,
                box.minX + 7,
                box.minY + 3,
                box.minZ + 7
            );
            List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity( null, portalRegion );

            if( ! entities.isEmpty() ) {
                DimensionType type = null;
                if( world.dimension.getType() == MDDimensions.MURK_SURFACE.getType() ) {
                    type = DimensionType.OVERWORLD;
                }
                if( world.dimension.getType() == DimensionType.OVERWORLD ) {
                    type = MDDimensions.MURK_SURFACE.getType();
                }
                if( type != null ) {
                    MinecraftServer server = world.getServer();
                    if( server != null ) {
//                        ServerWorld world = server.getWorld( type );
//                        RunesTeleporter tp = RunesTeleporter.get( world );
//                        for( Entity entity : entities ) {
//                            DimensionTraveling.changeDimension( entity, type, tp );
//                        }
                    }
                }
            }
        }
    }

    @Override
    public void particleTick( Random rand ) {
        if( isActive() ) {
            for( int i = 0; i < 4; i++ ) {
                double x = rand.nextDouble() * 2 + box.minX + 5;
                double y = rand.nextDouble() * 0.4 + box.minY + 2;
                double z = rand.nextDouble() * 2 + box.minZ + 5;

                world.addParticle( ParticleTypes.SMOKE, x, y, z, 0, 0, 0 );
            }

            for( int i = 0; i < 2; i++ ) {
                double x = rand.nextDouble() * 2 + box.minX + 5;
                double y = rand.nextDouble() * 0.4 + box.minY + 2;
                double z = rand.nextDouble() * 2 + box.minZ + 5;

                world.addParticle( ParticleTypes.POOF, x, y, z, 0, 0, 0 );
            }

            if( rand.nextInt( 3 ) == 0 ) {
                double x = rand.nextDouble() * 2 + box.minX + 5;
                double y = rand.nextDouble() * 0.4 + box.minY + 2;
                double z = rand.nextDouble() * 2 + box.minZ + 5;

                world.addParticle( ParticleTypes.ENTITY_EFFECT, x, y, z, 1, 0.7, 0 );
            }
        }
    }

    private static class ActivationMessage implements IAreaMessage<ForestRunesArea> {
        private boolean active;

        ActivationMessage( boolean active ) {
            this.active = active;
        }

        @Override
        public void write( PacketBuffer buf ) {
            buf.writeBoolean( active );
        }

        @Override
        public void read( PacketBuffer buf ) {
            active = buf.readBoolean();
        }

        @Override
        public void receive( ForestRunesArea area ) {
            area.active = active;
            System.out.println( "Activate" );
        }
    }
}
