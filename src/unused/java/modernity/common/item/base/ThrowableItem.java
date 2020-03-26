/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.item.base;

import modernity.common.entity.ThrownItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class ThrowableItem extends Item {
    public ThrowableItem( Properties properties ) {
        super( properties );
    }

    public abstract ThrownItemEntity make( World world, LivingEntity thrower );

    public abstract ThrownItemEntity make( World world, double x, double y, double z );

    public float getInaccuracy( @Nullable LivingEntity thrower ) {
        return thrower == null ? 6 : 1;
    }

    public float getVelocity( @Nullable LivingEntity thrower ) {
        return thrower == null ? 1.1F : 1.5F;
    }

    public ThrownItemEntity launch( World world, LivingEntity thrower, ItemStack stack ) {
        ThrownItemEntity projectile = make( world, thrower );
        projectile.setItemStack( stack );
        projectile.shoot(
            thrower,
            thrower.rotationPitch,
            thrower.rotationYaw,
            0,
            getVelocity( thrower ),
            getInaccuracy( thrower )
        );
        world.addEntity( projectile );
        return projectile;
    }

    public ThrownItemEntity dispense( World world, double x, double y, double z, Direction dir, ItemStack stack ) {
        Vec3i vec = dir.getDirectionVec();

        ThrownItemEntity projectile = make( world, x, y, z );

        projectile.setItemStack( stack );
        projectile.shoot(
            vec.getX(),
            vec.getY(),
            vec.getZ(),
            getVelocity( null ),
            getInaccuracy( null )
        );
        world.addEntity( projectile );
        return projectile;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick( World world, PlayerEntity player, Hand hand ) {
        ItemStack stack = player.getHeldItem( hand );
        if( ! player.abilities.isCreativeMode ) {
            stack.shrink( 1 );
        }

        world.playSound( null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / ( random.nextFloat() * 0.4F + 0.8F ) );
        if( ! world.isRemote ) {
            launch( world, player, stack );
        }

        player.addStat( Stats.ITEM_USED.get( this ) );
        return new ActionResult<>( ActionResultType.SUCCESS, stack );
    }
}
