/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 22 - 2020
 * Author: rgsw
 */

package modernity.common.entity;

import modernity.common.item.MDItems;
import modernity.common.particle.MDParticleTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class ShadeBallEntity extends ThrownItemEntity {
    private ItemStack renderStack = new ItemStack( MDItems.SHADE_BALL );

    @SuppressWarnings( "unchecked" )
    public ShadeBallEntity( EntityType type, World world ) {
        super( type, world );
    }

    public ShadeBallEntity( LivingEntity ent, World world ) {
        super( MDEntityTypes.SHADE_BALL, ent, world );
    }

    public ShadeBallEntity( double x, double y, double z, World world ) {
        super( MDEntityTypes.SHADE_BALL, x, y, z, world );
    }

    @Override
    protected Item getThrownItem() {
        return MDItems.SHADE_BALL;
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public ItemStack getItem() {
        return renderStack;
    }

    @Override
    protected void onImpact( RayTraceResult result ) {
        LivingEntity thrower = getThrower();

        // Damage entities
        if( result.getType() == RayTraceResult.Type.ENTITY && result instanceof EntityRayTraceResult ) {
            Entity entity = ( (EntityRayTraceResult) result ).getEntity();
            if( entity == thrower ) {
                return;
            }

            entity.attackEntityFrom( DamageSource.causeThrownDamage( this, thrower ), 0 );
        }

        // Teleport through gateway
        if( result.getType() == RayTraceResult.Type.BLOCK && result instanceof BlockRayTraceResult ) {
            BlockPos pos = ( (BlockRayTraceResult) result ).getPos();
            TileEntity te = world.getTileEntity( pos );
            if( te instanceof EndGatewayTileEntity ) {
                EndGatewayTileEntity gateway = (EndGatewayTileEntity) te;
                if( thrower != null ) {
                    if( thrower instanceof ServerPlayerEntity ) {
                        CriteriaTriggers.ENTER_BLOCK.trigger( (ServerPlayerEntity) thrower, world.getBlockState( pos ) );
                    }

                    gateway.teleportEntity( thrower );
                    remove();
                    return;
                }

                gateway.teleportEntity( this );
                return;
            }
        }

        // Spawn particles
        for( int i = 0; i < 32; ++ i ) {
            world.addParticle( MDParticleTypes.SHADE, getPosX(), getPosY() + rand.nextDouble() * 2, getPosZ(), rand.nextGaussian(), 0, rand.nextGaussian() );
        }

        // Attack player
        if( ! world.isRemote ) {
            if( thrower instanceof ServerPlayerEntity ) {
                ServerPlayerEntity player = (ServerPlayerEntity) thrower;
                EnderTeleportEvent event = new EnderTeleportEvent( player, getPosX(), getPosY(), getPosZ(), 5 );
                if( ! MinecraftForge.EVENT_BUS.post( event ) ) {
                    if( player.connection.getNetworkManager().isChannelOpen() && player.world == world && ! player.isSleeping() ) {
                        if( thrower.isPassenger() ) {
                            thrower.stopRiding();
                        }

                        thrower.setPositionAndUpdate( event.getTargetX(), event.getTargetY(), event.getTargetZ() );
                        thrower.fallDistance = 0.0F;
                        thrower.attackEntityFrom( DamageSource.FALL, event.getAttackDamage() );
                    }
                }
            } else if( thrower != null ) {
                thrower.setPositionAndUpdate( getPosX(), getPosY(), getPosZ() );
                thrower.fallDistance = 0;
            }

            remove();
        }
    }


    @Override
    public void tick() {
        LivingEntity thrower = getThrower();
        if( thrower instanceof PlayerEntity && ! thrower.isAlive() ) {
            remove();
        } else {
            super.tick();
        }

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return null;
//        return Modernity.network().toPlayClientPacket( new SSpawnEntityPacket( this ) );
    }
}
