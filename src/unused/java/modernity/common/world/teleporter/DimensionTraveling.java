/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.common.world.teleporter;

import modernity.api.entity.ICustomDimensionTravelEntity;
import modernity.api.reflect.FieldAccessor;
import modernity.api.reflect.MethodAccessor;
import modernity.api.util.Events;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Teleporter;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Utility class for having entities travelling between dimensions using custom teleporters. It must temporarily
 * replace the {@code ITeleporter} api that forge has removed.
 */
public final class DimensionTraveling {
    private static final MethodAccessor<ServerPlayerEntity, Void> triggerCriterionAccessor = new MethodAccessor<>( ServerPlayerEntity.class, "func_213846_b", ServerWorld.class );
    private static final MethodAccessor<AbstractVillagerEntity, Void> clearCustomerAccessor = new MethodAccessor<>( AbstractVillagerEntity.class, "func_213750_eg" );
    private static final MethodAccessor<ItemEntity, Void> searchForOtherItemsNearbyAccessor = new MethodAccessor<>( ItemEntity.class, "func_85054_d" );

    private static final FieldAccessor<ServerPlayerEntity, Boolean> invulnerableDimensionChangeAccessor = new FieldAccessor<>( ServerPlayerEntity.class, "field_184851_cj" );
    private static final FieldAccessor<ServerPlayerEntity, Boolean> seenCreditsAccessor = new FieldAccessor<>( ServerPlayerEntity.class, "field_192040_cp" );
    private static final FieldAccessor<ServerPlayerEntity, Vec3d> enteredNetherPositionAccessor = new FieldAccessor<>( ServerPlayerEntity.class, "field_193110_cw" );
    private static final FieldAccessor<ServerPlayerEntity, Integer> lastExperienceAccessor = new FieldAccessor<>( ServerPlayerEntity.class, "field_71144_ck" );
    private static final FieldAccessor<ServerPlayerEntity, Float> lastHealthAccessor = new FieldAccessor<>( ServerPlayerEntity.class, "field_71149_ch" );
    private static final FieldAccessor<ServerPlayerEntity, Integer> lastFoodLevelAccessor = new FieldAccessor<>( ServerPlayerEntity.class, "field_71146_ci" );
    private static final FieldAccessor<ContainerMinecartEntity, Boolean> dropContentsWhenDeadAccessor = new FieldAccessor<>( ContainerMinecartEntity.class, "field_94112_b" );
    private static final FieldAccessor<ThrowableEntity, LivingEntity> ownerAccessor = new FieldAccessor<>( ThrowableEntity.class, "field_70192_c" );


    private DimensionTraveling() {
    }

    public static Entity changeDimension( Entity entity, DimensionType to ) {
        MinecraftServer server = entity.getServer();
        assert server != null;
        ServerWorld world = server.getWorld( to );
        return changeDimension( entity, to, world.getDefaultTeleporter() );
    }

    public static Entity changeDimension( Entity entity, DimensionType to, Teleporter tp ) {
        if( entity instanceof ServerPlayerEntity ) {
            return changeDimPlayer( (ServerPlayerEntity) entity, to, tp );
        } else if( entity instanceof AbstractVillagerEntity ) {
            return changeDimVillager( (AbstractVillagerEntity) entity, to, tp );
        } else if( entity instanceof ContainerMinecartEntity ) {
            return changeDimMinecart( (ContainerMinecartEntity) entity, to, tp );
        } else if( entity instanceof EnderPearlEntity ) {
            return changeDimEnderPearl( (EnderPearlEntity) entity, to, tp );
        } else if( entity instanceof ItemEntity ) {
            return changeDimItem( (ItemEntity) entity, to, tp );
        } else if( entity instanceof ICustomDimensionTravelEntity ) {
            return changeDimCustom( (ICustomDimensionTravelEntity) entity, entity, to, tp );
        } else {
            return changeDim( entity, to, tp );
        }
    }

    private static Entity changeDim( Entity entity, DimensionType to, Teleporter tp ) {
        if( ! ForgeHooks.onTravelToDimension( entity, to ) ) return null;

        if( ! entity.world.isRemote && ! entity.removed ) {
            entity.world.getProfiler().startSection( "changeDimension" );
            MinecraftServer server = entity.getServer();
            assert server != null;

            DimensionType from = entity.dimension;
            ServerWorld fromWorld = server.getWorld( from );
            ServerWorld toWorld = server.getWorld( to );
            entity.dimension = to;
            entity.detach();
            entity.world.getProfiler().startSection( "reposition" );
            Vec3d motion = entity.getMotion();
            float additionalRotation = 0.0F;
            BlockPos pos;
            if( from == DimensionType.THE_END && to == DimensionType.OVERWORLD ) {
                pos = toWorld.getHeight( Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, toWorld.getSpawnPoint() );
            } else if( to == DimensionType.THE_END ) {
                pos = toWorld.getSpawnCoordinate();
            } else {
                double movementFactor = fromWorld.getDimension().getMovementFactor() / toWorld.getDimension().getMovementFactor();
                double newX = entity.posX * movementFactor;
                double newZ = entity.posZ * movementFactor;

                double borderMinX = Math.min( - 3E7, toWorld.getWorldBorder().minX() + 16 );
                double borderMinZ = Math.min( - 3E7, toWorld.getWorldBorder().minZ() + 16 );
                double morderMaxX = Math.min( 3E7, toWorld.getWorldBorder().maxX() - 16 );
                double borderMaxZ = Math.min( 3E7, toWorld.getWorldBorder().maxZ() - 16 );
                newX = MathHelper.clamp( newX, borderMinX, morderMaxX );
                newZ = MathHelper.clamp( newZ, borderMinZ, borderMaxZ );

                Vec3d lastPortalPos = entity.getLastPortalVec();
                pos = new BlockPos( newX, entity.posY, newZ );

                BlockPattern.PortalInfo portalInfo = tp.func_222272_a( pos, motion, entity.getTeleportDirection(), lastPortalPos.x, lastPortalPos.y, entity instanceof PlayerEntity );
                if( portalInfo == null ) {
                    return null;
                }

                pos = new BlockPos( portalInfo.field_222505_a );
                motion = portalInfo.field_222506_b;
                additionalRotation = (float) portalInfo.field_222507_c;
            }

            assert pos != null;

            entity.world.getProfiler().endStartSection( "reloading" );
            Entity newEntity = entity.getType().create( toWorld );
            if( newEntity != null ) {
                newEntity.copyDataFromOld( entity );
                newEntity.moveToBlockPosAndAngles( pos, newEntity.rotationYaw + additionalRotation, newEntity.rotationPitch );
                newEntity.setMotion( motion );
                toWorld.func_217460_e( newEntity );
            }

            entity.remove( false );
            entity.world.getProfiler().endSection();
            fromWorld.resetUpdateEntityTick();
            toWorld.resetUpdateEntityTick();
            entity.world.getProfiler().endSection();
            return newEntity;
        } else {
            return null;
        }
    }

    private static Entity changeDimCustom( ICustomDimensionTravelEntity entity, Entity et, DimensionType to, Teleporter tp ) {
        Entity ne;
        if( entity.preTravelDimension( to, tp ) ) {
            ne = entity.customTravelDimension( to, tp );
        } else {
            ne = changeDim( et, to, tp );
        }
        entity.postTravelDimension( to, tp, ne );
        return ne;
    }

    private static Entity changeDimItem( ItemEntity item, DimensionType to, Teleporter tp ) {
        Entity entity = changeDim( item, to, tp );
        if( ! item.world.isRemote && entity instanceof ItemEntity ) {
            searchForOtherItemsNearbyAccessor.call( (ItemEntity) entity );
        }

        return entity;
    }

    private static Entity changeDimEnderPearl( EnderPearlEntity enderPearl, DimensionType to, Teleporter tp ) {
        if( ownerAccessor.get( enderPearl ).dimension != to ) {
            ownerAccessor.set( enderPearl, null );
        }
        return changeDim( enderPearl, to, tp );
    }

    private static Entity changeDimMinecart( ContainerMinecartEntity minecart, DimensionType to, Teleporter tp ) {
        dropContentsWhenDeadAccessor.set( minecart, false );
        return changeDim( minecart, to, tp );
    }

    private static Entity changeDimVillager( AbstractVillagerEntity villager, DimensionType to, Teleporter tp ) {
        clearCustomerAccessor.call( villager );
        return changeDim( villager, to, tp );
    }

    private static Entity changeDimPlayer( ServerPlayerEntity entity, DimensionType to, Teleporter tp ) {
        if( ! ForgeHooks.onTravelToDimension( entity, to ) ) return null;
        invulnerableDimensionChangeAccessor.set( entity, true ); //entity.invulnerableDimensionChange = true;
        DimensionType from = entity.dimension;
        if( from == DimensionType.THE_END && to == DimensionType.OVERWORLD ) {
            entity.detach();
            entity.getServerWorld().removePlayer( entity );
            if( ! entity.queuedEndExit ) {
                entity.queuedEndExit = true;
                FieldAccessor<ServerPlayerEntity, Boolean>.Manager seenCredits = seenCreditsAccessor.forInstance( entity );
                entity.connection.sendPacket( new SChangeGameStatePacket( 4, seenCredits.get() ? 0.0F : 1.0F ) );
                seenCredits.set( true );
            }

            return entity;
        } else {
            ServerWorld fromWorld = entity.server.getWorld( from );
            ServerWorld toWorld = entity.server.getWorld( to );
            entity.dimension = to;

            WorldInfo fromWorldInfo = entity.world.getWorldInfo();
            NetworkHooks.sendDimensionDataPacket( entity.connection.netManager, entity );
            entity.connection.sendPacket( new SRespawnPacket( to, fromWorldInfo.getGenerator(), entity.interactionManager.getGameType() ) );
            entity.connection.sendPacket( new SServerDifficultyPacket( fromWorldInfo.getDifficulty(), fromWorldInfo.isDifficultyLocked() ) );

            PlayerList pl = entity.server.getPlayerList();
            pl.updatePermissionLevel( entity );
            fromWorld.removeEntity( entity, true ); //Forge: the player entity is moved to the new world, NOT cloned. So keep the data alive with no matching invalidate call.
            entity.revive();
            double posX = entity.posX;
            double posY = entity.posY;
            double posZ = entity.posZ;
            float rotPitch = entity.rotationPitch;
            float rotYaw = entity.rotationYaw;
            float oldRotYaw = rotYaw;

            fromWorld.getProfiler().startSection( "moving" );

            double moveFactor = fromWorld.getDimension().getMovementFactor() / toWorld.getDimension().getMovementFactor();
            posX *= moveFactor;
            posZ *= moveFactor;
            if( from == DimensionType.OVERWORLD && to == DimensionType.THE_NETHER ) {
                enteredNetherPositionAccessor.set( entity, new Vec3d( entity.posX, entity.posY, entity.posZ ) );
                // entity.enteredNetherPosition = new Vec3d(entity.posX, entity.posY, entity.posZ);
            } else if( from == DimensionType.OVERWORLD && to == DimensionType.THE_END ) {
                BlockPos spawn = toWorld.getSpawnCoordinate();
                assert spawn != null;
                posX = spawn.getX();
                posY = spawn.getY();
                posZ = spawn.getZ();
                rotYaw = 90;
                rotPitch = 0;
            }

            entity.setLocationAndAngles( posX, posY, posZ, rotYaw, rotPitch );
            fromWorld.getProfiler().endSection();
            fromWorld.getProfiler().startSection( "placing" );

            double borderMinX = Math.min( - 30000000, toWorld.getWorldBorder().minX() + 16.0D );
            double borderMinZ = Math.min( - 30000000, toWorld.getWorldBorder().minZ() + 16.0D );
            double borderMaxX = Math.min( 30000000, toWorld.getWorldBorder().maxX() - 16.0D );
            double borderMaxZ = Math.min( 30000000, toWorld.getWorldBorder().maxZ() - 16.0D );
            posX = MathHelper.clamp( posX, borderMinX, borderMaxX );
            posZ = MathHelper.clamp( posZ, borderMinZ, borderMaxZ );

            entity.setLocationAndAngles( posX, posY, posZ, rotYaw, rotPitch );

            if( to == DimensionType.THE_END ) {
                int px = MathHelper.floor( entity.posX );
                int py = MathHelper.floor( entity.posY ) - 1;
                int pz = MathHelper.floor( entity.posZ );

                for( int zi = - 2; zi <= 2; ++ zi ) {
                    for( int xi = - 2; xi <= 2; ++ xi ) {
                        for( int yi = - 1; yi < 3; ++ yi ) {
                            int rx = px + xi;
                            int ry = py + yi;
                            int rz = pz - zi;
                            boolean obsidian = yi < 0;
                            toWorld.setBlockState(
                                new BlockPos( rx, ry, rz ),
                                obsidian ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState()
                            );
                        }
                    }
                }

                entity.setLocationAndAngles( px, py, pz, rotYaw, 0 );
                entity.setMotion( Vec3d.ZERO );
            } else if( ! tp.func_222268_a( entity, oldRotYaw ) ) {
                tp.makePortal( entity );
                tp.func_222268_a( entity, oldRotYaw );
            }

            fromWorld.getProfiler().endSection();
            entity.setWorld( toWorld );
            toWorld.func_217447_b( entity );
            triggerCriterionAccessor.call( entity, fromWorld ); // entity.func_213846_b(fromWorld);
            entity.connection.setPlayerLocation( entity.posX, entity.posY, entity.posZ, rotYaw, rotPitch );
            entity.interactionManager.setWorld( toWorld );
            entity.connection.sendPacket( new SPlayerAbilitiesPacket( entity.abilities ) );
            pl.sendWorldInfo( entity, toWorld );
            pl.sendInventory( entity );

            for( EffectInstance effectinstance : entity.getActivePotionEffects() ) {
                entity.connection.sendPacket( new SPlayEntityEffectPacket( entity.getEntityId(), effectinstance ) );
            }


            entity.connection.sendPacket( new SPlaySoundEventPacket( Events.TRAVEL_PORTAL, BlockPos.ZERO, 0, false ) );

            lastExperienceAccessor.set( entity, - 1 ); // entity.lastExperience = - 1;
            lastHealthAccessor.set( entity, - 1.0F ); // entity.lastHealth = - 1.0F;
            lastFoodLevelAccessor.set( entity, - 1 ); // entity.lastFoodLevel = - 1.0F;

            BasicEventHooks.firePlayerChangedDimensionEvent( entity, from, to );
            return entity;
        }
    }
}
