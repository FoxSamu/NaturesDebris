/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 9 - 1 - 2019
 */

package modernity.common.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import modernity.api.event.CheckEntityInWaterEvent;
import modernity.common.block.MDMaterial;
import modernity.common.fluid.MDFluidTags;
import modernity.common.fluid.OilFluid;

public class EntitySwimHandler {
    @SubscribeEvent
    public void checkEntityInWater( CheckEntityInWaterEvent event ) {
        if( event.getEntity() instanceof EntityPlayer ) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if( player.abilities.isFlying || player.isSpectator() ) return;
        }
        if( isEntitySwimmingInOil( event.getEntity() ) ) {
            event.getEntity().motionY += 0.06;
        }
        if( isEntityInOil( event.getEntity() ) ) {
            event.getEntity().handleFluidAcceleration( MDFluidTags.OIL );
            if( event.getEntity() instanceof EntityLivingBase ) {
                EntityLivingBase entity = (EntityLivingBase) event.getEntity();
                if( entity.collidedHorizontally && entity.isOffsetPositionInLiquid( entity.motionX, entity.motionY + 0.6, entity.motionZ ) ) {
                    entity.motionY = 0.3;
                }
            }
            event.getEntity().motionX *= 0.7;
            event.getEntity().motionY *= 0.7;
            event.getEntity().motionZ *= 0.7;
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
        return e.world.isMaterialInBB( e.getBoundingBox().shrink( 0.1, 0.4, 0.1 ), MDMaterial.OIL );
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
                        IBlockState state = e.world.getBlockState( mpos.setPos( x, y, z ) );
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
