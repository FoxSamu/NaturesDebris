/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 8 - 2019
 */

package modernity.common.world.teleporter;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import modernity.api.util.MovingBlockPos;
import modernity.common.block.MDBlockTags;
import modernity.common.block.MDBlocks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;

public class ModernityTeleporter extends Teleporter {
    protected final Long2ObjectMap<Teleporter.PortalPosition> cache = new Long2ObjectOpenHashMap<>( 4096 );

    public ModernityTeleporter( WorldServer world ) {
        super( world );
    }

    public void placeInPortal( Entity entity, float yaw ) {
        if( ! this.placeInExistingPortal( entity, yaw ) ) {
            this.makePortal( entity );
            this.placeInExistingPortal( entity, yaw );
        }
    }

    public boolean placeInExistingPortal( Entity entity, float yaw ) {
        int entityX = MathHelper.floor( entity.posX );
        int entityZ = MathHelper.floor( entity.posZ );

        double distance = - 1.0D;
        boolean notCached = true;
        BlockPos portalPos = BlockPos.ORIGIN;

        long entityPosLong = ChunkPos.asLong( entityX, entityZ );
        if( cache.containsKey( entityPosLong ) ) {
            Teleporter.PortalPosition pos = cache.get( entityPosLong );
            distance = 0;
            portalPos = pos;
            pos.lastUpdateTime = world.getGameTime();
            notCached = false;
        } else {
            BlockPos entityPos = new BlockPos( entity );

            MovingBlockPos mpos = new MovingBlockPos();
            for( int x = - 128; x <= 128; ++ x ) {
                for( int z = - 128; z <= 128; ++ z ) {
                    for( int y = 255; y >= 0; y-- ) {
                        mpos.setPos( entityPos ).addPos( x, 0, z ).setYPos( y );

                        if( world.getBlockState( mpos ).isIn( MDBlockTags.PORTAL ) ) {

                            double portalDistance = mpos.distanceSq( entityPos );
                            if( distance < 0 || portalDistance < distance ) {
                                distance = portalDistance;
                                portalPos = mpos.toImmutable();
                            }
                        }
                    }
                }
            }
        }

        if( distance >= 0 ) {
            MovingBlockPos mpos = new MovingBlockPos();
            boolean found = false;

            Distance:
            for( int i = 0; i < 20; i++ ) {
                Facings:
                for( EnumFacing facing : EnumFacing.Plane.HORIZONTAL ) {
                    mpos.setPos( portalPos ).move( facing, i );

                    if( ! world.getBlockState( mpos ).isIn( MDBlockTags.PORTAL ) ) {
                        for( int h = 0; h < 20; h++ ) {
                            IBlockState state = world.getBlockState( mpos );
                            if( state.isIn( MDBlockTags.PORTAL ) ) {
                                continue Facings;
                            }
                            if( state.isSolid() ) {
                                break;
                            }
                            mpos.moveDown();
                        }
                        for( int h = 0; h < 20; h++ ) {
                            IBlockState state = world.getBlockState( mpos );
                            if( state.isIn( MDBlockTags.PORTAL ) ) {
                                continue Facings;
                            }
                            if( ! state.isSolid() ) {
                                break;
                            }
                            mpos.moveUp();
                        }
                        found = true;
                        break Distance;
                    }
                }
            }

            if( ! found ) {
                mpos.setPos( portalPos ).move( EnumFacing.Plane.HORIZONTAL.random( random ) );
                mpos.moveDown();
                world.setBlockState( mpos, Blocks.STONE_BRICKS.getDefaultState() );
                mpos.moveUp();
            }

            portalPos = mpos.toImmutable();

            if( notCached ) {
                cache.put( entityPosLong, new Teleporter.PortalPosition( portalPos, world.getGameTime() ) );
            }

            double portalX = portalPos.getX() + 0.5;
            double portalY = portalPos.getY() + 0.5;
            double portalZ = portalPos.getZ() + 0.5;

            entity.motionX = 0;
            entity.motionZ = 0;
            entity.rotationYaw = yaw;
            if( entity instanceof EntityPlayerMP ) {
                ( (EntityPlayerMP) entity ).connection.setPlayerLocation( portalX, portalY, portalZ, entity.rotationYaw, entity.rotationPitch );
                ( (EntityPlayerMP) entity ).connection.captureCurrentPosition();
            } else {
                entity.setLocationAndAngles( portalX, portalPos.getY(), portalZ, entity.rotationYaw, entity.rotationPitch );
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean makePortal( Entity entity ) {
        double distance = - 1;
        int entityX = MathHelper.floor( entity.posX );
        int entityY = MathHelper.floor( entity.posY );
        int entityZ = MathHelper.floor( entity.posZ );
        int portalX = entityX;
        int portalY = entityY;
        int portalZ = entityZ;
        MovingBlockPos mpos = new MovingBlockPos();

        for( int x = entityX - 16; x <= entityX + 16; ++ x ) {
            double localX = x + 0.5 - entity.posX;

            for( int z = entityZ - 16; z <= entityZ + 16; ++ z ) {
                double localZ = z + 0.5 - entity.posZ;

                for( int y = 255; y >= 0; -- y ) {
                    if( world.getBlockState( mpos.setPos( x, y, z ) ).getMaterial().isSolid() ) {
                        mpos.moveUp();

                        double dist = localX * localX + localZ * localZ;
                        if( distance < 0 || dist < distance ) {
                            distance = dist;
                            portalX = x;
                            portalY = y;
                            portalZ = z;
                            break;
                        }
                    }
                }
            }
        }

        if( distance < 0 ) {
            portalY = MathHelper.clamp( portalY, 76, world.getActualHeight() - 10 );
        }

        createPortal( portalX, portalY, portalZ );

        return true;
    }

    private void createPortal( int x, int y, int z ) {
        MovingBlockPos mpos = new MovingBlockPos();
        int portalY = - 1;
        Y:
        for( int dy = y; dy < 256; dy++ ) {
            for( int dx = x - 2; dx <= x + 2; dx++ ) {
                for( int dz = z - 2; dz <= z + 2; dz++ ) {
                    Material mat = world.getBlockState( mpos.setPos( dx, dy, dz ) ).getMaterial();
                    if( mat.isSolid() || mat.isLiquid() ) {
                        continue Y;
                    }
                }
            }
            portalY = dy;
            break;
        }

        boolean inBlock = false;

        if( portalY == - 1 ) {
            portalY = y;
            inBlock = true;
        }

        for( int dx = - 2; dx <= 2; dx++ ) {
            for( int dz = - 2; dz <= 2; dz++ ) {
                mpos.setPos( x, portalY, z ).addPos( dx, 0, dz );
                int ax = Math.abs( dx );
                int az = Math.abs( dz );

                for( int i = 0; i < 3; i++ ) {
                    mpos.moveUp();
                    boolean corner = ax == 2 && az == 2;
                    world.setBlockState( mpos, ( corner ? Blocks.STONE_BRICKS : Blocks.AIR ).getDefaultState() );
                }
                mpos.moveDown( 3 );

                if( dx == 0 && dz == 0 ) {
                    world.setBlockState( mpos, MDBlocks.PORTAL_FLUID.getDefaultState() );
                } else if( inBlock || ax == 2 && az == 2 || ax != 2 && az != 2 ) {
                    world.setBlockState( mpos, Blocks.STONE_BRICKS.getDefaultState() );
                } else if( ax == 2 ) {
                    world.setBlockState( mpos, Blocks.STONE_BRICK_STAIRS.getDefaultState().with( BlockStairs.FACING, dx > 0 ? EnumFacing.WEST : EnumFacing.EAST ) );
                } else {
                    world.setBlockState( mpos, Blocks.STONE_BRICK_STAIRS.getDefaultState().with( BlockStairs.FACING, dz > 0 ? EnumFacing.NORTH : EnumFacing.SOUTH ) );
                }
                mpos.moveDown();
                world.setBlockState( mpos, Blocks.STONE_BRICKS.getDefaultState() );
                mpos.moveDown();

                Material mat = world.getBlockState( mpos ).getMaterial();
                while( ! mat.isSolid() && mpos.getY() >= 0 ) {
                    world.setBlockState( mpos, Blocks.STONE_BRICKS.getDefaultState() );
                    mat = world.getBlockState( mpos.moveDown() ).getMaterial();
                }
            }
        }
    }

    public void tick( long worldTime ) {
        if( worldTime % 100L == 0L ) {
            long l = worldTime - 300L;

            this.cache.values().removeIf( pos -> pos == null || pos.lastUpdateTime < l );
        }

    }

    public class PortalPosition extends BlockPos {
        public long lastUpdateTime;

        public PortalPosition( BlockPos pos, long lastUpdate ) {
            super( pos.getX(), pos.getY(), pos.getZ() );
            this.lastUpdateTime = lastUpdate;
        }
    }

    @Override
    public void placeEntity( World world, Entity entity, float yaw ) {
        if( entity instanceof EntityPlayerMP )
            placeInPortal( entity, yaw );
        else
            placeInExistingPortal( entity, yaw );
    }

    public static ModernityTeleporter getTeleporter( WorldServer world ) {
        for( ITeleporter tp : world.customTeleporters ) {
            if( tp instanceof ModernityTeleporter ) {
                return (ModernityTeleporter) tp;
            }
        }
        ModernityTeleporter tp = new ModernityTeleporter( world );
        world.customTeleporters.add( tp );
        return tp;
    }
}
