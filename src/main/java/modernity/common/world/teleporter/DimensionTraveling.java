package modernity.common.world.teleporter;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;

/**
 * Unused utility class for having entities travelling between dimensions using custom teleporters. It must temporarily
 * replace the {@code ITeleporter} api that forge has removed.
 * @deprecated This class is not stable and should be finished before use.
 */
@Deprecated
public final class DimensionTraveling {
    private DimensionTraveling() {
    }

    public static Entity changeDimension( ServerPlayerEntity e, DimensionType destination, Teleporter tp ) {
        if( ! net.minecraftforge.common.ForgeHooks.onTravelToDimension( e, destination ) ) return null;
        DimensionType dimensiontype = e.dimension;
        {
            ServerWorld serverworld = e.server.getWorld( dimensiontype );
            e.dimension = destination;
            ServerWorld serverworld1 = e.server.getWorld( destination );
            WorldInfo worldinfo = e.world.getWorldInfo();
            e.connection.sendPacket( new SRespawnPacket( destination, worldinfo.getGenerator(), e.interactionManager.getGameType() ) );
            e.connection.sendPacket( new SServerDifficultyPacket( worldinfo.getDifficulty(), worldinfo.isDifficultyLocked() ) );
            PlayerList playerlist = e.server.getPlayerList();
            playerlist.updatePermissionLevel( e );
            serverworld.removeEntity( e, true ); //Forge: the player entity is moved to the new world, NOT cloned. So keep the data alive with no matching invalidate call.
            e.revive();
            double d0 = e.posX;
            double d1 = e.posY;
            double d2 = e.posZ;
            float f = e.rotationPitch;
            float f1 = e.rotationYaw;
            double d3 = 8.0D;
            float f2 = f1;
            serverworld.getProfiler().startSection( "moving" );
            double moveFactor = serverworld.getDimension().getMovementFactor() / serverworld1.getDimension().getMovementFactor();
            d0 *= moveFactor;
            d2 *= moveFactor;

            e.setLocationAndAngles( d0, d1, d2, f1, f );
            serverworld.getProfiler().endSection();
            serverworld.getProfiler().startSection( "placing" );
            double d7 = Math.min( - 2.9999872E7D, serverworld1.getWorldBorder().minX() + 16.0D );
            double d4 = Math.min( - 2.9999872E7D, serverworld1.getWorldBorder().minZ() + 16.0D );
            double d5 = Math.min( 2.9999872E7D, serverworld1.getWorldBorder().maxX() - 16.0D );
            double d6 = Math.min( 2.9999872E7D, serverworld1.getWorldBorder().maxZ() - 16.0D );
            d0 = MathHelper.clamp( d0, d7, d5 );
            d2 = MathHelper.clamp( d2, d4, d6 );
            e.setLocationAndAngles( d0, d1, d2, f1, f );
            if( ! tp.func_222268_a( e, f2 ) ) {
                tp.makePortal( e );
                tp.func_222268_a( e, f2 );
            }

            serverworld.getProfiler().endSection();
            e.setWorld( serverworld1 );
            serverworld1.func_217447_b( e );
            triggerCriterion( e, serverworld );
            e.connection.setPlayerLocation( e.posX, e.posY, e.posZ, f1, f );
            e.interactionManager.setWorld( serverworld1 );
            e.connection.sendPacket( new SPlayerAbilitiesPacket( e.abilities ) );
            playerlist.sendWorldInfo( e, serverworld1 );
            playerlist.sendInventory( e );

            for( EffectInstance effectinstance : e.getActivePotionEffects() ) {
                e.connection.sendPacket( new SPlayEntityEffectPacket( e.getEntityId(), effectinstance ) );
            }

            e.connection.sendPacket( new SPlaySoundEventPacket( 1032, BlockPos.ZERO, 0, false ) );
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerChangedDimensionEvent( e, dimensiontype, destination );
            return e;
        }
    }

    private static void triggerCriterion( ServerPlayerEntity entity, ServerWorld world ) {
        DimensionType from = world.dimension.getType();
        DimensionType to = entity.world.dimension.getType();
        CriteriaTriggers.CHANGED_DIMENSION.trigger( entity, from, to );
    }
}
