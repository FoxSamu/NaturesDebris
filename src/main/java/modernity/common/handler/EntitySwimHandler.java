/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.handler;

import modernity.api.event.CheckEntityInWaterEvent;
import modernity.common.block.MDMaterial;
import modernity.common.fluid.MDFluidTags;
import modernity.common.fluid.OilFluid;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public enum EntitySwimHandler {
    INSTANCE;

    @SubscribeEvent
    public void checkEntityInWater( CheckEntityInWaterEvent event ) {
        if( event.getEntity() instanceof PlayerEntity ) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if( player.abilities.isFlying || player.isSpectator() ) return;
        }
        if( isEntitySwimmingInOil( event.getEntity() ) ) {
            event.getEntity().setMotion( event.getEntity().getMotion().add( 0, 0.06, 0 ) );
        }
        if( isEntityInOil( event.getEntity() ) ) {
            event.getEntity().handleFluidAcceleration( MDFluidTags.OIL );
            if( event.getEntity() instanceof LivingEntity ) {
                LivingEntity entity = (LivingEntity) event.getEntity();

                Vec3d motion = entity.getMotion();
                if( entity.collidedHorizontally && entity.isOffsetPositionInLiquid( motion.x, motion.y + 0.6, motion.z ) ) {
                    entity.setMotion( motion.x, 0.3, motion.z );
                }
            }
            event.getEntity().setMotion( event.getEntity().getMotion().mul( 0.7, 0.7, 0.7 ) );
            event.getEntity().onGround = false;
            if( isEntityInBurningOil( event.getEntity() ) ) {
                event.getEntity().setFire( 15 );
            }
        }
    }

    public boolean isEntityInOil( Entity e ) {
        return e.world.isMaterialInBB( e.getBoundingBox().shrink( 0.001 ), MDMaterial.OIL );
    }

    public boolean isEntitySwimmingInOil( Entity e ) {
        return e.world.isMaterialInBB( e.getBoundingBox().grow( - 0.1, - 0.4, - 0.1 ), MDMaterial.OIL );
    }

    public boolean isEntityInBurningOil( Entity e ) {
        AxisAlignedBB bb = e.getBoundingBox().shrink( 0.001 );
        int minX = MathHelper.floor( bb.minX );
        int maxX = MathHelper.ceil( bb.maxX );
        int minY = MathHelper.floor( bb.minY );
        int maxY = MathHelper.ceil( bb.maxY );
        int minZ = MathHelper.floor( bb.minZ );
        int maxZ = MathHelper.ceil( bb.maxZ );

        try( BlockPos.PooledMutableBlockPos mpos = BlockPos.PooledMutableBlockPos.retain() ) {
            for( int x = minX; x < maxX; ++ x ) {
                for( int y = minY; y < maxY; ++ y ) {
                    for( int z = minZ; z < maxZ; ++ z ) {
                        BlockState state = e.world.getBlockState( mpos.setPos( x, y, z ) );
                        if( state.getFluidState().getFluid() instanceof OilFluid ) {
                            if( state.get( OilFluid.BURNING ) ) {
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        }
    }
}
